package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import utils.*;

public class AccountDAO {
    
    private utils.DBContext context;

    public AccountDAO() {
        context = new utils.DBContext();
    }
    

    public boolean register(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role) throws SQLException{
        // Ensure role is one of the allowed values
        if (!role.equals("customer") && !role.equals("staff") && !role.equals("shipper")) {
            return false;  // Invalid role
        }

        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive, dateAdded) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
        Object[] params = {username,password,role,firstName,lastName,email,phoneNumber,birthDate};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
        
    }
//    public boolean register(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role){
//        // Ensure role is one of the allowed values
//        if (!role.equals("customer") && !role.equals("staff") && !role.equals("shipper")) {
//            return false;  // Invalid role
//        }
//
//        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive, dateAdded) "
//                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
//        try {
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, username);
//            ps.setString(2, password);
//            ps.setString(3, role);  // Set role (customer, staff, shipper)
//            ps.setString(4, firstName);
//            ps.setString(5, lastName);
//            ps.setString(6, email);
//            ps.setString(7, phoneNumber);
//            ps.setString(8, birthDate);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return false;
//    }
    
    
    

    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean isActive = rs.getBoolean("isActive"); // 0 or 1, mapped as true or false
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
                        isActive // true or false based on the value of isActive (bit)
                );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;  // Account not found
    }

    public boolean isEmailExistEmailOfUser(String username, String email) {
        String sql = "SELECT * FROM Account WHERE email ? = AND username != ?";  // Start by checking if the email exists

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ps.setString(2, username);  // Set the current user's username

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;  // Email exists for another user
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;  // No other user has this email
    }

    public boolean isEmailExistForEmail(String email) {
        String sql = "SELECT * FROM Account WHERE email = ?";  // Check if the email already exists in the database

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);  // Set the email parameter
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;  // Email exists for another user
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;  // No other user has this email
    }

    public boolean updateAccount(String username, String firstName, String lastName, String email, String phoneNumber, String birthDate) {
        String sql = "UPDATE Account SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, birthDate = ? WHERE username = ? AND isActive = 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setString(5, birthDate);
            ps.setString(6, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean deactivateAccount(String username) {
        String sql = "UPDATE Account SET isActive = 0 WHERE username = ? AND isActive = 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE isActive = 1"; // Chỉ lấy tài khoản đang hoạt động
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return accounts;
    }

    public boolean addStaffOrShipper(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String role) {
        if (!role.equals("staff") && !role.equals("shipper")) {
            return false; // Chỉ cho phép thêm staff hoặc shipper
        }

        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive, dateAdded) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, firstName);
            ps.setString(5, lastName);
            ps.setString(6, email);
            ps.setString(7, phoneNumber);
            ps.setString(8, birthDate);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
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
