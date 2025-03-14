package controller.chat;

import dao.ChatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Chat;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ChatController", urlPatterns = {"/chat"})
public class ChatController extends HttpServlet {

    private static final int ADMIN_ID = 1;
    private ChatDAO chatDAO;
    private static final Logger LOGGER = Logger.getLogger(ChatController.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            chatDAO = new ChatDAO();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize ChatController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("account");
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        String userRole = account.getRole();
        int userID = account.getAccountID();

        try {
            if ("admin".equals(userRole) || "staff".equals(userRole)) {
                Set<Integer> customerIDs = chatDAO.getAdminChatCustomers();
                request.setAttribute("customerIDs", customerIDs);

                Map<Integer, String> customerNames = new HashMap<>();
                if (customerIDs != null) {
                    for (int customerID : customerIDs) {
                        customerNames.put(customerID, chatDAO.getCustomerName(customerID));
                    }
                }
                request.setAttribute("customerNames", customerNames);

                String customerIDParam = request.getParameter("customerID");
                if (customerIDParam != null && !customerIDParam.isEmpty()) {
                    int customerID = Integer.parseInt(customerIDParam);
                    List<Chat> chats = chatDAO.getChatsBetweenUsers(ADMIN_ID, customerID);
                    request.setAttribute("chats", chats);
                    request.setAttribute("selectedCustomerID", customerID);
                }
                request.getRequestDispatcher("/chatList.jsp").forward(request, response);
            } else {
                List<Chat> chats = chatDAO.getChatsBetweenUsers(userID, ADMIN_ID);
                request.setAttribute("chats", chats);
                request.getRequestDispatcher("/chat.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Failed to retrieve chat data", e);
        } catch (NumberFormatException e) {
            response.sendRedirect("chat");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        Account account = (Account) request.getSession().getAttribute("account");
        if (account == null) {
            throw new ServletException("User not logged in");
        }
        int userID = account.getAccountID();
        String userRole = account.getRole();

        String messageContent = request.getParameter("messageContent");
        if (messageContent == null || messageContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        Chat chat = new Chat();
        chat.setMessageContent(messageContent);
        chat.setSentAt(new Timestamp(System.currentTimeMillis()));
        chat.setSenderDeleted(false);
        chat.setReceiverDeleted(false);
        chat.setDialogueID(generateDialogueID(userID));
        chat.setIsSeen(false);

        if (userID == ADMIN_ID || userRole.equals("staff")) {
            String customerIDParam = request.getParameter("customerID");
            if (customerIDParam == null || customerIDParam.isEmpty()) {
                throw new IllegalArgumentException("Customer ID is required for admin");
            }
            int customerID = Integer.parseInt(customerIDParam);
            chat.setSenderID(ADMIN_ID);
            chat.setReceiverID(customerID);
        } else {
            chat.setSenderID(userID);
            chat.setReceiverID(ADMIN_ID);
        }

        try {
            boolean success = chatDAO.insertChat(chat);
            if (!success) {
                throw new SQLException("Failed to insert chat message");
            }
            response.getWriter().write("{\"success\": true, \"message\": \"Message sent successfully\"}");
        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
    }

    private int generateDialogueID(int userID) {
        return Math.abs(userID * 1000 + (int) (System.currentTimeMillis() % 1000));
    }
}
