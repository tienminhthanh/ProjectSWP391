/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import model.ImportItem;
import model.ProductSupplier;
import model.Supplier;

/**
 *
 * @author anhkc
 */
public interface IImportItemDAO {
    List<ProductSupplier> getAllProductSupplies() throws SQLException;

    boolean queueImports(List<ImportItem> importItemList) throws SQLException;

    Map<Supplier, List<ImportItem>> getPendingImports(int productID) throws SQLException;

    boolean executeImports(List<ImportItem> importItemList) throws SQLException;
}
