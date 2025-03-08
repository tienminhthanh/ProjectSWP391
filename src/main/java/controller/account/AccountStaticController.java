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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AccountStaticController", urlPatterns = {"/accountStatic"})
public class AccountStaticController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AccountDAO accountDAO = new AccountDAO();
        try {
            // Tổng số tài khoản
            int totalAccounts = accountDAO.getTotalAccounts(null);

            // Tài khoản đang hoạt động
            int activeAccounts = accountDAO.getTotalActiveAccounts();

            // Tài khoản bị khóa
            int lockedAccounts = totalAccounts - activeAccounts;

            // Tài khoản mới trong ngày, tuần, tháng
            int newAccountsDay = accountDAO.getNewAccountsByPeriod("day");
            int newAccountsWeek = accountDAO.getNewAccountsByPeriod("week");
            int newAccountsMonth = accountDAO.getNewAccountsByPeriod("month");

            // Thống kê theo vai trò
            Map<String, Integer> accountsByRole = accountDAO.getAccountsByRole();

            // Thống kê tăng trưởng theo tháng
            Map<String, Integer> monthlyGrowth = accountDAO.getMonthlyAccountGrowth();

            // Truyền dữ liệu vào request
            request.setAttribute("totalAccounts", totalAccounts);
            request.setAttribute("activeAccounts", activeAccounts);
            request.setAttribute("lockedAccounts", lockedAccounts);
            request.setAttribute("newAccountsDay", newAccountsDay);
            request.setAttribute("newAccountsWeek", newAccountsWeek);
            request.setAttribute("newAccountsMonth", newAccountsMonth);
            request.setAttribute("accountsByRole", accountsByRole);
            request.setAttribute("monthlyGrowth", monthlyGrowth);

            RequestDispatcher dispatcher = request.getRequestDispatcher("accountStatic.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AccountStaticController.class.getName()).log(Level.SEVERE, "SQL Error: " + ex.getMessage(), ex);
            request.setAttribute("errorMessage", "An error occurred while fetching account statistics: " + ex.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountStatic.jsp");
            dispatcher.forward(request, response);
        }
    }
}