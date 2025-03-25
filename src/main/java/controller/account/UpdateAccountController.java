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
import model.Customer;

@WebServlet(name = "UpdateAccountServlet", urlPatterns = {"/updateAccount"})
public class UpdateAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = request.getParameter("username");
        AccountDAO accountDAO = new AccountDAO();

        try {
            Account account = accountDAO.getAccountByUsername(username);
            if (account != null) {
                request.setAttribute("account", account);
                request.getRequestDispatcher("accountUpdate.jsp").forward(request, response);
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account loggedInAccount = (Account) session.getAttribute("account");

        if (loggedInAccount == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String defaultDeliveryAddress = request.getParameter("defaultDeliveryAddress");

        // Chỉ admin mới có quyền cập nhật vai trò
        String role = "admin".equals(loggedInAccount.getRole()) ? request.getParameter("role") : null;

        AccountDAO accountDAO = new AccountDAO();

        try {
            // Cập nhật thông tin tài khoản chung
            boolean success = accountDAO.updateAccount(username, firstName, lastName, email, phoneNumber, birthDate, role);

            // Nếu người dùng là khách hàng, cập nhật địa chỉ giao hàng
            if (loggedInAccount instanceof Customer && defaultDeliveryAddress != null) {
                Customer customer = (Customer) loggedInAccount;
                customer.setDefaultDeliveryAddress(defaultDeliveryAddress);
                accountDAO.updateCustomerAddress(customer.getAccountID(), defaultDeliveryAddress);
            }

            if (success) {
                Account updatedAccount = accountDAO.getAccountByUsername(username);

                // Chỉ cập nhật session nếu user đang cập nhật chính tài khoản của mình
                if (loggedInAccount.getUsername().equals(username)) {
                    session.setAttribute("account", updatedAccount);
                }

                // Điều hướng dựa trên vai trò
                if ("admin".equals(loggedInAccount.getRole())) {
                    response.sendRedirect("listAccount?message=Account updated successfully!");
                } else {
                    response.sendRedirect("readAccount?message=Account updated successfully!");
                }
            } else {
                request.setAttribute("message", "Account update failed! Please try again.");
                request.getRequestDispatcher("accountUpdate.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
