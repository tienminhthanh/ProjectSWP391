/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.registration;

import java.util.HashMap;
import java.util.Map;
import service.interfaces.IProductDetailsService;

/**
 *
 * @author anhkc
 */
public final class ProductDetailsServiceRegistration {

    private static final Map<String, IProductDetailsService> registry = new HashMap<>();

    public static void register(String type, IProductDetailsService service) {
        registry.put(type.toLowerCase(), service);
    }
    
    public static IProductDetailsService getProductDetailsService(String type){return registry.get(type);}
}
