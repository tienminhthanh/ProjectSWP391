/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.interfaces.IImportItemDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
                int productID = rs.getInt("productID");
                int supplierID = rs.getInt("supplierID");
                Supplier s = new Supplier(supplierID, rs.getString("supplierName"));
                Product p = new Product()
                        .setProductID(productID)
                        .setProductName(rs.getString("productName"))
                        .setImageURL(rs.getString("imageURL"))
                        .setStockCount(rs.getInt("stockCount"))
                        .setPrice(rs.getDouble("price"));
                productSupplyList.add(new ProductSupplier(productID, supplierID, rs.getDouble("defaultImportPrice"), rs.getInt("minImportQuant"), rs.getInt("maxImportQuant"), s, p));
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
    public Map<Supplier, List<ImportItem>> getPendingImports(int productID) throws SQLException {
        String sql = "SELECT ii.*, \n"
                + "       p.productName, \n"
                + "       p.specialFilter, \n"
                + "       p.releaseDate, \n"
                + "       p.price, \n"
                + "       s.supplierName\n"
                + "FROM ImportItem AS ii\n"
                + "INNER JOIN Product AS p ON ii.productID = p.productID\n"
                + "INNER JOIN Supplier AS s ON ii.supplierID = s.supplierID\n"
                + "WHERE ii.productID = ? AND ii.isImported = 0";

        Object[] params = {productID};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            Map<Supplier, List<ImportItem>> importMap = new HashMap<>();
            while (rs.next()) {
                LocalDate importDate = rs.getDate("importDate") != null ? rs.getDate("importDate").toLocalDate() : LocalDate.EPOCH;
                LocalDate releaseDate = rs.getDate("releaseDate") != null ? rs.getDate("releaseDate").toLocalDate() : LocalDate.MAX;
                Supplier supplier = new Supplier(rs.getInt("supplierID"), rs.getString("supplierName"));
                ImportItem item = new ImportItem()
                        .setImportItemID(rs.getInt("importItemID"))
                        .setProduct(new Product().setProductID(rs.getInt("productID")).setProductName(rs.getString("productName"))
                                .setSpecialFilter(rs.getString("specialFilter")).setReleaseDate(releaseDate).setPrice(rs.getDouble("price")))
                        .setSupplier(supplier)
                        .setImportDate(importDate)
                        .setImportPrice(rs.getDouble("importPrice"))
                        .setImportQuantity(rs.getInt("importQuantity"))
                        .setIsImported(rs.getBoolean("isImported"));

                importMap.computeIfAbsent(supplier, key -> new ArrayList<>()).add(item);
            }

            return importMap;
        }
    }

    @Override
    public boolean executeImports(List<ImportItem> importItemList) throws SQLException {
        Connection connection = null;
        List<Object> paramList = new ArrayList<>();
        int itemListSize = importItemList.size();
        Product product = importItemList.get(0).getProduct();
        int productID = product.getProductID();
        String specialFilter = product.getSpecialFilter();
        int quantity = 0;

        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);

            StringBuilder sqlBuilder = new StringBuilder();

            String placeHolder = String.join(",", Collections.nCopies(itemListSize, "?"));
            sqlBuilder.append("UPDATE ImportItem SET isImported = ? WHERE importItemID IN")
                    .append("(").append(placeHolder).append(")");

            paramList.add(true);
            for (ImportItem importItem : importItemList) {
                paramList.add(importItem.getImportItemID());
                quantity += importItem.getImportQuantity();
            }

            if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                throw new SQLException("Failed to update import status!");
            }

            sqlBuilder.setLength(0);
            paramList.clear();

            sqlBuilder.append("UPDATE Product SET stockCount = stockCount + ?\n");
            paramList.add(quantity);

            if (!specialFilter.equalsIgnoreCase("new")) {
                sqlBuilder.append(", specialFilter = ?\n");
                paramList.add("new");
            }

            sqlBuilder.append("WHERE productID = ?\n");
            paramList.add(productID);

            if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                throw new SQLException("Failed to update product info after import!");
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


}
