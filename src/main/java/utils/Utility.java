/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author anhkc
 */
public class Utility {
    public LocalDate getLocalDate(Date date, int duration) {
        return date != null ? date.toLocalDate().plusDays(duration) : null;
    }
    
    public LocalDateTime getLocalDateTime(Timestamp dateTime) {
        return dateTime != null ? dateTime.toLocalDateTime() : null;
    }

    // Convert LocalDate to String
    public String formatLocalDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    // Convert LocalDateTime to String
    public String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    
    //Format String for Display
    public String toTitleCase(String input) {
    if (input == null || input.isEmpty()) return input;

    return Arrays.stream(input.toLowerCase().split("\\s+"))
        .filter(word -> !word.isEmpty())
        .map(word -> word.substring(0, 1).toUpperCase() + 
                     (word.length() > 1 ? word.substring(1).toLowerCase() : ""))
        .collect(Collectors.joining(" "));
}


    public static void main(String[] args) {
//        Utility main = new Utility();
        
    }
}
