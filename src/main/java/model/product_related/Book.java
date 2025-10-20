/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.product_related;

import dao.BookDAO;
import service.BookDetailsService;
import service.factory.ProductDetailsFactory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import model.OrderProduct;
import model.product_related.Category;
import model.product_related.Creator;
import model.product_related.Genre;
import model.product_related.Product;
import model.product_related.Publisher;


/**
 *
 * @author anhkc
 */
public class Book extends Product {

    private Publisher publisher;
    private String duration;
    private List<Genre> genreList;
    

    public Book() {
    }
    
    // For view details
    public Book(Publisher publisher, String duration, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL, int discountPercentage, LocalDate eventEndDate, int salesRank) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, discountPercentage, eventEndDate, salesRank);
        this.publisher = publisher;
        this.duration = duration;
    }
    
    
    
    //For management
    public Book(Publisher publisher, String duration, List<Genre> genreList, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL, List<Creator> creatorList) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, creatorList);
        this.publisher = publisher;
        this.duration = duration;
        this.genreList = genreList;
    }

    
    
    
    //Getters
    public Publisher getPublisher() {
        return publisher;
    }

    public String getDuration() {
        return duration;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
    
    
        
  // Fluent Setters
    public Book setPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }

    public Book setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public Book setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
        return this;
    }
    
    
    public static void main(String[] args) {
//        ProductDetailsFactory.register("book", (id,isMangement) -> BookDAO.getInstance().getProductById(id, isMangement));
//        ProductDetailsFactory.registerExtraAttributes("book", (product,id) -> BookDetailsService.getInstance().loadExtraAttributes(product, id));
    }
}
