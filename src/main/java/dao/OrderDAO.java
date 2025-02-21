/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrderInfo;
import model.OrderProduct;

/**
 *
 * @author Macbook
 */
public class OrderDAO {

    private utils.DBContext context;

    public OrderDAO() {
        context = new utils.DBContext();
    }

    public List<OrderInfo> selectAllOrderInfo() throws SQLException {
        String sql = "select * from OrderInfo";
        List<OrderInfo> orderList = new ArrayList<>();
        ResultSet rs = context.exeQuery(sql, null);
        while (rs.next()) {
            orderList.add(new OrderInfo(rs.getInt("orderID"),
                    rs.getDate("orderDate"),
                    rs.getString("deliveryAddress"),
                    rs.getInt("deliveryOptionID"),
                    rs.getInt("customerID"),
                    rs.getInt("preVoucherAmount"),
                    rs.getInt("voucherID"),
                    rs.getInt("staffID"),
                    rs.getInt("shipperID"),
                    rs.getString("deliveryStatus"),
                    rs.getString("orderStatus"),
                    rs.getInt("adminID"),
                    rs.getDate("deliveredAt"),
                    rs.getString("paymentMethod"),
                    rs.getInt("paymentExpiredTime"),
                    rs.getString("paymentStatus")));
        }
        return orderList;
    }

//chen mot order vo orderInfo
    public void insertOrderInfo(OrderInfo orderInfo) throws SQLException {
        String sql = "INSERT INTO OrderInfo (\n"
                + "    orderID, orderDate, deliveryAddress, deliveryOptionID, customerID, \n"
                + "    preVoucherAmount, voucherID, deliveryStatus, orderStatus\n"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Object[] params = {orderInfo.getOrderID(),
            orderInfo.getOrderDate(),
            orderInfo.getDeliveryAddress(),
            orderInfo.getDeliveryOptionID(),
            orderInfo.getCustomerID(),
            orderInfo.getPreVoucherAmount(),
            orderInfo.getVoucherID(),
            orderInfo.getDeliveryStatus(),
            orderInfo.getOrderStatus()};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        callInsertOrderProduct(orderInfo);
    }

// insert row vao bang Order_product
    public void insertOrderPoduct(Object[] params) throws SQLException {
        String sql = "INSERT INTO Order_Product (orderID, productID, quantity, priceWithQuantity)  \n"
                + "VALUES (?, ?, ?, ?);";
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
    }

// for them tung obj vao bang order_product
    public void callInsertOrderProduct(OrderInfo orderInfo) throws SQLException {
        for (int i = 0; i < orderInfo.getOrderProductList().size(); i++) {
            OrderProduct orderProduct = orderInfo.getOrderProductList().get(i);
            Object[] params = {orderInfo.getOrderID(), orderProduct.getProductID(),
                orderProduct.getQuantity(), orderProduct.getPriceWithQuantity()};
            insertOrderPoduct(params);
        }
    }

    public OrderInfo getOrderByOrderID(int orderID) throws SQLException {
        String sql = "SELECT * FROM OrderInfo WHERE orderID = ?";
        Object[] param = {orderID};

        try ( ResultSet rs = context.exeQuery(sql, param)) {
            if (rs.next()) {
                return mapResultSetToOrderInfo(rs);
            }
        }
        return null;
    }

    private OrderInfo mapResultSetToOrderInfo(ResultSet rs) throws SQLException {
        return new OrderInfo(
                rs.getInt("orderID"),
                rs.getDate("orderDate"),
                rs.getString("deliveryAddress"),
                rs.getInt("deliveryOptionID"),
                rs.getInt("customerID"),
                rs.getInt("preVoucherAmount"),
                rs.getInt("voucherID"),
                rs.getInt("staffID"),
                rs.getInt("shipperID"),
                rs.getString("deliveryStatus"),
                rs.getString("orderStatus"),
                rs.getInt("adminID"),
                rs.getDate("deliveredAt"),
                rs.getString("paymentMethod"),
                rs.getInt("paymentExpiredTime"),
                rs.getString("paymentStatus")
        );
    }

}
