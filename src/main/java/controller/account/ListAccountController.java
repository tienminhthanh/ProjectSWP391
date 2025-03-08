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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;

@WebServlet(name = "ListAccountSevlet", urlPatterns = {"/listAccount"})
public class ListAccountController extends HttpServlet {

    private static final int PAGE_SIZE = 3; // Hiển thị 3 tài khoản mỗi trang

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roleFilter = request.getParameter("role");
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AccountDAO accountDAO = new AccountDAO();
        try {
            // Lấy danh sách tài khoản phân trang
            List<Account> accounts = accountDAO.getAccountsPaginated(roleFilter, page, PAGE_SIZE);

            // Tính tổng số trang
            int totalAccounts = accountDAO.getTotalAccounts(roleFilter);
            int totalPages = (int) Math.ceil((double) totalAccounts / PAGE_SIZE);

            // Set attributes cho JSP
            request.setAttribute("accounts", accounts);
            request.setAttribute("roleFilter", roleFilter);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalAccounts > 0 ? totalPages : 1); // Đảm bảo ít nhất 1 trang
            request.setAttribute("totalAccounts", totalAccounts);

            RequestDispatcher dispatcher = request.getRequestDispatcher("accountList.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ListAccountController.class.getName()).log(Level.SEVERE, "SQL Error: " + ex.getMessage(), ex);
            request.setAttribute("errorMessage", "An error occurred while fetching the accounts: " + ex.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountList.jsp"); // Giữ trên cùng trang
            dispatcher.forward(request, response);
        } catch (NumberFormatException ex) {
            Logger.getLogger(ListAccountController.class.getName()).log(Level.SEVERE, "Invalid page number: " + pageStr, ex);
            request.setAttribute("errorMessage", "Invalid page number: " + pageStr);
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountList.jsp");
            dispatcher.forward(request, response);
        }
    }
}
