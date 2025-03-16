/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 *
 * @author anhkc
 */
public class ProductDAO {

    private final utils.DBContext context;

    public ProductDAO() {
        context = new utils.DBContext();
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
                + "       PD.dateStarted,\n"
                + "	   PD.eventDuration\n"
                + "FROM Product AS P\n"
                + "LEFT JOIN ProductDiscount PD \n"
                + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C \n"
                + "    ON C.categoryID = P.categoryID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?\n");

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
     * @return
     * @throws SQLException
     */
    public Product callGetProductByTypeAndId(String type, int productID) throws SQLException {
        switch (type) {
            case "merch":
                return getMerchById(productID);
            case "book":
                return getBookById(productID);
            default:
                return null;
        }
    }

    /**
     * For view merch details
     *
     * @param productID
     * @return
     * @throws SQLException
     */
    public Product getMerchById(int productID) throws SQLException {
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
                + "PD.dateStarted,\n"
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
                + "WHERE P.isActive = 1\n"
                + "AND P.productID = ? \n");

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
    public Product getBookById(int productID) throws SQLException {

        StringBuilder sql = getCTETables("rank").append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.duration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.dateStarted,PD.eventDuration,TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN TopSale TS ON TS.productID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?");

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

    public HashMap<String, Creator> getCreatorsOfThisProduct(int productID) throws SQLException {
        String sql = "SELECT PC.creatorID, C.creatorName, C.creatorRole\n"
                + "FROM Creator AS C\n"
                + "JOIN Product_Creator AS PC ON C.creatorID = PC.creatorID\n"
                + "WHERE PC.productID = ?";
        Object[] params = {productID};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            HashMap<String, Creator> creatorMap = new HashMap<>();
            while (rs.next()) {

                creatorMap.put(rs.getString(3), new Creator(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            return creatorMap;
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
     * When user does not enter anything in the search bar
     *
     * @param type
     * @param sortCriteria
     * @param filterMap
     * @return
     * @throws SQLException
     */
    public List<Product> getActiveProducts(String type, String sortCriteria, Map<String, String> filterMap) throws SQLException {
//        Prepare the query with CTE first
        StringBuilder sql = getCTETables(null);

        //Append base query
        sql.append("SELECT P.*, \n"
                + "       C.categoryName, \n"
                + "       PD.discountPercentage, \n"
                + "       PD.dateStarted,\n"
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
                + "WHERE P.isActive = 1\n");

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

    public List<Product> getSearchResult(String query, String type, String sortCriteria, Map<String, String> filterMap) throws SQLException {

        //Prepare the query with CTE first
        StringBuilder sql = getCTETables(null);

        // Base SELECT clause
        sql.append("SELECT P.*,");
        sql.append("\n       C.categoryName,");
        sql.append("\n       PD.discountPercentage,");
        sql.append("\n       PD.dateStarted,");
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
        sql.append("\nWHERE P.isActive = 1");

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
        String[] queryParts = query.split("\\s+");
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
        String[] queryParts = query.split("\\s+");
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

    public List<Product> getProductsByCondition(int conditionID, String sortCriteria, Map<String, String> filterMap, String condition, String generalCategory, String location) throws SQLException {
        StringBuilder sql = getCTETables(null);
        sql.append(location.equals("home") ? "SELECT TOP 7\n" : "SELECT\n");
        sql.append("P.*, C.categoryName, PD.discountPercentage, PD.dateStarted, PD.eventDuration\n"
                + "FROM Product AS P\n");

        //Conditional joins
        sql.append(getSpecificJoin(condition, generalCategory));

        //Discount join
        sql.append("LEFT JOIN ProductDiscount PD \n"
                + "    ON P.productID = PD.productID AND PD.rn = 1\n"
        );

        //Initialize where clause
        sql.append("WHERE P.isActive = 1\n").append(getInitialWhereClause(condition, conditionID, location));

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
        switch (condition) {
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
                return conditionID == 18 && location != null && location.equals("home") ? "AND BG.genreID = ?\n AND P.specialFilter is null\n" : "AND BG.genreID = ?\n";
            case "pbl":
                return "AND B.publisherID = ?\n";
            case "srs":
                return conditionID == 1 && location != null && location.equals("home") ? "AND M.seriesID = ?\n AND P.specialFilter is null\n" : "AND M.seriesID = ?\n";
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
                + "PD.dateStarted,\n"
                + "PD.eventDuration,\n"
                + "TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN TopSale TS ON TS.productID = P.productID\n");

        //Type specific join
        sql.append(type.equals("book") ? "JOIN Book B ON B.bookID = P.productID\n" : "JOIN Merchandise M ON M.merchandiseID = P.productID\n");

        //Common part
        sql.append("LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "WHERE P.isActive = 1 \n"
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
                + "e.dateStarted,\n"
                + "e.duration as eventDuration,\n"
                + "ep.discountPercentage,\n"
                + "ROW_NUMBER() OVER (PARTITION BY ep.productID ORDER BY e.dateStarted DESC, ep.eventID DESC) AS rn\n"
                + "FROM Event e\n"
                + "JOIN Event_Product ep ON e.eventID = ep.eventID\n"
                + "WHERE e.isActive = 1\n"
                + "AND GETDATE() <= DATEADD(day, e.duration, e.dateStarted)\n"
                + "AND GETDATE() >= e.dateStarted\n"
                + ")\n");
    }

    private Product mapResultSetToProduct(ResultSet rs, String type) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

        LocalDate eventEndDate = null;
        int discountPercentage = 0;
        java.sql.Date sqlDateStarted = rs.getDate("dateStarted");
        if (sqlDateStarted != null) {
            eventEndDate = sqlDateStarted.toLocalDate().plusDays(rs.getInt("eventDuration"));
            discountPercentage = LocalDate.now().isAfter(eventEndDate) ? 0 : rs.getInt("discountPercentage");
        }

        switch (type != null ? type : "") {
            case "book":
                // For book details
                Publisher publisher = new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));
                return new Book(publisher, rs.getString("duration"),
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("stockCount"),
                        category,
                        rs.getString("description"),
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getTimestamp("lastModifiedTime").toLocalDateTime(),
                        rs.getDouble("averageRating"),
                        rs.getInt("numberOfRating"),
                        rs.getString("specialFilter"),
                        rs.getInt("adminID"),
                        rs.getString("keywords"),
                        rs.getString("generalCategory"),
                        rs.getBoolean("isActive"),
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
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getTimestamp("lastModifiedTime").toLocalDateTime(),
                        rs.getDouble("averageRating"),
                        rs.getInt("numberOfRating"),
                        rs.getString("specialFilter"),
                        rs.getInt("adminID"),
                        rs.getString("keywords"),
                        rs.getString("generalCategory"),
                        rs.getBoolean("isActive"),
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
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getTimestamp("lastModifiedTime").toLocalDateTime(),
                        rs.getDouble("averageRating"),
                        rs.getInt("numberOfRating"),
                        rs.getString("specialFilter"),
                        rs.getInt("adminID"),
                        rs.getString("keywords"),
                        rs.getString("generalCategory"),
                        rs.getBoolean("isActive"),
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
                + "    ON p.categoryID = c.categoryID AND p.isActive = 1  \n"
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
                return new Creator(rs.getInt("creatorID"), rs.getString("creatorName"), rs.getString("creatorRole"), rs.getString("generalCategory"));
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
                + "    AND p.isActive = 1  \n"
                + "GROUP BY c.creatorID, c.creatorName, c.creatorRole;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Creator, Integer> creatorMap = new HashMap<>();
            while (rs.next()) {
                creatorMap.put(new Creator(rs.getInt("creatorID"), rs.getString("creatorName"), rs.getString("creatorRole")), rs.getInt("productCount"));
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
                + "LEFT JOIN Product p ON b.bookID = p.productID AND p.isActive = 1\n"
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
                + "LEFT JOIN Product pr ON b.bookID = pr.productID AND pr.isActive = 1\n"
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
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.isActive = 1\n"
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
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.isActive = 1\n"
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
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.isActive = 1\n"
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
        sql.append("\n       ,PD.dateStarted");
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

    public boolean addNewProducts(Product newProduct) throws SQLException {
        String sql = "INSERT INTO Product (categoryID, adminID, keywords, generalCategory, isActive, imageURL, description, releaseDate, specialFilter, productName, price, stockCount) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {
            newProduct.getSpecificCategory().getCategoryID(), // categoryID
            newProduct.getAdminID(), // adminID
            newProduct.getKeywords(), // keywords
            newProduct.getGeneralCategory(), // generalCategory
            newProduct.isIsActive(), // isActive
            newProduct.getImageURL(), // imageURL
            newProduct.getDescription(), // description
            java.sql.Date.valueOf(newProduct.getReleaseDate()), // releaseDate
            newProduct.getSpecialFilter(), // specialFilter
            newProduct.getProductName(), // productName
            newProduct.getPrice(), // price
            newProduct.getStockCount() // stockCount
        };

        return context.exeNonQuery(sql, params) > 0;
    }

    public int getLatestProductID() throws SQLException {
        String sql = "SELECT TOP 1 productID FROM Product ORDER BY productID DESC";
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            if (rs.next()) {
                return rs.getInt("productID");
            }
        }
        return 0;
    }

    public boolean addNewCreators(Creator newCreator) {
        try {
            String sql = "INSERT INTO [dbo].[Creator]\n"
                    + "           ([creatorName]\n"
                    + "           ,[creatorRole])\n"
                    + "     VALUES\n"
                    + "           (?,?)";
            Object[] params = {newCreator.getCreatorName(), newCreator.getCreatorRole()};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException e) {
            return false;
        }
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

    public boolean assignCreatorsToProduct(int productID, int creatorID) {
        try {
            String sql = "INSERT INTO [dbo].[Product_Creator]\n"
                    + "           ([productID]\n"
                    + "           ,[creatorID])\n"
                    + "     VALUES\n"
                    + "           (?,?)";
            Object[] params = {productID, creatorID};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean assignGenresToBook(int bookID, int genreID) {
        try {
            String sql = "INSERT INTO [dbo].[Book_Genre]\n"
                    + "           ([bookID]\n"
                    + "           ,[genreID])\n"
                    + "     VALUES\n"
                    + "           (?,?)";
            Object[] params = {bookID, genreID};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException ex) {
            return false;
        }
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

    public boolean addNewPublishers(Publisher newPublisher) {
        try {
            String sql = "INSERT INTO [dbo].[Publisher]\n"
                    + "           ([publisherName])\n"
                    + "     VALUES\n"
                    + "           (?)";
            Object[] params = {newPublisher.getPublisherName()};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException ex) {
            return false;
        }
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

    public boolean addNewMerchSeries(Series newSeries) {
        try {
            String sql = "INSERT INTO [dbo].[Series]\n"
                    + "           ([seriesName])\n"
                    + "     VALUES\n"
                    + "           (?)";
            Object[] params = {newSeries.getSeriesName()};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean addNewMerchBrand(Brand newBrand) {
        try {
            String sql = "INSERT INTO [dbo].[Brand]\n"
                    + "           ([brandName])\n"
                    + "     VALUES\n"
                    + "           (?)";
            Object[] params = {newBrand.getBrandName()};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean addNewMerchCharacter(OGCharacter newCharacter) {
        try {
            String sql = "INSERT INTO [dbo].[Character]\n"
                    + "           ([characterName])\n"
                    + "     VALUES\n"
                    + "           (?)";
            Object[] params = {newCharacter.getCharacterName()};
            return context.exeNonQuery(sql, params) > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean updateBooks(Book updatedBook) throws SQLException {
        StringBuilder sql = new StringBuilder("update book set\n");
        List<Object> paramList = new ArrayList<>();
        if(updatedBook.getPublisher() != null){
            sql.append("publisherID = ?,\n");
            paramList.add(updatedBook.getPublisher().getPublisherID());
        }
        sql.append("duration = ? where bookID = ?\n");
        paramList.add(updatedBook.getDuration());
        paramList.add(updatedBook.getProductID());
        Object[] params =paramList.toArray();
        return context.exeNonQuery(sql.toString(), params) > 0;
    }

    public boolean updateMerch(Merchandise updatedMerch) throws SQLException {
        List<Object> paramList  = new ArrayList<>();
        StringBuilder sql = new StringBuilder("UPDATE [dbo].[Merchandise] SET\n");
        
        if(updatedMerch.getSeries() != null){
            sql.append("[seriesID] = ?,\n");
            paramList.add(updatedMerch.getSeries().getSeriesID());
        }
        
        if(updatedMerch.getCharacter() != null){
            sql.append("[characterID] = ?,\n");
            paramList.add(updatedMerch.getCharacter().getCharacterID());
        }
        
        if(updatedMerch.getBrand() != null){
            sql.append("[brandID] = ?,\n");
            paramList.add(updatedMerch.getBrand().getBrandID());
        }
        
        sql.append("[size] = ?, [scaleLevel] = ?, [material] = ? WHERE [merchandiseID] = ?\n");
        paramList.add(updatedMerch.getSize());
        paramList.add(updatedMerch.getScaleLevel());
        paramList.add(updatedMerch.getMaterial());
        paramList.add(updatedMerch.getProductID());

        Object[] params = paramList.toArray();

        return context.exeNonQuery(sql.toString(), params) > 0;
    }

    
    public boolean updateProducts(Product updatedProduct) {
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        //WIP
        return true;
    }

    public boolean changeProductStatus(int productID, boolean newStatus) throws SQLException {
        String sql = "UPDATE Product SET isActive = ? WHERE productID = ?";
        Object[] params = {newStatus, productID};
        return context.exeNonQuery(sql, params) > 0;
    }
    


    public static void main(String[] args) {
        try {
            ProductDAO productDAO = new ProductDAO();
            System.out.println(productDAO.getPublisherIDByName(""));
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
