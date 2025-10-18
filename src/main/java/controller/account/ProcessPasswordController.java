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
private final AccountDAO accountDAO = AccountDAO.getInstance();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession(false);
       
        AccountLib lib = new AccountLib();
        Account account = (Account) session.getAttribute("account");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "New passwords do not match.");
            request.getRequestDispatcher("processPassword.jsp").forward(request, response);
            return;
        } else {
            if (account.getPassword().equals(lib.hashMD5(newPassword))) {
               request.setAttribute("errorMessage", "The new password matches the old password.");
            request.getRequestDispatcher("processPassword.jsp").forward(request, response);
            return; 
            }
        }

        if (!lib.isValidPassword(newPassword)) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters long and contain uppercase letters, lowercase letters, digits, and special characters.");
            request.getRequestDispatcher("processPassword.jsp").forward(request, response);
            return;
        }

        try {

            boolean success = accountDAO.updatePassword(account.getUsername(), lib.hashMD5(newPassword));
            if (success) {

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
    }
}
