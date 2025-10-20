/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service.interfaces;

import java.sql.SQLException;
import model.product_related.Product;

/**
 *
 * @author anhkc
 */
public interface IProductDetailsService {
    Product findProduct(int id, boolean isManagement) throws SQLException;
    void loadExtraAttributes(Product product, int id) throws SQLException;
}
