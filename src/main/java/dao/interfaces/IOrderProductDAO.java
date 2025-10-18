/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import model.OrderProduct;

/**
 *
 * @author anhkc
 */
public interface IOrderProductDAO {
    List<OrderProduct> getOrderProductsByProductID(int productID) throws SQLException;
}
