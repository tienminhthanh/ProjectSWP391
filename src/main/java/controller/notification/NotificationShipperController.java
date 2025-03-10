/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.notification;

import dao.NotificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
import model.OrderInfo;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "NotificationShipperController", urlPatterns = {"/notificationshipper"})
public class NotificationShipperController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private NotificationDAO notificationDAO;

    @Override
    public void init() {
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
//        Integer orderID = (Integer) request.getSession().getAttribute("orderID");
        OrderInfo order = (OrderInfo) session.getAttribute("order");
        Notification notification = new Notification();
        try {
            String receiverIDParam = request.getParameter("receiverID");
            int receiverID = Integer.parseInt(receiverIDParam);
            List<Notification> notifications = notificationDAO.getNotificationsByReceiver(receiverID);
            request.setAttribute("notifications", notifications);
            
            request.getRequestDispatcher("notificationListShipper.jsp").forward(request, response);
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
            action = "insert"; // Mặc định là thêm thông báo mới nếu không có action
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

    // Lấy danh sách thông báo theo receiverID
    private void listNotifications(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int receiverID = Integer.parseInt(request.getParameter("receiverID"));
            List<Notification> notifications = notificationDAO.getNotificationsByReceiver(receiverID);
            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("notificationListShipper.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException("Database error", ex);
        }
    }

    // Thêm thông báo mới
    private void insertNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int senderID = Integer.parseInt(request.getParameter("senderID"));
            int receiverID = Integer.parseInt(request.getParameter("receiverID"));
            String details = request.getParameter("notificationDetails");
            String title = request.getParameter("notificationTitle");
            Date dateCreated = new Date(System.currentTimeMillis());
            boolean isDeleted = false;
            boolean isRead = false;

            Notification notification = new Notification(0, senderID, receiverID, details, dateCreated, isDeleted, title, isRead);
            notificationDAO.insertNotification(notification);
            response.sendRedirect("notificationshipper?action=list&receiverID=" + receiverID);
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    // Đánh dấu thông báo là đã đọc
    private void markAsRead(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.markAsRead(notificationID);
            response.sendRedirect("notificationshipper?action=list&receiverID=" + request.getParameter("receiverID"));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    // Đánh dấu tất cả thông báo là đã đọc
    private void markAsAllRead(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int receiverID = Integer.parseInt(request.getParameter("receiverID"));
            notificationDAO.markAsAllRead(receiverID);
            response.sendRedirect("notificationshipper?action=list&receiverID=" + request.getParameter("receiverID"));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    // Xóa thông báo (cập nhật isDeleted thành true)
    private void deleteNotification(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            notificationDAO.deleteNotification(notificationID);
            response.sendRedirect("notificationshipper?action=list&receiverID=" + request.getParameter("receiverID"));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
