package controller.account;

import dao.AccountDAO;
import model.GoogleAccount;
import model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet that handles login via Google authentication.
 */
@WebServlet(name = "LoginWithGoogleServlet", urlPatterns = {"/loginGoogle"})
public class LoginWithGoogleController extends HttpServlet {
private final AccountDAO accountDAO = AccountDAO.getInstance();
    /**
     * Handles GET requests for Google authentication.
     * This method retrieves the authorization code, exchanges it for an access token,
     * fetches user information from Google, and checks if the user exists in the database.
     * If the user exists, they are logged in. If not, they are redirected to the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String error = request.getParameter("error");

        // If the user cancels Google login, redirect to the login page
        if (error != null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Initialize Google authentication helper
        GoogleLogin gg = new GoogleLogin();
        String accessToken = null;
        GoogleAccount googleAccount = null;

        try {
            // Exchange the authorization code for an access token
            accessToken = gg.getToken(code);
            // Fetch user information from Google using the access token
            googleAccount = gg.getUserInfo(accessToken);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LoginWithGoogleController.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Error during Google authentication.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Check if the Google account is already registered in the database
        Account existingAccount = null;

        try {
            existingAccount = accountDAO.getAccountByEmail(googleAccount.getEmail());
        } catch (SQLException ex) {
            Logger.getLogger(LoginWithGoogleController.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Database error while checking account.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (existingAccount != null) {
            // If the account exists, create a session and log the user in
            HttpSession session = request.getSession();
            session.setAttribute("account", existingAccount);
            session.setMaxInactiveInterval(60 * 60); // 60 minutes session timeout

            // Redirect user based on their role
            switch (existingAccount.getRole()) {
                case "customer":
                    response.sendRedirect("home"); // Redirect to the home page for customers
                    break;
               
                 
                default:
                    // Invalid role - log out and show error
                    session.invalidate();
                    request.setAttribute("errorMessage", "Invalid role!");
                    request.getRequestDispatcher("login").forward(request, response);
                    break;
            }
        } else {
            // If the account doesn't exist, prompt the user to register
            request.setAttribute("errorMessage", "Email account is not registered yet. "
                    + "You can use this account to register.");
            request.setAttribute("googleAccount", googleAccount); // Pass Google account data for registration
            request.getRequestDispatcher("register.jsp").forward(request, response); // Redirect to registration page
        }
    }

    /**
     * Handles POST requests. Since Google login is processed via GET,
     * any POST requests are redirected to the login page.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
