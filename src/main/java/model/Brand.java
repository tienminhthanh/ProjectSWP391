package model;

/**
 *
 * @author anhkc
 */
public class Brand {
    private int brandID;
    private String brandName;

    // Constructors
    public Brand() {}

    public Brand(int brandID, String brandName) {
        this.brandID = brandID;
        this.brandName = brandName;
    }

    // Getters
    public int getBrandID() { return brandID; }
    public String getBrandName() { return brandName; }

    // Fluent Setters
    public Brand setBrandID(int brandID) {
        this.brandID = brandID;
        return this;
    }

    public Brand setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }
}
