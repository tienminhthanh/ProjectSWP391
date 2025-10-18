package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Admin;
import model.Customer;
import model.DeliveryAddress;
import model.Shipper;
import model.Staff;
import utils.*;

public class AccountDAO {
    private static final AccountDAO instance = new AccountDAO();
    private final DBContext context = DBContext.getInstance();

    private AccountDAO() {
        
    }
    
    public static AccountDAO getInstance(){return instance;}
    /**
     * Cập nhật trạng thái tài khoản (kích hoạt hoặc vô hiệu hóa)
     */
    public boolean updateAccountStatus(String username, boolean accountIsActive) throws SQLException {
        String sql = "UPDATE Account SET accountIsActive = ? WHERE username = ?";
        Object[] params = {accountIsActive, username};
        return context.exeNonQuery(sql, params) > 0;
    }

    /**
     * Đăng ký tài khoản mới - chỉ dành cho khách hàng (role = 'customer')
     */
    public boolean register(String username, String password, String firstName, String lastName, String email, String phoneNumber, String birthDate, String defaultAddress) throws SQLException {
        boolean accountCreated = createAccount(username, password, firstName, lastName, email, phoneNumber, birthDate, "customer");
        if (!accountCreated) {
            return false;
        }
        Account account = getAccountByUsername(username);
        if (account == null) {
            return false;
        }
        int accountID = account.getAccountID();
        String sql = "INSERT INTO Customer (customerID, defaultDeliveryAddress, totalPurchasePoints) VALUES (?, ?, 0)";
        Object[] params = {accountID, defaultAddress};
        boolean success = false;
        success = context.exeNonQuery(sql, params) > 0;
        if(success){
            success = insertNewAddress(accountID, defaultAddress);
        }
        return success;
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

        String sql = "INSERT INTO Account (username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountIsActive, dateAdded) "
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
        sql.append(" WHERE username = ? AND accountIsActive = 1");
        params.add(username);

        return context.exeNonQuery(sql.toString(), params.toArray()) > 0;
    }

    public boolean updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE Account SET password = ? WHERE username = ? AND accountIsActive = 1"; // Check if account is active
        Object[] params = {newPassword, username}; // Parameters to pass into the query

        // Execute the query
        return context.exeNonQuery(sql, params) > 0; // Returns true if the update was successful
    }

    /**
     * Lấy danh sách tất cả tài khoản
     */
    public List<Account> getAllAccounts(String roleFilter) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Account");

        // If a role is selected, filter the accounts by role
        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" WHERE role = ?");
        }

        ResultSet rs = context.exeQuery(sql.toString(), roleFilter != null && !roleFilter.isEmpty() ? new Object[]{roleFilter} : null);

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

    public boolean updateCustomerAddress(int accountID, String newAddress) throws SQLException {
        String sql = "UPDATE Customer "
                + "SET defaultDeliveryAddress = ? "
                + "FROM Customer c "
                + "JOIN Account a ON c.customerID = a.accountID "
                + "WHERE a.accountID = ?";  // Cập nhật dựa trên accountID từ bảng Account

        Object[] params = {newAddress, accountID};  // Tham số cho câu lệnh SQL
        return context.exeNonQuery(sql, params) > 0;  // Thực thi câu lệnh SQL
    }

    public boolean insertNewAddress(int accountID, String newAddress) throws SQLException {
        String sql = "INSERT INTO DeliveryAddress (addressDetails, customerID)\n"
                + "SELECT ?, customerID\n"
                + "FROM Customer WHERE customerID = ?;";

        Object[] params = {newAddress, accountID};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean deleteAddress(int addressID) throws SQLException {
        String sql = "DELETE FROM DeliveryAddress WHERE addressID = ?";
        Object[] params = {addressID};
        return context.exeNonQuery(sql, params) > 0;
    }

    public List<DeliveryAddress> getAllAddressByCustomerID(int customerID) throws SQLException {
        List<DeliveryAddress> listAddress = new ArrayList<>();
        String sql = "select * from DeliveryAddress where customerID = ?";
        Object[] params = {customerID};
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            DeliveryAddress address = new DeliveryAddress(
                    rs.getInt("addressID"),
                    rs.getString("addressDetails"),
                    rs.getInt("customerID"));
            listAddress.add(address);
        }
        return listAddress;
    }

    public List<Account> getAccountsPaginated(String roleFilter, int page, int pageSize) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Account");

        Object[] params;
        int offset = (page - 1) * pageSize;

        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" WHERE role = ?");
            sql.append(" ORDER BY accountID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            params = new Object[]{roleFilter, offset, pageSize};
        } else {
            sql.append(" ORDER BY accountID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            params = new Object[]{offset, pageSize};
        }

        ResultSet rs = context.exeQuery(sql.toString(), params);

        while (rs.next()) {
            accounts.add(mapResultSetToAccount(rs));
        }

        return accounts;
    }

    public int getTotalAccounts(String roleFilter) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Account");
        Object[] params = null;

        // Thêm điều kiện lọc theo role nếu có
        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" WHERE role = ?");
            params = new Object[]{roleFilter};
        }

        ResultSet rs = context.exeQuery(sql.toString(), params);
        return rs.next() ? rs.getInt(1) : 0;
    }

    public int getTotalActiveAccounts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Account WHERE accountIsActive = 1";
        ResultSet rs = context.exeQuery(sql, null);
        return rs.next() ? rs.getInt(1) : 0;
    }

    // Lấy số tài khoản mới theo khoảng thời gian (day, week, month)
    public int getNewAccountsByPeriod(String period) throws SQLException {
        String sql;
        switch (period.toLowerCase()) {
            case "day":
                sql = "SELECT COUNT(*) FROM Account WHERE dateAdded >= DATEADD(day, -1, GETDATE())";
                break;
            case "week":
                sql = "SELECT COUNT(*) FROM Account WHERE dateAdded >= DATEADD(week, -1, GETDATE())";
                break;
            case "month":
                sql = "SELECT COUNT(*) FROM Account WHERE dateAdded >= DATEADD(month, -1, GETDATE())";
                break;
            default:
                return 0;
        }
        ResultSet rs = context.exeQuery(sql, null);
        return rs.next() ? rs.getInt(1) : 0;
    }

    public Map<String, Integer> getAccountsByRole() throws SQLException {
        Map<String, Integer> roleStats = new HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM Account GROUP BY role";
        ResultSet rs = context.exeQuery(sql, null);

        while (rs.next()) {
            roleStats.put(rs.getString("role"), rs.getInt("count"));
        }
        return roleStats;
    }

    public Map<String, Integer> getWeeklyAccountGrowth() throws SQLException {
        Map<String, Integer> weeklyGrowth = new LinkedHashMap<>();
        String sql = "SELECT CONCAT('Week ', DATEPART(WEEK, dateAdded), ' - ', YEAR(dateAdded)) as weekLabel, COUNT(*) as count "
                + "FROM Account "
                + "WHERE dateAdded >= DATEADD(week, -6, GETDATE()) "
                + // Lấy 6 tuần gần nhất
                "GROUP BY DATEPART(WEEK, dateAdded), YEAR(dateAdded) "
                + "ORDER BY YEAR(dateAdded), DATEPART(WEEK, dateAdded)";
        ResultSet rs = context.exeQuery(sql, null);

        while (rs.next()) {
            weeklyGrowth.put(rs.getString("weekLabel"), rs.getInt("count"));
        }
        return weeklyGrowth;
    }

    /**
     * Chuyển đổi ResultSet thành đối tượng Account
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        String role = rs.getString("role") ;
        switch (role) {
            case "admin":
                return new Admin(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("birthDate"),
                        rs.getBoolean("accountIsActive")
                );
            case "customer":
                return new Customer(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("birthDate"),
                        rs.getBoolean("accountIsActive")
                );
            case "staff":
                return new Staff(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("birthDate"),
                        rs.getBoolean("accountIsActive")
                );
            case "shipper":
                return new Shipper(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("birthDate"),
                        rs.getBoolean("accountIsActive")
                );
            default:
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
                        rs.getBoolean("accountIsActive")
                );
        }
    }
//     private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
//        return new Account(
//                rs.getInt("accountID"),
//                rs.getString("username"),
//                rs.getString("password"),
//                rs.getString("role"),
//                rs.getString("firstName"),
//                rs.getString("lastName"),
//                rs.getString("email"),
//                rs.getString("phoneNumber"),
//                rs.getString("birthDate"),
//                rs.getBoolean("accountIsActive")
//        );
//    }

    public Account getAdditionalInfo(Account account) throws SQLException {
        String sql = "";
        switch (account.getRole()) {
            case "admin":
                sql = "SELECT * from Admin where adminID = ?";
                break;
            case "customer":
                sql = "SELECT * from Customer where customerID = ?";
                break;
            case "staff":
                sql = "SELECT * from Staff where staffID = ?";
                break;
            case "shipper":
                sql = "SELECT * from Shipper where shipperID = ?";
                break;
        }

        Object[] params = {account.getAccountID()};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            switch (account.getRole()) {
                case "customer":
                    Customer customer = (Customer) account;
                    customer.setDefaultDeliveryAddress(rs.getString("defaultDeliveryAddress"));
                    customer.setTotalPurchasePoints(rs.getDouble("totalPurchasePoints"));
                    return customer;
                case "admin":
                    Admin ad = (Admin) account;
                    ad.setTotalEvents(rs.getInt("totalEvents"));
                    ad.setTotalVouchers(rs.getInt("totalVouchers"));
                    return ad;
                case "shipper":
                    Shipper shipper = (Shipper) account;

                    shipper.setTotalDeliveries(rs.getInt("totalDeliveries"));
                    return shipper;
                case "staff":
                    Staff staff = (Staff) account;
                    staff.setTotalOrders(rs.getInt("totalOrders"));

                    return staff;
            }
        }

        return account;
    }

    public List<Account> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM Account WHERE role = 'customer' AND accountIsActive = 1";
        ResultSet rs = context.exeQuery(sql, new Object[]{});
        List<Account> customers = new ArrayList<>();

        try {
            while (rs.next()) {
                Account account = new Account();
                account.setAccountID(rs.getInt("accountID"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setFirstName(rs.getString("firstName"));
                account.setLastName(rs.getString("lastName"));
                account.setEmail(rs.getString("email"));
                account.setPhoneNumber(rs.getString("phoneNumber"));
                account.setBirthDate(rs.getString("birthDate"));
                account.setAccountIsActive(rs.getBoolean("accountIsActive"));

                customers.add(account);
            }
        } finally {
            rs.close();
        }
        return customers;
    }

    public static void main(String[] args) {
        System.out.println("cúp điện");
    }

}
