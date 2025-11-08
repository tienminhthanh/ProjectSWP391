/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.interfaces.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Customer;
import model.OrderInfo;
import model.OrderProduct;
import utils.DBContext;

/**
 *
 * @author anhkc
 */
public class OrderProductDAO implements IProductExtraAttributesDAO<OrderProduct, Integer> {

    private static final OrderProductDAO instance = new OrderProductDAO();
    private final DBContext context;

    private OrderProductDAO() {
        context = DBContext.getInstance();
    }

    public static OrderProductDAO getInstance() {
        return instance;
    }

    @Override
    public List<OrderProduct> getExtraAttributesByProductID(Integer productID) throws SQLException {
        String sql = "SELECT Order_Product.orderID, Account.firstName, Account.lastName, Order_Product.rating, Order_Product.comment\n"
                + "FROM     Order_Product INNER JOIN\n"
                + "                  OrderInfo ON Order_Product.orderID = OrderInfo.orderID INNER JOIN\n"
                + "                  Customer ON OrderInfo.customerID = Customer.customerID INNER JOIN\n"
                + "                  Account ON Customer.customerID = Account.accountID\n"
                + "				  where productID = ? AND (rating is not null OR comment is not null)";
        int id = productID;
        Object[] params = {id};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            List<OrderProduct> orderProductList = new ArrayList<>();
            while (rs.next()) {
                int orderID = rs.getInt("orderID");

                OrderProduct item = new OrderProduct();
                OrderInfo orderInfo = new OrderInfo();
                Customer customer = new Customer();

                customer.setFirstName(rs.getString("firstName"));
                customer.setLastName(rs.getString("lastName"));

                orderInfo.setCustomer(customer);
                orderInfo.setOrderID(orderID);

                item.setOrderInfo(orderInfo);
                item.setOrderID(orderID);
                item.setProductID(productID);
                item.setComment(rs.getString("comment"));
                item.setRating(rs.getInt("rating"));

                orderProductList.add(item);

            }
            return orderProductList;
        }
    }

}
