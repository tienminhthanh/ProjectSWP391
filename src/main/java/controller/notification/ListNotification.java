package controller.notification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDAO;
import model.Notification;

@WebServlet(name = "ListNotification", urlPatterns = {"/listnotification"})
public class ListNotification extends HttpServlet {

    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        notificationDAO = new NotificationDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tất cả thông báo
            List<Notification> notifications = notificationDAO.getAllNotifications();

            // Đặt danh sách vào request attribute
            request.setAttribute("notifications", notifications);

            // Chuyển hướng đến trang hiển thị danh sách
            request.getRequestDispatcher("/notificationList.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/notificationList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int notificationID = Integer.parseInt(request.getParameter("notificationID"));

        try {
            if ("markAsRead".equals(action)) {
                notificationDAO.markAsRead(notificationID);
            } else if ("delete".equals(action)) {
                notificationDAO.deleteNotification(notificationID);
            }
            // Sau khi xử lý, quay lại danh sách
            response.sendRedirect("listnotification");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/notificationList.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to list all notifications";
    }
}