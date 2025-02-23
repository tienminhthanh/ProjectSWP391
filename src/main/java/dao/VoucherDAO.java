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
                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                Voucher voucher = new Voucher(id, name, value, quantity, mminimum, dateCreated, duration, adminID, !LocalDate.now().isAfter(expiryDate));

                listVoucher.add(voucher);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return listVoucher;
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

                LocalDate createDate = LocalDate.parse(dateCreated, formatter);
                LocalDate expiryDate = createDate.plusDays(duration);
                return new Voucher(id, name, value, quantity, mminimum, dateCreated, duration, adminID, !LocalDate.now().isAfter(expiryDate));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean updateVoucher(Voucher voucher) {
        try {
            String sql = "UPDATE [dbo].[Voucher]\n"
                    + "   SET [voucherName] = ?\n"
                    + "      ,[voucherValue] = ?\n"
                    + "      ,[quantity] = ?\n"
                    + "      ,[minimumPurchaseAmount] = ?\n"
                    + "      ,[duration] = ?\n"
                    + "      WHERE [voucherID] = ?";
            Object[] params = {voucher.getVoucherName(),
                voucher.getVoucherValue(),
                voucher.getQuantity(),
                voucher.getMinimumPurchaseAmount(),
                voucher.getDuration(),
                voucher.getVoucherID()};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteVoucher(int id) {
        String sql = "DELETE FROM [dbo].[Voucher]\n"
                + "      WHERE [voucherID] = ?";
        try {
            Object[] params = {id};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean addVoucher(String name, double value, int quantity, int minumum, String dateCreated, int duration, int adminID) {
        String sql = "INSERT INTO [dbo].[Voucher]\n"
                + "           ([voucherName]\n"
                + "           ,[voucherValue]\n"
                + "           ,[quantity]\n"
                + "           ,[minimumPurchaseAmount]\n"
                + "           ,[dateCreated]\n"
                + "           ,[duration]\n"
                + "           ,[adminID])\n"
                + "     VALUES (?, ?, ?, ?, ?, ?, ?)\n";
        try {
            Object[] params = {name, value, quantity, minumum, dateCreated, duration, adminID};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (Exception e) {
        }
        return false;
    }

}
