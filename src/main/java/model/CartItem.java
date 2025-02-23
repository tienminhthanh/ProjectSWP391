package model;

import java.math.BigDecimal;
import java.util.List;

public class CartItem {

    private int itemID;
    private int customerID;
    private int productID;
    private int quantity;
    private BigDecimal priceWithQuantity;
    private Product product;
    private List<CartItem> itemList;
    private Product productName;

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

    public CartItem(int itemID, int customerID, int productID, int quantity, BigDecimal priceWithQuantity, Product product, List<CartItem> itemList) {
        this.itemID = itemID;
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
        this.product = product;
        this.itemList = itemList;

    }

//    public CartItem(int customerID, int productID, int quantity, BigDecimal priceWithQuantity, int itemID) {
//        this.customerID = customerID;
//        this.productID = productID;
//        this.quantity = quantity;
//        this.priceWithQuantity = priceWithQuantity;
//    }
    // Getters vÃ  setters
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

    public void setQuantity(int quantity) throws Exception {
        this.quantity = quantity;
    }

    public BigDecimal getPriceWithQuantity() {
        return priceWithQuantity;
    }

    public void setPriceWithQuantity(BigDecimal priceWithQuantity) {
        this.priceWithQuantity = priceWithQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    public Product getProductName() {
        return productName;
    }

    public void setProductName(Product productName) {
        this.productName = productName;
    }
    
}
