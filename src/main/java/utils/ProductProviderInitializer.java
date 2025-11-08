/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import org.reflections.Reflections;
import java.util.Set;
import dao.interfaces.IProductProvider;

/**
 *
 * @author anhkc
 */

public final class ProductProviderInitializer {

    public static void init() throws ClassNotFoundException {
        Reflections reflections = new Reflections("dao.factory_product");
        Set<Class<? extends IProductProvider>> productDetailsProviders = reflections.getSubTypesOf(IProductProvider.class);

        for (Class<?> cls : productDetailsProviders) {
            Class.forName(cls.getName());
            System.out.println("Loaded provider of product sub type: " + cls.getSimpleName());
        }
    }
}
