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
import java.util.List;
import utils.*;
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
        String sql = "SELECT top 10 p.*, i.imageID, i.imageURL, c.categoryName\n"
                + "FROM Product p\n"
                + "join Category c\n"
                + "on c.categoryID = p.categoryID\n"
                + "LEFT JOIN Image i \n"
                + "ON i.imageID = (\n"
                + "    SELECT MIN(imageID) \n"
                + "    FROM Image \n"
                + "    WHERE Image.productID = p.productID\n"
                + ")\n"
                + "where isActive = 1 and generalCategory = 'book'\n"
                + "order by NEWID()";
        List<Product> bookList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, null);
        while (rs.next()) {
            List<Image> imageList = new ArrayList<>();
            imageList.add(new Image(rs.getInt(16),rs.getString(17)));
            Product currentProduct = new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), new Category(rs.getInt(5),rs.getString("categoryName")), 
                                                 rs.getString(6), rs.getDate(7).toLocalDate(), rs.getTimestamp(8).toLocalDateTime(),rs.getDouble(9), rs.getInt(10), 
                                                 rs.getString(11), rs.getInt(12), rs.getString(13), rs.getString(14), rs.getBoolean(15), imageList);
            bookList.add(currentProduct);

        }
        return bookList;

    }

    public Category getCategoryOfThisProduct(int productID) throws SQLException {
        String sql = "SELECT Category.categoryID, Category.categoryName\n"
                + "FROM     Category INNER JOIN\n"
                + "                  Product ON Category.categoryID = Product.categoryID\n"
                + "WHERE  (Product.productID = ?)";
        Object[] params = {productID};
        ResultSet rs = context.exeQuery(sql, params);

        if (rs.next()) {
            return new Category(rs.getInt(1),
                    rs.getString(2));
        }

        return null;
    }

    public List<Image> getImageListOfThisProduct(int productID) throws SQLException {
        String sql = "SELECT Image.imageID, Image.imageURL\n"
                + "FROM     Image INNER JOIN\n"
                + "                  Product ON Image.productID = Product.productID\n"
                + "WHERE  (Image.productID = ?)";
        Object[] params = {productID};

        List<Image> imageList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, params);

        while (rs.next()) {
            imageList.add(new Image(rs.getInt(1), rs.getString(2)));
        }

        return imageList;
    }
}
