package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "DeleteAccountSevlet", urlPatterns = {"/deleteAccount"})
public class DeleteAccountSevlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();

        // Lấy thông tin account từ username
        model.Account account = accountDAO.getAccountByUsername(username);

        if (account != null) {
            boolean success = accountDAO.deactivateAccount(username);

            // Kiểm tra quyền và xử lý theo vai trò
            if (success) {
                if ("admin".equals(account.getRole())) {
                    // Keep the session active for the admin and redirect back to the account list
                        HttpSession session = request.getSession(false);
                    session.setAttribute("account", account);  // Keep the admin session intact
                    response.sendRedirect("listAccount");  // Admin returns to the account list
                } else {
                    response.sendRedirect("login.jsp");  // Non-admin users are logged out
                }
            } else {
                response.sendRedirect("readAccount?username=" + username);  // If deactivation fails, return to the account page
            }
        } else {
            response.sendRedirect("login.jsp"); // If the account is not found, redirect to login
        }
    }
}
