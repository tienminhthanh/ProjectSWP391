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

    public boolean register(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role) throws SQLException {
        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive, dateAdded) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
        Object[] params = {username, password, role, firstName, lastName, email, phoneNumber, birthDate};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;

    }

    public Account getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Object[] params = {username};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            return mapResultSetToAccount(rs);
        }
        return null;
    }

    public boolean isEmailExistEmailOfUser(String username, String email) throws SQLException {
        String sql = "SELECT * FROM Account WHERE email  = ? AND username != ?";  // Start by checking if the email exists

        Object[] params = {email, username};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            return true;
        }
        return false;  // No other user has this email
    }

    public boolean isEmailExistForEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Account WHERE email = ?";  // Check if the email already exists in the database

        Object[] params = {email};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            return true;
        }
        return false;  // No other user has this email
    }


    public boolean updateAccount(String username, String firstName, String lastName, String email, String phoneNumber, String birthDate) throws SQLException  {
        String sql = "UPDATE Account SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, birthDate = ? WHERE username = ? AND isActive = 1";
        Object[] params = {firstName, lastName, email, phoneNumber, birthDate, username};
//>>>>>>> 
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean deactivateAccount(String username) throws SQLException {
        String sql = "UPDATE Account SET isActive = 0 WHERE username = ? AND isActive = 1";
        Object[] params = {username};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean updateAccountStatus(String username, boolean isActive) throws SQLException {
        String sql = "UPDATE Account SET isActive = ? WHERE username = ?";
        Object[] params = {isActive, username};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0; // Nếu có ít nhất một dòng bị ảnh hưởng, nghĩa là cập nhật thành công
    }

    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        ResultSet rs = context.exeQuery(sql, null);
        while (rs.next()) {
            accounts.add(mapResultSetToAccount(rs));
        }
        return accounts;
    }

    public boolean addStaffOrShipper(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role) throws SQLException {
        if (!role.equals("staff") && !role.equals("shipper")) {
            return false; // Only allow adding staff or shipper
        }

        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive, dateAdded) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
        Object[] params = {username, password, role, firstName, lastName, email, phoneNumber, birthDate};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

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
