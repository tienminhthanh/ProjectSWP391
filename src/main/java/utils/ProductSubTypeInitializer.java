/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import org.reflections.Reflections;
import java.util.Set;
import service.interfaces.IProductDetailsService;

/**
 *
 * @author anhkc
 */

public final class ProductSubTypeInitializer {

    public static void init() throws ClassNotFoundException {
        Reflections reflections = new Reflections("service");
        Set<Class<? extends IProductDetailsService>> productSubTypeServices = reflections.getSubTypesOf(IProductDetailsService.class);

        for (Class<?> cls : productSubTypeServices) {
            Class.forName(cls.getName());
            System.out.println("Loaded service of product sub type: " + cls.getSimpleName());
        }
    }
}
