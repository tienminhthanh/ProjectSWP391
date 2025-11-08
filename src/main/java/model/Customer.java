/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import model.OrderInfo;

/**
 *
 * @author Admin
 */
public class Customer extends Account {

    private double totalPurchasePoints;
    private String defaultDeliveryAddress;
    private List<OrderInfo> orderList;
    

    public Customer() {
    }

    public Customer(int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountIsActive);
    }

    public Customer(double totalPurchasePoints, String defaultDeliveryAddress, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean accountIsActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, accountIsActive);
        this.totalPurchasePoints = totalPurchasePoints;
        this.defaultDeliveryAddress = defaultDeliveryAddress;
    }

    public double getTotalPurchasePoints() {
        return totalPurchasePoints;
    }

    public void setTotalPurchasePoints(double totalPurchasePoints) {
        this.totalPurchasePoints = totalPurchasePoints;
    }

    public String getDefaultDeliveryAddress() {
        return defaultDeliveryAddress;
    }

    public void setDefaultDeliveryAddress(String defaultDeliveryAddress) {
        this.defaultDeliveryAddress = defaultDeliveryAddress;
    }

    public List<OrderInfo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfo> orderList) {
        this.orderList = orderList;
    }
    
}
