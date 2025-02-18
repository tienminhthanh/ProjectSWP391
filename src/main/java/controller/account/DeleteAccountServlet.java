package controller.account;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteAccountServlet", urlPatterns = {"/deleteAccount"})
public class DeleteAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();

        try {
            model.Account account = accountDAO.getAccountByUsername(username);

            if (account != null) {
                boolean success = accountDAO.deactivateAccount(username);

                if (success) {
                    if ("admin".equals(account.getRole())) {
                        HttpSession session = request.getSession(false);
                        session.setAttribute("account", account);
                        response.sendRedirect("listAccount"); 
                    } else {
                        response.sendRedirect("login.jsp"); 
                    }
                } else {
                    response.sendRedirect("readAccount?username=" + username); 
                }
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page
        }
    }
}
