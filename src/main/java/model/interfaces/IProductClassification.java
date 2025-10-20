/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.interfaces;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author anhkc
 */
public interface ProductClassification {
    int getId();
    String getName();
    String getType();
    String getCode();
    default Map<String,Object> getExtraAttributes(){
        return Collections.emptyMap();
    }
    
}
