/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Customer {

    private int customerID;
    private double totalPurchasePoints;
    private String defaultDeliveryAddress;

    public Customer() {
    }

    public Customer(int customerID, double totalPurchasePoints, String defaultDeliveryAddress) {
        this.customerID = customerID;
        this.totalPurchasePoints = totalPurchasePoints;
        this.defaultDeliveryAddress = defaultDeliveryAddress;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    
}
