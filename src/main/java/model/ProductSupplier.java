/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author anhkc
 */
public class ProductSupplier {

    private int productID;
    private int supplierID;
    private double defaultImportPrice;
    private int minImportQuant;
    private int maxImportQuant;
    private Supplier supplier;
    private Product product;
    private List<ImportItem> importItemList;
    private int hashedID;

    public ProductSupplier() {
    }

    public ProductSupplier(int productID, int supplierID, double defaultImportPrice, int minImportQuant, int maxImportQuant, Supplier supplier, Product product, List<ImportItem> importItemList) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.defaultImportPrice = defaultImportPrice;
        this.minImportQuant = minImportQuant;
        this.maxImportQuant = maxImportQuant;
        this.supplier = supplier;
        this.product = product;
        this.importItemList = importItemList;
        setHashedID();
    }

    public ProductSupplier(int productID, int supplierID, double defaultImportPrice, int minImportQuant, int maxImportQuant, Supplier supplier, Product product) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.defaultImportPrice = defaultImportPrice;
        this.minImportQuant = minImportQuant;
        this.maxImportQuant = maxImportQuant;
        this.supplier = supplier;
        this.product = product;
        this.importItemList = null;
        setHashedID();
    }

    public int getProductID() {
        return productID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public double getDefaultImportPrice() {
        return defaultImportPrice;
    }

    public int getMinImportQuant() {
        return minImportQuant;
    }

    public int getMaxImportQuant() {
        return maxImportQuant;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Product getProduct() {
        return product;
    }

    public List<ImportItem> getImportItemList() {
        return importItemList;
    }

    public int getHashedID() {
        if(supplierID > 0 && productID >0){
            setHashedID();
        }
        return hashedID;
    }

    public ProductSupplier setProductID(int productID) {
        this.productID = productID;
        return this;
    }

    public ProductSupplier setSupplierID(int supplierID) {
        this.supplierID = supplierID;
        return this;
    }

    public ProductSupplier setDefaultImportPrice(double defaultImportPrice) {
        this.defaultImportPrice = defaultImportPrice;
        return this;
    }

    public ProductSupplier setMinImportQuant(int minImportQuant) {
        this.minImportQuant = minImportQuant;
        return this;
    }

    public ProductSupplier setMaxImportQuant(int maxImportQuant) {
        this.maxImportQuant = maxImportQuant;
        return this;
    }

    public ProductSupplier setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public ProductSupplier setProduct(Product product) {
        this.product = product;
        return this;
    }

    public ProductSupplier setImportItemList(List<ImportItem> importItemList) {
        this.importItemList = importItemList;
        return this;
    }
    
    /**
     * PREVENT PUBLIC ACCESS
     * @return 
     */
    private void setHashedID() {
        this.hashedID = this.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProductSupplier)) {
            return false;
        }

        ProductSupplier other = (ProductSupplier) obj;
        return this.productID == other.productID && this.supplierID == other.supplierID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, supplierID);
    }

    @Override
    public String toString() {
        return "ProductSupplier{" + "productID=" + productID + ", supplierID=" + supplierID + '}';
    }

//    public static void main(String[] args) {
//        ProductSupplier ps = new ProductSupplier().setProductID(1).setSupplierID(14);
//        ProductSupplier ps2 = new ProductSupplier().setProductID(1).setSupplierID(14).setProduct(null).setMinImportQuant(5);
//        ProductSupplier ps3 = new ProductSupplier(1, 14, 0, 0, 0, null, null);
//        System.out.println(ps.getHashedID());
//        System.out.println(ps2.getHashedID());
//        System.out.println(ps3.getHashedID());
//    }
}
