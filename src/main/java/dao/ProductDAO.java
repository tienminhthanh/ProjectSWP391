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

    public List<Product> select10RandomActiveBooks() throws SQLException {
        String sql = "SELECT top 10 Product.productID, Product.productName, Product.price, Product.stockCount, Product.categoryID, Product.description, Product.releaseDate, Product.lastModifiedTime, Product.averageRating, Product.numberOfRating, \n"
                + "                  Product.specialFilter, Product.adminID, Product.keywords, Product.generalCategory, Product.isActive, Product.imageURL, Category.categoryName\n"
                + "FROM     Category INNER JOIN\n"
                + "                  Product ON Category.categoryID = Product.categoryID\n"
                + "WHERE  (Product.isActive = 1) AND (Product.generalCategory = 'book')\n"
                + "order by newid()";
        List<Product> bookList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, null);
        while (rs.next()) {
            Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));
            Product product = new Product(
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
            bookList.add(product);

        }
        return bookList;

    }

    public Book getBookById(int productID) throws SQLException {
        String sql = "SELECT Product.productID, Product.productName, Product.price, Product.stockCount, Product.categoryID, Product.description, Product.releaseDate, Product.lastModifiedTime, Product.averageRating, Product.numberOfRating, "
                + "       Product.specialFilter, Product.adminID, Product.keywords, Product.generalCategory, Product.isActive, Product.imageURL, "
                + "       Category.categoryName, Book.publisherID, Book.duration, Publisher.publisherName "
                + "FROM Product "
                + "INNER JOIN Category ON Product.categoryID = Category.categoryID "
                + "INNER JOIN Book ON Product.productID = Book.bookID "
                + "INNER JOIN Publisher ON Book.publisherID = Publisher.publisherID "
                + "WHERE Product.productID = ?";

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

    public HashMap<String,Creator> getCreatorsOfThisProduct(int productID) throws SQLException {
        String sql = "SELECT Product_Creator.creatorID, Creator.creatorName, Creator.creatorRole\n"
                + "FROM     Creator INNER JOIN\n"
                + "                  Product_Creator ON Creator.creatorID = Product_Creator.creatorID\n"
                + "where Product_Creator.productID = ?";
        Object[] params = {productID};

        HashMap<String,Creator> creatorMap = new HashMap<>();
        ResultSet rs = context.exeQuery(sql, params);

        while (rs.next()) {
            creatorMap.put(rs.getString(3),new Creator(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }

        return creatorMap;
    }

    public List<Genre> getGenresOfThisBook(int productID) throws SQLException {
        String sql = "SELECT Book_Genre.genreID, Genre.genreName\n"
                + "FROM     Book_Genre INNER JOIN\n"
                + "                  Genre ON Book_Genre.genreID = Genre.genreID\n"
                + "WHERE Book_Genre.bookID = ?";
        Object[] params = {productID};

        List<Genre> genreList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, params);

        while (rs.next()) {
            genreList.add(new Genre(rs.getInt(1),rs.getString(2)));
        }

        return genreList;
    }

}
