/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import model.*;

/**
 *
 * @author anhkc
 */
public class ProductDAO {

    private utils.DBContext context;

    public ProductDAO() {
        this.context = new utils.DBContext();
    }

    /**
     * For add, update cart
     *
     * @param productID
     * @return
     * @throws SQLException
     */
    public Product getProductById(int productID) throws SQLException {
        StringBuilder sql = getCTEProductDiscount().append("SELECT P.*, \n"
                + "       C.categoryName, \n"
                + "       PD.discountPercentage, \n"
                + "       PD.dateStarted,\n"
                + "       PD.eventDuration\n"
                + "FROM Product AS P\n"
                + "JOIN Book B \n"
                + "    ON B.bookID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD \n"
                + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C \n"
                + "    ON C.categoryID = P.categoryID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?\n");

        Object[] params = {productID};
        ResultSet rs = context.exeQuery(sql.toString(), params);

        if (rs.next()) {
            return mapResultSetToProduct(rs);

        }

        return null;
    }

    /**
     * For view product details
     *
     * @param productID
     * @return
     * @throws SQLException
     */
    public Book getBookById(int productID) throws SQLException {
        StringBuilder sql = getCTEProductDiscount().append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.duration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.dateStarted,PD.eventDuration\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?");

        Object[] params = {productID};
        ResultSet rs = context.exeQuery(sql.toString(), params);

        if (rs.next()) {
            //Get eventEndDate
            LocalDate eventEndDate = null;
            java.sql.Date sqlDateStarted = rs.getDate("dateStarted");
            if (sqlDateStarted != null) {
                eventEndDate = sqlDateStarted.toLocalDate().plusDays(rs.getInt("eventDuration"));
            }

            // Create Category
            Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

            // Create Publisher
            Publisher publisher = new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));

            // Create and return Book object
            return new Book(
                    publisher,
                    rs.getString("duration"),
                    rs.getInt("productID"),
                    rs.getString("productName"),
                    rs.getDouble("price"),
                    rs.getInt("stockCount"),
                    category,
                    rs.getString("description"),
                    rs.getDate("releaseDate").toLocalDate(),
                    rs.getTimestamp("lastModifiedTime").toLocalDateTime(),
                    rs.getDouble("averageRating"),
                    rs.getInt("numberOfRating"),
                    rs.getString("specialFilter"),
                    rs.getInt("adminID"),
                    rs.getString("keywords"),
                    rs.getString("generalCategory"),
                    rs.getBoolean("isActive"),
                    rs.getString("imageURL"),
                    rs.getInt("discountPercentage"),
                    eventEndDate
            );
        }

        return null;
    }

    public HashMap<String, Creator> getCreatorsOfThisProduct(int productID) throws SQLException {
        String sql = "SELECT PC.creatorID, C.creatorName, C.creatorRole\n"
                + "FROM Creator AS C\n"
                + "JOIN Product_Creator AS PC ON C.creatorID = PC.creatorID\n"
                + "WHERE PC.productID = ?";
        Object[] params = {productID};

        HashMap<String, Creator> creatorMap = new HashMap<>();
        ResultSet rs = context.exeQuery(sql, params);

        while (rs.next()) {
            creatorMap.put(rs.getString(3), new Creator(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }

        return creatorMap;
    }

    public List<Genre> getGenresOfThisBook(int productID) throws SQLException {
        String sql = "SELECT BG.genreID, G.genreName\n"
                + "FROM Book_Genre AS BG\n"
                + "JOIN Genre AS G ON BG.genreID = G.genreID\n"
                + "WHERE BG.bookID = ?";
        Object[] params = {productID};

        List<Genre> genreList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, params);

        while (rs.next()) {
            genreList.add(new Genre(rs.getInt(1), rs.getString(2)));
        }

        return genreList;
    }

    public List<Product> get10RandomActiveProducts(String type) throws SQLException {

        StringBuilder sql = getCTEProductDiscount();
        if (type.equals("book")) {
            sql.append("SELECT TOP 10 P.*, \n"
                    + "       C.categoryName, \n"
                    + "       PD.discountPercentage, \n"
                    + "       PD.dateStarted,\n"
                    + "	   PD.eventDuration\n"
                    + "FROM Product AS P\n"
                    + "JOIN Book B \n"
                    + "    ON B.bookID = P.productID\n"
                    + "LEFT JOIN ProductDiscount PD \n"
                    + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                    + "LEFT JOIN Category AS C \n"
                    + "    ON C.categoryID = P.categoryID\n"
                    + "WHERE P.isActive = 1\n"
                    + "ORDER BY NEWID()");
        } else if (type.equals("merch")) {
            sql.append("SELECT TOP 10 P.*, \n"
                    + "       C.categoryName, \n"
                    + "       PD.discountPercentage, \n"
                    + "       PD.dateStarted,\n"
                    + "	   PD.eventDuration\n"
                    + "FROM Product AS P\n"
                    + "JOIN Merchandise M \n"
                    + "    ON M.merchandiseID = P.productID\n"
                    + "LEFT JOIN ProductDiscount PD \n"
                    + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                    + "LEFT JOIN Category AS C \n"
                    + "    ON C.categoryID = P.categoryID\n"
                    + "WHERE P.isActive = 1\n"
                    + "ORDER BY NEWID()");
        }

        List<Product> bookList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql.toString(), null);
        while (rs.next()) {
            bookList.add(mapResultSetToProduct(rs));

        }
        return bookList;

    }

    /**
     * When user does not enter anything in the search bar
     *
     * @param type
     * @param sortCriteria
     * @return
     * @throws SQLException
     */
    public List<Product> getAllActiveProducts(String type, String sortCriteria) throws SQLException {
        StringBuilder sql = getCTEProductDiscount();
        if (type.equals("book")) {
            sql.append("SELECT P.*, \n"
                    + "       C.categoryName, \n"
                    + "       PD.discountPercentage, \n"
                    + "       PD.dateStarted,\n"
                    + "	   PD.eventDuration\n"
                    + "FROM Product AS P\n"
                    + "JOIN Book B \n"
                    + "    ON B.bookID = P.productID\n"
                    + "LEFT JOIN ProductDiscount PD \n"
                    + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                    + "LEFT JOIN Category AS C \n"
                    + "    ON C.categoryID = P.categoryID\n"
                    + "WHERE P.isActive = 1\n");
        } else if (type.equals("merch")) {
            sql.append("SELECT P.*, \n"
                    + "       C.categoryName, \n"
                    + "       PD.discountPercentage, \n"
                    + "       PD.dateStarted,\n"
                    + "	   PD.eventDuration\n"
                    + "FROM Product AS P\n"
                    + "JOIN Merchandise M \n"
                    + "    ON M.merchandiseID = P.productID\n"
                    + "LEFT JOIN ProductDiscount PD \n"
                    + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                    + "LEFT JOIN Category AS C \n"
                    + "    ON C.categoryID = P.categoryID\n"
                    + "WHERE P.isActive = 1\n");
        }

        sql.append("ORDER BY ");
        sql.append(getSortOrder(sortCriteria));

        ResultSet rs = context.exeQuery(sql.toString(), null);

        List<Product> productList = new ArrayList<>();
        while (rs.next()) {
            productList.add(mapResultSetToProduct(rs));
        }
        return productList;

    }

    public List<Product> getSearchResult(String query, String type, String sortCriteria) throws SQLException {

        //Prepare the query with CTE first
        StringBuilder sql = getCTEProductDiscount();

        // Base SELECT clause
        sql.append("SELECT P.*,");
        sql.append("\n       C.categoryName,");
        sql.append("\n       PD.discountPercentage,");
        sql.append("\n       PD.dateStarted,");
        sql.append("\n       PD.eventDuration,");
        sql.append("\n       KEY_TBL.RANK AS relevance_score");
        sql.append("\nFROM Product AS P");
        sql.append("\nJOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL");
        sql.append("\n    ON P.productID = KEY_TBL.[KEY]");

        // Type-specific JOIN
        if (type.equals("book")) {
            sql.append("\nJOIN Book B");
            sql.append("\n    ON B.bookID = P.productID");
        } else if (type.equals("merch")) {
            sql.append("\nJOIN Merchandise M");
            sql.append("\n    ON M.merchandiseID = P.productID");
        }

        // Common LEFT JOINs and WHERE clause
        sql.append("\nLEFT JOIN ProductDiscount PD");
        sql.append("\n    ON P.productID = PD.productID AND PD.rn = 1");
        sql.append("\nLEFT JOIN Category AS C");
        sql.append("\n    ON C.categoryID = P.categoryID");
        sql.append("\nWHERE P.isActive = 1");

        // Append sorting
        sql.append("\nORDER BY ");
        String formattedQuery = formatQuery(query);
        sql.append(getSortOrder(sortCriteria));

        // Execute query
        Object[] params = {formattedQuery};
        ResultSet rs = context.exeQuery(sql.toString(), params);

        List<Product> productList = new ArrayList<>();
        while (rs.next()) {
            productList.add(mapResultSetToProduct(rs));
        }
        return productList;
    }

    private String formatQuery(String query) {
        String[] queryParts = query.split("\\s+");
        for (int i = 0; i < queryParts.length; i++) {
            queryParts[i] = "\"" + queryParts[i] + "*\"";
        }

        return String.join(" OR ", queryParts);
    }

    private String getSortOrder(String sortCriteria) {
        switch (sortCriteria) {
            case "relevance":
                return "KEY_TBL.RANK DESC, P.productName ASC";
            case "name":
                return "P.productName ASC";
            case "hotDeal":
                return "PD.discountPercentage DESC";
            case "priceLowToHigh":
                return "P.price ASC";
            case "priceHighToLow":
                return "P.price DESC";
            case "rating":
                return "P.averageRating DESC";
            case "releaseDate":
            default:
                return "P.releaseDate DESC";

        }
    }

    private StringBuilder getCTEProductDiscount() {

        return new StringBuilder("WITH ProductDiscount AS (\n"
                + "SELECT ep.productID,\n"
                + "e.dateStarted,\n"
                + "e.duration as eventDuration,\n"
                + "ep.discountPercentage,\n"
                + "ROW_NUMBER() OVER (PARTITION BY ep.productID ORDER BY e.dateStarted DESC, ep.eventID DESC) AS rn\n"
                + "FROM Event e\n"
                + "JOIN Event_Product ep ON e.eventID = ep.eventID\n"
                + "WHERE e.isActive = 1\n"
                + "AND GETDATE() <= DATEADD(day, e.duration, e.dateStarted)\n"
                + "AND GETDATE() >= e.dateStarted\n"
                + ")\n");
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

        LocalDate eventEndDate = null;
        java.sql.Date sqlDateStarted = rs.getDate("dateStarted");
        if (sqlDateStarted != null) {
            eventEndDate = sqlDateStarted.toLocalDate().plusDays(rs.getInt("eventDuration"));
        }

        return new Product(rs.getInt("productID"),
                rs.getString("productName"),
                rs.getDouble("price"),
                rs.getInt("stockCount"),
                category,
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getTimestamp("lastModifiedTime").toLocalDateTime(),
                rs.getDouble("averageRating"),
                rs.getInt("numberOfRating"),
                rs.getString("specialFilter"),
                rs.getInt("adminID"),
                rs.getString("keywords"),
                rs.getString("generalCategory"),
                rs.getBoolean("isActive"),
                rs.getString("imageURL"),
                rs.getInt("discountPercentage"),
                eventEndDate);
    }

    public static void main(String[] args) {
        ProductDAO myDAO = new ProductDAO();
        StringBuilder sql = myDAO.getCTEProductDiscount().append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.duration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.dateStarted,PD.duration\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?");
        System.out.println(sql);
    }

}
