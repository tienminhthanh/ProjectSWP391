package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import model.Account;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Retrieve form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");

        try {
            AccountDAO accountDAO = new AccountDAO();
            String message = null;

            // Check if the email exists in the system
            Account accountByEmail = accountDAO.getAccountByEmail(email);

            if (accountByEmail != null && !"admin".equals(accountByEmail.getRole())) {
                // If email exists, check if the account is active or locked
                if (accountByEmail.getIsActive()) {
                    // If account is active, deny registration
                    message = "Email already exists!";
                } else {
                    request.getSession().setAttribute("tempEmail", email);
                    // If account is locked, redirect to unlock/re-register page
                    request.setAttribute("lockedAccount", accountByEmail);
                    request.setAttribute("message", "This email is associated with a locked account. Do you want to unlock it or remove the email?");
                    request.getRequestDispatcher("unlockOrRegister.jsp").forward(request, response);
                    return;
                }
            }

            // Check if username already exists
            if (accountDAO.getAccountByUsername(username) != null) {
                message = "Username already exists!";
            } // Check if passwords match
            else if (!password.equals(confirmPassword)) {
                message = "Passwords do not match!";
            } // Proceed with registration
            else {
                AccountLib lib = new AccountLib();
                password = lib.hashMD5(confirmPassword);
                boolean success = accountDAO.register(username, password, firstName, lastName, email, phoneNumber, birthDate);
                if (success) {
                    // Store temporary email for verification
                    request.getSession().setAttribute("tempEmail", email);
                    request.getSession().setAttribute("tempUsername", username);
                    response.sendRedirect("emailAuthentication");
                    return;
                } else {
                    message = "Registration failed. Please try again.";
                }
            }

            // If there is an error, return to the registration page
            if (message != null) {
                request.setAttribute("message", message);
                forwardToRegisterPage(request, response, username, firstName, lastName, email, phoneNumber, birthDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Database error: " + e.getMessage());
            forwardToRegisterPage(request, response, username, firstName, lastName, email, phoneNumber, birthDate);
        }
    }

    // Forward request to register page and keep entered data
    private void forwardToRegisterPage(HttpServletRequest request, HttpServletResponse response,
            String username, String firstName, String lastName,
            String email, String phoneNumber, String birthDate)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("birthDate", birthDate);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

}
