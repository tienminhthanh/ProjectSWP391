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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;

@WebServlet(name = "ListAccountSevlet", urlPatterns = {"/listAccount"})
public class ListAccountSevlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the role filter from the request
        String roleFilter = request.getParameter("role");

        // Check session and user role
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AccountDAO accountDAO = new AccountDAO();
        List<Account> accounts = null;
        try {
            // Fetch accounts based on the role filter (if provided)
            accounts = accountDAO.getAllAccounts(roleFilter);

            // Reverse the list to get it in descending order (from most recent to oldest)
            Collections.reverse(accounts);

            // Set attributes for the JSP
            request.setAttribute("accounts", accounts);
            request.setAttribute("roleFilter", roleFilter);

            RequestDispatcher dispatcher = request.getRequestDispatcher("accountList.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ListAccountSevlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "An error occurred while fetching the accounts.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
