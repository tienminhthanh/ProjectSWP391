/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author anhkc
 */
public class Utility {
    
    // Convert LocalDate to String
    public String formatLocalDate(LocalDate date) {
        if (date == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    // Convert LocalDateTime to String
    public String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public String removeAccents(String input) {
        // Normalize and remove diacritics
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
    
    public String generateKeywordsFromProductName(String productName){
        if (productName == null) return null;
        ArrayList<String> keywords = new ArrayList<>();
        
        keywords.add(productName);
        keywords.add(productName.toLowerCase());
        keywords.add(removeAccents(productName));
        keywords.add(removeAccents(productName).toLowerCase());
        
        return String.join(",", keywords);
    }

    public void processFile(String inputFilePath, String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(generateKeywordsFromProductName(line));
                writer.newLine(); // Preserve line breaks
            }
            System.out.println("Processing complete! Output saved to: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Utility main = new Utility();
        String inputFile = "C:\\Users\\anhkc\\Desktop\\book_title.txt";  // Change this to your file path
        String outputFile = "C:\\Users\\anhkc\\Desktop\\keyword_title.txt"; // Processed output file
        
        main.processFile(inputFile, outputFile);
    }
}
