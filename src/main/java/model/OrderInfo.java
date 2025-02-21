package model;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author Macbook
 */
public class OrderInfo {

    private int orderID;
    private Date orderDate;
    private String deliveryAddress;
    private int deliveryOptionID;
    private int customerID;
    private int preVoucherAmount;
    private int voucherID;
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

    public OrderInfo() {
    }

    public OrderInfo(int orderID, Date orderDate, String deliveryAddress, int deliveryOptionID, int customerID, int preVoucherAmount, int voucherID, int staffID, int shipperID, String deliveryStatus, String orderStatus, int adminID, Date deliveredAt, String paymentMethod, int paymentExpiredTime, String paymentStatus) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.deliveryOptionID = deliveryOptionID;
        this.customerID = customerID;
        this.preVoucherAmount = preVoucherAmount;
        this.voucherID = voucherID;
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
// dung de insert 
    public OrderInfo(int orderID, Date orderDate, String deliveryAddress, int deliveryOptionID, int customerID, int preVoucherAmount, int voucherID, String deliveryStatus, String orderStatus) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.deliveryOptionID = deliveryOptionID;
        this.customerID = customerID;
        this.preVoucherAmount = preVoucherAmount;
        this.voucherID = voucherID;
        this.deliveryStatus = deliveryStatus;
        this.orderStatus = orderStatus;
    }

    public OrderInfo(int i, Date orderDate, String deliveryAddress, int deliveryOptionID, int parseInt, int preVoucherAmount, int voucherID, int i0, int i1, String deliveryStatus, String orderStatus, int i2, Object object, String cod, int i3, String unpaid, List<OrderProduct> orderProducts) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

    public int getPreVoucherAmount() {
        return preVoucherAmount;
    }

    public void setPreVoucherAmount(int preVoucherAmount) {
        this.preVoucherAmount = preVoucherAmount;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
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



    @Override
    public String toString() {
        return "OrderInfo{" + "orderID=" + orderID + ", orderDate=" + orderDate + ", deliveryAddress=" + deliveryAddress + ", deliveryOptionID=" + deliveryOptionID + ", customerID=" + customerID + ", preVoucherAmount=" + preVoucherAmount + ", voucherID=" + voucherID + ", staffID=" + staffID + ", shipperID=" + shipperID + ", deliveryStatus=" + deliveryStatus + ", orderStatus=" + orderStatus + ", adminID=" + adminID + ", deliveredAt=" + deliveredAt + ", paymentMethod=" + paymentMethod + ", paymentExpiredTime=" + paymentExpiredTime + ", paymentStatus=" + paymentStatus + '}';
    }

}
