/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author anhkc
 */

/**
 * A generic interface for DAOs that retrieve a list of related entities
 * based on a common key (like a product ID).
 * * @param <T> The type of the entity/attribute being returned (e.g., Genre, Creator).
 * @param <K> The type of the lookup key (e.g., Integer for productID).
 */
public interface IProductExtraAttributesDAO<T,K> {

    /**
     * Retrieves a list of entities of type T associated with a key K.
     * @param key The key used for lookup (e.g., productID).
     * @return A list of the associated entities.
     * @throws SQLException If a database error occurs.
     */
    List<T> getExtraAttributesByProductID(K key) throws SQLException;
}