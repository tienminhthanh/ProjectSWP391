package model.product_related;

import service.factory.ProductDetailsFactory;
import service.MerchDetailsService;
import dao.MerchDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import model.OrderProduct;
import model.product_related.Brand;
import model.product_related.Category;
import model.product_related.Creator;
import model.product_related.OGCharacter;
import model.product_related.Product;
import model.product_related.Series;

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
    
    //For view details
    public Merchandise(Series series, OGCharacter character, Brand brand, String size, String scaleLevel, String material, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL, int discountPercentage, LocalDate eventEndDate, int salesRank) {    
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, discountPercentage, eventEndDate, salesRank);
        this.series = series;
        this.character = character;
        this.brand = brand;
        this.size = size;
        this.scaleLevel = scaleLevel;
        this.material = material;
    }

    //For management
    public Merchandise(Series series, OGCharacter character, Brand brand, String size, String scaleLevel, String material, int productID, String productName, double price, int stockCount, Category specificCategory, String description, LocalDate releaseDate, LocalDateTime lastModifiedTime, double averageRating, int numberOfRating, String specialFilter, int adminID, String keywords, String generalCategory, boolean isActive, String imageURL, List<Creator> creatorList) {
        super(productID, productName, price, stockCount, specificCategory, description, releaseDate, lastModifiedTime, averageRating, numberOfRating, specialFilter, adminID, keywords, generalCategory, isActive, imageURL, creatorList);
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
    
    public static void main(String[] args) {
//        ProductDetailsFactory.register("merch", (id,isManagement) -> MerchDAO.getInstance().getProductById(id, isManagement));
//        ProductDetailsFactory.registerExtraAttributes("merch", (product,id) -> MerchDetailsService.getInstance().loadExtraAttributes(product,id));
    }
}
