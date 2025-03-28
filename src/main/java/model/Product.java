/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Product {
//Maps from categoryID int
    private int productID;
    private String productName;
    private double price;
    private int stockCount;
    private Category specificCategory;
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
    private int discountPercentage;
    private LocalDate eventEndDate;
    private int salesRank;

    /**
     * EMPTY - Set attributes later
     */
    public Product() {
    }
    
    /**
     * Omit salesRank, set later if query for Leaderboard - For Catalog
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
     * @param discountPercentage
     * @param eventEndDate 
     */
    public Product(int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL, int discountPercentage, LocalDate eventEndDate) {
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
        this.discountPercentage = discountPercentage;
        this.eventEndDate = eventEndDate;
    }
    
    
    
    /**
     * Omit discountPercentage, eventEndDate, salesRank - For Management
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

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getStockCount() {
        return stockCount;
    }

    public Category getSpecificCategory() {
        return specificCategory;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public String getSpecialFilter() {
        return specialFilter;
    }

    public int getAdminID() {
        return adminID;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getGeneralCategory() {
        return generalCategory;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getEventEndDate() {
        return eventEndDate;
    }

    public int getSalesRank() {
        return salesRank;
    }
    

    // Fluent Setters
    public Product setProductID(int productID) {
        this.productID = productID;
        return this;
    }

    public Product setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public Product setPrice(double price) {
        this.price = price;
        return this;
    }

    public Product setStockCount(int stockCount) {
        this.stockCount = stockCount;
        return this;
    }

    public Product setSpecificCategory(Category specificCategory) {
        this.specificCategory = specificCategory;
        return this;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public Product setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Product setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public Product setAverageRating(double averageRating) {
        this.averageRating = averageRating;
        return this;
    }

    public Product setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
        return this;
    }

    public Product setSpecialFilter(String specialFilter) {
        this.specialFilter = specialFilter;
        return this;
    }

    public Product setAdminID(int adminID) {
        this.adminID = adminID;
        return this;
    }

    public Product setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public Product setGeneralCategory(String generalCategory) {
        this.generalCategory = generalCategory;
        return this;
    }

    public Product setIsActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Product setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public Product setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
        return this;
    }

    public Product setEventEndDate(LocalDate eventEndDate) {
        this.eventEndDate = eventEndDate;
        return this;
    }

    public Product setSalesRank(int salesRank) {
        this.salesRank = salesRank;
        return this;
    }

    @Override
    public String toString() {
        return "{productID=" + productID + ", productName=" + productName + ", price=" + price + ", stockCount=" + stockCount + ", specificCategory=" + specificCategory + ", description=" + description + ", releaseDate=" + releaseDate + ", lastModifiedTime=" + lastModifiedTime + ", averageRating=" + averageRating + ", numberOfRating=" + numberOfRating + ", specialFilter=" + specialFilter + ", adminID=" + adminID + ", keywords=" + keywords + ", generalCategory=" + generalCategory + ", isActive=" + isActive + ", imageURL=" + imageURL + ", discountPercentage=" + discountPercentage + ", eventEndDate=" + eventEndDate + ", salesRank=" + salesRank + '}';
    }

    
    
    public double getCurrentPrice() {
        LocalDate now = LocalDate.now();
        if (eventEndDate != null && now.isBefore(eventEndDate.plusDays(1)) && discountPercentage > 0) {
            return price * (1 - discountPercentage / 100.0); // Giá sau giảm
        }
        return price;
    }
    
}
    

    


    
    
