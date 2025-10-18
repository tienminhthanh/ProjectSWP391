/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.Map;
import model.interfaces.ProductClassification;
import model.product_related.Brand;

/**
 *
 * @author anhkc
 */
public interface IClassificationEntityDAO {

    Map<ProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException;
    ProductClassification getById(int id) throws SQLException;

    
}
