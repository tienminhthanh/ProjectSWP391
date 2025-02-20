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
public class Book extends Product {

    private Publisher publisher;
    private String duration;

    public Book() {
    }

    /**
     * FULL - Retrieve data from Book join Product join Category join Image join
     * Publisher
     *
     * @param publisher
     * @param duration
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
     * @param imageList
     */
    public Book(Publisher publisher, String duration, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, List<Image> imageList) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageList);
        this.publisher = publisher;
        this.duration = duration;
    }

    /**
     * Omit lastModifiedTime, averageRating, numberOfRating - For Add/Update
     * May be we should omit imageList as well
     *
     * @param publisher
     * @param duration
     * @param productID
     * @param productName
     * @param price
     * @param stockCount
     * @param specificCategory
     * @param description
     * @param releaseDate
     * @param specialFilter
     * @param adminID
     * @param keywords
     * @param generalCategory
     * @param isActive
     * @param imageList
     */
    public Book(Publisher publisher, String duration, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, List<Image> imageList) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, specialFilter, adminID, keywords, generalCategory, isActive, imageList);
        this.publisher = publisher;
        this.duration = duration;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
