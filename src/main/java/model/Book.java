/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 *
 * @author anhkc
 */
public class Book extends Product {

    private Publisher publisher;
    private String duration;

    public Book() {
    }
    
    
    //Omit salesRank
    public Book(Publisher publisher, String duration, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL, int discountPercentage, LocalDate eventEndDate) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, discountPercentage, eventEndDate);
        this.publisher = publisher;
        this.duration = duration;
    }

    public Book(Publisher publisher, String duration, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, specialFilter, adminID, keywords, generalCategory, isActive, imageURL);
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
