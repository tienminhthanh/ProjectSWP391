/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Shipper extends Account{
    private String deliveryAreas;
    private int totalDeliveries;

    public Shipper() {
    }

    public Shipper(String deliveryAreas, int totalDeliveries, int accountID, String username, String password, String role, String firstName, String lastName, String email, String phoneNumber, String birthDate, boolean isActive) {
        super(accountID, username, password, role, firstName, lastName, email, phoneNumber, birthDate, isActive);
        this.deliveryAreas = deliveryAreas;
        this.totalDeliveries = totalDeliveries;
    }

    public String getDeliveryAreas() {
        return deliveryAreas;
    }

    public void setDeliveryAreas(String deliveryAreas) {
        this.deliveryAreas = deliveryAreas;
    }

    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }
    
    
    
}
