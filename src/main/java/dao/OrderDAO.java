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
//  CREATE ORDER
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

    // GET CAC LOAI THONG TIN
    // for admin with satff manage orderlist
    public List<OrderInfo> getAllOrders() throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT * FROM OrderInfo";

        try ( ResultSet rs = context.exeQuery(sql, null)) {
            while (rs.next()) {
                orderList.add(mapResultSetToOrderInfo(rs));
            }
        }
        return orderList;
    }

    //dung truy van khi ng dung hoac admin xoa order thi an di
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

// lay list order cho cus
    public List<OrderInfo> getOrdersByCustomerID(int customerID) throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT * FROM OrderInfo WHERE customerID = ?";
        Object[] params = {customerID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                orderList.add(mapResultSetToOrderInfo(rs));
            }
        }
        return orderList;
    }

// lay list order cho shipper
    public List<OrderInfo> getOrdersByShipperID(int shipperID) throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT * FROM OrderInfo WHERE shipperID = ?";
        Object[] params = {shipperID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                orderList.add(mapResultSetToOrderInfo(rs));
            }
        }
        return orderList;
    }

//lay gia tri vocher de dua vao orderdeatil
    public int getVoucherValueByOrderID(int orderID) throws SQLException {
        int voucherValue = 0;
        String sql = "SELECT v.voucherValue FROM OrderInfo o "
                + "JOIN Voucher v ON o.voucherID = v.voucherID "
                + "WHERE o.orderID = ?";
        Object[] params = {orderID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            if (rs.next()) {
                voucherValue = rs.getInt("voucherValue");
            }
        }
        return voucherValue;
    }

    // MAP THONG TIN 
// lay thong tin cac san pham theo oderId de in ra orderdeatil
    public List<OrderProduct> getOrderProductByOrderID(int orderID) throws SQLException {
        List<OrderProduct> OrderProductList = new ArrayList<>();
        String sql = "SELECT op.productID, op.quantity, op.priceWithQuantity "
                + "FROM Order_Product op "
                + "WHERE op.orderID = ?";
        Object[] params = {orderID};
        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                OrderProductList.add(mapResultSetToOrderProduct(rs));
            }

        }
        return OrderProductList;
    }

// map de lay all orderinfo cho listAll
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

    //map de lay orderproduct cua mot don
    private OrderProduct mapResultSetToOrderProduct(ResultSet rs) throws SQLException {
        return new OrderProduct(
                rs.getInt("productID"),
                rs.getInt("quantity"),
                rs.getInt("priceWithQuantity")
        );
    }

// UPDATE CAC THU
    //upadte trang thai giao hang cho shipper
    public boolean updateDeliveryOptionByOrderID(int orderID, int newDeliveryOptionID) throws SQLException {
        String sql = "UPDATE OrderInfo SET deliveryOptionID = ? WHERE orderID = ? AND deliveryStatus != 'Delivered'";
        Object[] params = {newDeliveryOptionID, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    // staff update shipper cho order 
    public boolean assignShipperToOrder(int orderID, int shipperID) throws SQLException {
        String sql = "UPDATE OrderInfo SET shipperID = ? WHERE orderID = ? AND shipperID IS NULL";
        Object[] params = {shipperID, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    // cho cus update lai address khi ma chua giao hang 
    public boolean updateDeliveryAddressByOrderID(int orderID, String newAddress) throws SQLException {
        String sql = "UPDATE OrderInfo SET deliveryAddress = ? WHERE orderID = ? AND deliveryStatus != 'Delivered'";
        Object[] params = {newAddress, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    // update cho COD khi cus nhan hang va tra tien
    public boolean updatePaymentStatus(int orderID, String newStatus) throws SQLException {
        String sql = "UPDATE OrderInfo SET paymentStatus = ? WHERE orderID = ?";
        Object[] params = {newStatus, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    // dung cho cus hoac admin huy don 
    public boolean cancelOrder(int orderID) throws SQLException {
        String sql = "UPDATE OrderInfo SET orderStatus = 'Canceled' WHERE orderID = ? AND deliveryStatus != 'Delivered'";
        Object[] params = {orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

}
