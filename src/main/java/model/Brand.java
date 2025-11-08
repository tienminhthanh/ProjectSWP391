package model;

import java.util.List;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class Brand implements IProductClassification {

    private int brandID;
    private String brandName;
    private List<Merchandise> merchList;

    // Constructors
    public Brand() {
    }

    public Brand(int brandID, String brandName, List<Merchandise> merchList) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.merchList = merchList;
    }
    
    public Brand(int brandID, String brandName) {
        this(brandID,brandName,null);
    }

    // Getters
    public int getBrandID() {
        return brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public List<Merchandise> getMerchList() {
        return merchList;
    }

    // Fluent Setters
    public Brand setBrandID(int brandID) {
        this.brandID = brandID;
        return this;
    }

    public Brand setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public Brand setMerchList(List<Merchandise> merchList) {
        this.merchList = merchList;
        return this;
    }

    @Override
    public int getId() {
        return getBrandID();
    }

    @Override
    public String getName() {
        return getBrandName();
    }

    @Override
    public String getType() {
        return "merch";
    }

    @Override
    public String getCode() {
        return "brand";
    }

}
