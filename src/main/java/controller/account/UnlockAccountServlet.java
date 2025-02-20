package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import model.Account;

@WebServlet(name = "UnlockAccountServlet", urlPatterns = {"/unlockAccount"})
public class UnlockAccountServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();

        try {
            Account account = accountDAO.getAccountByUsername(username);
            if (account != null) {
                boolean success = accountDAO.updateAccountStatus(username, true);
                if (success) {
                    response.sendRedirect("listAccount");
                } else {
                    request.setAttribute("errorMessage", "Failed to unlock the account. Please try again.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to unlock accounts";
    }
}
