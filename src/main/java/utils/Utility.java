/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author anhkc
 */
public final class Utility {

    private Utility() {
    }
    ;

    private static final Map<String, Set<String>> APPLICABLE_FILTERS = Map.of(
            "merch", Set.of("ftSrs", "ftBrn", "ftChr","ftCrt","ftCtg","ftPrc"),
            "book", Set.of("ftGnr", "ftPbl","ftCrt","ftCtg","ftPrc")
    );
    private static final Set<String> SINGLE_FILTERS = Set.of("ftCtg", "ftPbl", "ftSrs", "ftBrn", "ftChr");

    public static LocalDate getLocalDate(Date date, int duration) {
        return date != null ? date.toLocalDate().plusDays(duration) : null;
    }

    public static LocalDateTime getLocalDateTime(Timestamp dateTime) {
        return dateTime != null ? dateTime.toLocalDateTime() : null;
    }

    // Convert LocalDate to String
    public static String formatLocalDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    // Convert LocalDateTime to String
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    //Format String for Display
    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Arrays.stream(input.toLowerCase().split("\\s+"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase()
                + (word.length() > 1 ? word.substring(1).toLowerCase() : ""))
                .collect(Collectors.joining(" "));
    }

    public static SimpleEntry<StringBuilder, Map<String, String>> getFilterMapAndMessage(Map<String, String[]> paramMap, String clsfType) {
        Map<String, String> filterMap = new HashMap<>();
        StringBuilder message = new StringBuilder();
        if (paramMap != null) {
            Set<String> applicableFilterSet = APPLICABLE_FILTERS.get(clsfType);
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String name = entry.getKey();
                String[] values = entry.getValue();

                //Skip non-filter params
                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                if (!applicableFilterSet.contains(name)) {
                    message.append("Cannot apply this filter to ")
                            .append(getDisplayTextBasedOnType(clsfType))
                            .append("!");
                    continue;
                }

                //Prevent SINGLE_FILTERS from being selected multiple times
                if (SINGLE_FILTERS.contains(name) && values[0].split(",").length > 1) {
                    message.append("Only genres and creators can be selected multiple times!\n");
                    continue;
                }

                //Special case for price range filter
                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    //Normal case
                    filterMap.put(name, values[0]);
                }
            }
        }
        return new SimpleEntry<>(message, filterMap);
    }

    public static String getDisplayTextBasedOnType(String type) {
        switch (type) {
            case "book":
                return "Books";
            case "merch":
                return "Merchandise";
            default:
                return "";
        }
    }

    public static String getDefaultSortCriteria(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "releaseDate";
        } else {
            return "relevance";
        }

    }

    public static String getNoResultMessage(String type) {
        String message = "";
        if (type.equals("book")) {
            message = "No result found! Try entering series title, name of author/artist, categories or genre.\n";
        } else if (type.equals("merch")) {
            message = "No result found! Try entering series title, name of sculptor/artist/character/brand or category.\n";
        }
        return message;
    }

    public static String formatURL(String encodedURL) throws UnsupportedEncodingException, MalformedURLException {

        Map<String, String> decodedURLParts = new HashMap<>();

        // Decode the URL
        String decodedUrl = URLDecoder.decode(encodedURL, "UTF-8");

        // Parse the URL
        URL url = new URL(decodedUrl);

        // Extract components
        String protocol = url.getProtocol();    //http
        String host = url.getHost();        //locohost
        int port = url.getPort();           //8080
        String path = url.getPath();      //Servlet path  
        String query = url.getQuery();       //parameters

        // Extract parameters from query string
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");           //Split param name and value
            String key = keyValue[0];

            if (key.equalsIgnoreCase("page")) {
                continue;
            }

            //Ensure value never null
            String value = keyValue.length > 1 ? keyValue[1] : "";

            //Put only valid param
            decodedURLParts.put(key, value);
        }

        StringBuilder formattedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : decodedURLParts.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            formattedParams.append(key.toString()).append("=").append(val.toString());
            formattedParams.append("&");
        }
        formattedParams.deleteCharAt(formattedParams.lastIndexOf("&"));

        return protocol + "://" + host + (port != -1 ? ":" + port : "") + path + (formattedParams.length() > 0 ? "?" + formattedParams.toString() : "");

    }

    public static String resolveServletContextAtributeName(String clsfCode) {
        String attrName;
        switch (clsfCode != null ? clsfCode : "") {
            case "category":
                attrName = "categories";
                break;
            case "series":
                attrName = clsfCode;
                break;
            case "creator":
            case "brand":
            case "character":
            case "publisher":
            case "genre":
                attrName = clsfCode + "s";
                break;
            default:
                attrName = "";
        }
        return attrName;
    }

    public static String generateSHA256Hash(String input) {
        try {
            // 1. Get the MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 2. Compute the hash
            // The input string is first converted to bytes using UTF-8 encoding
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // 3. Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                // Convert byte to hex, ensuring it's two characters long
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // This should not happen in a standard Java environment
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }

    public static String maskName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }

        // 1. Split the name by one or more spaces ("\\s+")
        // This handles names with single or multiple spaces between words.
        String[] parts = fullName.trim().split("\\s+");

        // Check for valid name structure (at least two parts for masking)
        if (parts.length < 2) {
            // If it's just "Tran" or "Teo", return the name as is or just the first initial
            return parts[0];
        }

        // 2. Identify the parts:
        // The last part is conventionally the First Name (Teo)
        // The first part is the Last Name (Tran)
        // Everything in between is Middle Name(s) (Van)
        String lastName = parts[0];

        // Use a StringBuilder for efficient string concatenation
        StringBuilder maskedName = new StringBuilder();

        // 3. Append the Last Name (First part)
        // We assume the first word is the family/last name you want fully displayed
        maskedName.append(lastName);

        // 4. Iterate through the Middle and First Names (parts[1] to parts[length-1])
        for (int i = 1; i < parts.length; i++) {
            String namePart = parts[i];
            if (!namePart.isEmpty()) {
                // Append a space before the initial
                maskedName.append(" ");

                // Append the first character of the middle/first name, capitalized, and a period
                // Example: 'V' for Van, 'T' for Teo
                maskedName.append(Character.toUpperCase(namePart.charAt(0)));
                maskedName.append(".");
            }
        }

        return maskedName.toString();
    }

    public static void main(String[] args) {
        System.out.println(maskName("Tien Minh Thanh Thanh Minh Tien"));

    }
}
