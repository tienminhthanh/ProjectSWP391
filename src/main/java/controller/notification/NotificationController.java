package controller.notification;

import dao.NotificationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Notification;

@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
public class NotificationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
private final NotificationDAO notificationDAO = NotificationDAO.getInstance(); 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session == null) {
            response.sendRedirect("login");
            return;
        }
        try {
            String receiverIDParam = request.getParameter("receiverID");
            int receiverID = Integer.parseInt(receiverIDParam);
            List<Notification> notifications = notificationDAO.getNotificationsByReceiverDESC(receiverID);
            session.setAttribute("notifications", notifications);
            request.getRequestDispatcher("notification.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException("Database error", ex);
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid receiverID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "insert";
        }

        switch (action) {
            case "insert":
                insertNotification(request, response);
                break;
            case "markAsRead":
                markAsRead(request, response);
                break;
            case "markAsAllRead":
                markAsAllRead(request, response);
                break;
            case "delete":
                deleteNotification(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

//    private void listNotifications(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            int receiverID = Integer.parseInt(request.getParameter("receiverID"));
//            List<Notification> notifications = notificationDAO.getNotificationsByReceiver(receiverID);
//            request.setAttribute("notifications", notifications);
//            request.getRequestDispatcher("notifications.jsp").forward(request, response);
//        } catch (SQLException ex) {
//            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
//            throw new ServletException("Database error", ex);
//        }
//    }

    private void insertNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int senderID = Integer.parseInt(request.getParameter("senderID"));
            int receiverID = Integer.parseInt(request.getParameter("receiverID"));
            String details = request.getParameter("notificationDetails");
            String title = request.getParameter("notificationTitle");
            Date notificationDateCreated = new Date(System.currentTimeMillis());
            boolean isDeleted = false;
            boolean isRead = false;

            Notification notification = new Notification(0, senderID, receiverID, details, notificationDateCreated, isDeleted, title, isRead);
            notificationDAO.insertNotification(notification);
            response.sendRedirect("notification?action=list&receiverID=" + receiverID);
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void markAsRead(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.markAsRead(notificationID);
            response.sendRedirect("notification?action=list&receiverID=" + request.getParameter("receiverID"));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void markAsAllRead(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int receiverID = Integer.parseInt(request.getParameter("receiverID"));
            notificationDAO.markAsAllRead(receiverID);
            response.sendRedirect("notification?action=list&receiverID=" + request.getParameter("receiverID"));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void deleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.deleteNotification(notificationID);
            response.sendRedirect("notification?action=list&receiverID=" + request.getParameter("receiverID"));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}