/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import model.Product;

/**
 *
 * @author anhkc
 */
public interface ISpecificProductDAO {
    Product getProductById(int productID, boolean isManagement) throws SQLException;
    boolean updateProduct(Product product) throws SQLException;
    boolean addNewProduct(Product product) throws SQLException;
}
