/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Macbook
 */
public class Shipper {
    private int shipperID;
    private String deliveryAreas;
    private int totalDeliveries;
    private Account Account;
    public Shipper() {
    }

    public Shipper(int shipperID, String deliveryAreas, int totalDeliveries) {
        this.shipperID = shipperID;
        this.deliveryAreas = deliveryAreas;
        this.totalDeliveries = totalDeliveries;
    }

    public int getShipperID() {
        return shipperID;
    }

    public void setShipperID(int shipperID) {
        this.shipperID = shipperID;
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

    public Account getAccount() {
        return Account;
    }

    public void setAccount(Account Account) {
        this.Account = Account;
    }

    @Override
    public String toString() {
        return "Shipper{" + "shipperID=" + shipperID + ", deliveryAreas=" + deliveryAreas + ", totalDeliveries=" + totalDeliveries + '}';
    }
    
    
}
