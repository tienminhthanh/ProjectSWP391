/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.factory_product;

import java.util.HashMap;
import java.util.Map;
import dao.interfaces.IProductProvider;

/**
 *
 * @author anhkc
 */
public final class ProductProviderRegistration {

    private static final Map<String, IProductProvider> registry = new HashMap<>();


    public static void register(String type, IProductProvider service) {
        registry.put(type.toLowerCase(), service);
    }

    public static IProductProvider getProductProvider(String type) {
        return registry.get(type);
    }
    
    public static void main(String[] args) {
    }

}
