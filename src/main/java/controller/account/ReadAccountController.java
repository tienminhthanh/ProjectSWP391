package controller.account;

import dao.AccountDAO;
import model.Account;
import jakarta.servlet.RequestDispatcher;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.DeliveryAddress;

@WebServlet(name = "ReadAccountServlet", urlPatterns = {"/readAccount"})
public class ReadAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            // Lấy thêm thông tin dựa theo role
            AccountDAO accountDAO = new AccountDAO();
            try {

                account = accountDAO.getAdditionalInfo(account);
            } catch (SQLException ex) {
                Logger.getLogger(ReadAccountController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Cập nhật tài khoản có đầy đủ thông tin vào session
            session.setAttribute("account", account);
            request.setAttribute("account", account);
            RequestDispatcher dispatcher = request.getRequestDispatcher("accountRead.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAO();

        try {
            // Lấy tài khoản theo username
            String username = "anhk_cus2"; // Thay bằng username của bạn
            Account account = accountDAO.getAccountByUsername(username);

            if (account == null) {
                System.out.println("⚠ Không tìm thấy tài khoản với username: " + username);
                return;
            }

            // Lấy thông tin bổ sung dựa theo role
            account = accountDAO.getAdditionalInfo(account);

            // Kiểm tra nếu account là customer
            if (account instanceof Customer) {
                Customer customer = (Customer) account;
                System.out.println("✅ Đã lấy thông tin tài khoản khách hàng:");
                System.out.println("Username: " + customer.getUsername());
                System.out.println("Total Purchase Points: " + customer.getTotalPurchasePoints());
                System.out.println("Default Delivery Address: " + customer.getDefaultDeliveryAddress());
            } else {
                System.out.println("⚠ Tài khoản không phải là khách hàng.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi xảy ra khi truy vấn dữ liệu.");
        }
    }
}
