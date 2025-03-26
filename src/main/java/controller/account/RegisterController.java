package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String address = request.getParameter("address");

        try {
            AccountDAO accountDAO = new AccountDAO();
            AccountLib lib = new AccountLib();
            String message = null;

            Account accountByEmail = accountDAO.getAccountByEmail(email);
            if (accountByEmail != null && !"admin".equals(accountByEmail.getRole())) {
                if (accountByEmail.getAccountIsActive()) {
                    message = "This email is already in use!";
                } else {
                    request.getSession().setAttribute("tempEmail", email);
                    request.setAttribute("lockedAccount", accountByEmail);
                    request.setAttribute("message", "This email belongs to a locked account. Do you want to unlock it or remove the email?");
                    request.getRequestDispatcher("unlockOrRegister.jsp").forward(request, response);
                    return;
                }
            }

            if (accountDAO.getAccountByUsername(username) != null) {
                message = "Username is already taken!";
            } else if (!password.equals(confirmPassword)) {
                message = "Passwords do not match!";
            } else if (!lib.isValidPassword(password)) {
                message = "Password must be at least 8 characters long and contain uppercase letters, lowercase letters, digits, and special characters.";
            } else {
                String hashedPassword = lib.hashMD5(confirmPassword);
                boolean success = accountDAO.register(username, hashedPassword, firstName, lastName, null, phoneNumber, birthDate, address);
                if (success) {
                    request.getSession().setAttribute("tempEmail", email);
                    request.getSession().setAttribute("tempUsername", username);
                    response.sendRedirect("emailAuthentication");
                    return;
                } else {
                    message = "Registration failed. Please try again.";
                }
            }

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
