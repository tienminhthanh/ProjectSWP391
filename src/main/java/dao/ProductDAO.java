/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import model.*;
import utils.*;

/**
 *
 * @author anhkc
 */
public class ProductDAO {

    private final DBContext context;
    private final Utility tool;
    
    //Normal run
    public ProductDAO() {
        context = new DBContext();
        tool = new Utility();
    }
    
    //Test run
    public ProductDAO(DBContext context, Utility tool) {
        this.context = context;
        this.tool = tool;
    }
   
    
    /**
     * For add, update cart
     *
     * @param productID
     * @return
     * @throws SQLException
     */
    public Product getProductById(int productID) throws SQLException {
        StringBuilder sql = getCTETables(null).append("SELECT P.*, \n"
                + "       C.categoryName, \n"
                + "       PD.discountPercentage, \n"
                + "       PD.eventDateStarted,\n"
                + "	   PD.eventDuration\n"
                + "FROM Product AS P\n"
                + "LEFT JOIN ProductDiscount PD \n"
                + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C \n"
                + "    ON C.categoryID = P.categoryID\n"
                + "WHERE P.productIsActive = 1 AND P.productID = ?\n");

        Object[] params = {productID};
        ResultSet rs = context.exeQuery(sql.toString(), params);

        if (rs.next()) {
            return mapResultSetToProduct(rs, null);

        }

        return null;
    }

    /**
     * Caller method for getBookById and getMerchById
     *
     * @param type
     * @param productID
     * @param isManagement
     * @return
     * @throws SQLException
     */
    public Product callGetProductByTypeAndId(String type, int productID, boolean isManagement) throws SQLException {
        switch (type) {
            case "merch":
                return getMerchById(productID, isManagement);
            case "book":
                return getBookById(productID, isManagement);
            default:
                return null;
        }
    }

    /**
     * For view merch details
     *
     * @param productID
     * @param isManagement
     * @return
     * @throws SQLException
     */
    public Product getMerchById(int productID, boolean isManagement) throws SQLException {
        StringBuilder sql = getCTETables("rank").append("SELECT\n"
                + "P.*,\n"
                + "C.categoryName,\n"
                + "M.seriesID,\n"
                + "M.characterID,\n"
                + "M.brandID,\n"
                + "M.size,\n"
                + "M.scaleLevel,\n"
                + "M.material,\n"
                + "S.seriesName,\n"
                + "Ch.characterName,\n"
                + "B.brandName,\n"
                + "PD.discountPercentage,\n"
                + "PD.eventDateStarted,\n"
                + "PD.eventDuration,\n"
                + "TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN Merchandise AS M ON P.productID = M.merchandiseID\n"
                + "LEFT JOIN TopSale TS ON TS.productID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Brand AS B ON M.brandID = B.brandID\n"
                + "LEFT JOIN Character AS Ch ON M.characterID = Ch.characterID\n"
                + "LEFT JOIN Series AS S ON M.seriesID = S.seriesID\n"
                + "WHERE P.productID = ?\n");

        if (!isManagement) {
            sql.append(" AND P.productIsActive = 1\n");
        }

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        Object[] params = {productID};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            if (rs.next()) {
                return mapResultSetToProduct(rs, rs.getString("generalCategory"));
            }
        }
        return null;
    }

    /**
     * For view book details
     *
     * @param productID
     * @return
     * @throws SQLException
     */
    public Product getBookById(int productID, boolean isManagement) throws SQLException {

        StringBuilder sql = getCTETables("rank").append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.bookDuration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.eventDateStarted,PD.eventDuration,TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN TopSale TS ON TS.productID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "WHERE P.productID = ?\n");

        if (!isManagement) {
            sql.append(" AND P.productIsActive = 1\n");
        }

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        Object[] params = {productID};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            if (rs.next()) {
                return mapResultSetToProduct(rs, rs.getString("generalCategory"));
            }
        }
        return null;
    }

    public List<Creator> getCreatorsOfThisProduct(int productID) throws SQLException {
        String sql = "SELECT PC.creatorID, C.creatorName, C.creatorRole\n"
                + "FROM Creator AS C\n"
                + "JOIN Product_Creator AS PC ON C.creatorID = PC.creatorID\n"
                + "WHERE PC.productID = ?";
        Object[] params = {productID};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            List<Creator> creatorList = new ArrayList<>();
            while (rs.next()) {
                creatorList.add(new Creator(rs.getInt("creatorID"), rs.getString("creatorName"), rs.getString("creatorRole")));
            }
            return creatorList;
        }
    }

    public List<Genre> getGenresOfThisBook(int productID) throws SQLException {
        String sql = "SELECT BG.genreID, G.genreName\n"
                + "FROM Book_Genre AS BG\n"
                + "JOIN Genre AS G ON BG.genreID = G.genreID\n"
                + "WHERE BG.bookID = ?";
        Object[] params = {productID};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            List<Genre> genreList = new ArrayList<>();
            while (rs.next()) {
                genreList.add(new Genre(rs.getInt(1), rs.getString(2)));
            }
            return genreList;
        }
    }

    /**
     * Count search result
     *
     * @param query
     * @param type
     * @param filterMap
     * @return
     * @throws SQLException
     */
    public int getProductsCount(String query, String type, Map<String, String> filterMap) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product P\n");

        //If there is search query
        sql.append(query != null && !query.trim().isEmpty() ? "\nJOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL ON P.productID = KEY_TBL.[KEY]" : "");

        // Type-specific JOIN if needed
        type = type != null ? type : "";    //Handling null
        if (type.equals("book")) {
            sql.append("\nJOIN Book B");
            sql.append("\n    ON B.bookID = P.productID");
        } else if (type.equals("merch")) {
            sql.append("\nJOIN Merchandise M");
            sql.append("\n    ON M.merchandiseID = P.productID");
        }

        //WHERE Clause
        sql.append("\nWHERE P.productIsActive = 1");

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();
        //Add search term if any
        if (query != null && !query.trim().isEmpty()) {
            paramList.add(formatQueryBroad(query));
        }

        //Append filter
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(processFilter(filterOption, selectedFilters.length));

                //Then add filter param to the param list to match with the appended clause
                if (filterOption.equals("ftPrc")) {
                    //Handle special case with price range
                    String[] valParts = filterParam.split("-");
                    paramList.add(valParts[0]);
                    paramList.add(valParts[1]);
                } else {
                    //Normal case
                    Collections.addAll(paramList, selectedFilters);
                }
            }
        }

        Object[] params = paramList.toArray();

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            return rs.next() ? rs.getInt(1) : 0;
        }

    }

    /**
     * When user does not enter anything in the search bar
     *
     * @param type
     * @param sortCriteria
     * @param filterMap
     * @param page
     * @param pageSize
     * @return
     * @throws SQLException
     */
    public List<Product> getActiveProducts(String type, String sortCriteria, Map<String, String> filterMap, int page, int pageSize) throws SQLException {
//        Prepare the query with CTE first
        StringBuilder sql = getCTETables(null);

        //Append base query
        sql.append("SELECT P.*, \n"
                + "       C.categoryName, \n"
                + "       PD.discountPercentage, \n"
                + "       PD.eventDateStarted,\n"
                + "	   PD.eventDuration\n"
                + "FROM Product AS P\n");

        //Type specific join
        if (type.equals("book")) {
            sql.append("JOIN Book B \n"
                    + "    ON B.bookID = P.productID\n"
            );
        } else if (type.equals("merch")) {
            sql.append("JOIN Merchandise M \n"
                    + "    ON M.merchandiseID = P.productID\n"
            );
        }

        //Common left join and where clause
        sql.append("LEFT JOIN ProductDiscount PD \n"
                + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C \n"
                + "    ON C.categoryID = P.categoryID\n"
                + "WHERE P.productIsActive = 1\n");

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();

        //Append filter
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(processFilter(filterOption, selectedFilters.length));

                //Then add filter param to the param list to match with the appended clause
                if (filterOption.equals("ftPrc")) {
                    //Handle special case with price range
                    String[] valParts = filterParam.split("-");
                    paramList.add(valParts[0]);
                    paramList.add(valParts[1]);
                } else {
                    //Normal case
                    Collections.addAll(paramList, selectedFilters);
                }
            }
        }

        //Append order
        sql.append("ORDER BY ");
        sql.append(processSort(sortCriteria));

        //Append paginated
        sql.append("\nOFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        int offset = (page - 1) * pageSize;
        paramList.add(offset);
        paramList.add(pageSize);

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, null));
            }
            return productList;
        }

    }

    public List<Product> getSearchResult(String query, String type, String sortCriteria, Map<String, String> filterMap, int page, int pageSize) throws SQLException {

        //Prepare the query with CTE first
        StringBuilder sql = getCTETables(null);

        // Base SELECT clause
        sql.append("SELECT P.*,");
        sql.append("\n       C.categoryName,");
        sql.append("\n       PD.discountPercentage,");
        sql.append("\n       PD.eventDateStarted,");
        sql.append("\n       PD.eventDuration,");
        sql.append("\n       KEY_TBL.RANK AS relevance_score");
        sql.append("\nFROM Product AS P");
        sql.append("\nJOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL");
        sql.append("\n    ON P.productID = KEY_TBL.[KEY]");

        // Type-specific JOIN
        if (type.equals("book")) {
            sql.append("\nJOIN Book B");
            sql.append("\n    ON B.bookID = P.productID");
        } else if (type.equals("merch")) {
            sql.append("\nJOIN Merchandise M");
            sql.append("\n    ON M.merchandiseID = P.productID");
        }

        // Common LEFT JOINs and WHERE clause
        sql.append("\nLEFT JOIN ProductDiscount PD");
        sql.append("\n    ON P.productID = PD.productID AND PD.rn = 1");
        sql.append("\nLEFT JOIN Category AS C");
        sql.append("\n    ON C.categoryID = P.categoryID");
        sql.append("\nWHERE P.productIsActive = 1");

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();
        paramList.add(formatQueryBroad(query));

        //Append filter
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(processFilter(filterOption, selectedFilters.length));

                //Then add filter param to the param list to match with the appended clause
                if (filterOption.equals("ftPrc")) {
                    //Handle special case with price range
                    String[] valParts = filterParam.split("-");
                    paramList.add(valParts[0]);
                    paramList.add(valParts[1]);
                } else {
                    //Normal case
                    Collections.addAll(paramList, selectedFilters);
                }
            }
        }

        // Append sorting
        sql.append("\nORDER BY ");
        sql.append(processSort(sortCriteria));

        //Append paginated
        sql.append("\nOFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        int offset = (page - 1) * pageSize;
        paramList.add(offset);
        paramList.add(pageSize);

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, null));
            }
            return productList;
        }
    }

    private String formatQueryBroad(String query) {
        String normalizedQuery = query.replaceAll("[^A-Za-z0-9_']", " ").trim();
        String[] queryParts = normalizedQuery.split("\\s+");
        String[] formattedParts = new String[queryParts.length * 2]; // Double the size for FORMSOF and prefix

        for (int i = 0; i < queryParts.length; i++) {
            // Add FORMSOF(INFLECTIONAL, word)
            formattedParts[i * 2] = "FORMSOF(INFLECTIONAL, " + queryParts[i] + ")";
            // Add "word*" for prefix wildcard
            formattedParts[i * 2 + 1] = "\"" + queryParts[i] + "*\"";
        }

        return String.join(" OR ", formattedParts);
    }

    private String formatQueryTight(String query, String logic) {
        String normalizedQuery = query.replaceAll("[^A-Za-z0-9_']", " ").trim();
        String[] queryParts = normalizedQuery.split("\\s+");
        for (int i = 0; i < queryParts.length; i++) {
            queryParts[i] = "FORMSOF(INFLECTIONAL, " + queryParts[i] + ")";
        }

        return String.join(" " + logic + " ", queryParts);
    }

    private String processSort(String sortCriteria) {
        switch (sortCriteria) {
            case "relevance":
                return "KEY_TBL.RANK DESC, P.productName ASC";
            case "name":
                return "P.productName ASC";
            case "hotDeal":
                return "PD.discountPercentage DESC";
            case "priceLowToHigh":
                return "P.price ASC";
            case "priceHighToLow":
                return "P.price DESC";
            case "rating":
                return "P.averageRating DESC, P.numberOfRating DESC";
            case "releaseDate":
                return "P.releaseDate DESC";
            case "":
            default:
                return "P.lastModifiedTime DESC, P.productID DESC";

        }
    }

    /**
     * Overload for filtered list
     *
     * @param conditionID
     * @param condition
     * @param type
     * @param filterMap
     * @return
     * @throws SQLException
     */
    public int getProductsCount(int conditionID, Map<String, String> filterMap, String condition, String type) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product P\n");

        //Conditional joins
        sql.append(getSpecificJoin(condition, type));

        //Initialize where clause
        sql.append("WHERE P.productIsActive = 1\n").append(getInitialWhereClause(condition, conditionID, null));

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();
        if (conditionID > 0) {
            paramList.add(conditionID);
        }

        //Append filter
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(processFilter(filterOption, selectedFilters.length));

                //Then add filter param to the param list to match with the appended clause
                if (filterOption.equals("ftPrc")) {
                    //Handle special case with price range
                    String[] valParts = filterParam.split("-");
                    paramList.add(valParts[0]);
                    paramList.add(valParts[1]);
                } else {
                    //Normal case
                    Collections.addAll(paramList, selectedFilters);
                }
            }
        }

        Object[] params = paramList.toArray();

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            return rs.next() ? rs.getInt(1) : 0;
        }

    }

    public List<Product> getProductsByCondition(int conditionID, String sortCriteria, Map<String, String> filterMap, String condition, String generalCategory, String location, int page, int pageSize) throws SQLException {
        StringBuilder sql = getCTETables(null);
        sql.append(location.equals("home") ? "SELECT TOP 7\n" : "SELECT\n");
        sql.append("P.*, C.categoryName, PD.discountPercentage, PD.eventDateStarted, PD.eventDuration\n"
                + "FROM Product AS P\n");

        //Conditional joins
        sql.append(getSpecificJoin(condition, generalCategory));

        //Discount join
        sql.append("LEFT JOIN ProductDiscount PD \n"
                + "    ON P.productID = PD.productID AND PD.rn = 1\n"
        );

        //Initialize where clause
        sql.append("WHERE P.productIsActive = 1\n").append(getInitialWhereClause(condition, conditionID, location));

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();
        if (conditionID > 0) {
            paramList.add(conditionID);
        }

        //Append filter
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(processFilter(filterOption, selectedFilters.length));

                //Then add filter param to the param list to match with the appended clause
                if (filterOption.equals("ftPrc")) {
                    //Handle special case with price range
                    String[] valParts = filterParam.split("-");
                    paramList.add(valParts[0]);
                    paramList.add(valParts[1]);
                } else {
                    //Normal case
                    Collections.addAll(paramList, selectedFilters);
                }
            }
        }

        //Append order
        sql.append("ORDER BY ");
        sql.append(processSort(sortCriteria));
        
        //Append paginated (these values are ZERO for home lists)
        if (page > 0 && pageSize > 0) {
            sql.append("\nOFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            int offset = (page - 1) * pageSize;
            paramList.add(offset);
            paramList.add(pageSize);
        }

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, null));
            }
            return productList;
        }

    }

    private StringBuilder getSpecificJoin(String condition, String generalCategory) {
        StringBuilder joinClause = new StringBuilder();
        generalCategory = generalCategory != null ? generalCategory.toLowerCase() : "";
        switch (condition != null ? condition.toLowerCase() : "") {
            case "ctg":
                joinClause.append(generalCategory.equals("book")
                        ? "JOIN Book B on B.bookID = P.productID\n" : "JOIN Merchandise M on M.merchandiseID = P.productID\n");
                joinClause.append("JOIN Category AS C \n"
                        + "    ON C.categoryID = P.categoryID\n");
                break;
            case "crt":
                joinClause.append(generalCategory.equals("book")
                        ? "JOIN Book B on B.bookID = P.productID\n" : "JOIN Merchandise M on M.merchandiseID = P.productID\n");
                joinClause.append("JOIN Product_Creator PC ON PC.productID = P.productID\n"
                        + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n");
                break;
            case "gnr":
                joinClause.append("JOIN Book AS B ON P.productID = B.bookID\n"
                        + "JOIN Book_Genre as BG ON BG.bookID = B.bookID\n"
                        + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n");
                break;
            case "pbl":
                joinClause.append("JOIN Book AS B ON P.productID = B.bookID\n"
                        + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n");
                break;
            case "srs":
            case "chr":
            case "brn":
                joinClause.append("JOIN Merchandise AS M ON P.productID = M.merchandiseID\n"
                        + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n");
                break;
            case "sale":
            case "new":
                joinClause.append(generalCategory.equals("book")
                        ? "JOIN Book B on B.bookID = P.productID\n" : "JOIN Merchandise M on M.merchandiseID = P.productID\n");
                joinClause.append("LEFT JOIN Category AS C \n"
                        + "    ON C.categoryID = P.categoryID\n");
                break;
        }
        return joinClause;
    }

    private String getInitialWhereClause(String condition, int conditionID, String location) {
        switch (condition) {
            case "ctg":
                return "AND P.categoryID = ?\n";
            case "crt":
                return "AND PC.creatorID = ?\n";
            case "gnr":
                return conditionID == 18 && location != null && location.equals("home") ? "AND BG.genreID = ?\n AND P.specialFilter not in ('pre-order','new')\n" : "AND BG.genreID = ?\n";
            case "pbl":
                return "AND B.publisherID = ?\n";
            case "srs":
                return conditionID == 1 && location != null && location.equals("home") ? "AND M.seriesID = ?\n AND P.specialFilter not in ('pre-order')\n" : "AND M.seriesID = ?\n";
            case "chr":
                return "AND M.characterID = ?\n";
            case "brn":
                return "AND M.brandID = ?\n";
            case "new":
                return "AND P.specialFilter = 'new'\n";
            case "sale":
                return "AND PD.discountPercentage is not null\n";
            default:
                return "";
        }

    }

    private String processFilter(String filterOption, int filterCount) {
        String placeHolder = String.join(",", Collections.nCopies(filterCount, "?"));
        switch (filterOption) {
            case "ftGnr":
                // Count matching genres and ensure it equals the number of selected filters
                return "AND (SELECT COUNT(*) FROM Book_Genre BG WHERE BG.bookID = B.bookID "
                        + "AND BG.genreID IN (" + placeHolder + ")) = " + filterCount + " \n";
            case "ftCrt":
                // Count matching creators and ensure it equals the number of selected filters
                return "AND (SELECT COUNT(*) FROM Product_Creator PC WHERE PC.productID = P.productID "
                        + "AND PC.creatorID IN (" + placeHolder + ")) = " + filterCount + " \n";
            case "ftPbl":
                return "AND B.publisherID = ? \n";
            case "ftPrc":
                return "AND P.price BETWEEN ? AND ? \n";
            case "ftCtg":
                return "AND P.categoryID = ? \n";
            case "ftBrn":
                return "AND M.brandID = ? \n";
            case "ftChr":
                return "AND M.characterID = ? \n";
            case "ftSrs":
                return "AND M.seriesID = ?  \n";
            default:
                return "";
        }
    }

    public List<Product> getRankedProducts(String type) throws SQLException {
        //Base query
        StringBuilder sql = getCTETables("rank").append("SELECT P.*,\n"
                + "C.categoryName,\n"
                + "PD.discountPercentage,\n"
                + "PD.eventDateStarted,\n"
                + "PD.eventDuration,\n"
                + "TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN TopSale TS ON TS.productID = P.productID\n");

        //Type specific join
        sql.append(type.equals("book") ? "JOIN Book B ON B.bookID = P.productID\n" : "JOIN Merchandise M ON M.merchandiseID = P.productID\n");

        //Common part
        sql.append("LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "WHERE P.productIsActive = 1 \n"
                + "ORDER BY TS.salesRank ;");

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        //Execute query
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), null)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, null).setSalesRank(rs.getInt("salesRank")));
            }
            return productList;

        }
    }

    private StringBuilder getCTETables(String condition) {
        StringBuilder cte = new StringBuilder("WITH ");
        if (condition != null && condition.equals("rank")) {
            cte.append("TopSale AS (\n"
                    + "    SELECT SH.productID, SUM(soldQuantity) AS totalSoldQuantity,\n"
                    + "	ROW_NUMBER() OVER (ORDER BY SUM(SH.soldQuantity) DESC,Pr.averageRating DESC, Pr.numberOfRating DESC) AS salesRank\n"
                    + "    FROM SaleHistory SH\n"
                    + "	JOIN Product Pr ON Pr.productID = SH.productID\n"
                    + "    WHERE saleDate >= DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 1, 0)\n"
                    + "    AND saleDate < DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0)\n"
                    + "    GROUP BY SH.productID,Pr.averageRating,Pr.numberOfRating \n"
                    + "),\n");
        }
        return cte.append("ProductDiscount AS (\n"
                + "SELECT ep.productID,\n"
                + "e.eventDateStarted,\n"
                + "e.eventDuration,\n"
                + "ep.discountPercentage,\n"
                + "ROW_NUMBER() OVER (PARTITION BY ep.productID ORDER BY e.eventDateStarted DESC, ep.eventID DESC) AS rn\n"
                + "FROM Event e\n"
                + "JOIN Event_Product ep ON e.eventID = ep.eventID\n"
                + "WHERE e.eventIsActive = 1\n"
                + "AND GETDATE() <= DATEADD(day, e.eventDuration, e.eventDateStarted)\n"
                + "AND GETDATE() >= e.eventDateStarted\n"
                + ")\n");
    }

    private Product mapResultSetToProduct(ResultSet rs, String type) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

        LocalDate eventEndDate = tool.getLocalDate(rs.getDate("eventDateStarted"), rs.getInt("eventDuration"));
        int discountPercentage = 0;
        if(eventEndDate !=null){
            discountPercentage = LocalDate.now().isAfter(eventEndDate) ? 0 : rs.getInt("discountPercentage");
        }
        
        LocalDate rlsDate = tool.getLocalDate(rs.getDate("releaseDate"),0);
        
        LocalDateTime lastMdfTime = tool.getLocalDateTime(rs.getTimestamp("lastModifiedTime"));

        switch (type != null ? type : "") {
            case "book":
                // For book details
                Publisher publisher = new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));
                return new Book(publisher, rs.getString("bookDuration"),
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("stockCount"),
                        category,
                        rs.getString("description"),
                        rlsDate,
                        lastMdfTime,
                        rs.getDouble("averageRating"),
                        rs.getInt("numberOfRating"),
                        rs.getString("specialFilter"),
                        rs.getInt("adminID"),
                        rs.getString("keywords"),
                        rs.getString("generalCategory"),
                        rs.getBoolean("productIsActive"),
                        rs.getString("imageURL"),
                        discountPercentage,
                        eventEndDate).setSalesRank(rs.getInt("salesRank"));
            case "merch":
                // For merch details
                Brand brand = new Brand(rs.getInt("brandID"), rs.getString("brandName"));
                Series series = new Series(rs.getInt("seriesID"), rs.getString("seriesName"));
                OGCharacter character = new OGCharacter(rs.getInt("characterID"), rs.getString("characterName"));
                return new Merchandise(series, character, brand, rs.getString("size"), rs.getString("scaleLevel"), rs.getString("material"),
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("stockCount"),
                        category,
                        rs.getString("description"),
                        rlsDate,
                        lastMdfTime,
                        rs.getDouble("averageRating"),
                        rs.getInt("numberOfRating"),
                        rs.getString("specialFilter"),
                        rs.getInt("adminID"),
                        rs.getString("keywords"),
                        rs.getString("generalCategory"),
                        rs.getBoolean("productIsActive"),
                        rs.getString("imageURL"),
                        discountPercentage,
                        eventEndDate).setSalesRank(rs.getInt("salesRank"));

            default:
                //For listing, sort, search, filter
                return new Product(rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("stockCount"),
                        category,
                        rs.getString("description"),
                        rlsDate,
                        lastMdfTime,
                        rs.getDouble("averageRating"),
                        rs.getInt("numberOfRating"),
                        rs.getString("specialFilter"),
                        rs.getInt("adminID"),
                        rs.getString("keywords"),
                        rs.getString("generalCategory"),
                        rs.getBoolean("productIsActive"),
                        rs.getString("imageURL"),
                        discountPercentage,
                        eventEndDate);
        }

    }

    public Category getCategoryById(int id) throws SQLException {
        String sql = "SELECT *\n"
                + "FROM Category\n"
                + "WHERE categoryID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Category(rs.getInt("categoryID"), rs.getString("categoryName"), rs.getString("generalCategory"));
            }
        }
        return null;

    }

    public Map<Category, Integer> getAllCategories() throws SQLException {
        String sql = "SELECT c.*, COUNT(p.productID) AS productCount  \n"
                + "FROM Category AS c  \n"
                + "LEFT JOIN Product AS p  \n"
                + "    ON p.categoryID = c.categoryID AND p.productIsActive = 1  \n"
                + "GROUP BY c.categoryID, c.categoryName, c.generalCategory;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Category, Integer> categoryMap = new HashMap<>();
            while (rs.next()) {
                categoryMap.put(new Category(rs.getInt("categoryID"), rs.getString("categoryName"), rs.getString("generalCategory")), rs.getInt("productCount"));
            }
            return categoryMap;
        }
    }

    public Creator getCreatorById(int id) throws SQLException {
        String sql = "SELECT top 1 Creator.creatorID, Creator.creatorName, Creator.creatorRole, Product.generalCategory\n"
                + "FROM     Creator INNER JOIN\n"
                + "                  Product_Creator ON Creator.creatorID = Product_Creator.creatorID INNER JOIN\n"
                + "                  Product ON Product_Creator.productID = Product.productID\n"
                + "WHERE  Product_Creator.creatorID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Creator(rs.getInt("creatorID"), rs.getString("creatorName"),
                        tool.toTitleCase(rs.getString("creatorRole")), rs.getString("generalCategory"));
            }
        }
        return null;
    }

    public Map<Creator, Integer> getAllCreators() throws SQLException {

        String sql = "SELECT \n"
                + "    c.creatorID, \n"
                + "    c.creatorName, \n"
                + "    c.creatorRole, \n"
                + "    COUNT(p.productID) AS productCount\n"
                + "FROM Creator c\n"
                + " JOIN Product_Creator pc \n"
                + "    ON c.creatorID = pc.creatorID\n"
                + "LEFT JOIN Product p\n"
                + "    ON pc.productID = p.productID \n"
                + "    AND p.productIsActive = 1  \n"
                + "GROUP BY c.creatorID, c.creatorName, c.creatorRole;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Creator, Integer> creatorMap = new HashMap<>();
            while (rs.next()) {

                creatorMap.put(new Creator(rs.getInt("creatorID"), rs.getString("creatorName"),
                        tool.toTitleCase(rs.getString("creatorRole"))), rs.getInt("productCount"));
            }
            return creatorMap;
        }
    }

    public Genre getGenreById(int id) throws SQLException {
        String sql = "select * from Genre where genreID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Genre(rs.getInt("genreID"), rs.getString("genreName"));
            }
        }
        return null;

    }

    public Map<Genre, Integer> getAllGenres() throws SQLException {
        String sql = "SELECT \n"
                + "    g.genreID, \n"
                + "    g.genreName, \n"
                + "    COUNT(P.productID) AS productCount\n"
                + "FROM Genre g  \n"
                + "JOIN Book_Genre bg ON g.genreID = bg.genreID  \n"
                + "JOIN Book b ON bg.bookID = b.bookID  \n"
                + "LEFT JOIN Product p ON b.bookID = p.productID AND p.productIsActive = 1\n"
                + "GROUP BY g.genreID, g.genreName;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Genre, Integer> genreMap = new HashMap<>();
            while (rs.next()) {
                genreMap.put(new Genre(rs.getInt("genreID"), rs.getString("genreName")), rs.getInt("productCount"));
            }
            return genreMap;
        }
    }

    public Publisher getPublisherById(int id) throws SQLException {
        String sql = "select * from Publisher where publisherID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));
            }
        }
        return null;
    }

    public Map<Publisher, Integer> getAllPublishers() throws SQLException {
        String sql = "SELECT \n"
                + "    p.publisherID, \n"
                + "    p.publisherName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Publisher p\n"
                + "JOIN Book b ON p.publisherID = b.publisherID\n"
                + "LEFT JOIN Product pr ON b.bookID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY p.publisherID, p.publisherName;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Publisher, Integer> publisherMap = new HashMap<>();
            while (rs.next()) {
                publisherMap.put(new Publisher(rs.getInt("publisherID"), rs.getString("publisherName")), rs.getInt("productCount"));
            }
            return publisherMap;
        }
    }

    public Series getSeriesById(int id) throws SQLException {
        String sql = "select * from Series where seriesID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Series(rs.getInt("seriesID"), rs.getString("seriesName"));
            }
        }
        return null;
    }

    public Map<Series, Integer> getAllSeries() throws SQLException {
        String sql = "SELECT \n"
                + "    s.seriesID, \n"
                + "    s.seriesName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Series s\n"
                + "JOIN Merchandise m ON m.seriesID = s.seriesID\n"
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY s.seriesID, s.seriesName";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Series, Integer> seriesMap = new HashMap<>();
            while (rs.next()) {
                seriesMap.put(new Series(rs.getInt("seriesID"), rs.getString("seriesName")), rs.getInt("productCount"));
            }
            return seriesMap;
        }
    }

    public OGCharacter getCharacterById(int id) throws SQLException {
        String sql = "select * from Character where characterID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new OGCharacter(rs.getInt("characterID"), rs.getString("characterName"));
            }
        }
        return null;
    }

    public Map<OGCharacter, Integer> getAllCharacters() throws SQLException {
        String sql = "SELECT \n"
                + "    ch.characterID, \n"
                + "    ch.characterName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Character ch\n"
                + "JOIN Merchandise m ON m.characterID = ch.characterID\n"
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY ch.characterID, ch.characterName";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<OGCharacter, Integer> characterMap = new HashMap<>();
            while (rs.next()) {
                characterMap.put(new OGCharacter(rs.getInt("characterID"), rs.getString("characterName")), rs.getInt("productCount"));
            }
            return characterMap;
        }
    }

    public Brand getBrandById(int id) throws SQLException {
        String sql = "select * from Brand where brandID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Brand(rs.getInt("brandID"), rs.getString("brandName"));
            }
        }
        return null;
    }

    public Map<Brand, Integer> getAllBrands() throws SQLException {
        String sql = "SELECT \n"
                + "    br.brandID, \n"
                + "    br.brandName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Brand br\n"
                + "JOIN Merchandise m ON m.brandID = br.brandID\n"
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY br.brandID, br.brandName";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Brand, Integer> brandMap = new HashMap<>();
            while (rs.next()) {
                brandMap.put(new Brand(rs.getInt("brandID"), rs.getString("brandName")), rs.getInt("productCount"));
            }
            return brandMap;
        }
    }

    /**
     * For management
     *
     * @param query
     * @param type
     * @param sortCriteria
     * @param page
     * @param pageSize
     * @return
     * @throws SQLException
     */
    public List<Product> getAllProducts(String query, String type, String sortCriteria, int page, int pageSize) throws SQLException {

        StringBuilder sql = getCTETables(null);
        int offset = (page - 1) * pageSize;

        // Base SELECT clause
        sql.append("SELECT P.*");
        sql.append("\n       ,C.categoryName");
        sql.append("\n       ,PD.discountPercentage");
        sql.append("\n       ,PD.eventDateStarted");
        sql.append("\n       ,PD.eventDuration");
        sql.append(query != null && !query.trim().isEmpty() ? "\n       ,KEY_TBL.RANK AS relevance_score" : "");
        sql.append("\nFROM Product AS P");
        sql.append(query != null && !query.trim().isEmpty() ? "\nJOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL ON P.productID = KEY_TBL.[KEY]" : "");

        // Type-specific JOIN if needed
        type = type != null ? type : "";    //Handling null
        if (type.equals("book")) {
            sql.append("\nJOIN Book B");
            sql.append("\n    ON B.bookID = P.productID");
        } else if (type.equals("merch")) {
            sql.append("\nJOIN Merchandise M");
            sql.append("\n    ON M.merchandiseID = P.productID");
        }

        // Common LEFT JOINs and WHERE clause
        sql.append("\nLEFT JOIN ProductDiscount PD");
        sql.append("\n    ON P.productID = PD.productID AND PD.rn = 1");
        sql.append("\nLEFT JOIN Category AS C");
        sql.append("\n    ON C.categoryID = P.categoryID");
        sql.append("\nWHERE 1 = 1");    //Place holder for conditional clauses

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();
        //Add search term if any
        if (query != null && !query.trim().isEmpty()) {
            paramList.add(formatQueryBroad(query));
        }

        // Append sorting
        sortCriteria = sortCriteria != null ? sortCriteria : ""; //Handling null
        sql.append("\nORDER BY ");
        sql.append(processSort(sortCriteria));

        //Append paginated
        sql.append("\nOFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        paramList.add(offset);
        paramList.add(pageSize);

        //Print the final query to console
        System.out.println(sql);
        System.out.println("-------------------------------------------------------------------------------------");

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, null));
            }
            return productList;
        }
    }

    /**
     * Overload for management
     *
     * @param query
     * @param type
     * @return
     * @throws SQLException
     */
    public int getProductsCount(String query, String type) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product P\n");

        //If there is search query
        sql.append(query != null && !query.trim().isEmpty() ? "\nJOIN CONTAINSTABLE(Product, keywords, ?) AS KEY_TBL ON P.productID = KEY_TBL.[KEY]" : "");

        // Type-specific JOIN if needed
        type = type != null ? type : "";    //Handling null
        if (type.equals("book")) {
            sql.append("\nJOIN Book B");
            sql.append("\n    ON B.bookID = P.productID");
        } else if (type.equals("merch")) {
            sql.append("\nJOIN Merchandise M");
            sql.append("\n    ON M.merchandiseID = P.productID");
        }

        //Initialize the param list
        List<Object> paramList = new ArrayList<>();
        //Add search term if any
        if (query != null && !query.trim().isEmpty()) {
            paramList.add(formatQueryBroad(query));
        }

        Object[] params = paramList.toArray();

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean addNewProducts(Product newProduct, Object[] dataArray) throws SQLException {
        Connection connection = null;
        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);
            SimpleEntry<String, Object[]> stmtEntry;

            dataArray = dataArray != null ? dataArray : new Object[0];
            int insertedProductID = 0;
            int creatorID = 0;
            int genreID = 0;

            Publisher publisher = new Publisher();
            Series series = new Series();
            OGCharacter character = new OGCharacter();
            Brand brand = new Brand();

            stmtEntry = generateInsertStatement(new Object[]{newProduct}, "product");
            insertedProductID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
            //Insert failed
            if (insertedProductID == 0) {
                throw new SQLException("Failed to add this product!");
            }

            newProduct.setProductID(insertedProductID);

            Set<Integer> associatedCreatorIDs = new HashSet<>();
            for (Object dataObj : dataArray) {
                if (dataObj instanceof Creator) {
                    Creator creator = (Creator) dataObj;
                    creatorID = creator.getCreatorID();

                    //Add new Creator
                    if (creatorID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "creator");
                        creatorID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (creatorID == 0) {
                            throw new SQLException("Error adding creator: " + creator.getCreatorName() + " - " + creator.getCreatorRole());

                        }
                        creator.setCreatorID(creatorID);
                    }

                    //Associate creators with product
                    if (creatorID > 0 && !associatedCreatorIDs.contains(creatorID) && insertedProductID > 0) {
                        stmtEntry = generateInsertStatement(new Object[]{insertedProductID, creatorID}, "product_creator");
                        if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) == 0) {
                            throw new SQLException("Error assigning creatorID " + creatorID + " to productID " + insertedProductID);
                        }
                        //Track associated ids to ensure only non-duplicate ids are associated
                        associatedCreatorIDs.add(creatorID);
                    }

                } else if (dataObj instanceof Genre) {
                    Genre genre = (Genre) dataObj;
                    genreID = genre.getGenreID();
                    //Associate genres with book
                    if (genreID > 0 && insertedProductID > 0) {
                        stmtEntry = generateInsertStatement(new Object[]{insertedProductID, genreID}, "book_genre");
                        if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) == 0) {
                            throw new SQLException("Error assigning genreID " + genreID + " to productID " + insertedProductID);
                        }
                    }
                } else if (dataObj instanceof Publisher) {
                    publisher = (Publisher) dataObj;
                    int publisherID = publisher.getPublisherID();

                    //Add new Publisher
                    if (publisherID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "publisher");
                        publisherID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (publisherID == 0) {
                            throw new SQLException("Error adding publisher: " + publisher.getPublisherName());
                        }
                        publisher.setPublisherID(publisherID);
                    }

                } else if (dataObj instanceof Series) {
                    series = (Series) dataObj;
                    int seriesID = series.getSeriesID();

                    //Add new Series
                    if (seriesID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "series");
                        seriesID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (seriesID == 0) {
                            throw new SQLException("Error adding merch series: " + series.getSeriesName());
                        }
                        series.setSeriesID(seriesID);
                    }
                } else if (dataObj instanceof Brand) {
                    brand = (Brand) dataObj;
                    int brandID = brand.getBrandID();

                    //Add new Brand
                    if (brandID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "brand");
                        brandID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (brandID == 0) {
                            throw new SQLException("Error adding merch brand: " + brand.getBrandName());
                        }
                        brand.setBrandID(brandID);
                    }
                } else if (dataObj instanceof OGCharacter) {
                    character = (OGCharacter) dataObj;
                    int characterID = character.getCharacterID();

                    //Add new Character
                    if (characterID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "character");
                        characterID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (characterID == 0) {
                            throw new SQLException("Error adding merch character: " + character.getCharacterName());
                        }

                        character.setCharacterID(characterID);
                    }
                }

            }

            //Handle type-specific data
            String className = "";
            if (newProduct instanceof Book) {
                ((Book) newProduct).setPublisher(publisher);
                className = Book.class.getSimpleName();
            } else if (newProduct instanceof Merchandise) {
                ((Merchandise) newProduct).setBrand(brand).setCharacter(character).setSeries(series);
                className = Merchandise.class.getSimpleName();
            }

            stmtEntry = generateUpdateStatement(new Object[]{newProduct}, className);

            //Update Book or Merch based on type
            if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                    connection.close();
                } catch (SQLException e) {
                    throw e;
                }
            }

        }
    }

    private SimpleEntry<String, Object[]> generateInsertStatement(Object[] newObjects, String classNames) {
        newObjects = newObjects != null ? newObjects : new Object[1];
        StringBuilder sql = new StringBuilder();
        List<Object> paramList = new ArrayList<>();

        switch (classNames != null ? classNames.toLowerCase() : "") {
            case "product":
                if (newObjects[0] instanceof Product) {
                    Product newProduct = (Product) newObjects[0];
                    sql.append("INSERT INTO Product (categoryID, adminID, keywords, generalCategory, productIsActive, imageURL, description, releaseDate, specialFilter, productName, price, stockCount) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    paramList.add(newProduct.getSpecificCategory().getCategoryID());
                    paramList.add(newProduct.getAdminID());
                    paramList.add(newProduct.getKeywords());
                    paramList.add(newProduct.getGeneralCategory());
                    paramList.add(newProduct.isIsActive());
                    paramList.add(newProduct.getImageURL());
                    paramList.add(newProduct.getDescription());
                    paramList.add(java.sql.Date.valueOf(newProduct.getReleaseDate()));
                    paramList.add(newProduct.getSpecialFilter());
                    paramList.add(newProduct.getProductName());
                    paramList.add(newProduct.getPrice());
                    paramList.add(newProduct.getStockCount());
                }
                break;
            case "creator":
                if (newObjects[0] instanceof Creator) {
                    Creator newCreator = (Creator) newObjects[0];
                    sql.append("INSERT INTO [dbo].[Creator]\n"
                            + "           ([creatorName]\n"
                            + "           ,[creatorRole])\n"
                            + "     VALUES\n"
                            + "           (?,?)");
                    paramList.add(newCreator.getCreatorName());
                    paramList.add(newCreator.getCreatorRole());
                }

                break;
            case "product_creator":
                if (newObjects[0] instanceof Integer && newObjects[1] instanceof Integer) {
                    sql.append("INSERT INTO [dbo].[Product_Creator]\n"
                            + "           ([productID]\n"
                            + "           ,[creatorID])\n"
                            + "     VALUES\n"
                            + "           (?,?)");
                    paramList.add((int) newObjects[0]);
                    paramList.add((int) newObjects[1]);
                }

                break;
            case "book_genre":
                if (newObjects[0] instanceof Integer && newObjects[1] instanceof Integer) {
                    sql.append("INSERT INTO [dbo].[Book_Genre]\n"
                            + "           ([bookID]\n"
                            + "           ,[genreID])\n"
                            + "     VALUES\n"
                            + "           (?,?)");
                    paramList.add((int) newObjects[0]);
                    paramList.add((int) newObjects[1]);
                }

                break;
            case "publisher":
                if (newObjects[0] instanceof Publisher) {
                    Publisher publisher = (Publisher) newObjects[0];
                    sql.append("INSERT INTO [dbo].[Publisher]\n"
                            + "           ([publisherName])\n"
                            + "     VALUES\n"
                            + "           (?)");
                    paramList.add(publisher.getPublisherName());
                }

                break;
            case "series":
                if (newObjects[0] instanceof Series) {
                    Series series = (Series) newObjects[0];
                    sql.append("INSERT INTO [dbo].[Series]\n"
                            + "           ([seriesName])\n"
                            + "     VALUES\n"
                            + "           (?)");
                    paramList.add(series.getSeriesName());
                }

                break;
            case "brand":
                if (newObjects[0] instanceof Brand) {
                    Brand brand = (Brand) newObjects[0];
                    sql.append("INSERT INTO [dbo].[Brand]\n"
                            + "           ([brandName])\n"
                            + "     VALUES\n"
                            + "           (?)");
                    paramList.add(brand.getBrandName());
                }

                break;
            case "character":
                if (newObjects[0] instanceof OGCharacter) {
                    OGCharacter character = (OGCharacter) newObjects[0];
                    sql.append("INSERT INTO [dbo].[Character]\n"
                            + "           ([characterName])\n"
                            + "     VALUES\n"
                            + "           (?)");
                    paramList.add(character.getCharacterName());
                }

                break;
            default:
                throw new IllegalArgumentException("Unexpected entity name: " + classNames);
        }

        return new SimpleEntry<>(sql.toString(), paramList.toArray());
    }

    private SimpleEntry<String, Object[]> generateUpdateStatement(Object[] updatedObjs, String classNames) {
        updatedObjs = updatedObjs != null ? updatedObjs : new Object[1];
        StringBuilder sql = new StringBuilder();
        List<Object> paramList = new ArrayList<>();

        switch (classNames != null ? classNames.toLowerCase() : "") {
            case "product":
                if (updatedObjs[0] instanceof Product) {
                    Product updatedProduct = (Product) updatedObjs[0];
                    sql.append("UPDATE Product SET categoryID = ?, adminID = ?, keywords = ?, generalCategory = ?, "
                            + "productIsActive = ?, imageURL = ?, description = ?, releaseDate = ?, specialFilter = ?, "
                            + "productName = ?, price = ?, stockCount = ? "
                            + "WHERE productID = ?");
                    paramList.add(updatedProduct.getSpecificCategory().getCategoryID());
                    paramList.add(updatedProduct.getAdminID());
                    paramList.add(updatedProduct.getKeywords());
                    paramList.add(updatedProduct.getGeneralCategory());
                    paramList.add(updatedProduct.isIsActive());
                    paramList.add(updatedProduct.getImageURL());
                    paramList.add(updatedProduct.getDescription());
                    paramList.add(java.sql.Date.valueOf(updatedProduct.getReleaseDate()));
                    paramList.add(updatedProduct.getSpecialFilter());
                    paramList.add(updatedProduct.getProductName());
                    paramList.add(updatedProduct.getPrice());
                    paramList.add(updatedProduct.getStockCount());
                    paramList.add(updatedProduct.getProductID());
                }
                break;
            case "book":
                if (updatedObjs[0] instanceof Book) {
                    Book updatedBook = (Book) updatedObjs[0];
                    sql.append("UPDATE Book SET\n");
                    if (updatedBook.getPublisher() != null && updatedBook.getPublisher().getPublisherID() > 0) {
                        sql.append("publisherID = ?,\n");
                        paramList.add(updatedBook.getPublisher().getPublisherID());
                    }
                    sql.append("bookDuration = ? WHERE bookID = ?\n");
                    paramList.add(updatedBook.getDuration());
                    paramList.add(updatedBook.getProductID());
                }
                break;
            case "merchandise":
                if (updatedObjs[0] instanceof Merchandise) {
                    Merchandise updatedMerch = (Merchandise) updatedObjs[0];
                    sql.append("UPDATE [dbo].[Merchandise] SET\n");

                    if (updatedMerch.getSeries() != null && updatedMerch.getSeries().getSeriesID() > 0) {
                        sql.append("[seriesID] = ?,\n");
                        paramList.add(updatedMerch.getSeries().getSeriesID());
                    }

                    if (updatedMerch.getCharacter() != null && updatedMerch.getCharacter().getCharacterID() > 0) {
                        sql.append("[characterID] = ?,\n");
                        paramList.add(updatedMerch.getCharacter().getCharacterID());
                    }

                    if (updatedMerch.getBrand() != null && updatedMerch.getBrand().getBrandID() > 0) {
                        sql.append("[brandID] = ?,\n");
                        paramList.add(updatedMerch.getBrand().getBrandID());
                    }

                    sql.append("[size] = ?, [scaleLevel] = ?, [material] = ? WHERE [merchandiseID] = ?\n");
                    paramList.add(updatedMerch.getSize());
                    paramList.add(updatedMerch.getScaleLevel());
                    paramList.add(updatedMerch.getMaterial());
                    paramList.add(updatedMerch.getProductID());
                }

                break;
            case "importitem":
                String placeHolder = String.join(",", Collections.nCopies(updatedObjs.length, "?"));
                sql.append("UPDATE ImportItem SET isImported = ? WHERE importItemID IN (")
                        .append(placeHolder)
                        .append(")\n");

                paramList.add(true);
                for (Object updatedObj : updatedObjs) {
                    if (updatedObj instanceof ImportItem) {
                        ImportItem item = (ImportItem) updatedObj;
                        paramList.add(item.getImportItemID());
                    }
                }

                break;
            case "importedproduct":
                int quantity = 0;
                int productID = 0;
                String specialFilter = "";
                LocalDate releaseDate = null;
                for (Object updatedObj : updatedObjs) {
                    if (updatedObj instanceof ImportItem) {
                        ImportItem item = (ImportItem) updatedObj;
                        Product product = item.getProduct();
                        quantity += item.getImportQuantity();
                        if (productID == 0) {
                            productID = product.getProductID();
                        }
                        if (specialFilter.equals("")) {
                            specialFilter = product.getSpecialFilter();
                        }

                        if (releaseDate == null) {
                            releaseDate = product.getReleaseDate();
                        }
                    }
                }

                sql.append("UPDATE Product SET stockCount = stockCount + ?\n");
                paramList.add(quantity);

                if (releaseDate != null && releaseDate.isBefore(LocalDate.now())) {
                    sql.append(", releaseDate = ?\n");
                    paramList.add(LocalDate.now());
                }

                if (!specialFilter.equalsIgnoreCase("new")) {
                    sql.append(", specialFilter = ?\n");
                    paramList.add("new");
                }

                sql.append("WHERE productID = ?\n");
                paramList.add(productID);

                break;
            default:
                throw new IllegalArgumentException("Unexpected entity name: " + classNames);
        }

        return new SimpleEntry<>(sql.toString(), paramList.toArray());
    }

    private SimpleEntry<String, Object[]> generateDeleteStatement(Object[] deletedParams, String classNames) {
        if (deletedParams == null || deletedParams.length == 0) {
            throw new IllegalArgumentException("Cannot generate statement from NULLs!");
        }
        int length = deletedParams.length;

        String placeHolder = length > 1 ? String.join(",", Collections.nCopies(length - 1, "?")) : "";
        StringBuilder sql = new StringBuilder();

        switch (classNames != null ? classNames.toLowerCase() : "") {
            case "product_creator":
                sql.append("DELETE FROM Product_Creator\n")
                        .append("WHERE productID = ? AND creatorID NOT IN (")
                        .append(placeHolder)
                        .append(")\n");

                break;
            case "book_genre":
                sql.append("DELETE FROM Book_Genre\n")
                        .append("WHERE bookID = ?\n");

                if (length == 1) {
                    break;
                }

                sql.append("AND genreID NOT IN (")
                        .append(placeHolder)
                        .append(")\n");

                break;
            default:
                throw new IllegalArgumentException("Invalid entity name: " + classNames);
        }

        return new SimpleEntry<>(sql.toString(), deletedParams);
    }

    public int getCreatorIDByNameAndRole(String name, String role) throws SQLException {
        String sql = "SELECT creatorID from Creator\n"
                + "WHERE contains(creatorName, ?) AND creatorRole = ?";
        Object[] params = {formatQueryTight(name, "AND"), role};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return rs.getInt("creatorID");
            }
        }
        return 0;
    }

    public int getPublisherIDByName(String publisherName) throws SQLException {
        String sql = "SELECT publisherID\n"
                + "FROM     Publisher\n"
                + "WHERE  contains(publisherName,?)";
        Object[] params = {formatQueryTight(publisherName, "AND")};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return rs.getInt("publisherID");
            }
        }
        return 0;
    }

    public int getSeriesIDByName(String seriesName) throws SQLException {
        String sql = "SELECT seriesID\n"
                + "FROM     Series\n"
                + "WHERE  contains(seriesName,?)";
        Object[] params = {formatQueryTight(seriesName, "AND")};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return rs.getInt("seriesID");
            }
        }
        return 0;
    }

    public int getCharacterIDByName(String characterName) throws SQLException {
        String sql = "SELECT characterID\n"
                + "FROM     Character\n"
                + "WHERE  contains(characterName,?)";
        Object[] params = {formatQueryTight(characterName, "AND")};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return rs.getInt("characterID");
            }
        }
        return 0;
    }

    public int getBrandIDByName(String brandName) throws SQLException {
        String sql = "SELECT brandID\n"
                + "FROM     Brand\n"
                + "WHERE  contains(brandName,?)";
        Object[] params = {formatQueryTight(brandName, "AND")};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return rs.getInt("brandID");
            }
        }
        return 0;
    }

    public boolean updateProducts(Product updatedProduct, Object[] dataArray) throws SQLException {
        Connection connection = null;
        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);
            SimpleEntry<String, Object[]> stmtEntry;

            dataArray = dataArray != null ? dataArray : new Object[0];
            int updatedProductID = updatedProduct.getProductID();
            int creatorID = 0;
            int genreID = 0;

            Publisher publisher = new Publisher();
            Series series = new Series();
            OGCharacter character = new OGCharacter();
            Brand brand = new Brand();

            stmtEntry = generateUpdateStatement(new Object[]{updatedProduct}, "product");
            boolean updateSuccess = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) > 0;
            //Update failed
            if (!updateSuccess) {
                throw new SQLException("Failed to update this product!");
            }

            Set<Integer> associatedCreatorIDs = new LinkedHashSet<>();
            Set<Integer> associatedGenreIDs = new LinkedHashSet<>();
            Set<Integer> deleteAllGenreIDs = new LinkedHashSet<>();

            for (Object dataObj : dataArray) {
                if (dataObj instanceof Creator) {
                    Creator creator = (Creator) dataObj;
                    creatorID = creator.getCreatorID();
                    String creatorName = creator.getCreatorName();

                    if (creatorName.trim().endsWith("(associated)")) {
                        associatedCreatorIDs.add(creatorID);
                        continue;
                    }

                    if (creatorID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "creator");
                        creatorID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (creatorID == 0) {
                            throw new SQLException("Error adding creator: " + creator.getCreatorName() + " - " + creator.getCreatorRole());

                        }
                    }

                    if (creatorID > 0 && !associatedCreatorIDs.contains(creatorID) && updatedProductID > 0) {
                        stmtEntry = generateInsertStatement(new Object[]{updatedProductID, creatorID}, "product_creator");
                        if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) == 0) {
                            throw new SQLException("Error assigning creatorID " + creatorID + " to productID " + updatedProductID);
                        }
                        associatedCreatorIDs.add(creatorID);
                    }

                } else if (dataObj instanceof Genre) {
                    Genre genre = (Genre) dataObj;
                    genreID = genre.getGenreID();
                    String genreName = genre.getGenreName();
                    genreName = genreName != null ? genreName : "";

                    if (genreName.trim().endsWith("(deleteAll)")) {
                        deleteAllGenreIDs.add(genreID);
                        continue;
                    }

                    if (genreName.trim().endsWith("(associated)")) {
                        associatedGenreIDs.add(genreID);
                        continue;
                    }

                    if (genreID > 0 && !associatedGenreIDs.contains(genreID) && updatedProductID > 0) {
                        stmtEntry = generateInsertStatement(new Object[]{updatedProductID, genreID}, "book_genre");
                        if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) == 0) {
                            throw new SQLException("Error assigning genreID " + genreID + " to productID " + updatedProductID);
                        }
                        associatedGenreIDs.add(genreID);
                    }
                } else if (dataObj instanceof Publisher) {
                    publisher = (Publisher) dataObj;
                    int publisherID = publisher.getPublisherID();
                    if (publisherID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "publisher");
                        publisherID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (publisherID == 0) {
                            throw new SQLException("Error adding publisher: " + publisher.getPublisherName());

                        }
                        publisher.setPublisherID(publisherID);
                    }

                } else if (dataObj instanceof Series) {
                    series = (Series) dataObj;
                    int seriesID = series.getSeriesID();
                    if (seriesID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "series");
                        seriesID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (seriesID == 0) {
                            throw new SQLException("Error adding merch series: " + series.getSeriesName());

                        }
                        series.setSeriesID(seriesID);
                    }
                } else if (dataObj instanceof Brand) {
                    brand = (Brand) dataObj;
                    int brandID = brand.getBrandID();
                    if (brandID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "brand");
                        brandID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (brandID == 0) {
                            throw new SQLException("Error adding merch brand: " + brand.getBrandName());

                        }
                        brand.setBrandID(brandID);
                    }
                } else if (dataObj instanceof OGCharacter) {
                    character = (OGCharacter) dataObj;
                    int characterID = character.getCharacterID();
                    if (characterID == 0) {
                        stmtEntry = generateInsertStatement(new Object[]{dataObj}, "character");
                        characterID = context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), true);
                        //Insert failed
                        if (characterID == 0) {
                            throw new SQLException("Error adding merch character: " + character.getCharacterName());

                        }
                        character.setCharacterID(characterID);
                    }
                }

            }

            if (!associatedCreatorIDs.isEmpty()) {
                Integer[] params = Stream.concat(Stream.of(updatedProductID), associatedCreatorIDs.stream()).toArray(Integer[]::new);
                stmtEntry = generateDeleteStatement(params, "product_creator");
                context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false);
            }

            if (!associatedGenreIDs.isEmpty()) {
                //Delete some
                Integer[] params = Stream.concat(Stream.of(updatedProductID), associatedGenreIDs.stream()).toArray(Integer[]::new);
                stmtEntry = generateDeleteStatement(params, "book_genre");
                context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false);
            } else if (!deleteAllGenreIDs.isEmpty()) {
                //Delete all
                stmtEntry = generateDeleteStatement(new Object[]{updatedProductID}, "book_genre");
                context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false);
            }

            String className = "";
            if (updatedProduct instanceof Book) {
                ((Book) updatedProduct).setPublisher(publisher);
                className = Book.class.getSimpleName();
            } else if (updatedProduct instanceof Merchandise) {
                ((Merchandise) updatedProduct).setBrand(brand).setCharacter(character).setSeries(series);
                className = Merchandise.class.getSimpleName();
            }

            stmtEntry = generateUpdateStatement(new Object[]{updatedProduct}, className);

            if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                    connection.close();
                } catch (SQLException e) {
                    throw e;
                }
            }

        }
    }

    public boolean changeProductStatus(int productID, boolean newStatus) throws SQLException {
        String sql = "UPDATE Product SET productIsActive = ? WHERE productID = ?";
        Object[] params = {newStatus, productID};
        return context.exeNonQuery(sql, params) > 0;
    }

    public Map<Supplier, List<ImportItem>> getPendingImportMapByProductID(int productID) throws SQLException {
        String sql = "SELECT ii.*, \n"
                + "       p.productName, \n"
                + "       p.specialFilter, \n"
                + "       p.releaseDate, \n"
                + "       s.supplierName\n"
                + "FROM ImportItem AS ii\n"
                + "INNER JOIN Product AS p ON ii.productID = p.productID\n"
                + "INNER JOIN Supplier AS s ON ii.supplierID = s.supplierID\n"
                + "WHERE ii.productID = ? AND ii.isImported = 0";

        Object[] params = {productID};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            Map<Supplier, List<ImportItem>> importMap = new HashMap<>();
            while (rs.next()) {
                LocalDate importDate = rs.getDate("importDate") != null ? rs.getDate("importDate").toLocalDate() : LocalDate.EPOCH;
                LocalDate releaseDate = rs.getDate("releaseDate") != null ? rs.getDate("releaseDate").toLocalDate() : LocalDate.MAX;
                Supplier supplier = new Supplier(rs.getInt("supplierID"), rs.getString("supplierName"));
                ImportItem item = new ImportItem()
                        .setImportItemID(rs.getInt("importItemID"))
                        .setProduct(new Product().setProductID(rs.getInt("productID")).setProductName(rs.getString("productName"))
                                .setSpecialFilter(rs.getString("specialFilter")).setReleaseDate(releaseDate))
                        .setSupplier(supplier)
                        .setImportDate(importDate)
                        .setImportPrice(rs.getDouble("importPrice"))
                        .setImportQuantity(rs.getInt("importQuantity"))
                        .setIsImported(rs.getBoolean("isImported"));

                importMap.computeIfAbsent(supplier, key -> new ArrayList<>()).add(item);
            }

            return importMap;
        }
    }

    public boolean importProducts(List<ImportItem> items) throws SQLException {
        if (items == null) {
            throw new IllegalArgumentException("Cannot import null items!");
        }

        Connection connection = null;
        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);
            SimpleEntry<String, Object[]> stmtEntry;
            ImportItem[] itemArr = items.toArray(ImportItem[]::new);

            stmtEntry = generateUpdateStatement(itemArr, "ImportItem");
            if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) == 0) {
                throw new SQLException("Failed to update import status!");
            }

            stmtEntry = generateUpdateStatement(itemArr, "ImportedProduct");
            if (context.exeNonQuery(connection, stmtEntry.getKey(), stmtEntry.getValue(), false) == 0) {
                throw new SQLException("Failed to update product info after import!");
            }

            connection.commit();
            return true;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                    connection.close();
                } catch (SQLException e) {
                    throw e;
                }
            }

        }
    }

    public List<Product> getAllProductsForQueueing() throws SQLException {
        String sql = "SELECT productID, \n"
                + "       productName, \n"
                + "       stockCount, \n"
                + "       releaseDate\n"
                + "FROM Product\n"
                + "WHERE releaseDate <= DATEADD(DAY, 30, GETDATE())\n"
                + "ORDER BY stockCount ASC, \n"
                + "         DATEDIFF(DAY, releaseDate, GETDATE()) ASC";
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(new Product().setProductID(rs.getInt("productID"))
                        .setProductName(rs.getString("productName"))
                        .setStockCount(rs.getInt("stockCount"))
                        .setReleaseDate(rs.getDate("releaseDate") != null ? rs.getDate("releaseDate").toLocalDate() : LocalDate.MAX));
            }
            return productList;
        }

    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        String sql = "SELECT supplierID, supplierName FROM Supplier";
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            List<Supplier> supplierList = new ArrayList<>();
            while (rs.next()) {
                supplierList.add(new Supplier(rs.getInt("supplierID"), rs.getString("supplierName")));
            }
            return supplierList;
        }
    }

    public boolean queueImport(ImportItem queuedItem) throws SQLException {
        String sql = "INSERT INTO ImportItem (productID, supplierID, importPrice, "
                + "importQuantity, importDate, isImported) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Object[] params = {
            queuedItem.getProduct().getProductID(),
            queuedItem.getSupplier().getSupplierID(),
            queuedItem.getImportPrice(),
            queuedItem.getImportQuantity(),
            queuedItem.getImportDate(),
            queuedItem.isIsImported()
        };

        return context.exeNonQuery(sql, params) > 0;
    }

    public static void main(String[] args) {
        try {
            ProductDAO productDAO = new ProductDAO();
            // Checking the final map
            System.out.println(productDAO.getPendingImportMapByProductID(2)); // Output: {1=[Item A, Item B]}
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    

}
