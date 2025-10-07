package model;

import model.product_related.Product;

/**
 *
 * @author Macbook
 */
public class OrderProduct {

    private int orderID;
    private int productID;
    private int quantity;
    private int priceWithQuantity;
    private int rating;
    private String comment;
    private Product product;

    public OrderProduct() {
    }

    public OrderProduct(int orderID, int productID, int quantity, int priceWithQuantity, int rating, String comment) {
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
        this.rating = rating;
        this.comment = comment;
    }

    public OrderProduct(int productID, int quantity, int priceWithQuantity) {
        this.productID = productID;
        this.quantity = quantity;
        this.priceWithQuantity = priceWithQuantity;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderProduct{" + "orderID=" + orderID + ", productID=" + productID + ", quantity=" + quantity + ", priceWithQuantity=" + priceWithQuantity + ", rating=" + rating + ", comment=" + comment + '}';
    }

}
