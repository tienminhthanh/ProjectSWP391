package model;

import java.math.BigDecimal;

public class CartItem {
    private int itemID;
    private int customerID;
    private int productID;
    private int quantity;
    private BigDecimal priceWithQuantity;
    
    public CartItem() {
    }
    
    public CartItem(int itemID, int customerID, int productID, int quantity, BigDecimal priceWithQuantity) {
        this.itemID = itemID;
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
    }

    public CartItem(int customerID, int productID, int quantity, BigDecimal priceWithQuantity) {
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
    }
    
    public CartItem(int customerID, int productID, int quantity, BigDecimal priceWithQuantity, int itemID) {
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
    }
    
    // Getters và setters
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
    public BigDecimal getPriceWithQuantity() {
        return priceWithQuantity;
    }
    public void setPriceWithQuantity(BigDecimal priceWithQuantity) {
        this.priceWithQuantity = priceWithQuantity;
    }
}
