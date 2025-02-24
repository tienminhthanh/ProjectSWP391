/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author anhkc
 */
public class Merchandise extends Product {


    private Series series;
    private Character character;
    private Brand brand;
    private String size;
    private String scaleLevel;
    private String material;

    // Constructor

    public Merchandise() {
    }

    /**
     * Full
     *
     * @param series
     * @param character
     * @param brand
     * @param size
     * @param scaleLevel
     * @param material
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
    public Merchandise(Series series, Character character, Brand brand, String size, String scaleLevel, String material, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL);
        this.series = series;
        this.character = character;
        this.brand = brand;
        this.size = size;
        this.scaleLevel = scaleLevel;
        this.material = material;
    }

    /**
     * Omit some
     *
     * @param series
     * @param character
     * @param brand
     * @param size
     * @param scaleLevel
     * @param material
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
    public Merchandise(Series series, Character character, Brand brand, String size, String scaleLevel, String material, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL);
        this.series = series;
        this.character = character;
        this.brand = brand;
        this.size = size;
        this.scaleLevel = scaleLevel;
        this.material = material;
    }


    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getScaleLevel() {
        return scaleLevel;
    }

    public void setScaleLevel(String scaleLevel) {
        this.scaleLevel = scaleLevel;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }


}
