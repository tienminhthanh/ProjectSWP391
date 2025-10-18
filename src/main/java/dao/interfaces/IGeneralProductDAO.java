/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import model.interfaces.ProductClassification;
import model.product_related.Product;

/**
 *
 * @author anhkc
 */
public interface IGeneralProductDAO {

    Product getProductById(int productID) throws SQLException;

    List<Product> getClassifiedProductList(ProductClassification clsf, String sortCriteria, Map<String, String> filterMap, int page, int pageSize, boolean isHomepage) throws SQLException;

    int countClassifiedProductList(ProductClassification clsf, Map<String, String> filterMap) throws SQLException;

    List<Product> getRankedProducts(String type) throws SQLException;

    List<Product> getSearchResult(String query, String type, String sortCriteria, Map<String, String> filterMap, int page, int pageSize) throws SQLException;

    int countSearchResult(String query, String type, Map<String, String> filterMap) throws SQLException;

    boolean changeProductStatus(int productID, boolean newStatus) throws SQLException;
}
