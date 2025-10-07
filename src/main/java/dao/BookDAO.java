/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import model.product_related.Book;
import model.product_related.Brand;
import model.product_related.Category;
import model.product_related.Merchandise;
import model.product_related.OGCharacter;
import model.product_related.Product;
import model.product_related.Publisher;
import model.product_related.Series;
import utils.Utility;

/**
 *
 * @author anhkc
 */
public class BookDAO extends ProductDAO {

    public BookDAO() {
        super();

    }

    @Override
    protected Book mapResultSetToProduct(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));
        
        LocalDate eventEndDate = tool.getLocalDate(rs.getDate("eventDateStarted"), rs.getInt("eventDuration"));
        int discountPercentage = 0;
        if (eventEndDate != null) {
            discountPercentage = LocalDate.now().isAfter(eventEndDate) ? 0 : rs.getInt("discountPercentage");
        }

        LocalDate rlsDate = tool.getLocalDate(rs.getDate("releaseDate"), 0);

        LocalDateTime lastMdfTime = tool.getLocalDateTime(rs.getTimestamp("lastModifiedTime"));

        Publisher publisher = new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));
        return new  Book(publisher, rs.getString("bookDuration"),
                rs.getInt("productID"),
                rs.getString("productName"),
                rs.getDouble("price"),
                rs.getInt("stockCount"),
                category,
                rs.getString("description"),
                rlsDate,
                lastMdfTime,
                rs.getDouble("averageRating"),
                rs.getInt("numberOfRating"),
                rs.getString("specialFilter"),
                rs.getInt("adminID"),
                rs.getString("keywords"),
                rs.getString("generalCategory"),
                rs.getBoolean("productIsActive"),
                rs.getString("imageURL"),
                discountPercentage,
                eventEndDate,
                rs.getInt("salesRank"));
    }
    
    public Book getBookById(int productID, boolean isManagement) throws SQLException {

        StringBuilder sql = getCTETables("rank").append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.bookDuration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.eventDateStarted,PD.eventDuration,TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN TopSale TS ON TS.productID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "WHERE P.productID = ?\n");

        if (!isManagement) {
            sql.append(" AND P.productIsActive = 1\n");
        }

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        Object[] params = {productID};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            if (rs.next()) {
                return this.mapResultSetToProduct(rs);
            }
        }
        return null;
    }

}
