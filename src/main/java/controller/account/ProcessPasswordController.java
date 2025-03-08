package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import model.Account;

@WebServlet(name = "ProcessPasswordServlet", urlPatterns = {"/processPassword"})
public class ProcessPasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the input values from the form
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp"); // Redirect to login if session is invalid
            return;
        }

        Account account = (Account) session.getAttribute("account");

        // Check if the current password matches the password in session
        if (newPassword.equals(confirmPassword)) {
            try {
                // Update the account with the new password
                AccountDAO accountDAO = new AccountDAO();
                AccountLib lib = new AccountLib();
                boolean success = accountDAO.updatePassword(account.getUsername(), lib.hashMD5(newPassword));
                if (success) {
                    // Successfully changed the password
                    request.setAttribute("message", "Password changed successfully.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Failed to change password.");
                    request.getRequestDispatcher("processPassword.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Database error occurred.");
                request.getRequestDispatcher("processPassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "New passwords do not match.");
            request.getRequestDispatcher("processPassword.jsp").forward(request, response);
        }

    }
}
