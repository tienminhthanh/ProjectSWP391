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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib= new AccountLib();
        
        String username = request.getParameter("username");
        String password = lib.hashMD5(request.getParameter("password"));
        String currentURL = request.getParameter("currentURL");

        try {
            Account account = accountDAO.getAccountByUsername(username);

            if (account != null) {
                if (account.getIsActive()) {
                    if (account.getPassword().equals(password)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("account", account);
                        session.setMaxInactiveInterval(30 * 60); // 30 minutes

                        switch (account.getRole()) {
                            case "admin":
                                response.sendRedirect("listAccount"); // Redirect to account list page
                                break;
                            case "customer":
                                if (currentURL == null || currentURL.isEmpty()) {
                                    response.sendRedirect("home");
                                } else {
                                    response.sendRedirect(currentURL);
                                }
                                break;
                            case "staff":
                                response.sendRedirect("listAccount");
                                break;
                            case "shipper":
                                response.sendRedirect("OrderListForShipperController");
                                break;
                            default:
                                session.invalidate();
                                request.setAttribute("errorMessage", "Invalid access!");
                                forwardToLoginPage(request, response, username); // Forward with data
                                break;
                        }
                    } else {
                        request.setAttribute("errorMessage", "Wrong password!");
                        forwardToLoginPage(request, response, username); // Forward with data
                    }
                } else {
                    request.setAttribute("errorMessage", "Your account is deactivated or locked!");
                    forwardToLoginPage(request, response, username); // Forward with data
                }
            } else {
                request.setAttribute("errorMessage", "Account not found!");
                forwardToLoginPage(request, response, username); // Forward with data
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            Account account = (Account) session.getAttribute("account");
            switch (account.getRole()) {
                case "staff":
                    response.sendRedirect("dashboard.jsp");
                    break;
                case "shipper":
                    response.sendRedirect("dashboardShipper.jsp");
                    break;
                case "admin":
                    response.sendRedirect("listAccount"); // Redirect to account list
                    break;
                case "customer":
                default:
                    response.sendRedirect("home");
            }
        } else {
            String currentURL = request.getParameter("currentURL");
            if (currentURL != null) {
                request.setAttribute("currentURL", currentURL);
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);  // Redirect to login if not logged in
        }
    }

    

    // Helper method to forward to login page and keep entered data
    private void forwardToLoginPage(HttpServletRequest request, HttpServletResponse response, String username)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
