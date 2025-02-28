/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DeliveryOption;
import model.OrderInfo;
import model.OrderProduct;
import model.Product;

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
    // sau them voucher
    public void insertOrderInfo(OrderInfo orderInfo) throws SQLException {
        String sql = "INSERT INTO OrderInfo ("
                + "  deliveryAddress, deliveryOptionID, customerID, "
                + "preVoucherAmount,  deliveryStatus, "
                + "orderStatus, deliveredAt, paymentMethod, paymentExpiredTime, paymentStatus,voucherID"
                + ") VALUES (  ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
            orderInfo.getDeliveryAddress(),
            orderInfo.getDeliveryOptionID(),
            orderInfo.getCustomerID(),
            orderInfo.getPreVoucherAmount(),
            orderInfo.getDeliveryStatus(),
            orderInfo.getOrderStatus(),
            orderInfo.getDeliveredAt(),
            orderInfo.getPaymentMethod(),
            orderInfo.getPaymentExpiredTime(),
            orderInfo.getPaymentStatus(),
            orderInfo.getVoucherID()
        };

        int rowsAffected = context.exeNonQuery(sql, params);
        int orderID = getLastInsertedOrderID(orderInfo.getCustomerID()); // lay orderID moi duoc tao
//        orderInfo.setOrderID(orderID);

        System.out.println(rowsAffected
                + " rows affected");

        callInsertOrderProduct(orderInfo, orderID);
        for (OrderProduct orderProduct
                : orderInfo.getOrderProductList()) {
            updateProductStock(orderProduct.getProductID(), orderProduct.getQuantity());
        }

        deleteCartItemsByCustomerID(orderInfo.getCustomerID());
    }

// insert row vao bang Order_product
    public void insertOrderProduct(Object[] params) throws SQLException {
        String sql = "INSERT INTO Order_Product (orderID, productID, quantity, priceWithQuantity)  \n"
                + "VALUES (?, ?, ?, ?);";
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");

    }

// for them tung obj vao bang order_product
    public void callInsertOrderProduct(OrderInfo orderInfo, int orderID) throws SQLException {
        for (int i = 0; i < orderInfo.getOrderProductList().size(); i++) {
            OrderProduct orderProduct = orderInfo.getOrderProductList().get(i);
            Object[] params = {orderID, orderProduct.getProductID(),
                orderProduct.getQuantity(), orderProduct.getPriceWithQuantity()};
            insertOrderProduct(params);
        }
    }

    // GET CAC LOAI THONG TIN
    // lay orderID moi nhat
    public int getLastInsertedOrderID(int customerID) throws SQLException {
        String sql = "select top 1 orderID\n"
                + "from OrderInfo\n"
                + "where customerID = ?\n"
                + "order by orderID desc";
        Object[] params = {customerID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    // for admin with satff manage orderlist
    public List<OrderInfo> getAllOrders() throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT o.*, c.firstName + ' ' + c.lastName AS customerName, "
                + "s.firstName + ' ' + s.lastName AS shipperName, "
                + "st.firstName + ' ' + st.lastName AS staffName "
                + "FROM OrderInfo o "
                + "LEFT JOIN Customer c ON o.customerID = c.customerID "
                + "LEFT JOIN Shipper s ON o.shipperID = s.shipperID "
                + "LEFT JOIN Staff st ON o.staffID = st.staffID";

        try ( ResultSet rs = context.exeQuery(sql, null)) {
            while (rs.next()) {
                orderList.add(mapResultSetToOrderInfo(rs));
            }
        }
        return orderList;
    }

    // lay thong tin product cho orderdetail
    public List<OrderProduct> getProductsByOrderID(int orderID) throws SQLException {
        List<OrderProduct> productList = new ArrayList<>();
        String sql = "SELECT * FROM OrderProducts WHERE orderID = ?";
        Object[] params = {orderID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                productList.add(mapResultSetToOrderProduct(rs));
            }
        }
        return productList;
    }

    //dung truy van khi ng dung hoac admin xoa order thi an di
    public OrderInfo getOrderByOrderID(int orderID) throws SQLException {
        String sql = "SELECT o.*, c.firstName + ' ' + c.lastName AS customerName, "
                + "s.firstName + ' ' + s.lastName AS shipperName, "
                + "st.firstName + ' ' + st.lastName AS staffName "
                + "FROM OrderInfo o "
                + "LEFT JOIN Customer c ON o.customerID = c.customerID "
                + "LEFT JOIN Shipper s ON o.shipperID = s.shipperID "
                + "LEFT JOIN Staff st ON o.staffID = st.staffID "
                + "WHERE o.orderID = ?";
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
        String sql = "SELECT OrderInfo.*, DeliveryOption.optionName\n"
                + "FROM  OrderInfo INNER JOIN\n"
                + "         DeliveryOption ON OrderInfo.deliveryOptionID = DeliveryOption.deliveryOptionID\n"
                + "WHERE (OrderInfo.customerID = ?)";
        Object[] params = {customerID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                OrderInfo orderInfo = mapResultSetToOrderInfo(rs);
                DeliveryOption deliveryOption = new DeliveryOption();
                deliveryOption.setOptionName(rs.getString("optionName"));
                deliveryOption.setDeliveryOptionID(rs.getInt("deliveryOptionID"));
                orderInfo.setDeliveryOption(deliveryOption);
                orderList.add(orderInfo);
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

// lay thong tin cac san pham theo oderId de in ra orderdeatil
    public List<OrderProduct> getOrderProductByOrderID(int orderID) throws SQLException {
        List<OrderProduct> OrderProductList = new ArrayList<>();
        String sql = "SELECT Order_Product.orderID, Order_Product.productID, Order_Product.quantity, Order_Product.priceWithQuantity, Product.productName, Product.imageURL\n"
                + "FROM     Order_Product INNER JOIN\n"
                + "                  Product ON Order_Product.productID = Product.productID\n"
                + "WHERE  (Order_Product.orderID = ?)";
        Object[] params = {orderID};
        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                OrderProduct orderProduct = mapResultSetToOrderProduct(rs);
                Product product = new Product();
                product.setProductID(orderProduct.getProductID());
                product.setProductName(rs.getString("productName"));
                product.setImageURL(rs.getString("imageURL"));
                orderProduct.setProduct(product);
                OrderProductList.add(orderProduct);
            }
        }
        return OrderProductList;
    }

    public OrderInfo getOrderByID(int orderID, int customerID) throws SQLException {
        String sql = "SELECT OrderInfo.*\n"
                + "FROM     OrderInfo\n"
                + "WHERE  (orderID = ?) AND (customerID = ?)";
        Object[] params = {orderID, customerID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                OrderInfo orderInfo = mapResultSetToOrderInfo(rs);
                orderInfo.setOrderProductList(getOrderProductByOrderID(orderInfo.getOrderID()));
                return orderInfo;
            }
        }
        return null;

    }

    // MAP THONG TIN 
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

    // update stock
    public boolean updateProductStock(int productID, int quantityOrdered) throws SQLException {
        String sql = "UPDATE Product SET stockCount = stockCount - ? WHERE productID = ? AND stockCount >= ?";
        Object[] params = {quantityOrdered, productID, quantityOrdered};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    public void restoreProductStockByOrderID(int orderID) throws SQLException {
        // Lấy dữ liệu từ Order_Product, nhưng không ép kiểu trực tiếp
        String selectSQL = "SELECT productID, quantity FROM Order_Product WHERE orderID = ?";
        ResultSet rs = (ResultSet) context.exeQuery(selectSQL, new Object[]{orderID});

        List<Object[]> productList = new ArrayList<>();

        // Chuyển từ ResultSet sang List<Object[]>
        while (rs.next()) {
            productList.add(new Object[]{rs.getInt("productID"), rs.getInt("quantity")});
        }

        int rowsUpdated = 0;
        for (Object[] row : productList) {
            int productID = (int) row[0];
            int quantityOrdered = (int) row[1];

            // Cập nhật lại tồn kho
            String updateSQL = "UPDATE Product SET stockCount = stockCount + ? WHERE productID = ?";
            Object[] updateParams = {quantityOrdered, productID};
            rowsUpdated += context.exeNonQuery(updateSQL, updateParams);
        }

        System.out.println(rowsUpdated + " rows updated in stock");
    }

    //DELETE CAC THU
    public boolean deleteCartItemsByCustomerID(int customerID) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE customerID = ?";
        Object[] params = {customerID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " cart items deleted.");
        return rowsAffected > 0;
    }

    public void deleteOrderInfoByOrderID(int orderID) throws SQLException {
        String sql = "DELETE FROM OrderInfo WHERE orderID = ?";
        Object[] params = {orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " order info deleted.");
    }

    public void deleteOrderProductByOrderID(int orderID) throws SQLException {
        String sql = "DELETE FROM Order_Product WHERE orderID = ?";
        Object[] params = {orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " order products deleted.");
    }

}
