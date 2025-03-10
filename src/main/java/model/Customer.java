/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Customer extends Account {

    private double totalPurchasePoints;
    private String defaultDeliveryAddress;

    public Customer() {
    }

    public Customer(double totalPurchasePoints, String defaultDeliveryAddress, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean isActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive);
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

}
