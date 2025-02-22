
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
    
    
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            Account account = accountDAO.getAccountByUsername(username);

            if (account != null) {
                if (account.getIsActive()) {
                    if (account.getPassword().equals(password)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("account", account);
                        switch (account.getRole()) {
                            case "admin":
                                response.sendRedirect("dashboard.jsp");
                                break;
                            case "customer":
                                response.sendRedirect("/home");
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
                        request.setAttribute("errorMessage", "Your account is deactivated or locked!");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "Account not found!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
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
            response.sendRedirect("home.jsp");
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);  // Redirect to login if not logged in
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for handling login and account status verification";
    }
}
