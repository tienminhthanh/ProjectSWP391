package model;

import java.math.BigDecimal;

public class CartItem {
    private int itemID;
    private int customerID;
    private int productID;
    private int cartItemQuantity;
    private BigDecimal cartItemPrice;
    private Product product;

    public CartItem() {
    }

    public CartItem(int customerID, Product product, int cartItemQuantity, BigDecimal cartItemPrice) {
        this.customerID = customerID;
        this.product = product;
        this.productID = product.getProductID();
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }

    public CartItem(int itemID, int customerID, int productID, int cartItemQuantity, BigDecimal cartItemPrice) {
        this.itemID = itemID;
        this.customerID = customerID;
        this.productID = productID;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }

    public CartItem(int customerID, int productID, int cartItemQuantity, BigDecimal cartItemPrice) {
        this.customerID = customerID;
        this.productID = productID;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }

    // Getters and setters
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

    public int getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(int cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public BigDecimal getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(BigDecimal cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}