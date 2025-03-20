/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Voucher;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class VoucherDAO {

    private utils.DBContext context;

    public VoucherDAO() {
        context = new utils.DBContext();
    }

    public List<Voucher> getVoucherByPage(String searchKeyword, String searchType, String filtered, int page, int pageSize) {
        List<Voucher> list = new ArrayList<>();
        List<Object> paramsList = new ArrayList<>();

        String sql = "SELECT * FROM Voucher WHERE 1=1";

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += " AND (voucherName LIKE ? OR voucherType LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, voucherID) LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, voucherValue) LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, voucherQuantity) LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, [minimumPurchaseAmount]) LIKE ?)";

            String keywordPattern = "%" + searchKeyword.trim() + "%";
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
        }

        if (filtered != null && !filtered.isEmpty()) {
            sql += "AND voucherIsActive = ? ";
            paramsList.add(Boolean.parseBoolean(filtered));
        }
        if (searchType != null && !searchType.isEmpty()) {
            sql += " AND voucherType = ? ";
            paramsList.add(searchType);
        }

        // Thêm ORDER BY và OFFSET để hỗ trợ phân trang
        sql += " ORDER BY voucherID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        paramsList.add((page - 1) * pageSize);
        paramsList.add(pageSize);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("SQL Query: " + sql);
        System.out.println("Params: " + paramsList);

        try {
            Object[] params = paramsList.toArray();
            ResultSet rs = context.exeQuery(sql, params);

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double value = rs.getDouble(3);
                int quantity = rs.getInt(4);
                int minimum = rs.getInt(5);
                String dateCreated = rs.getString(6);
                int duration = rs.getInt(7);
                int adminID = rs.getInt(8);
                boolean isActive = rs.getBoolean(9);
                String type = rs.getString(10);
                Double maximum = rs.getDouble(11);
                String dateStarted = rs.getString(12);

                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);

                Voucher voucher = new Voucher(id, name, value, quantity, minimum, dateCreated, duration, adminID, isActive, !LocalDate.now().isAfter(expiryDate), type, maximum, dateStarted);
                list.add(voucher);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return list;

    }

    public int getTotalVoucher(String searchName, String searchType, String filtered) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Voucher WHERE 1=1";
        List<Object> paramsList = new ArrayList<>();
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql += " AND (voucherName LIKE ? OR voucherType LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, voucherID) LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, voucherValue) LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, voucherQuantity) LIKE ? \n"
                    + "    OR CONVERT(VARCHAR, [minimumPurchaseAmount]) LIKE ?)";
            String keywordPattern = "%" + searchName.trim() + "%";
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
            paramsList.add(keywordPattern);
        }
        if (searchType != null && !searchType.isEmpty()) {
            sql += "AND voucherType = ? ";
            paramsList.add(searchType);
        }
        if (filtered != null && !filtered.isEmpty()) {
            sql += "AND voucherIsActive = ? ";
            paramsList.add(Boolean.parseBoolean(filtered));
        }

        try {
            Object[] params = paramsList.toArray();
            ResultSet rs = context.exeQuery(sql, params);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public Voucher getVoucherByID(int voucherID) {
        try {
            String sql = "SELECT * FROM [dbo].[Voucher] WHERE [voucherID] = ?";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Object[] params = {voucherID};
            ResultSet rs = context.exeQuery(sql, params);
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double value = rs.getDouble(3);
                int quantity = rs.getInt(4);
                int mminimum = rs.getInt(5);
                String dateCreated = rs.getString(6);
                int duration = rs.getInt(7);
                int adminID = rs.getInt(8);
                boolean isActive = rs.getBoolean(9);
                String type = rs.getString(10);
                Double maximum = rs.getDouble(11);
                String dateStarted = rs.getString(12);
                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                return new Voucher(id, name, value, quantity, mminimum, dateCreated, duration, adminID, isActive, !LocalDate.now().isAfter(expiryDate), type, maximum, dateStarted);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Voucher> getListVoucher() {
        List<Voucher> listVoucher = new ArrayList<>();

        try {
            String sql = "SELECT * FROM [dbo].[Voucher]";
            ResultSet rs = context.exeQuery(sql, null);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double value = rs.getDouble(3);
                int quantity = rs.getInt(4);
                int mminimum = rs.getInt(5);
                String dateCreated = rs.getString(6);
                int duration = rs.getInt(7);
                int adminID = rs.getInt(8);
                boolean isActive = rs.getBoolean(9);
                String type = rs.getString(10);
                Double maximum = rs.getDouble(11);
                String dateStarted = rs.getString(12);
                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                Voucher voucher = new Voucher(id, name, value, quantity, mminimum, dateCreated, duration, adminID, isActive, !LocalDate.now().isAfter(expiryDate), type, maximum, dateStarted);

                listVoucher.add(voucher);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return listVoucher;
    }

    public List<Voucher> getListVoucherComeSoon() {
        List<Voucher> listVoucher = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Voucher]  \n"
                + "WHERE voucherDateStarted BETWEEN ? AND ?  \n"
                + "ORDER BY voucherDateStarted ASC;";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalDate threeDaysLater = LocalDate.now().plusDays(3);
            Object[] params = {tomorrow, threeDaysLater};
            ResultSet rs = context.exeQuery(sql, params);

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double value = rs.getDouble(3);
                int quantity = rs.getInt(4);
                int mminimum = rs.getInt(5);
                String dateCreated = rs.getString(6);
                int duration = rs.getInt(7);
                int adminID = rs.getInt(8);
                boolean isActive = rs.getBoolean(9);
                String dateStarted = rs.getString(12);
                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                String type = rs.getString(10);
                Double maximum = rs.getDouble(11);
                Voucher voucher = new Voucher(id, name, value, quantity, mminimum, dateCreated, duration, adminID, isActive, !LocalDate.now().isAfter(expiryDate), type, maximum, dateStarted);

                listVoucher.add(voucher);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return listVoucher;
    }

    public List<Voucher> getListVoucherAvailableNow() {
        List<Voucher> listVoucher = new ArrayList<>();
        String sql = "SELECT * FROM Voucher \n"
                + "WHERE voucherDateStarted <= ? \n"
                + "AND DATEADD(DAY, voucherDuration, voucherDateStarted) >= ?";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate today = LocalDate.now();
            Object[] params = {today, today};
            ResultSet rs = context.exeQuery(sql, params);

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double value = rs.getDouble(3);
                int quantity = rs.getInt(4);
                int mminimum = rs.getInt(5);
                String dateCreated = rs.getString(6);
                int duration = rs.getInt(7);
                int adminID = rs.getInt(8);
                boolean isActive = rs.getBoolean(9);
                String dateStarted = rs.getString(12);
                LocalDate createDate = LocalDate.parse(dateStarted, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                String type = rs.getString(10);
                Double maximum = rs.getDouble(11);
                Voucher voucher = new Voucher(id, name, value, quantity, mminimum, dateCreated, duration, adminID, isActive, !LocalDate.now().isAfter(expiryDate), type, maximum, dateStarted);

                listVoucher.add(voucher);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return listVoucher;
    }

    public boolean updateVoucher(Voucher voucher) {
        String sql = "UPDATE [dbo].[Voucher]\n"
                + "   SET [voucherName] = ?\n"
                + "      ,[voucherValue] = ?\n"
                + "      ,[voucherQuantity] = ?\n"
                + "      ,[minimumPurchaseAmount] = ?\n"
                + "      ,[voucherDuration] = ?\n"
                + "      ,[voucherType] = ?\n"
                + "      ,[maxDiscountAmount] = ?\n"
                + "      ,[voucherDateStarted] = ?\n"
                + "      ,[voucherIsActive] = ?\n"
                + "      WHERE [voucherID] = ?";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate today = LocalDate.now();
            LocalDate createDate = LocalDate.parse(voucher.getDateCreated(), formatter);
            LocalDate expiryDate = createDate.plusDays(voucher.getDuration());

            Object[] params = {voucher.getVoucherName(),
                voucher.getVoucherValue(),
                voucher.getQuantity(),
                voucher.getMinimumPurchaseAmount(),
                voucher.getDuration(),
                voucher.getVoucherType(),
                voucher.getMaxDiscountAmount(),
                voucher.getDateStarted(),
                !LocalDate.now().isAfter(expiryDate),
                voucher.getVoucherID()};

            voucher.setExpiry(!LocalDate.now().isAfter(expiryDate));
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteVoucher(int id) {
        try {
            Voucher voucher = getVoucherByID(id);

            if (!voucher.isExpiry() && !voucher.isIsActive()) {
                return false;
            }

            String sql = "UPDATE [dbo].[Voucher]\n"
                    + "   SET [voucherIsActive] = ?\n"
                    + " WHERE [voucherID] = ?";
            Object[] params = {!voucher.isIsActive(), id};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean addVoucher(Voucher voucher) {
        try {
            String sql = "INSERT INTO [dbo].[Voucher]"
                    + "([voucherName], [voucherValue], [voucherQuantity], [minimumPurchaseAmount],"
                    + "[voucherDateCreated], [voucherDuration], [adminID], [voucherIsActive], [voucherType], [maxDiscountAmount], [voucherDateStarted]) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Object[] params = {
                voucher.getVoucherName(),
                voucher.getVoucherValue(),
                voucher.getQuantity(),
                voucher.getMinimumPurchaseAmount(),
                voucher.getDateCreated(),
                voucher.getDuration(),
                voucher.getAdminID(),
                voucher.isIsActive(),
                voucher.getVoucherType(),
                voucher.getMaxDiscountAmount(),
                voucher.getDateStarted()};

            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void main(String[] args) {
        VoucherDAO vd = new VoucherDAO();
        System.out.println(vd.getVoucherByPage("name", "", "", 1, 5));
    }
}