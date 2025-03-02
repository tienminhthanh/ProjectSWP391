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

@WebServlet(name = "LoginWithGoogleServlet", urlPatterns = {"/loginGoogle"})
public class LoginWithGoogleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String error = request.getParameter("error");

        // If the user cancels the Google login, forward to the login page
        if (error != null) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        GoogleLogin gg = new GoogleLogin();
        String accessToken = null;
        GoogleAccount googleAccount = null;

        try {
            // Get access token and Google user info
            accessToken = gg.getToken(code);
            googleAccount = gg.getUserInfo(accessToken);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(LoginWithGoogleServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Error during Google authentication.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Check if the user already exists in the database by email
        AccountDAO accountDAO = new AccountDAO();
        Account existingAccount = null;

        try {
            existingAccount = accountDAO.getAccountByEmail(googleAccount.getEmail());
        } catch (SQLException ex) {
            Logger.getLogger(LoginWithGoogleServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Database error while checking account.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (existingAccount != null) {
            // User exists, log them in and create a session
            HttpSession session = request.getSession();
            session.setAttribute("account", existingAccount);
            session.setMaxInactiveInterval(60 * 60); // 6   0 minutes

            // Role-based redirection
            switch (existingAccount.getRole()) {
              
                case "customer":
                    response.sendRedirect("home"); // Redirect to the home page for customers
                    break;
                case "staff":
                    response.sendRedirect("dashboard.jsp"); // Redirect to the dashboard page for staff
                    break;
                case "shipper":
                    response.sendRedirect("shipperDashboard.jsp"); // Redirect to the shipper's dashboard
                    break;
                default:
                    session.invalidate();
                    request.setAttribute("errorMessage", "Invalid role!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    break;
            }
        } else {
            // User doesn't exist in the database, prompt to register
            request.setAttribute("errorMessage", "Email account is not registered yet, \n"
                    + "you can use that account to register.");
            request.setAttribute("googleAccount", googleAccount); // Pass Google account data for registration
            request.getRequestDispatcher("register.jsp").forward(request, response); // Redirect to registration page
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST request not needed, as we're handling Google login with GET
        response.sendRedirect("login.jsp");
    }
}
