package controller.notification;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDAO;
import jakarta.servlet.http.HttpSession;
import model.Notification;
import java.sql.SQLException;
import model.Account;

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
            // Kiểm tra vai trò người dùng từ session
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            String role = account.getRole();
            // Fetch notification
            Notification notification = notificationDAO.getNotificationById(notificationID);
//            if (notification == null || (receiverID != null && notification.getReceiverID() != Integer.parseInt(receiverID))) {
//                request.setAttribute("error", "Không tìm thấy thông báo hoặc bạn không có quyền xem.");
//                request.getRequestDispatcher("/notificationDetail.jsp").forward(request, response);
//                return;
//            }

            // Mark as read if not already read
            if (!notification.isRead() && !(role.equals("admin") || role.equals("staff"))) {
                notificationDAO.markAsRead(notificationID);
                notification.setRead(true); // Update local object to reflect change
            }

            // Chuyển hướng dựa trên vai trò
            String destinationPage;
            if (role.equals("admin") || role.equals("staff")) {
                destinationPage = "/notificationDetailForAdmin.jsp";
            } else if (role.equals("customer")) {
                destinationPage = "/notificationDetail.jsp";
            } else {
                destinationPage = "/notificationDetailForShipper.jsp";
            }

            request.setAttribute("notification", notification);
            request.getRequestDispatcher(destinationPage).forward(request, response);

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
                    response.sendRedirect("listnotification" + (receiverID != null ? "?receiverID=" + receiverID : ""));
                } else {
                    request.setAttribute("error", "Delete fail!");
                    doGet(request, response); // Reload the detail page with error
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Delete fail: " + e.getMessage());
                doGet(request, response); // Reload with error
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Notification invalid.");
                doGet(request, response); // Reload with error
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles notification detail view and deletion";
    }
}
