package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import model.Account;

@WebServlet(name = "ListAccountSevlet", urlPatterns = {"/listAccount"})
public class ListAccountSevlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if the user is logged in and has the admin role
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            // Redirect to login page if not logged in
            response.sendRedirect("login.jsp");
            return;
        }

        // Get the logged-in user's role from the session
        Account account = (Account) session.getAttribute("account");
        if (!"admin".equals(account.getRole())) {
            // If the user is not an admin, redirect them to the home page or another appropriate page
            response.sendRedirect("home");
            return;
        }

        AccountDAO accountDAO = new AccountDAO();

        try {
            List<Account> accounts = accountDAO.getAllAccounts();
            Collections.reverse(accounts);  // Reverse the list to show the most recent accounts first
            request.setAttribute("accounts", accounts);

            // Forward to the account list page
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountList.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "An error occurred while fetching the accounts.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
