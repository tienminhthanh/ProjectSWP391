/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Admin;
import model.Customer;
import model.DeliveryOption;
import model.OrderInfo;
import model.OrderProduct;
import model.product_related.Product;
import model.Shipper;
import model.Staff;
import utils.DBContext;

/**
 *
 * @author Macbook
 */
public class OrderDAO {

    private static final OrderDAO instance = new OrderDAO();
    private final DBContext context;

    private OrderDAO() {
        context = DBContext.getInstance();
    }

    public static OrderDAO getInstance() {
        return instance;
    }

    public boolean insertOrderInfo(OrderInfo orderInfo) throws SQLException {
        String sql = "INSERT INTO OrderInfo ("
                + "  deliveryAddress, deliveryOptionID, customerID, "
                + "finalAmount, deliveryStatus, "
                + "orderStatus, deliveredAt, paymentMethod, paymentExpiredTime, paymentStatus, voucherID"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {
            orderInfo.getDeliveryAddress(),
            orderInfo.getDeliveryOptionID(),
            orderInfo.getCustomerID(),
            orderInfo.getPreVoucherAmount(), // Giữ nguyên không đổi
            orderInfo.getDeliveryStatus(),
            orderInfo.getOrderStatus(),
            orderInfo.getDeliveredAt(),
            orderInfo.getPaymentMethod(),
            orderInfo.getPaymentExpiredTime(),
            orderInfo.getPaymentStatus(),
            orderInfo.getVoucherID()
        };

        int rowsAffected = context.exeNonQuery(sql, params);
        int orderID = getLastInsertedOrderID(orderInfo.getCustomerID()); // Lấy orderID mới được tạo

        System.out.println(rowsAffected + " rows affected");

        callInsertOrderProduct(orderInfo, orderID);
        for (OrderProduct orderProduct : orderInfo.getOrderProductList()) {
            updateProductStock(orderProduct.getProductID(), orderProduct.getQuantity());
        }

        deleteCartItemsByCustomerID(orderInfo.getCustomerID());
        return rowsAffected > 0;
    }

// insert row vao bang Order_product
    //choose
    public void insertOrderProduct(Object[] params) throws SQLException {
        String sql = "INSERT INTO Order_Product (orderID, productID, orderProductQuantity, orderProductPrice) "
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
        String sql = "SELECT TOP 1 orderID "
                + "FROM OrderInfo "
                + "WHERE customerID = ? "
                + "ORDER BY orderID DESC";
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

// lay list order cho cus
    // choose
    public List<OrderInfo> getOrdersByCustomerID(int customerID) throws SQLException {
        List<OrderInfo> orderList = new ArrayList<>();
        String sql = "SELECT OrderInfo.*, DeliveryOption.optionName, DeliveryOption.deliveryOptionID "
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
        String sql = "SELECT * FROM OrderInfo WHERE shipperID = ? order by orderID desc";
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
        String sql = "SELECT a.*, c.*, ad.*, s.* "
                + "FROM Account a "
                + "JOIN OrderInfo o "
                + "    ON a.accountID = o.customerID "
                + "    OR a.accountID = o.shipperID "
                + "    OR a.accountID = o.staffID "
                + "    OR a.accountID = o.adminID "
                + "LEFT JOIN Customer c ON a.accountID = c.customerID "
                + "LEFT JOIN Admin ad ON a.accountID = ad.adminID "
                + "LEFT JOIN Staff s ON a.accountID = s.staffID "
                + "WHERE o.orderID = ?;";

        Object[] params = {orderID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            return rs.next() ? mapResultSetToAccount(rs) : null;
        }
    }

    public List<Account> getOrderHandlerByOrderID(int orderID) throws SQLException {
        String sql = "SELECT oi.*, a.*, ad.*, s.*, sp.*, c.*\n"
                + "FROM OrderInfo oi\n"
                + "LEFT JOIN Account a ON a.accountID IN (oi.adminID, oi.staffID, oi.shipperID, oi.customerID)\n"
                + "LEFT JOIN Admin ad ON ad.adminID = oi.adminID\n"
                + "LEFT JOIN Staff s ON s.staffID = oi.staffID\n"
                + "LEFT JOIN Shipper sp ON sp.shipperID = oi.shipperID\n"
                + "LEFT JOIN Customer c ON \n"
                + "    (oi.adminID IS NULL AND oi.staffID IS NULL AND oi.shipperID IS NULL AND c.customerID = oi.adminID)\n"
                + "WHERE oi.orderID = ?";
        List<Account> accounts = new ArrayList<>();
        Object[] params = {orderID};
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            accounts.add(mapResultSetToAccount(rs));
        }
        return accounts;
    }

    public OrderInfo getTransactionInfoByOrderID(int orderID) throws SQLException {
        String sql = "select o.vnpTxnRef, o.vnpTransactionNo, o.finalAmount, o.vnpTransactionDate, o.paymentMethod, o.orderID, o.vnpTransactionStatus from OrderInfo o where orderID =?";
        Object[] params = {orderID};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            OrderInfo orderinfo;
            orderinfo = new OrderInfo(
                    rs.getDouble("finalAmount"),
                    rs.getString("vnpTxnRef"),
                    rs.getString("vnpTransactionNo"),
                    rs.getString("vnpTransactionDate"),
                    rs.getString("vnpTransactionStatus"),
                    rs.getString("paymentMethod"),
                    rs.getInt("orderID")
            );
            return orderinfo;
        }
        return null;
    }

    //choose
    public Account getCustomerByOrderID(int orderID) throws SQLException {
        String sql = "SELECT a.*, c.* "
                + "FROM Account a "
                + "JOIN OrderInfo o ON a.accountID = o.customerID "
                + "JOIN Customer c ON a.accountID = c.customerID "
                + "WHERE o.orderID = ? AND a.role = 'customer';";

        Object[] params = {orderID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            return rs.next() ? mapResultSetToAccount(rs) : null;
        }
    }

    public Account getShipperByOrderID(int orderID) throws SQLException {
        String sql = "SELECT a.*, s.* "
                + "FROM Account a "
                + "JOIN OrderInfo o ON a.accountID = o.shipperID "
                + "JOIN Shipper s ON a.accountID = s.shipperID "
                + "WHERE o.orderID = ? AND a.role = 'shipper';";

        Object[] params = {orderID};

        try ( ResultSet rs = context.exeQuery(sql, params)) {
            return rs.next() ? mapResultSetToAccount(rs) : null;
        }
    }

    //choose maybe fix lai
    public Account getAccountByShipperIDAndOrderID(int orderID, int shipperID) throws SQLException {
        String sql = "SELECT "
                + "    a.accountID, a.username, a.firstName, a.lastName, "
                + "    a.email, a.phoneNumber, a.birthDate, a.role, "
                + "    a.accountIsActive, a.dateAdded "
                + "FROM Account a "
                + "JOIN Customer c ON a.accountID = c.customerID "
                + "JOIN OrderInfo o ON c.customerID = o.customerID "
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
                acc.setAccountIsActive(rs.getBoolean("accountIsActive")); // Đổi isActive -> accountIsActive
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

        String sql = "SELECT Order_Product.*, Product.productName, Product.imageURL, Product.description\n"
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
                product.setDescription(rs.getString("description"));
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
            if (rs.next()) {
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

    public String getAddressDetailByAddressID(int addressID) throws SQLException {
        String addressDetails = "";
        String sql = "select addressDetails from DeliveryAddress where addressID = ?";
        Object[] params = {addressID};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            addressDetails = rs.getString("addressDetails");
        }
        return addressDetails;
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
                rs.getDouble("finalAmount"),
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
                rs.getInt("orderID"),
                rs.getInt("productID"),
                rs.getInt("orderProductQuantity"),
                rs.getInt("orderProductPrice"),
                rs.getInt("rating"),
                rs.getString("comment")
        );
    }

// UPDATE CAC THU
    // cho cus update lai address khi ma chua giao hang 
    //choose
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        // Tạo đối tượng Account chung trước
        Account account = new Account(
                rs.getInt("accountID"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("phoneNumber"),
                rs.getString("birthDate"),
                rs.getBoolean("accountIsActive")
        );

        // Kiểm tra role và ép kiểu sang lớp con để set thêm thuộc tính
        switch (account.getRole()) {
            case "admin":

                Admin admin = new Admin(
                        rs.getInt("totalEvents"),
                        rs.getInt("totalVouchers"),
                        account.getAccountID(),
                        account.getUsername(),
                        account.getPassword(),
                        account.getRole(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getEmail(),
                        account.getPhoneNumber(),
                        account.getBirthDate(),
                        account.getAccountIsActive()
                );
                return admin;

            case "shipper":
                Shipper shipper = new Shipper(
                        rs.getInt("totalDeliveries"),
                        account.getAccountID(),
                        account.getUsername(),
                        account.getPassword(),
                        account.getRole(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getEmail(),
                        account.getPhoneNumber(),
                        account.getBirthDate(),
                        account.getAccountIsActive()
                );
                return shipper;

            case "customer":
                Customer customer = new Customer(
                        rs.getDouble("totalPurchasePoints"),
                        rs.getString("defaultDeliveryAddress"),
                        account.getAccountID(),
                        account.getUsername(),
                        account.getPassword(),
                        account.getRole(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getEmail(),
                        account.getPhoneNumber(),
                        account.getBirthDate(),
                        account.getAccountIsActive()
                );
                return customer;

            case "staff":
                Staff staff = new Staff(
                        rs.getInt("totalOrders"),
                        account.getAccountID(),
                        account.getUsername(),
                        account.getPassword(),
                        account.getRole(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getEmail(),
                        account.getPhoneNumber(),
                        account.getBirthDate(),
                        account.getAccountIsActive()
                );
                return staff;

            default:
                return account;
        }
    }

    public boolean updateDeliveryAddressByOrderID(int orderID, String newAddress) throws SQLException {
        String sql = "UPDATE OrderInfo SET deliveryAddress = ? WHERE orderID = ? AND deliveryStatus != 'Delivered'";
        Object[] params = {newAddress, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    public boolean updateTxnRefByOrderID(int orderID, String vnp_TxnRef) throws SQLException {
        String sql = "	update orderinfo set vnpTxnRef = ? where orderID = ?";
        Object[] params = {vnp_TxnRef, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    public boolean updateTransactionDateByOrderID(String vnp_TransactionDate, String vnp_TransactionStatus, String vnp_TxnRef) throws SQLException {
        String sql = "	update orderinfo set vnpTransactionDate = ?, vnpTransactionStatus =? where vnpTxnRef = ?";
        Object[] params = {vnp_TransactionDate, vnp_TransactionStatus, vnp_TxnRef};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    public boolean updateTransactionNoByTxnRef(String vnp_TransactionNo, String vnp_TxnRef) throws SQLException {
        String sql = "	update orderinfo set vnpTransactionNo = ? where vnpTxnRef = ?";
        Object[] params = {vnp_TransactionNo, vnp_TxnRef};
        int rowsAffected = context.exeNonQuery(sql, params);
        System.out.println(rowsAffected + " rows affected");
        return rowsAffected > 0;
    }

    public boolean updatepaymentStatusByOrderID(int orderID) throws SQLException {
        String sql = "update OrderInfo set paymentStatus ='paid'  where  orderID = ? and vnpTransactionStatus = '00'";
        Object[] params = {orderID};
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

    public boolean updateQuantityVoucher(int voucherID) throws SQLException {
        String sql = "UPDATE Voucher SET voucherQuantity = voucherQuantity - 1 WHERE voucherID = ?  AND voucherQuantity > 0";
        Object[] params = {voucherID};
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

    public boolean updateAdminAndShipperForOrder(int orderID, int adminID, int shipperID) throws SQLException {
        String sql = "UPDATE OrderInfo SET adminID = ?, shipperID = ? WHERE orderID = ?;";
        Object[] params = {adminID, shipperID, orderID};
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

    public boolean updateTotalDeliveries(int shipperID, int totalDeliveries) throws SQLException {
        String sql = "	update Shipper set totalDeliveries = ? where shipperID = ?";
        Object[] params = {totalDeliveries, shipperID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean updatePreVoucherAmount(int orderID, Double preVoucherAmount) throws SQLException {
        String sql = "UPDATE OrderInfo \n"
                + "SET finalAmount = ? \n"
                + "WHERE orderID = ?";
        Object[] params = {preVoucherAmount, orderID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean updateAdminIdForOrderInfo(int adminID, int orderID) throws SQLException {
        String sql = "UPDATE OrderInfo \n"
                + "SET adminID = ? \n"
                + "WHERE orderID = ?";
        Object[] params = {adminID, orderID};
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
    public boolean restoreProductStockByOrderID(int orderID) throws SQLException {
        // Lấy dữ liệu từ Order_Product, nhưng không ép kiểu trực tiếp
        String selectSQL = "SELECT productID, orderProductQuantity FROM Order_Product WHERE orderID = ?";
        ResultSet rs = (ResultSet) context.exeQuery(selectSQL, new Object[]{orderID});

        List<Object[]> productList = new ArrayList<>();

        // Chuyển từ ResultSet sang List<Object[]>
        while (rs.next()) {
            productList.add(new Object[]{rs.getInt("productID"), rs.getInt("orderProductQuantity")});
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
        return rowsUpdated > 0;
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

//    public Map<String, String[]> getRatingsAndCommentsByProduct(int productID) throws SQLException {
//        String sql = "SELECT Order_Product.productID, Order_Product.orderID, Account.firstName, Account.lastName, Order_Product.rating, Order_Product.comment\n"
//                + "FROM     Order_Product INNER JOIN\n"
//                + "                  OrderInfo ON Order_Product.orderID = OrderInfo.orderID INNER JOIN\n"
//                + "                  Customer ON OrderInfo.customerID = Customer.customerID INNER JOIN\n"
//                + "                  Account ON Customer.customerID = Account.accountID\n"
//                + "				  where productID = ? AND (rating is not null OR comment is not null)";
//        Object[] params = {productID};
//        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
//            Map<String, String[]> reviewMap = new HashMap<>();
//            while (rs.next()) {
//                reviewMap.put(rs.getString("lastName") + " " + rs.getString("firstName"), new String[]{rs.getInt("rating") + "", rs.getString("comment")});
//            }
//            return reviewMap;
//        }
//    }
//    
}
