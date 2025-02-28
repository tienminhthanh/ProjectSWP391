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

@WebServlet(name = "UpdateNotification", urlPatterns = {"/updatenotification"})
public class UpdateNotificationController extends HttpServlet {

    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String notificationID = request.getParameter("notificationID");
        try {
            Notification notification = notificationDAO.getNotificationById(Integer.parseInt(notificationID));

            if (notification != null) {
                request.setAttribute("notification", notification);
                request.getRequestDispatcher("/notificationUpdate.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Notification not found");
                request.getRequestDispatcher("/listnotification").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error retrieving notification: " + e.getMessage());
            request.getRequestDispatcher("/listnotification").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid notification ID");
            request.getRequestDispatcher("/listnotification").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy giá trị mới
            String newTitle = request.getParameter("notificationTitle");
            String newDetails = request.getParameter("notificationDetails");

            // Lấy giá trị cũ để dùng trong WHERE
            String oldTitle = request.getParameter("oldNotificationTitle");
            String oldDetails = request.getParameter("oldNotificationDetails");

            boolean updated = notificationDAO.updateNotification(newTitle, newDetails, oldTitle, oldDetails);

            if (updated) {
                response.sendRedirect("listnotification");
            } else {
                request.setAttribute("error", "Failed to update notification");
                request.getRequestDispatcher("/notificationUpdate.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error updating notification: " + e.getMessage());
            request.getRequestDispatcher("/notificationUpdate.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles notification updates";
    }
}
