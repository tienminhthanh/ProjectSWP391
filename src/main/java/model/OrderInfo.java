package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class OrderInfo {

    private int orderID;
    private Date orderDate;
    private Date expectedDeliveryDate;
    private String deliveryAddress;
    private int deliveryOptionID;
    private int customerID;
    private Double preVoucherAmount;
    private Integer voucherID;  // Đổi từ int → Integer
    private int staffID;
    private int shipperID;
    private String deliveryStatus;
    private String orderStatus;
    private int adminID;
    private Date deliveredAt;
    private String paymentMethod;
    private int paymentExpiredTime;
    private String paymentStatus;
    private List<OrderProduct> orderProductList;
    private DeliveryOption deliveryOption;
    private String vnp_TxnRef;
    private String vnp_TransactionNo;
    private String vnp_TransactionDate;

    public OrderInfo() {
        this.paymentStatus = "pending";
        this.orderStatus = "pending";
        this.deliveryStatus = "pending";
    }

    public OrderInfo(Double preVoucherAmount, String vnp_TxnRef, String vnp_TransactionNo, String vnp_TransactionDate, String paymentMethod, int orderID) {
        this.preVoucherAmount = preVoucherAmount;
        this.vnp_TxnRef = vnp_TxnRef;
        this.vnp_TransactionNo = vnp_TransactionNo;
        this.vnp_TransactionDate = vnp_TransactionDate;
        this.paymentMethod = paymentMethod;
        this.orderID = orderID;
    }

    // Constructor tạo đơn hàng mới
    public OrderInfo(String deliveryAddress, int deliveryOptionID, int customerID,
            String paymentMethod, List<OrderProduct> orderProductList, Integer voucherID) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryOptionID = deliveryOptionID;
        this.customerID = customerID;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "pending";
        this.orderStatus = "pending";
        this.deliveryStatus = "pending";
        this.orderProductList = orderProductList;
        this.voucherID = voucherID;  // Giữ nguyên nếu có, nếu không thì null
    }

    // Constructor đầy đủ
    public OrderInfo(int orderID, Date orderDate, String deliveryAddress, int deliveryOptionID, int customerID,
            Double preVoucherAmount, Integer voucherID, int staffID, int shipperID,
            String deliveryStatus, String orderStatus, int adminID, Date deliveredAt,
            String paymentMethod, int paymentExpiredTime, String paymentStatus) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.deliveryOptionID = deliveryOptionID;
        this.customerID = customerID;
        this.preVoucherAmount = preVoucherAmount;
        this.voucherID = voucherID;  // Hỗ trợ null
        this.staffID = staffID;
        this.shipperID = shipperID;
        this.deliveryStatus = deliveryStatus;
        this.orderStatus = orderStatus;
        this.adminID = adminID;
        this.deliveredAt = deliveredAt;
        this.paymentMethod = paymentMethod;
        this.paymentExpiredTime = paymentExpiredTime;
        this.paymentStatus = paymentStatus;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getDeliveryOptionID() {
        return deliveryOptionID;
    }

    public void setDeliveryOptionID(int deliveryOptionID) {
        this.deliveryOptionID = deliveryOptionID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Double getPreVoucherAmount() {
        return preVoucherAmount;
    }

    public void setPreVoucherAmount(Double preVoucherAmount) {
        this.preVoucherAmount = preVoucherAmount;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getShipperID() {
        return shipperID;
    }

    public void setShipperID(int shipperID) {
        this.shipperID = shipperID;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentExpiredTime() {
        return paymentExpiredTime;
    }

    public void setPaymentExpiredTime(int paymentExpiredTime) {
        this.paymentExpiredTime = paymentExpiredTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public DeliveryOption getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOption deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public Integer getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(Integer voucherID) {
        this.voucherID = voucherID;  // Hỗ trợ null
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public String getVnp_TransactionNo() {
        return vnp_TransactionNo;
    }

    public void setVnp_TransactionNo(String vnp_TransactionNo) {
        this.vnp_TransactionNo = vnp_TransactionNo;
    }

    public String getVnp_TransactionDate() {
        return vnp_TransactionDate;
    }

    public void setVnp_TransactionDate(String vnp_TransactionDate) {
        this.vnp_TransactionDate = vnp_TransactionDate;
    }

    @Override
    public String toString() {
        return "OrderInfo{" + "orderID=" + orderID + ", orderDate=" + orderDate + ", expectedDeliveryDate=" + expectedDeliveryDate + ", deliveryAddress=" + deliveryAddress + ", deliveryOptionID=" + deliveryOptionID + ", customerID=" + customerID + ", preVoucherAmount=" + preVoucherAmount + ", voucherID=" + voucherID + ", staffID=" + staffID + ", shipperID=" + shipperID + ", deliveryStatus=" + deliveryStatus + ", orderStatus=" + orderStatus + ", adminID=" + adminID + ", deliveredAt=" + deliveredAt + ", paymentMethod=" + paymentMethod + ", paymentExpiredTime=" + paymentExpiredTime + ", paymentStatus=" + paymentStatus + ", orderProductList=" + orderProductList + ", deliveryOption=" + deliveryOption + ", vnp_TxnRef=" + vnp_TxnRef + ", vnp_TransactionNo=" + vnp_TransactionNo + '}';
    }

}
