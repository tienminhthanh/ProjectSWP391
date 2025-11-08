/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.Map;
import model.Brand;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public interface IClassificationEntityDAO {

    Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException;
    IProductClassification getById(int id) throws SQLException;

    
}
