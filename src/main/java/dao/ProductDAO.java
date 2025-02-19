/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    
    public List<Product> select10RandomBooks() throws SQLException {
        String sql = "select top 10 * from Book order by NEWID()";
        List<Product> bookList = new ArrayList<>();
        ResultSet resultSet = context.exeQuery(sql,null);
        while (resultSet.next()) {
            List<String> imageURLList = new ArrayList<>();
            imageURLList.add(resultSet.getString("bookCover"));
            bookList.add(new Product(resultSet.getInt("bookID"), 
                    resultSet.getString("bookTitle"),
                    imageURLList,
                    resultSet.getDate("bookPublishDate"),
                    resultSet.getDate("bookImportDate"), resultSet.getInt("bookQuantity"),
                    resultSet.getDouble("bookPrice"), resultSet.getInt("bookDiscount")));
            
        }
        return bookList;

    }
}
