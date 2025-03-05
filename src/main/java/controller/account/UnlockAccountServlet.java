package controller.account;

import dao.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Account;

@WebServlet(name = "UnlockAccountServlet", urlPatterns = {"/unlockAccount"})
public class UnlockAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account loggedInAccount = (session != null) ? (Account) session.getAttribute("account") : null;
        String username = request.getParameter("username");

        try {
            AccountDAO accountDAO = new AccountDAO();
            Account accountToUnlock = accountDAO.getAccountByUsername(username);

            if (accountToUnlock == null) {
                request.setAttribute("errorMessage", "Account not found!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

          
            if (loggedInAccount != null && "admin".equals(loggedInAccount.getRole())) {
                boolean success = accountDAO.updateAccountStatus(username, true);
                if (success) {
                    response.sendRedirect("listAccount"); // Admin mở khóa xong thì về danh sách tài khoản
                } else {
                    request.setAttribute("errorMessage", "Failed to unlock the account. Please try again.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            } else {
                // Nếu không phải admin (guest), yêu cầu xác thực email trước khi mở khóa
                request.getSession().setAttribute("tempEmail", accountToUnlock.getEmail());
                response.sendRedirect("emailAuthentication");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "System error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to unlock accounts";
    }
}
