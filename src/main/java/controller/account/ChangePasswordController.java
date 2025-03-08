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

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/changePassword"})
public class ChangePasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib();

        // Get the input values from the form
        String currentPassword = lib.hashMD5(request.getParameter("currentPassword"));

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp"); // Redirect to login if session is invalid
            return;
        }

        Account account = (Account) session.getAttribute("account");

        // Check if the current password matches the password in session
        if (currentPassword.equals(account.getPassword())) {
            request.getRequestDispatcher("processPassword.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Current password is incorrect.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        }
    }
}
