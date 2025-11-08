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
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.DeliveryAddress;

@WebServlet(name = "UpdateAccountServlet", urlPatterns = {"/updateAccount"})
public class UpdateAccountController extends HttpServlet {
private final AccountDAO accountDAO = AccountDAO.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        List<DeliveryAddress> listAddress = new ArrayList<>();
        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect("login.jsp");

            return;
        }
        Account acc = (Account) session.getAttribute("account");

        String username = request.getParameter("username");

        try {
            Account account = accountDAO.getAccountByUsername(username);
            listAddress = accountDAO.getAllAddressByCustomerID(acc.getAccountID());
            if (account != null) {
                request.setAttribute("account", account);
                request.setAttribute("addressList", listAddress);

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
        String actionType = request.getParameter("actionType");
        String selectedAddress = request.getParameter("selectedAddress");
        AccountLib lib = new AccountLib();
        if (!lib.isValidPhoneNumber(phoneNumber)) {
            session.setAttribute("message", "The phone number must be phone Viet Nam has 10 number");
            session.setAttribute("account", loggedInAccount);
            response.sendRedirect("updateAccount?username=" + username);
            return;
        }

        // Chỉ admin mới có quyền cập nhật vai trò
        String role = "admin".equals(loggedInAccount.getRole()) ? request.getParameter("role") : null;


        try {
            if (accountDAO.isEmailExist(email, username)) {
                session.setAttribute("message", "Email already exists! Please try again.");
                session.setAttribute("account", loggedInAccount);
                response.sendRedirect("updateAccount?username=" + username);
                return;
            }

            // Cập nhật thông tin tài khoản chung
            boolean success = accountDAO.updateAccount(username, firstName, lastName, email, phoneNumber, birthDate, role);

            // Nếu người dùng là khách hàng, cập nhật địa chỉ giao hàng
            if (loggedInAccount instanceof Customer && defaultDeliveryAddress != null) {
                Customer customer = (Customer) loggedInAccount;
                customer.setDefaultDeliveryAddress(defaultDeliveryAddress);
                accountDAO.updateCustomerAddress(customer.getAccountID(), defaultDeliveryAddress);

            }

            if ("addAddress".equals(actionType)) {
                accountDAO.insertNewAddress(loggedInAccount.getAccountID(), selectedAddress);
                response.sendRedirect("updateAccount?username=" + loggedInAccount.getUsername());
                return;
            } else if ("deleteAddress".equals(actionType)) {
                int id = Integer.parseInt(selectedAddress);
                accountDAO.deleteAddress(id);
                response.sendRedirect("updateAccount?username=" + loggedInAccount.getUsername());
                return;

            }
            // Cập nhật thông tin tài khoản

            // Nếu cập nhật thành công, chỉ cập nhật session nếu người dùng đang cập nhật chính tài khoản của mình
            if (success) {
                Account updatedAccount = accountDAO.getAccountByUsername(username);

                // Chỉ cập nhật session nếu user đang cập nhật chính tài khoản của mình
                if (loggedInAccount.getUsername().equals(username)) {
                    loggedInAccount = updatedAccount;
                    session.setAttribute("account", updatedAccount); // Cập nhật lại session
                }

                // Điều hướng dựa trên vai trò
                if (loggedInAccount.getRole().equals("admin")) {
                    response.sendRedirect("listAccount");
                } else {
                    response.sendRedirect("readAccount?message=Account updated successfully!");
                }

            } else {
                session.setAttribute("message", "Account update failed! Please try again.");
                request.getRequestDispatcher("accountUpdate.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
