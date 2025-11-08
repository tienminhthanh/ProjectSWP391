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
public class Supplier {

    private int supplierID;
    private String supplierName;
    private List<ProductSupplier> productSupplyList;

    public Supplier() {
    }

    // Constructor
    public Supplier(int supplierID, String supplierName, List<ProductSupplier> productSupplyList) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.productSupplyList = productSupplyList;
    }

    public Supplier(int supplierID, String supplierName) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.productSupplyList = null;
    }
    
    

    // Getters
    public int getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public List<ProductSupplier> getProductSupplyList() {
        return productSupplyList;
    }


    // Fluent Setters
    public Supplier setSupplierID(int supplierID) {
        this.supplierID = supplierID;
        return this;
    }

    public Supplier setSupplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }
    public Supplier setProductSupplyList(List<ProductSupplier> productSupplyList) {
        this.productSupplyList = productSupplyList;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Supplier)) {
            return false;
        }

        Supplier other = (Supplier) obj;
        return this.supplierID == other.supplierID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierID);
    }

    @Override
    public String toString() {
        return String.valueOf(supplierID);
    }

}
