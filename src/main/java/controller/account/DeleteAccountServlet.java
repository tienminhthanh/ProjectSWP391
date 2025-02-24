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
                        response.sendRedirect("listAccount");
                    } else {
                        response.sendRedirect("listAccount");
                    }
                } else {
                    request.setAttribute("errorMessage", "Failed to deactivate account.");
                    request.getRequestDispatcher("readAccount?username=" + username).forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Account not found!");
                request.getRequestDispatcher("listAccount").forward(request, response);  // Chuyển hướng về danh sách tài khoản
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }
}