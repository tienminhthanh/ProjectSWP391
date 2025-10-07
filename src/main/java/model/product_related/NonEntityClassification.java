/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.product_related;

import java.util.Map;
import model.interfaces.ProductClassification;
/**
 *
 * @author anhkc
 */
public class NonEntityClassification implements ProductClassification{
    private final int id;
    private final String name;
    private final String type;

    public NonEntityClassification(String name, String type) {
        this.id = 0;
        this.name = name;
        this.type = type;
    }
    

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Map<String, Object> getExtraAttributes() {
        return Map.of("isNonEntity",true);
    }
    
    
    
    
}
