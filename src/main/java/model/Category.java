package model;

/**
 *
 * @author anhkc
 */
public class Category {
    private int categoryID; // Maps to INT
    private String categoryName; // Maps to NVARCHAR(100)
    private String generalCategory;

    // Constructors
    public Category() {}

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public Category(int categoryID, String categoryName, String generalCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.generalCategory = generalCategory;
    }

    // Getters
    public int getCategoryID() { return categoryID; }
    public String getCategoryName() { return categoryName; }
    public String getGeneralCategory() { return generalCategory; }

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
}
