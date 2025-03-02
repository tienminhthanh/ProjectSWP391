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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountLib lib = new AccountLib();

        String username = request.getParameter("username");
        String password = lib.hashMD5(request.getParameter("password"));
        HttpSession session = request.getSession();

        try {
            Account account = accountDAO.getAccountByUsername(username);

            if (account != null) {
                if (account.getIsActive()) {
                    Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
                    if (failedAttempts == null) {
                        failedAttempts = 0;
                    }

                    if (failedAttempts >= 5) {
                        response.sendRedirect("deleteAccount?username=" + username);
                        return;
                    }

                    if (account.getPassword().equals(password)) {
                        session.setAttribute("account", account);
                        session.setMaxInactiveInterval(30 * 60); // 30 minutes
                        session.removeAttribute("failedAttempts");

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
                                session.invalidate();
                                request.setAttribute("errorMessage", "Invalid access!");
                                forwardToLoginPage(request, response, username);
                                break;
                        }
                    } else {
                        failedAttempts++;
                        session.setAttribute("failedAttempts", failedAttempts);

                        if (failedAttempts >= 5) {
                            response.sendRedirect("deleteAccount?username=" + username);
                            return;
                        }

                        request.setAttribute("errorMessage", "Wrong password! You have " + (5 - failedAttempts) + " attempts left.");
                        forwardToLoginPage(request, response, username);
                    }
                } else {
                    request.setAttribute("errorMessage", "Your account is deactivated or locked!");
                    forwardToLoginPage(request, response, username);
                }
            } else {
                request.setAttribute("errorMessage", "Account not found!");
                forwardToLoginPage(request, response, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void forwardToLoginPage(HttpServletRequest request, HttpServletResponse response, String username)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
