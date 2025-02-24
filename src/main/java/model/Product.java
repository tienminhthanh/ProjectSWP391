/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Product {

    private int productID;
    private String productName;
    private double price;
    private int stockCount;
    private Category specificCategory; //Maps from categoryID int
    private String description;
    private LocalDate releaseDate;
    private LocalDateTime lastModifiedTime;
    private double averageRating;
    private int numberOfRating;
    private String specialFilter;
    private int adminID;
    private String keywords;
    private String generalCategory;
    private boolean isActive;

    private String imageURL;

    /**
     * EMPTY - Set attributes later
     */
    public Product() {
    }

    /**

     * FULL - For retrieve data from Product join Category
     *
     * @param productID
     * @param productName
     * @param price
     * @param stockCount
     * @param specificCategory
     * @param description
     * @param releaseDate
     * @param lastModifiedTime
     * @param averageRating
     * @param numberOfRating
     * @param specialFilter
     * @param adminID
     * @param keywords
     * @param generalCategory
     * @param isActive

     * @param imageURL
     */
    public Product(int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockCount = stockCount;
        this.specificCategory = specificCategory;
        this.description = description;
        this.releaseDate = releaseDate;
        this.lastModifiedTime = lastModifiedTime;
        this.averageRating = averageRating;
        this.numberOfRating = numberOfRating;
        this.specialFilter = specialFilter;
        this.adminID = adminID;
        this.keywords = keywords;
        this.generalCategory = generalCategory;
        this.isActive = isActive;

        this.imageURL = imageURL;
    }

    /**
     * Omit lastModifiedTime, averageRating, numberOfRating - For Add/Update

     *
     *
     * @param productID
     * @param productName
     * @param price
     * @param stockCount
     * @param specificCategory
     * @param description
     * @param releaseDate

     * @param numberOfRating
     * @param specialFilter
     * @param adminID
     * @param keywords
     * @param generalCategory
     * @param isActive

     * @param imageURL
     */
    public Product(int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockCount = stockCount;
        this.specificCategory = specificCategory;
        this.description = description;
        this.releaseDate = releaseDate;

        this.numberOfRating = numberOfRating;
        this.specialFilter = specialFilter;
        this.adminID = adminID;
        this.keywords = keywords;
        this.generalCategory = generalCategory;
        this.isActive = isActive;

        this.imageURL = imageURL;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public Category getSpecificCategory() {
        return specificCategory;
    }

    public void setSpecificCategory(Category specificCategory) {
        this.specificCategory = specificCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public String getSpecialFilter() {
        return specialFilter;
    }

    public void setSpecialFilter(String specialFilter) {
        this.specialFilter = specialFilter;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getGeneralCategory() {
        return generalCategory;
    }

    public void setGeneralCategory(String generalCategory) {
        this.generalCategory = generalCategory;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    
}
