package controller.account;

import dao.AccountDAO;
import model.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UpdateAccountSevlet", urlPatterns = {"/updateAccount"})
public class UpdateAccountSevlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.getAccountByUsername(username);

        if (account != null) {
            request.setAttribute("account", account);
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountUpdate.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp"); // If account not found, redirect to login
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");

        AccountDAO accountDAO = new AccountDAO();

        // Step 1: Check if the email already exists (for another user, excluding the current user's username)
        if (accountDAO.isEmailExistEmailOfUser(username, email)) {
            // If the email exists for another user, display an error message
            request.setAttribute("message", "The email address is already in use by another account.");
            doGet(request, response);  // Show the update form again with error message
        } else {
            // Step 2: Proceed with account update
            boolean success = accountDAO.updateAccount(username, firstName, lastName, email, phoneNumber, birthDate);

            if (success) {
                // Update the session with the updated account information
                HttpSession session = request.getSession();
                Account updatedAccount = accountDAO.getAccountByUsername(username);
                session.setAttribute("account", updatedAccount);  // Update session

                response.sendRedirect("readAccount?username=" + username);  // Redirect back to the account details page
            } else {
                request.setAttribute("message", "Account update failed! Please try again.");
                doGet(request, response);  // Show the update form again with error message
            }
        }

    }
}
