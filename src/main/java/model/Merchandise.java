package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author anhkc
 */
public class Merchandise extends Product {

    private Series series;
    private OGCharacter character;
    private Brand brand;
    private String size;
    private String scaleLevel;
    private String material;

    // Constructors
    public Merchandise() {}

    public Merchandise(Series series, OGCharacter character, Brand brand, String size, String scaleLevel, String material, 
                       int productID, String productName, double price, int stockCount, Category specificCategory, 
                       String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, 
                       int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, 
                       boolean isActive, String imageURL, int discountPercentage, LocalDate eventEndDate) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, 
              averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, 
              discountPercentage, eventEndDate);
        this.series = series;
        this.character = character;
        this.brand = brand;
        this.size = size;
        this.scaleLevel = scaleLevel;
        this.material = material;
    }

    public Merchandise(Series series, OGCharacter character, Brand brand, String size, String scaleLevel, String material, 
                       int productID, String productName, double price, int stockCount, Category specificCategory, 
                       String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, 
                       int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, 
                       boolean isActive, String imageURL) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, 
              averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL);
        this.series = series;
        this.character = character;
        this.brand = brand;
        this.size = size;
        this.scaleLevel = scaleLevel;
        this.material = material;
    }

    // Getters
    public Series getSeries() { return series; }
    public OGCharacter getCharacter() { return character; }
    public Brand getBrand() { return brand; }
    public String getSize() { return size; }
    public String getScaleLevel() { return scaleLevel; }
    public String getMaterial() { return material; }

    // Fluent Setters
    public Merchandise setSeries(Series series) {
        this.series = series;
        return this;
    }

    public Merchandise setCharacter(OGCharacter character) {
        this.character = character;
        return this;
    }

    public Merchandise setBrand(Brand brand) {
        this.brand = brand;
        return this;
    }

    public Merchandise setSize(String size) {
        this.size = size;
        return this;
    }

    public Merchandise setScaleLevel(String scaleLevel) {
        this.scaleLevel = scaleLevel;
        return this;
    }

    public Merchandise setMaterial(String material) {
        this.material = material;
        return this;
    }
}
