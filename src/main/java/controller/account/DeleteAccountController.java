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
import model.Customer;

@WebServlet(name = "DeleteAccountServlet", urlPatterns = {"/deleteAccount"})
public class DeleteAccountController extends HttpServlet {
private final AccountDAO accountDAO = AccountDAO.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Ensure session is valid and retrieve the account attribute
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp"); // redirect to login if session is invalid
            return;
        }
        
        Account account = (Account) session.getAttribute("account");
        String username = request.getParameter("username");
        
        if (username == null || username.isEmpty()) {
            request.setAttribute("errorMessage", "Username is required.");
            request.getRequestDispatcher("listAccount").forward(request, response);
            return;
        }

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
                        session.invalidate(); // invalidate session for deleted account
                        request.setAttribute("errorMessage", "Your account is deactivated or locked!");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                       
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
            e.printStackTrace();  // Log the exception stack trace for debugging
            request.setAttribute("errorMessage", "Database error occurred. Please try again later.");
            request.getRequestDispatcher("listAccount").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unknown error occurred. Please try again.");
            request.getRequestDispatcher("listAccount").forward(request, response);
        }
    }
}
