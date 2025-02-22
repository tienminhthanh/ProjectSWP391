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
    private Image image;
    
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
    public void setQuantity(int quantity) throws Exception {
        if (this.itemList == null) {
            throw new Exception("Cart is empty.");
        }
        int totalQuantity = 0;
        for (CartItem item : this.itemList) {
            totalQuantity += item.getQuantity();
        }
        this.quantity = totalQuantity;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
