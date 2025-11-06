/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.Product;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author anhkc
 */
public class ImportItem {

    private int importItemID;
    private int productID;
    private int supplierID;
    private double importPrice;
    private int importQuantity;
    private LocalDate importDate;
    private boolean isImported;
    private ProductSupplier productSupplier;

    ////OLD
    private Product product;
    private Supplier supplier;

    public ImportItem() {
    }

    public ImportItem(int importItemID, int productID, int supplierID, double importPrice, int importQuantity, LocalDate importDate, boolean isImported, ProductSupplier productSupplier) {
        this.importItemID = importItemID;
        this.productID = productID;
        this.supplierID = supplierID;
        this.importPrice = importPrice;
        this.importQuantity = importQuantity;
        this.importDate = importDate;
        this.isImported = isImported;
        this.productSupplier = productSupplier;
    }

    public ImportItem(int importItemID, int productID, int supplierID, double importPrice, int importQuantity, LocalDate importDate, boolean isImported) {
        this.importItemID = importItemID;
        this.productID = productID;
        this.supplierID = supplierID;
        this.importPrice = importPrice;
        this.importQuantity = importQuantity;
        this.importDate = importDate;
        this.isImported = isImported;
        this.productSupplier = null;
    }

    ///OLD
    public ImportItem(int importItemID, double importPrice, int importQuantity, LocalDate importDate, boolean isImported, Product product, Supplier supplier) {
        this.importItemID = importItemID;
        this.importPrice = importPrice;
        this.importQuantity = importQuantity;
        this.importDate = importDate;
        this.isImported = isImported;
        this.product = product;
        this.supplier = supplier;
    }
    
    
    public Product getProduct() {
        return product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public ImportItem setProduct(Product product) {
        this.product = product;
        return this;
    }

    public ImportItem setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    // Getters
    public int getImportItemID() {
        return importItemID;
    }

    public int getProductID() {
        return productID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public int getImportQuantity() {
        return importQuantity;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public boolean isIsImported() {
        return isImported;
    }

    public ProductSupplier getProductSupplier() {
        return productSupplier;
    }

    // Fluent Setters
    public ImportItem setImportDate(LocalDate importDate) {
        this.importDate = importDate;
        return this;
    }

    public ImportItem setImportItemID(int importItemID) {
        this.importItemID = importItemID;
        return this;
    }

    public ImportItem setProductID(int productID) {
        this.productID = productID;
        return this;
    }

    public ImportItem setSupplierID(int supplierID) {
        this.supplierID = supplierID;
        return this;
    }

    public ImportItem setImportPrice(double importPrice) {
        this.importPrice = importPrice;
        return this;
    }

    public ImportItem setImportQuantity(int importQuantity) {
        this.importQuantity = importQuantity;
        return this;
    }

    public ImportItem setIsImported(boolean isImported) {
        this.isImported = isImported;
        return this;
    }

    public ImportItem setProductSupplier(ProductSupplier productSupplier) {
        this.productSupplier = productSupplier;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImportItem)) {
            return false;
        }

        ImportItem other = (ImportItem) obj;
        return this.importItemID == other.importItemID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(importItemID);
    }

    @Override
    public String toString() {
        return "ImportItem{" + "importItemID=" + importItemID + ", productID=" + productID + ", supplierID=" + supplierID + ", importPrice=" + importPrice + ", importQuantity=" + importQuantity + ", importDate=" + importDate + ", isImported=" + isImported + ", productSupplier=" + productSupplier + '}';
    }

}
