package controller.notification;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDAO;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import model.Notification;
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
//            String receiverID = request.getParameter("receiverID");
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            String role = account.getRole();

            Notification notification = notificationDAO.getNotificationById(notificationID);

            if (!notification.isRead() && !(role.equals("admin") || role.equals("staff"))) {
                notificationDAO.markAsRead(notificationID);
                notification.setRead(true);
            }

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
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        String role = account.getRole();

        if ("delete".equals(action)) {
            try {
                int notificationID = Integer.parseInt(request.getParameter("notificationID"));
                String receiverID = request.getParameter("receiverID");

                boolean success = notificationDAO.deleteNotification(notificationID);
                if (success) {
                    if (role.equals("customer")) {
                        response.sendRedirect("notification" + (receiverID != null ? "?receiverID=" + receiverID : ""));
                    } else {
                        response.sendRedirect("notificationshipper" + (receiverID != null ? "?receiverID=" + receiverID : ""));
                    }
                } else {
                    request.setAttribute("error", "Delete fail!");
                    doGet(request, response);
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Delete fail: " + e.getMessage());
                doGet(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Notification invalid.");
                doGet(request, response);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles notification detail view and deletion";
    }
}