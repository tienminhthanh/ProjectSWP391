/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Macbook
 */
public class CartItem {
    private int itemID;
    private int customerID;
    private int productID;
    private int quantity;
    private int priceWithQuantity;

    public CartItem() {
    }

    public CartItem(int itemID, int customerID, int productID, int quantity, int priceWithQuantity) {
        this.itemID = itemID;
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriceWithQuantity() {
        return priceWithQuantity;
    }

    public void setPriceWithQuantity(int priceWithQuantity) {
        this.priceWithQuantity = priceWithQuantity;
    }

    @Override
    public String toString() {
        return "CartItem{" + "itemID=" + itemID + ", customerID=" + customerID + ", productID=" + productID + ", quantity=" + quantity + ", priceWithQuantity=" + priceWithQuantity + '}';
    }
    
    
}
