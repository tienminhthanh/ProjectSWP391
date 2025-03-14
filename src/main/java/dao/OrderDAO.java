/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.DeliveryOption;
import model.OrderInfo;
import model.OrderProduct;
import model.Product;
import model.Shipper;

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
    //choose
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
    //choose
    public void insertOrderProduct(Object[] params) throws SQLException {
        String sql = "INSERT INTO Order_Product (orderID, productID, quantity, priceWithQuantity)  \n"
                + "VALUES (?, ?, ?, ?);";
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");

    }

// for them tung obj vao bang order_product
    //choose
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
    //choose
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
    //choose
    public List<OrderInfo> getAllOrders() throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT * FROM OrderInfo ORDER BY orderID DESC"; // Sắp xếp theo orderID từ lớn đến bé

        try ( ResultSet rs = context.exeQuery(sql, null)) {
            while (rs.next()) {
                orderList.add(mapResultSetToOrderInfo(rs));
            }
        }
        return orderList;
    }

    //choose
    public List<OrderInfo> getOrdersByStatus(String orderStatus) throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT * from OrderInfo  where orderStatus =? ORDER BY orderID DESC";
        Object params[] = {orderStatus};
        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                orderList.add(mapResultSetToOrderInfo(rs));
            }
        }
        return orderList;
    }

// lay list order cho cus
    // choose
    public List<OrderInfo> getOrdersByCustomerID(int customerID) throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT OrderInfo.*, DeliveryOption.optionName "
                + "FROM OrderInfo "
                + "INNER JOIN DeliveryOption ON OrderInfo.deliveryOptionID = DeliveryOption.deliveryOptionID "
                + "WHERE OrderInfo.customerID = ? "
                + "ORDER BY OrderInfo.orderID DESC"; // Sắp xếp theo orderID giảm dần

        Object[] params = {customerID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            while (rs.next()) {
                OrderInfo orderInfo = mapResultSetToOrderInfo(rs);
                // Tạo đối tượng DeliveryOption
                DeliveryOption deliveryOption = new DeliveryOption();
                deliveryOption.setOptionName(rs.getString("optionName"));
                deliveryOption.setDeliveryOptionID(rs.getInt("deliveryOptionID"));
                // Gán phương thức giao hàng cho đơn hàng
                orderInfo.setDeliveryOption(deliveryOption);

                // Thêm vào danh sách
                orderList.add(orderInfo);
            }
        }
        return orderList; // Trả về danh sách đơn hàng đã sắp xếp
    }

    // lay list order cho shipper
    //choose
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

    public OrderInfo getOrderByOrderID(int orderID) throws SQLException {
        String sql = "SELECT * FROM OrderInfo WHERE orderID = ?";
        Object[] params = {orderID};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next() ? mapResultSetToOrderInfo(rs) : null;
    }

    //choose
    public Account getInfoCustomerByOrderID(int orderID) throws SQLException {
        String sql = "SELECT a.* \n"
                + "FROM Account a\n"
                + "JOIN OrderInfo o ON a.accountID = o.customerID OR \n"
                + "                    a.accountID = o.shipperID OR \n"
                + "                    a.accountID = o.staffID OR \n"
                + "                    a.accountID = o.adminID\n"
                + "WHERE o.orderID = ?";
        Object[] params = {orderID};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next() ? mapResultSetToAccount(rs) : null;
    }

    //choose
    public Account getCustomerByOrderID(int orderID) throws SQLException {
        String sql = "SELECT a.*\n"
                + "FROM Account a\n"
                + "JOIN OrderInfo o ON a.accountID = o.customerID\n"
                + "WHERE o.orderID = ?";
        Object[] params = {orderID};
        ResultSet rs = context.exeQuery(sql, params);
        return rs.next() ? mapResultSetToAccount(rs) : null;
    }

    //choose maybe fix lai
    public Account getAccountByShipperIDAndOrderID(int orderID, int shipperID) throws SQLException {
        String sql = "SELECT \n"
                + "    a.accountID,\n"
                + "    a.username,\n"
                + "    a.firstName , a.lastName ,\n"
                + "    a.email,\n"
                + "    a.phoneNumber,\n"
                + "    a.birthDate,\n"
                + "    a.role,\n"
                + "    a.isActive,\n"
                + "    a.dateAdded\n"
                + "FROM Account a\n"
                + "JOIN Customer c ON a.accountID = c.customerID\n"
                + "JOIN OrderInfo o ON c.customerID = o.customerID\n"
                + "WHERE o.shipperID = ? AND o.orderID = ?;";

        Object[] params = {shipperID, orderID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            if (rs.next()) {
                Account acc = new Account();
                acc.setAccountID(rs.getInt("accountID"));
                acc.setUsername(rs.getString("username"));
                acc.setFirstName(rs.getString("firstName"));
                acc.setLastName(rs.getString("lastName"));
                acc.setEmail(rs.getString("email"));
                acc.setPhoneNumber(rs.getString("phoneNumber"));
                acc.setBirthDate(rs.getDate("birthDate") != null ? rs.getDate("birthDate").toString() : null);
                acc.setRole(rs.getString("role"));
                acc.setIsActive(rs.getBoolean("isActive"));
                return acc;
            }
        }
        return null;
    }

    //lay gia tri vocher de dua vao orderdeatil
    //choose
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
    //choose
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
//choose

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

    //choose de 1
    public List<DeliveryOption> getAllDeliveryOptions() throws SQLException {
        List<DeliveryOption> deliveryOptions = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryOption";

        try ( ResultSet rs = context.exeQuery(sql, null)) {
            while (rs.next()) {
                DeliveryOption deliveryOption = new DeliveryOption(
                        rs.getInt("deliveryOptionID"),
                        rs.getString("optionName"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("optionCost")
                );
                deliveryOptions.add(deliveryOption);
            }
        }
        return deliveryOptions;
    }

    //choose de 2
    public DeliveryOption getDeliveryOption(int deliveryOptionID) throws SQLException {
        String sql = "SELECT * FROM DeliveryOption WHERE deliveryOptionID = ?";
        Object[] params = {deliveryOptionID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            if (rs.next()) {
                return new DeliveryOption(
                        rs.getInt("deliveryOptionID"),
                        rs.getString("optionName"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("optionCost")
                );
            }
        }
        return null; // Nếu không tìm thấy ID, trả về `null`
    }

    //choose
    public List<Shipper> getAllShippers() {
        List<Shipper> shipperList = new ArrayList<>();
        String sql = "SELECT Account.*, Shipper.* "
                + "FROM Account "
                + "JOIN Shipper ON Account.accountID = Shipper.shipperID;";

        try ( ResultSet rs = context.exeQuery(sql, null)) {
            while (rs.next()) {
                // Tạo đối tượng Account từ dữ liệu bảng Account
                Shipper shipper = new Shipper();

                shipper.setAccountID(rs.getInt("accountID"));
                shipper.setUsername(rs.getString("username"));
                shipper.setFirstName(rs.getString("firstName"));
                shipper.setLastName(rs.getString("lastName"));
                shipper.setEmail(rs.getString("email"));
                shipper.setPhoneNumber(rs.getString("phoneNumber"));
                shipper.setBirthDate(rs.getString("birthDate"));

                shipper.setDeliveryAreas(rs.getString("deliveryAreas"));
                shipper.setTotalDeliveries(rs.getInt("totalDeliveries"));

                shipperList.add(shipper);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in production
        }
        return shipperList;
    }

    // MAP THONG TIN 
// map de lay all orderinfo cho listAll
    //choose
    private OrderInfo mapResultSetToOrderInfo(ResultSet rs) throws SQLException {
        return new OrderInfo(
                rs.getInt("orderID"),
                rs.getDate("orderDate"),
                rs.getString("deliveryAddress"),
                rs.getInt("deliveryOptionID"),
                rs.getInt("customerID"),
                rs.getDouble("preVoucherAmount"),
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
    //choose
    private OrderProduct mapResultSetToOrderProduct(ResultSet rs) throws SQLException {
        return new OrderProduct(
                rs.getInt("productID"),
                rs.getInt("quantity"),
                rs.getInt("priceWithQuantity")
        );
    }
//choose

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("accountID"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("phoneNumber"),
                rs.getString("birthDate"),
                rs.getBoolean("isActive")
        );
    }
// UPDATE CAC THU
    // cho cus update lai address khi ma chua giao hang 
    //choose

    public boolean updateDeliveryAddressByOrderID(int orderID, String newAddress) throws SQLException {
        String sql = "UPDATE OrderInfo SET deliveryAddress = ? WHERE orderID = ? AND deliveryStatus != 'Delivered'";
        Object[] params = {newAddress, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    // update stock
    //choose
    public boolean updateProductStock(int productID, int quantityOrdered) throws SQLException {
        String sql = "UPDATE Product SET stockCount = stockCount - ? WHERE productID = ? AND stockCount >= ?";
        Object[] params = {quantityOrdered, productID, quantityOrdered};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    //choose
    public boolean updateStaffAndShipperForOrder(int orderID, int staffID, int shipperID) throws SQLException {
        String sql = "UPDATE OrderInfo SET staffID = ?, shipperID = ? WHERE orderID = ?;";
        Object[] params = {staffID, shipperID, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    //choose
    public boolean updateDeliverystatus(int orderID, String newStatus) throws SQLException {
        String sql = "UPDATE OrderInfo \n"
                + "SET deliveryStatus = ? \n"
                + "WHERE orderID = ?";
        Object[] params = {newStatus, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    //choose
    public boolean updateOrderstatus(int orderID, String newStatus) throws SQLException {
        String sql = "UPDATE OrderInfo \n"
                + "SET orderStatus =?  \n"
                + "WHERE orderID = ?";
        Object[] params = {newStatus, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    //set rate cho sp
    public boolean updateRatingForProduct(int orderID, int productID, int rate) throws SQLException {
        String sql = "UPDATE Order_Product SET rating =  ? WHERE productID = ? and orderID = ?";
        Object[] params = {rate, productID, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }
// review
    public boolean updateCommentForProduct(int orderID, int productID, String comment) throws SQLException {
        String sql = "UPDATE Order_Product SET comment = ? WHERE orderID = ? AND productID = ?";
        Object[] params = {comment, orderID, productID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

// choose
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
    //choose
    public boolean deleteCartItemsByCustomerID(int customerID) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE customerID = ?";
        Object[] params = {customerID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " cart items deleted.");
        return rowsAffected > 0;
    }

}
