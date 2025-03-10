package controller.notification;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.AccountDAO;
import dao.NotificationDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Notification;

@WebServlet(name = "CreateNotificationController", urlPatterns = {"/createnotification"})
public class CreateNotificationController extends HttpServlet {

    private NotificationDAO notificationDAO;
    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        try {
            notificationDAO = new NotificationDAO();
            accountDAO = new AccountDAO();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize DAOs", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String senderStr = request.getParameter("senderID");
        try {
            // Set senderID if provided
            if (senderStr != null && !senderStr.trim().isEmpty()) {
                int senderID = Integer.parseInt(senderStr);
                request.setAttribute("senderID", senderID);
            }

            // Fetch all customers for the checkbox list
            List<Account> customers = accountDAO.getAllCustomers();
            request.setAttribute("customers", customers);

            request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid sender ID");
        } catch (SQLException e) {
            log("Database error fetching customers: ", e);
            request.setAttribute("error", "Error loading customers: " + e.getMessage());
            request.getRequestDispatcher("/listnotification").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Input validation
            String senderStr = request.getParameter("senderID");
            String[] receiverIDs = request.getParameterValues("receiverID"); // Array of selected receiver IDs
            String notificationTitle = request.getParameter("notificationTitle");
            String notificationDetails = request.getParameter("notificationDetails");

            if (notificationTitle == null || notificationTitle.trim().isEmpty()
                    || notificationDetails == null || notificationDetails.trim().isEmpty()) {
                throw new IllegalArgumentException("Title and details are required");
            }

            int senderID = Integer.parseInt(senderStr);

            if (receiverIDs != null && receiverIDs.length > 0) {
                // Send to selected customers
                for (String receiverIDStr : receiverIDs) {
                    int receiverID = Integer.parseInt(receiverIDStr);
                    Notification notification = new Notification(senderID, receiverID, notificationDetails.trim(), new Date(System.currentTimeMillis()), false, notificationTitle.trim(), false);
                    notificationDAO.insertNotification(notification);
                }
                response.sendRedirect("listnotification");
            } else {
                request.setAttribute("error", "Please select at least one receiver.");
                request.setAttribute("senderID", senderID);
                List<Account> customers = accountDAO.getAllCustomers();
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            try {
                request.setAttribute("error", "Invalid ID format");
                List<Account> customers = accountDAO.getAllCustomers();
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(CreateNotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
            try {
                log("Database error: ", e);
                request.setAttribute("error", "Database error occurred");
                List<Account> customers = accountDAO.getAllCustomers();
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(CreateNotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IllegalArgumentException e) {
            try {
                request.setAttribute("error", e.getMessage());
                List<Account> customers = accountDAO.getAllCustomers();
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(CreateNotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles creation of new notifications with customer selection via checkboxes";
    }
}
