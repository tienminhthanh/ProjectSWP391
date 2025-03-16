/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import model.*;
import dao.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class EventProductDAO {

    private utils.DBContext context;

    public EventProductDAO() {
        context = new utils.DBContext();
    }

    public boolean deleteProductFromEvent(int eventId, int productId) {
        String sql = "DELETE FROM [dbo].[Event_Product]\n"
                + "      WHERE eventID = ? and productID = ?";

        try {
            Object[] params = {eventId, productId};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean addProductToEvent(int eventId, int productId, int discountPercent) {
        String sql = "INSERT INTO [dbo].[Event_Product] ([eventID], [productID], [discountPercentage]) VALUES (?, ?, ?)";

        try {
            Object[] params = {eventId, productId, discountPercent};
            int rowsAffected = context.exeNonQuery(sql, params);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//    public int getTotalProductToAddForEvent(int eventId) {
//        String sql = "SELECT COUNT(*) FROM Product p "
//                + "LEFT JOIN Event_Product ep ON p.productID = ep.productID AND ep.eventID = ? "
//                + "WHERE ep.productID IS NULL AND ([specialFilter] != 'pre-order' OR [specialFilter] IS NULL)";
//        try {
//            ResultSet rs = context.exeQuery(sql, new Object[]{eventId});
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
    public List<Product> getListProductToAddForEvent(int eventId) {
        List<Product> listProduct = new ArrayList<>();
        ProductDAO pDao = new ProductDAO();
        String sql = "SELECT \n"
                + "    p.productID, \n"
                + "    p.productName, \n"
                + "    p.price, \n"
                + "    p.stockCount, \n"
                + "    p.categoryID, \n"
                + "    p.description, \n"
                + "    p.releaseDate, \n"
                + "    p.lastModifiedTime, \n"
                + "    p.averageRating, \n"
                + "    p.numberOfRating, \n"
                + "    p.specialFilter, \n"
                + "    p.adminID, \n"
                + "    p.keywords, \n"
                + "    p.generalCategory, \n"
                + "    p.isActive, \n"
                + "    p.imageURL\n"
                + "FROM Product p\n"
                + "LEFT JOIN Event_Product ep ON p.productID = ep.productID AND ep.eventID = ?\n"
                + "WHERE ep.productID IS NULL\n"
                + "AND ([specialFilter] != 'pre-order' or [specialFilter] is null);";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                .optionalEnd()
                .toFormatter();
        try {
            Object[] params = {eventId};
            ResultSet rs = context.exeQuery(sql, params);
            while (rs.next()) {
                int productID = rs.getInt(1);
                String productName = rs.getString(2);
                double price = rs.getDouble(3);
                int stockCount = rs.getInt(4);
                int categoryID = rs.getInt(5);
                Category category = pDao.getCategoryById(categoryID);
                String description = rs.getString(6);
                String releaseDate_raw = rs.getString(7);
                LocalDate releaseDate = LocalDate.parse(releaseDate_raw, formatter);
                String lastModifiedTime_raw = rs.getString(8);
                LocalDateTime lastModifiedTime = LocalDateTime.parse(lastModifiedTime_raw, dateTimeFormatter);
                double averageRating = rs.getDouble(9);
                int numberOfRating = rs.getInt(10);
                String specialFilter = rs.getString(11);
                int adminID = rs.getInt(12);
                String keywords = rs.getString(13);
                String generalCategory = rs.getString(14);
                boolean isActive = rs.getBoolean(15);
                String imageURL = rs.getString(16);

                Product product = new Product(productID, productName, price, stockCount, category, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, stockCount, releaseDate);
                listProduct.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProduct;
    }

    public List<Product> getListProductInEvent(int eventID) {
        List<Product> listProductInEvent = new ArrayList<>();
        ProductDAO pDao = new ProductDAO();
        String sql = "SELECT p.[productID],\n"
                + "       p.[productName],\n"
                + "       p.[price],\n"
                + "       p.[stockCount],\n"
                + "       p.[categoryID],\n"
                + "       p.[description],\n"
                + "       p.[releaseDate],\n"
                + "       p.[lastModifiedTime],\n"
                + "       p.[averageRating],\n"
                + "       p.[numberOfRating],\n"
                + "       p.[specialFilter],\n"
                + "       p.[adminID],\n"
                + "       p.[keywords],\n"
                + "       p.[generalCategory],\n"
                + "       p.[isActive],\n"
                + "       p.[imageURL]\n"
                + "FROM [WIBOOKS].[dbo].[Product] p\n"
                + "JOIN [WIBOOKS].[dbo].[Event_Product] ep\n"
                + "    ON p.[productID] = ep.[productID]\n"
                + "WHERE ep.[eventID] = ?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                .optionalEnd()
                .toFormatter();
        try {
            Object[] params = {eventID};
            ResultSet rs = context.exeQuery(sql, params);
            while (rs.next()) {
                int productID = rs.getInt(1);
                String productName = rs.getString(2);
                double price = rs.getDouble(3);
                int stockCount = rs.getInt(4);
                int categoryID = rs.getInt(5);
                Category category = pDao.getCategoryById(categoryID);
                String description = rs.getString(6);
                String releaseDate_raw = rs.getString(7);
                LocalDate releaseDate = LocalDate.parse(releaseDate_raw, formatter);
                String lastModifiedTime_raw = rs.getString(8);
                LocalDateTime lastModifiedTime = LocalDateTime.parse(lastModifiedTime_raw, dateTimeFormatter);
                double averageRating = rs.getDouble(9);
                int numberOfRating = rs.getInt(10);
                String specialFilter = rs.getString(11);
                int adminID = rs.getInt(12);
                String keywords = rs.getString(13);
                String generalCategory = rs.getString(14);
                boolean isActive = rs.getBoolean(15);
                String imageURL = rs.getString(16);

                Product product = new Product(productID, productName, price, stockCount, category, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, stockCount, releaseDate);
                listProductInEvent.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProductInEvent;
    }

    public List<EventProduct> getListEventProduct(int eventID) {
        List<EventProduct> listEventProduct = new ArrayList<>();
        String sql = "SELECT [productID]\n"
                + "      ,[discountPercentage]\n"
                + "  FROM [dbo].[Event_Product]"
                + "  WHERE [eventID] = ?";
        try {
            Object[] params = {eventID};
            ResultSet rs = context.exeQuery(sql, params);

            while (rs.next()) {
                int productID = rs.getInt(1);
                int discountPercent = rs.getInt(2);
                EventProduct ep = new EventProduct(eventID, productID, discountPercent);
                listEventProduct.add(ep);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEventProduct;
    }

    public int getTotalProductInAnEvent(int eventId) {
        String sql = "SELECT COUNT (*)\n"
                + "FROM [WIBOOKS].[dbo].[Event_Product]\n"
                + "WHERE eventID = ?";
        try {
            Object[] params = {eventId};
            ResultSet rs = context.exeQuery(sql, params);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        EventProductDAO ep = new EventProductDAO();
        System.out.println(ep.getListProductToAddForEvent(41).size());
    }
}
