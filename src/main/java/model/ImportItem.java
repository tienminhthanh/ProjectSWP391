/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
/**
 *
 * @author anhkc
 */

public class ImportItem {
    private LocalDate importDate;
    private int importItemID;
    private Supplier supplier;
    private Product product;
    private double importPrice;
    private int importQuantity;
    private boolean isImported;

    public ImportItem() {
    }

    public ImportItem(int importItemID, Supplier supplier, Product product, double importPrice, int importQuantity, LocalDate importDate, boolean isImported) {
        this.importItemID = importItemID;
        this.supplier = supplier;
        this.product = product;
        this.importPrice = importPrice;
        this.importQuantity = importQuantity;
        this.importDate = importDate;
        this.isImported = isImported;
    }

 

    // Getters
    public int getImportItemID() {
        return importItemID;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Product getProduct() {
        return product;
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

    // Fluent Setters
    public ImportItem setImportItemID(int importItemID) {
        this.importItemID = importItemID;
        return this;
    }

    public ImportItem setSupplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public ImportItem setProduct(Product product) {
        this.product = product;
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

    public ImportItem setImportDate(LocalDate importDate) {
        this.importDate = importDate;
        return this;
    }

    public ImportItem setIsImported(boolean isImported) {
        this.isImported = isImported;
        return this;
    }

    @Override
    public String toString() {
        return "ImportItem{" + "importItemID=" + importItemID + ", importPrice=" + importPrice + ", importQuantity=" + importQuantity + ", importDate=" + importDate + ", isImported=" + isImported + '}';
    }
    
    
}