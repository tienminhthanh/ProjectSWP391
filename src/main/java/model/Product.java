/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author anhkc
 */
public class Product {
    private int productID;
    private String productName;
    private List<String> imageURLList;
    private String productVersion;
    private String publisherID;
    private Date releaseDate;
    private Date saleEndDate;
    private String productIntro;
    private int stockCount;
    private double price;
    private int productDiscount;
    private int productFlashSale;
    private boolean onSale;
    private String specialFilter;

    public Product() {
    }

    public Product(int productID, String productName, List<String> imageURLList, Date releaseDate, Date saleEndDate, int stockCount, double price, int productDiscount) {
        this.productID = productID;
        this.productName = productName;
        this.imageURLList = imageURLList;
        this.releaseDate = releaseDate;
        this.saleEndDate = saleEndDate;
        this.stockCount = stockCount;
        this.price = price;
        this.productDiscount = productDiscount;
        this.onSale = productDiscount > 0;
    }

    public String getSpecialFilter() {
        return specialFilter;
    }

    public void setSpecialFilter(String specialFilter) {
        this.specialFilter = specialFilter;
    }
    
    

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getImageURLList() {
        return imageURLList;
    }

    public void setImageURLList(List<String> imageURLList) {
        this.imageURLList = imageURLList;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(String publisherID) {
        this.publisherID = publisherID;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(Date saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public String getProductIntro() {
        return productIntro;
    }

    public void setProductIntro(String productIntro) {
        this.productIntro = productIntro;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(int productDiscount) {
        this.productDiscount = productDiscount;
    }

    public int getProductFlashSale() {
        return productFlashSale;
    }

    public void setProductFlashSale(int productFlashSale) {
        this.productFlashSale = productFlashSale;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }
    
    
    
}
