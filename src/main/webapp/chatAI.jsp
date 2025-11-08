<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Chat với Gemini AI</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100">

        <div id="chatContainer1" class="w-full max-w-4xl mx-auto">
            <div id="chatBox1" class="w-full h-96 overflow-y-auto border border-gray-300 p-4 bg-gray-50 text-left ">
            </div>
            <div id="inputContainer1" class="mt-4 flex justify-center">
                <input type="text" id="userMessage" class="w-3/4 p-3 text-lg border border-gray-300 rounded-md" placeholder="Enter the message..." onkeypress="if (event.keyCode == 13) sendMessage1()">
                <button class="p-3 text-lg bg-orange-500 text-white border-none rounded-md cursor-pointer hover:bg-blue-600 ml-2" onclick="sendMessage1()">Send</button>
            </div>
        </div>

        <script>
            console.log("Start chat AI");
            function sendMessage1() {
                var userMessage = document.getElementById("userMessage").value;
                if (userMessage.trim() === "") {
                    alert("Vui lòng nhập tin nhắn!");
                    return;
                }

                var chatBox1 = document.getElementById("chatBox1");

                // Hiển thị tin nhắn người dùng
                chatBox1.innerHTML += "<div class='message user bg-blue-600 text-white p-2 mb-2 rounded-lg max-w-4/5 float-right'><b>Bạn:</b> " + userMessage + "</div><br style='clear:both;'>";

                // Hiển thị hiệu ứng "đang gõ..."
                var typingIndicator = document.createElement("div");
                typingIndicator.className = "typing bot text-gray-600 inline-block text-lg mb-2 float-left";
                typingIndicator.id = "typingIndicator";
                typingIndicator.innerHTML = "<span>.</span><span>.</span><span>.</span>";
                chatBox1.appendChild(typingIndicator);

                // Cuộn xuống cuối
                chatBox1.scrollTop = chatBox1.scrollHeight;

                // Gửi request đến Servlet
                fetch("ChatServletAI", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "message=" + encodeURIComponent(userMessage)
                })
                        .then(response => response.json())
                        .then(data => {
                            // Xóa dấu ba chấm "đang gõ..."
                            document.getElementById("typingIndicator").remove();

                            // Hiển thị phản hồi từ chatbot
                            chatBox1.innerHTML += "<div class='message bot bg-gray-300 text-black p-2 mb-2 rounded-lg max-w-4/5 float-left'><b>AI:</b> " + data.response + "</div><br style='clear:both;'>";

                            // Xóa ô nhập tin nhắn
                            document.getElementById("userMessage").value = "";

                            // Cuộn xuống cuối
                            chatBox1.scrollTop = chatBox1.scrollHeight;
                        })
                        .catch(error => console.error("Lỗi khi gửi yêu cầu:", error));
            }
            console.log("End chat AI");
        </script>
    </body>
</html>
