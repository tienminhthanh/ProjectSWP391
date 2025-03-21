package controller.extend;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

@WebServlet("/ChatServletAI")
public class ChatAIController extends HttpServlet {
//AIzaSyAz8Jeb-L8TI_DZw-x7n-OKVs6sQN2ZOPQ
    //private static final String API_KEY = "AIzaSyAdr95aaOBJifRSD9qEMCH8AHeCu9rGYg8"; // 🔴 Replace with your API key

    private static final String API_KEY = "AIzaSyAz8Jeb-L8TI_DZw-x7n-OKVs6sQN2ZOPQ";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private static final String SYSTEM_PROMPT = "Bạn là một nhân viên tư vấn sách chuyên nghiệp tên là Wibooks AI, chuyên hỗ trợ và giải đáp các thắc mắc liên quan đến sách, bao gồm nội dung truyện, nhân vật trong sách hoặc truyện tranh, cốt truyện, "
            + "cũng như thông tin về tác giả của những cuốn sách hoặc bộ truyện đó. "
            + "Nếu bạn có câu hỏi như 'Conan là ai?', 'Cốt truyện của One Piece như thế nào?', hay 'Tác giả của Harry Potter là ai?', hãy sẵn lòng giúp người tìm câu trả lời.\n"
            + "Tuy nhiên, nếu câu hỏi không liên quan đến sách, truyện tranh hoặc các nhân vật trong đó, chẳng hạn như 'Tòa nhà cao nhất thế giới là gì?', 'Cách nấu món ăn này như thế nào?' hay 'Sự kiện thể thao nào sắp diễn ra?', tôi xin phép không trả lời. "
            + "Mong bạn hãy đặt những câu hỏi liên quan đến sách và truyện để tôi có thể tư  một cách tốt nhất. Và tôi sẽ trả lời bạn thành một đoạn văn chứ không trình bày theo kiểu liệt kê sử dung  dấu *";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Receive user input from JSP
        String userMessage = request.getParameter("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            out.write("{\"error\": \"Message is empty\"}");
            return;
        }

        // Retrieve or initialize AiSession for conversation history
        HttpSession session = request.getSession();
        String previousUserMessage = (String) session.getAttribute("AiSession_UserMessage");
        String previousAiResponse = (String) session.getAttribute("AiSession_AiResponse");

        if (previousUserMessage == null || previousAiResponse == null) {
            previousUserMessage = "";
            previousAiResponse = "";
        }

        // Introduce a delay of 1 seconds before sending the message to AI
        try {
            Thread.sleep(1000);  // Sleep for 30 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send message with conversation history
        String aiResponse = sendMessageToGemini(userMessage, previousUserMessage, previousAiResponse);

        // Store conversation history in AiSession
        session.setAttribute("AiSession_UserMessage", userMessage);
        session.setAttribute("AiSession_AiResponse", aiResponse);

        // Send AI response back to the frontend
        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("response", aiResponse)
                .build();

        try ( JsonWriter jsonWriter = Json.createWriter(out)) {
            jsonWriter.writeObject(jsonResponse);
        }
    }

    /**
     * Sends a message to the Gemini AI API and retrieves the response.
     *
     * @param message The user's input message to be sent to the AI.
     * @param previousUserMessage The previous message from the user.
     * @param previousAiResponse The previous response from the AI.
     * @return The AI's response as a String, or an error message if the request
     * fails.
     */
    private String sendMessageToGemini(String message, String previousUserMessage, String previousAiResponse) {
        try {
            // Create a URL object with the API endpoint
            URL url = new URL(GEMINI_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // ✅ Maintain conversation history using AiSession
            String jsonInputString = "{"
                    + "\"contents\": [{"
                    + "\"parts\": ["
                    + "{\"text\": \"" + SYSTEM_PROMPT + "\"},"
                    + "{\"text\": \"User: " + previousUserMessage + "\"},"
                    + "{\"text\": \"" + previousAiResponse + "\"},"
                    + "{\"text\": \"User: " + message + "\"}"
                    + "]"
                    + "}]"
                    + "}";

            try ( OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try ( InputStream is = conn.getInputStream();  JsonReader reader = Json.createReader(is)) {
                    JsonObject jsonResponse = reader.readObject();
                    JsonArray candidates = jsonResponse.getJsonArray("candidates");

                    if (!candidates.isEmpty()) {
                        JsonObject firstCandidate = candidates.getJsonObject(0);
                        JsonObject content = firstCandidate.getJsonObject("content");
                        JsonArray parts = content.getJsonArray("parts");

                        if (!parts.isEmpty()) {
                            return parts.getJsonObject(0).getString("text");
                        }
                    }
                }
                return "No response from Gemini.";
            } else {
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    // Main method for testing in the console
    public static void main(String[] args) {
        ChatAIController chatServlet = new ChatAIController();
        Scanner scanner = new Scanner(System.in);

        // Simulated AiSession storage
        String previousUserMessage = "";
        String previousAiResponse = "";

        while (true) {
            System.out.print("Nhập câu hỏi của bạn (gõ 'exit' để thoát): ");
            String userMessage = scanner.nextLine();

            if (userMessage.equalsIgnoreCase("exit")) {
                System.out.println("Thoát chương trình chat.");
                break;
            }

            System.out.println("Bạn: " + userMessage);
            String response = chatServlet.sendMessageToGemini(userMessage, previousUserMessage, previousAiResponse);
            System.out.println("Gemini: " + response);

            // Update AiSession (simulated in console mode)
            previousUserMessage = userMessage;
            previousAiResponse = response;
        }
        scanner.close();
    }
}
