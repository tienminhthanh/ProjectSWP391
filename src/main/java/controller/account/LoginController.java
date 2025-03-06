package controller.account;

import dao.AccountDAO;
import model.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet responsible for handling user login.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private AccountDAO accountDAO;

    /**
     * Initializes the servlet and creates an instance of AccountDAO.
     */
    @Override
    public void init() {
        accountDAO = new AccountDAO();
    }

    /**
     * Handles GET requests by forwarding users to the login page.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - validates login credentials.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib(); // Utility class for password hashing

        // Retrieve login details from the request
        String username = request.getParameter("username");
        String password = lib.hashMD5(request.getParameter("password")); // Hash the password using MD5
        HttpSession session = request.getSession();

        try {
            // Retrieve the account from the database using the provided username
            Account account = accountDAO.getAccountByUsername(username);

            if (account != null) { // If account exists
                if (account.getIsActive()) { // Check if the account is active
                    Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
                    if (failedAttempts == null) {
                        failedAttempts = 0;
                    }

                    // Lock the account if login fails 5 times (only for non-admin users)
                    if (!"admin".equals(account.getRole()) && failedAttempts >= 5) {
                        response.sendRedirect("deleteAccount?username=" + username);
                        return;
                    }

                    // Verify the password
                    if (account.getPassword().equals(password)) {
                        session.setAttribute("account", account); // Store the account in session
                        session.setMaxInactiveInterval(30 * 60); // 30-minute session timeout
                        session.removeAttribute("failedAttempts"); // Reset failed attempts counter

                        // Redirect based on user role
                        switch (account.getRole()) {
                            case "admin":
                                response.sendRedirect("listAccount");
                                break;
                            case "customer":
                                response.sendRedirect("home");
                                break;
                            case "staff":
                                response.sendRedirect("dashboard.jsp");
                                break;
                            case "shipper":
                                response.sendRedirect("dashboardShipper.jsp");
                                break;
                            default:
                                session.invalidate(); // Invalidate session for invalid roles
                                request.setAttribute("errorMessage", "Invalid access!");
                                forwardToLoginPage(request, response, username);
                                break;
                        }
                    } else { // Incorrect password
                        failedAttempts++;
                        session.setAttribute("failedAttempts", failedAttempts);

                        // Lock the account if login fails 5 times (only for non-admin users)
                        if (!"admin".equals(account.getRole()) && failedAttempts >= 5) {
                            response.sendRedirect("deleteAccount?username=" + username);
                            return;
                        }

                        // Display remaining attempts
                        request.setAttribute("errorMessage", "Wrong password! You have " + (5 - failedAttempts) + " attempts left.");
                        forwardToLoginPage(request, response, username);
                    }
                } else { // Account is locked or deactivated
                    request.setAttribute("errorMessage", "Your account is deactivated or locked!");
                    forwardToLoginPage(request, response, username);
                }
            } else { // Account not found
                request.setAttribute("errorMessage", "Account not found!");
                forwardToLoginPage(request, response, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Forwards the request back to the login page with an error message.
     */
    private void forwardToLoginPage(HttpServletRequest request, HttpServletResponse response, String username)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
