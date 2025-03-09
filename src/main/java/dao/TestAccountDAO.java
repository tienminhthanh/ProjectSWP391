package dao;

import model.Account;
import model.Customer;
import java.sql.SQLException;

public class TestAccountDAO {
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
