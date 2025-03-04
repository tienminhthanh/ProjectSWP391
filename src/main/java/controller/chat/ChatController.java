package controller.chat;

import dao.ChatDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account; // Giả định đây là class chứa thông tin account
import model.Chat;

@WebServlet(name = "ChatController", urlPatterns = {"/chat"})
public class ChatController extends HttpServlet {

    private static final int ADMIN_ID = 1; // Admin's accountID
    private ChatDAO chatDAO;
    private static final Logger LOGGER = Logger.getLogger(ChatController.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            chatDAO = new ChatDAO();
            LOGGER.info("ChatDAO initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize ChatDAO", e);
            throw new ServletException("Failed to initialize ChatController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy account từ session
        Account account = (Account) request.getSession().getAttribute("account");
        if (account == null) {
            LOGGER.warning("User not logged in, redirecting to login");
            response.sendRedirect("login"); // Giả định có trang login
            return;
        }
        int userID = account.getAccountID();

        try {
            if (userID == ADMIN_ID) {
                // Admin: Hiển thị danh sách khách hàng và cuộc chat đã chọn
                Set<Integer> customerIDs = chatDAO.getAdminChatCustomers();
                request.setAttribute("customerIDs", customerIDs.isEmpty() ? null : customerIDs);

                String customerIDParam = request.getParameter("customerID");
                if (customerIDParam != null && !customerIDParam.isEmpty()) {
                    int customerID = Integer.parseInt(customerIDParam);
                    List<Chat> chats = chatDAO.getChatsBetweenUsers(ADMIN_ID, customerID);
                    request.setAttribute("chats", chats.isEmpty() ? null : chats);
                    request.setAttribute("selectedCustomerID", customerID);
                }
            } else {
                // Customer: Chỉ hiển thị chat với admin
                List<Chat> chats = chatDAO.getChatsBetweenUsers(userID, ADMIN_ID);
                request.setAttribute("chats", chats.isEmpty() ? null : chats);
            }
            LOGGER.log(Level.INFO, "Forwarding to chat.jsp for accountID: {0}", userID);
            request.getRequestDispatcher("/chat.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving chat data", e);
            throw new ServletException("Failed to retrieve chat data", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid customerID parameter", e);
            response.sendRedirect("chat");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy account từ session
        Account account = (Account) request.getSession().getAttribute("account");
        if (account == null) {
            LOGGER.warning("User not logged in, redirecting to login");
            response.sendRedirect("login");
            return;
        }
        int userID = account.getAccountID();

        String messageContent = request.getParameter("messageContent");
        if (messageContent == null || messageContent.trim().isEmpty()) {
            LOGGER.warning("Empty message content, redirecting back");
            response.sendRedirect("chat");
            return;
        }

        Chat chat = new Chat();
        chat.setMessageContent(messageContent);
        chat.setSentAt(new Timestamp(System.currentTimeMillis()));
        chat.setSenderDeleted(false);
        chat.setReceiverDeleted(false);
        chat.setDialogueID(generateDialogueID(userID)); // Sinh dialogueID
        chat.setIsSeen(false);

        try {
            if (userID == ADMIN_ID) {
                String customerIDParam = request.getParameter("customerID");
                if (customerIDParam == null || customerIDParam.isEmpty()) {
                    LOGGER.warning("Missing customerID for admin message");
                    throw new ServletException("Customer ID is required for admin to send message");
                }
                int customerID = Integer.parseInt(customerIDParam);
                chat.setSenderID(ADMIN_ID);
                chat.setReceiverID(customerID);
            } else {
                chat.setSenderID(userID);
                chat.setReceiverID(ADMIN_ID);
            }

            boolean success = chatDAO.insertChat(chat);
            if (!success) {
                LOGGER.severe("Failed to insert chat message");
                throw new SQLException("Failed to insert chat message");
            }
            LOGGER.log(Level.INFO, "Message sent successfully by accountID: {0}", userID);
            String redirectUrl = userID == ADMIN_ID ? "chat?customerID=" + chat.getReceiverID() : "chat";
            response.sendRedirect(redirectUrl);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error sending chat message", e);
            throw new ServletException("Failed to send chat message", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid customerID parameter", e);
            response.sendRedirect("chat");
        }
    }

    private int generateDialogueID(int userID) {
        // Logic sinh dialogueID đơn giản dựa trên userID và thời gian
        return Math.abs(userID * 1000 + (int) (System.currentTimeMillis() % 1000));
    }
}
