package model;

import model.Product;
import java.util.List;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class Category implements IProductClassification {

    private int categoryID; // Maps to INT
    private String categoryName; // Maps to NVARCHAR(100)
    private String generalCategory;
    private List<Product> productList;

    // Constructors
    public Category() {
    }

    public Category(int categoryID, String categoryName, String generalCategory, List<Product> productList) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.generalCategory = generalCategory;
        this.productList = productList;
    }

    public Category(int categoryID, String categoryName, String generalCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.generalCategory = generalCategory;
    }

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    // Getters
    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getGeneralCategory() {
        return generalCategory;
    }

    public List<Product> getProductList() {
        return productList;
    }

    // Fluent Setters
    public Category setCategoryID(int categoryID) {
        this.categoryID = categoryID;
        return this;
    }

    public Category setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Category setGeneralCategory(String generalCategory) {
        this.generalCategory = generalCategory;
        return this;
    }

    public Category setProductList(List<Product> productList) {
        this.productList = productList;
        return this;
    }
    

    @Override
    public int getId() {
        return getCategoryID();
    }

    @Override
    public String getName() {
        return getCategoryName();
    }

    @Override
    public String getType() {
        return getGeneralCategory();
    }

    @Override
    public String getCode() {
        return "category";
    }
}
