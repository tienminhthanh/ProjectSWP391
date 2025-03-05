package controller.notification;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDAO;
import model.Notification;
import java.sql.SQLException;

@WebServlet(name = "NotificationDetailController", urlPatterns = {"/notificationdetail"})
public class NotificationDetailController extends HttpServlet {

    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int notificationID = Integer.parseInt(request.getParameter("notificationID"));
            String receiverID = request.getParameter("receiverID");

            // Fetch notification
            Notification notification = notificationDAO.getNotificationById(notificationID);
            if (notification == null || (receiverID != null && notification.getReceiverID() != Integer.parseInt(receiverID))) {
                request.setAttribute("error", "Không tìm thấy thông báo hoặc bạn không có quyền xem.");
                request.getRequestDispatcher("/notificationDetail.jsp").forward(request, response);
                return;
            }

            // Mark as read if not already read
            if (!notification.isRead()) {
                notificationDAO.markAsRead(notificationID);
                notification.setRead(true); // Update local object to reflect change
            }

            request.setAttribute("notification", notification);
            request.getRequestDispatcher("/notificationDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID thông báo không hợp lệ.");
            request.getRequestDispatcher("/notificationDetail.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi truy cập cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/notificationDetail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int notificationID = Integer.parseInt(request.getParameter("notificationID"));
                String receiverID = request.getParameter("receiverID");

                // Delete notification
                boolean success = notificationDAO.deleteNotification(notificationID);
                if (success) {
                    response.sendRedirect("notification?action=list&receiverID=" + receiverID);
                } else {
                    request.setAttribute("error", "Không thể xóa thông báo.");
                    doGet(request, response); // Reload the detail page with error
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Lỗi xóa thông báo: " + e.getMessage());
                doGet(request, response); // Reload with error
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID thông báo không hợp lệ.");
                doGet(request, response); // Reload with error
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles notification detail view and deletion";
    }
}