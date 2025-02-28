package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;

public class AccountDAO {

    private utils.DBContext context;

    public AccountDAO() {
        context = new utils.DBContext();
    }

    /**
     * Cập nhật trạng thái tài khoản (kích hoạt hoặc vô hiệu hóa)
     */
    public boolean updateAccountStatus(String username, boolean isActive) throws SQLException {
        String sql = "UPDATE Account SET isActive = ? WHERE username = ?";
        Object[] params = {isActive, username};
        return context.exeNonQuery(sql, params) > 0;
    }

    /**
     * Đăng ký tài khoản mới - chỉ dành cho khách hàng (role = 'customer')
     */
    public boolean register(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate) throws SQLException {
        return createAccount(username, password, firstName, lastName, email, phoneNumber, birthDate, "customer");
    }

    public Account getAccountByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Account WHERE email = ?";
        Object[] params = {email};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next() ? mapResultSetToAccount(rs) : null;
    }

    /**
     * Tạo tài khoản với quyền chỉ định (dành cho admin tạo staff hoặc shipper)
     */
    public boolean createAccount(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role) throws SQLException {
        if (!role.equals("customer") && !role.equals("staff") && !role.equals("shipper")) {
            return false; // Chỉ cho phép các quyền hợp lệ
        }

        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive, dateAdded) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
        Object[] params = {username, password, role, firstName, lastName, email, phoneNumber, birthDate};
        return context.exeNonQuery(sql, params) > 0;
    }

    /**
     * Kiểm tra xem email đã tồn tại chưa Nếu `username` null => kiểm tra email
     * trong toàn bộ hệ thống (cho đăng ký) Nếu `username` không null => kiểm
     * tra email trừ tài khoản của chính user đó (cho cập nhật)
     */
    public boolean isEmailExist(String email, String username) throws SQLException {
        String sql = username == null
                ? "SELECT 1 FROM Account WHERE email = ?"
                : "SELECT 1 FROM Account WHERE email = ? AND username != ?";
        Object[] params = username == null ? new Object[]{email} : new Object[]{email, username};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next();
    }

    /**
     * Lấy thông tin tài khoản dựa trên username
     */
    public Account getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Object[] params = {username};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next() ? mapResultSetToAccount(rs) : null;
    }

    public Account getIDByUsername(String username) throws SQLException {
        String sql = "SELECT accountID FROM Account WHERE username = ?";
        Object[] params = {username};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next() ? mapResultSetToAccount(rs) : null;
    }

    /**
     * Cập nhật thông tin tài khoản
     */
    public boolean updateAccount(String username, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE Account SET ");
        List<Object> params = new ArrayList<>();

        if (firstName != null) {
            sql.append("firstName = ?, ");
            params.add(firstName);
        }
        if (lastName != null) {
            sql.append("lastName = ?, ");
            params.add(lastName);
        }
        if (email != null) {
            sql.append("email = ?, ");
            params.add(email);
        }
        if (phoneNumber != null) {
            sql.append("phoneNumber = ?, ");
            params.add(phoneNumber);
        }
        if (birthDate != null) {
            sql.append("birthDate = ?, ");
            params.add(birthDate);
        }
        if (role != null) {
            sql.append("role = ?, ");
            params.add(role);
        }

        if (params.isEmpty()) {
            return false; // Không có gì để cập nhật
        }
        sql.setLength(sql.length() - 2); // Xóa dấu ", " cuối cùng
        sql.append(" WHERE username = ? AND isActive = 1");
        params.add(username);

        return context.exeNonQuery(sql.toString(), params.toArray()) > 0;
    }

    public boolean updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE Account SET password = ? WHERE username = ? AND isActive = 1"; // Check if account is active
        Object[] params = {newPassword, username}; // Parameters to pass into the query

        // Execute the query
        return context.exeNonQuery(sql, params) > 0; // Returns true if the update was successful
    }

    /**
     * Lấy danh sách tất cả tài khoản
     */
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        ResultSet rs = context.exeQuery(sql, null);
        while (rs.next()) {
            accounts.add(mapResultSetToAccount(rs));
        }
        return accounts;
    }

    public boolean removeEmailFromAccount(String username) throws SQLException {
        String sql = "UPDATE Account SET email = ? WHERE username = ?";
        Object[] params = {"", username};  // Đặt email thành rỗng
        return context.exeNonQuery(sql, params) > 0;
    }

    /**
     * Chuyển đổi ResultSet thành đối tượng Account
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("accountID"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("phoneNumber"),
                rs.getString("birthDate"),
                rs.getBoolean("isActive")
        );
    }
}
