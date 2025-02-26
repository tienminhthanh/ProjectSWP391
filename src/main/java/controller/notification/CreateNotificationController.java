package controller.notification;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDAO;
import model.Notification;

@WebServlet(name = "CreateNotificationController", urlPatterns = {"/createnotification"})
public class CreateNotificationController extends HttpServlet {

    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        try {
            notificationDAO = new NotificationDAO();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize NotificationDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String senderStr = request.getParameter("senderID");
        if (senderStr != null && !senderStr.trim().isEmpty()) {
            try {
                int senderID = Integer.parseInt(senderStr);
                request.setAttribute("senderID", senderID);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid sender ID");
                return;
            }
        }
        request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Input validation
            String senderStr = request.getParameter("senderID");
            String receiverStr = request.getParameter("receiverID");
            String notificationTitle = request.getParameter("notificationTitle");
            String notificationDetails = request.getParameter("notificationDetails");

            if (notificationTitle == null || notificationTitle.trim().isEmpty()
                    || notificationDetails == null || notificationDetails.trim().isEmpty()) {
                throw new IllegalArgumentException("Title and details are required");
            }

            int senderID = Integer.parseInt(senderStr);
            int receiverID = Integer.parseInt(receiverStr);

            Notification notification = new Notification();
            notification.setSenderID(senderID);
            notification.setReceiverID(receiverID);
            notification.setNotificationTitle(notificationTitle.trim());
            notification.setNotificationDetails(notificationDetails.trim());
            notification.setDateCreated(new Date(System.currentTimeMillis()));
            notification.setDeleted(false);
            notification.setRead(false);

            boolean success = notificationDAO.insertNotification(notification);

            if (success) {
                response.sendRedirect("listnotification?receiverID=" + receiverID);
            } else {
                request.setAttribute("error", "Failed to create notification");
                request.setAttribute("senderID", senderID);
                request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ID format");
            request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
        } catch (SQLException e) {
            log("Database error: ", e);  // Add logging
            request.setAttribute("error", "Database error occurred");
            request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/notificationAddNew.jsp").forward(request, response);
        }
    }
}
