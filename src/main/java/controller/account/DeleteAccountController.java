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

@WebServlet(name = "DeleteAccountServlet", urlPatterns = {"/deleteAccount"})
public class DeleteAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");

        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();

        try {
            Account account2 = accountDAO.getAccountByUsername(username);
            if (account2 != null) {
                // Check if the account to delete is an admin account
                if ("admin".equals(account2.getRole())) {
                    request.setAttribute("errorMessage", "Cannot deactivate admin account.");
                    request.getRequestDispatcher("listAccount").forward(request, response);
                    return;
                }

                // Proceed with deactivation for non-admin accounts
                boolean success = accountDAO.updateAccountStatus(username, false);
                if (success) {
                    if ("admin".equals(account.getRole())) {
                        response.sendRedirect("listAccount");
                    } else {
                        session.invalidate();
                        response.sendRedirect("login.jsp");
                    }
                } else {
                    request.setAttribute("errorMessage", "Failed to deactivate account.");
                    request.getRequestDispatcher("readAccount?username=" + username).forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Account not found!");
                request.getRequestDispatcher("listAccount").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

}
