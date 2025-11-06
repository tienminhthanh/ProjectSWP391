/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.interfaces.IImportItemDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import model.ImportItem;
import model.Product;
import model.ProductSupplier;
import model.Supplier;
import utils.DBContext;

/**
 *
 * @author anhkc
 */
public class ImportItemDAO implements IImportItemDAO {

    private static final ImportItemDAO instance = new ImportItemDAO();
    private final DBContext context;

    //Normal run
    private ImportItemDAO() {
        context = DBContext.getInstance();
    }

    public static ImportItemDAO getInstance() {
        return instance;
    }

    @Override
    public List<ProductSupplier> getAllProductSupplies() throws SQLException {
        String sql = "SELECT Product_Supplier.*, Product.productName, Product.imageURL, Product.stockCount, Product.price, Supplier.supplierName\n"
                + "FROM Product INNER JOIN\n"
                + "Product_Supplier ON Product.productID = Product_Supplier.productID INNER JOIN\n"
                + "Supplier ON Product_Supplier.supplierID = Supplier.supplierID\n"
                + "ORDER BY Supplier.supplierName ASC";
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            List<ProductSupplier> productSupplyList = new ArrayList<>();
            while (rs.next()) {
                productSupplyList.add(mapResultSetToProductSupplier(rs));
            }
            return productSupplyList;
        }
    }

    @Override
    public boolean queueImports(List<ImportItem> importItemList) throws SQLException {
        Connection connection = null;
        List<Object> paramList = new ArrayList<>();
        Set<Integer> productIDs = new LinkedHashSet<>();
        int itemListSize = importItemList.size();
        for (ImportItem importItem : importItemList) {
            int productID = importItem.getProductID();
            if (!productIDs.contains(productID)) {
                productIDs.add(productID);
            }

            paramList.add(productID);
            paramList.add(importItem.getSupplierID());
            paramList.add(importItem.getImportPrice());
            paramList.add(importItem.getImportQuantity());
            paramList.add(importItem.getImportDate());
            paramList.add(importItem.isIsImported());
        }
        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);
            String placeHolder = String.join(",", Collections.nCopies(itemListSize, "(?, ?, ?, ?, ?, ?)"));
            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ImportItem (productID, supplierID, importPrice,")
                    .append("\nimportQuantity, importDate, isImported)")
                    .append("\nVALUES")
                    .append(placeHolder);
            if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                throw new SQLException("Failed to add new imports to queue!");
            }

            placeHolder = String.join(",", Collections.nCopies(productIDs.size(), "?"));
            sqlBuilder.setLength(0);
            sqlBuilder.append("UPDATE Product SET lastModifiedTime = GETDATE() WHERE productID IN")
                    .append("(").append(placeHolder).append(")");

            if (context.exeNonQuery(connection, sqlBuilder.toString(), productIDs.toArray(), false) == 0) {
                throw new SQLException("Failed to update product info after queueing!");
            }

            connection.commit();
            return true;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                    connection.close();
                } catch (SQLException e) {
                    throw e;
                }
            }

        }
    }

    @Override
    public List<ImportItem> getPendingImportItem() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean executeImports(List<ImportItem> importItemList) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private ProductSupplier mapResultSetToProductSupplier(ResultSet rs) throws SQLException {
        int productID = rs.getInt("productID");
        int supplierID = rs.getInt("supplierID");
        Supplier s = new Supplier(supplierID, rs.getString("supplierName"));
        Product p = new Product()
                .setProductID(productID)
                .setProductName(rs.getString("productName"))
                .setImageURL(rs.getString("imageURL"))
                .setStockCount(rs.getInt("stockCount"))
                .setPrice(rs.getDouble("price"));
        return new ProductSupplier(productID, supplierID, rs.getDouble("defaultImportPrice"), rs.getInt("minImportQuant"), rs.getInt("maxImportQuant"), s, p);
    }

}
