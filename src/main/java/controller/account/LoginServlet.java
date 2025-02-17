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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();  // Initialize AccountDAO to handle login
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Step 1: Check if the account exists
        Account account = accountDAO.getAccountByUsername(username);

        if (account != null) {
            // Step 2: Check the account status (active or inactive)
            if (account.getIsActive()) {
                // Step 3: If account is active, check username and password
                if (account.getPassword().equals(password)) {
                    // If login successful, set session and redirect based on role
                    HttpSession session = request.getSession();
                    session.setAttribute("account", account); // Set the account in the session

                    // Redirect to the appropriate dashboard based on role
                    switch (account.getRole()) {
                        case "admin":
                            response.sendRedirect("listAccount");
                            break;
                        case "customer":
                            response.sendRedirect("home.jsp");
                            break;
                        case "staff":
                            response.sendRedirect("dashboard.jsp");
                            break;
                        case "shipper":
                            response.sendRedirect("shipperDashboard.jsp");
                            break;
                        default:
                            session.invalidate();
                            request.setAttribute("errorMessage", "Invalid access!");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                            break;
                    }
                } else {
                    // Incorrect password
                    request.setAttribute("errorMessage", "Wrong password!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // Account is locked or inactive
                request.setAttribute("errorMessage", "Your account is deactivated or locked!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            // If the username doesn't exist
            request.setAttribute("errorMessage", "Account not found!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            response.sendRedirect("home.jsp");  // If already logged in, redirect to home
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);  // Redirect to login if not logged in
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for handling login and account status verification";
    }
}
