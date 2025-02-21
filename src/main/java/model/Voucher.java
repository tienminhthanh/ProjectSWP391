/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Macbook
 */
public class Voucher {
    private int voucherID;
    private String voucherName;
    private int voucherValue;
    private int quantity;
    private int minimumPurchaseAmount;
    private Date dateCreated;
    private int duration;
    private int adminID;

    public Voucher() {
    }

    public Voucher(int voucherID, String voucherName, int voucherValue, int quantity, int minimumPurchaseAmount, Date dateCreated, int duration, int adminID) {
        this.voucherID = voucherID;
        this.voucherName = voucherName;
        this.voucherValue = voucherValue;
        this.quantity = quantity;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.adminID = adminID;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public int getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(int voucherValue) {
        this.voucherValue = voucherValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(int minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    @Override
    public String toString() {
        return "Voucher{" + "voucherID=" + voucherID + ", voucherName=" + voucherName + ", voucherValue=" + voucherValue + ", quantity=" + quantity + ", minimumPurchaseAmount=" + minimumPurchaseAmount + ", dateCreated=" + dateCreated + ", duration=" + duration + ", adminID=" + adminID + '}';
    }
    
    
}
