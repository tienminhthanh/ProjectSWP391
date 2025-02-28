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
     * @param productID
     * @return
     * @throws SQLException 
     */
    public Product getProductById(int productID) throws SQLException {
        String sql = "SELECT Product.productID, Product.productName, Product.price, Product.stockCount, Product.categoryID, Product.description, Product.releaseDate, Product.lastModifiedTime, Product.averageRating, Product.numberOfRating, "
                + "       Product.specialFilter, Product.adminID, Product.keywords, Product.generalCategory, Product.isActive, Product.imageURL, "
                + "       Category.categoryName "
                + "FROM Product "
                + "INNER JOIN Category ON Product.categoryID = Category.categoryID "
                + "WHERE Product.productID = ?";

        Object[] params = {productID};
        ResultSet rs = context.exeQuery(sql, params);

        if (rs.next()) {
            // Create Category
            Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

            // Create and return Product object
            return new Product(
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
                    rs.getString("imageURL")
            );
        }

        return null;
    }
    
    /**
     * For view product details
     * @param productID
     * @return
     * @throws SQLException 
     */
    public Book getBookById(int productID) throws SQLException {
        String sql = "SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.duration,\n"
                + "Pub.publisherName, EP.discountPercentage, EP.eventID, E.isActive as isActiveEvent\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "LEFT JOIN Event_Product AS EP ON P.productID = EP.productID\n"
                + "LEFT JOIN Event AS E ON EP.eventID = E.eventID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?";

        Object[] params = {productID};
        ResultSet rs = context.exeQuery(sql, params);

        if (rs.next()) {

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
                    rs.getString("imageURL")
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
        String sql = "SELECT top 10 P.*, C.categoryName, EP.eventID, EP.discountPercentage,\n"
                + "E.isActive AS isActiveEvent\n"
                + "FROM Product AS P\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Event_Product AS EP ON P.productID = EP.productID\n"
                + "LEFT JOIN Event AS E ON EP.eventID = E.eventID\n"
                + "WHERE P.isActive = 1 and generalCategory = ?\n"
                + "ORDER BY NEWID()";

        Object[] params = {type};
        List<Product> bookList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            bookList.add(mapResultSetToProduct(rs));

        }
        return bookList;

    }
    
    /**
     * When user does not enter anything in the search bar
     * @param type
     * @param sortCriteria
     * @return
     * @throws SQLException 
     */
    public List<Product> getAllActiveProducts(String type, String sortCriteria) throws SQLException {
        String sql = "SELECT P.*, C.categoryName, EP.eventID, EP.discountPercentage,\n"
                + "E.isActive AS isActiveEvent\n"
                + "FROM Product AS P\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Event_Product AS EP ON P.productID = EP.productID\n"
                + "LEFT JOIN Event AS E ON EP.eventID = E.eventID\n"
                + "WHERE P.isActive = 1 and generalCategory = ?\n"
                + "ORDER BY ";

        sql += getSortOrder(sortCriteria);

        Object[] params = {type};
        ResultSet rs = context.exeQuery(sql, params);

        List<Product> productList = new ArrayList<>();
        while (rs.next()) {
            productList.add(mapResultSetToProduct(rs));
        }
        return productList;

    }
    
    
    public List<Product> getSearchResult(String query, String type, String sortCriteria) throws SQLException {
        String sql = "SELECT P.*, C.categoryName, EP.discountPercentage, EP.eventID, E.isActive AS isActiveEvent, KEY_TBL.RANK AS relevance_score \n"
                + "FROM Product AS P \n"
                + "    JOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL ON P.productID = KEY_TBL.[KEY] \n"
                + "	LEFT JOIN Category AS C  ON C.categoryID = P.categoryID \n"
                + "    LEFT JOIN Event_Product AS EP ON EP.productID = P.productID\n"
                + "	LEFT JOIN Event AS E ON E.eventID = EP.eventID\n"
                + "WHERE P.isActive = 1 AND P.generalCategory = ? \n"
                + "ORDER BY ";

        String formattedQuery = formatQuery(query);
        sql += getSortOrder(sortCriteria);

        Object[] params = {formattedQuery, type};
        ResultSet rs = context.exeQuery(sql, params);

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
            case "rank":
            case "releaseDate":
            default:
                return "P.releaseDate DESC";

        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));
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
                rs.getString("imageURL"));
    }

    public static void main(String[] args) {
        String sql = "SELECT Product.productID, Product.productName, Product.price, Product.stockCount, Product.categoryID, Product.description,\n"
                + "Product.releaseDate, Product.lastModifiedTime, Product.averageRating, Product.numberOfRating, Product.specialFilter,\n"
                + "Product.adminID, Product.keywords, Product.generalCategory, Product.isActive, Product.imageURL, Category.categoryName,\n"
                + "KEY_TBL.RANK AS relevance_score\n"
                + "FROM Category \n"
                + "JOIN Product ON Category.categoryID = Product.categoryID\n"
                + "JOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL\n"
                + "ON Product.productID = KEY_TBL.[KEY]\n"
                + "WHERE  (Product.isActive = 1) and (Product.generalCategory = ?)\n"
                + "ORDER BY ";
        System.out.println(sql);
    }

}
