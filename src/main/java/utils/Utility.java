/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
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
    private Utility(){};

    private static final Map<String, Set<String>> APPLICABLE_FILTERS = Map.of(
            "merch", Set.of("ftSrs", "ftBrn", "ftChr"),
            "book", Set.of("ftGnr", "ftPbl")
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
                    message.append("Cannot apply this filter to")
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

    public static void main(String[] args) {
//        Utility main = new Utility();

    }
}
