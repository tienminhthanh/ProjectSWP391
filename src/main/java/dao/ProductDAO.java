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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        StringBuilder sql = getCTEProductDiscount().append("SELECT P.*, \n"
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
            return mapResultSetToProduct(rs, "");

        }

        return null;
    }

    /**
     * Caller method for get[SpecializeProduct]ByID
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
        StringBuilder sql = getCTEProductDiscount(new String[]{"rank"}).append("SELECT\n"
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

        StringBuilder sql = getCTEProductDiscount(new String[]{"rank"}).append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.duration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.dateStarted,PD.eventDuration,TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN TopSale TS ON TS.productID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
                + "WHERE P.isActive = 1 AND P.productID = ?");

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

    public List<Product> get10RandomActiveProducts(String type) throws SQLException {

        StringBuilder sql = getCTEProductDiscount();
        if (type.equals("book")) {
            sql.append("SELECT TOP 10 P.*, \n"
                    + "       C.categoryName, \n"
                    + "       PD.discountPercentage, \n"
                    + "       PD.dateStarted,\n"
                    + "	   PD.eventDuration\n"
                    + "FROM Product AS P\n"
                    + "JOIN Book B \n"
                    + "    ON B.bookID = P.productID\n"
                    + "LEFT JOIN ProductDiscount PD \n"
                    + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                    + "LEFT JOIN Category AS C \n"
                    + "    ON C.categoryID = P.categoryID\n"
                    + "WHERE P.isActive = 1\n"
                    + "ORDER BY NEWID()");
        } else if (type.equals("merch")) {
            sql.append("SELECT TOP 10 P.*, \n"
                    + "       C.categoryName, \n"
                    + "       PD.discountPercentage, \n"
                    + "       PD.dateStarted,\n"
                    + "	   PD.eventDuration\n"
                    + "FROM Product AS P\n"
                    + "JOIN Merchandise M \n"
                    + "    ON M.merchandiseID = P.productID\n"
                    + "LEFT JOIN ProductDiscount PD \n"
                    + "    ON P.productID = PD.productID AND PD.rn = 1\n"
                    + "LEFT JOIN Category AS C \n"
                    + "    ON C.categoryID = P.categoryID\n"
                    + "WHERE P.isActive = 1\n"
                    + "ORDER BY NEWID()");
        }
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), null)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, ""));
            }
            return productList;
        }

    }

    /**
     * When user does not enter anything in the search bar
     *
     * @param type
     * @param sortCriteria
     * @return
     * @throws SQLException
     */
    public List<Product> getAllActiveProducts(String type, String sortCriteria, Map<String, String> filterMap) throws SQLException {
//        Prepare the query with CTE first
        StringBuilder sql = getCTEProductDiscount();

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
        if (!filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(getFilterClause(filterOption, selectedFilters.length));

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
        sql.append(getSortOrder(sortCriteria));

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, ""));
            }
            return productList;
        }

    }

    public List<Product> getSearchResult(String query, String type, String sortCriteria, Map<String, String> filterMap) throws SQLException {

        //Prepare the query with CTE first
        StringBuilder sql = getCTEProductDiscount();

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
        paramList.add(formatQuery(query));

        //Append filter
        if (!filterMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                String filterOption = entry.getKey();
                String filterParam = entry.getValue();

                //Split the filter params if there are more than 1
                String[] selectedFilters = filterParam != null && !filterParam.trim().isEmpty() ? filterParam.split(",") : new String[0];

                //Append filter clause based on filterOption
                sql.append(getFilterClause(filterOption, selectedFilters.length));

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
        sql.append(getSortOrder(sortCriteria));

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, ""));
            }
            return productList;
        }
    }

    private String formatQuery(String query) {
        String[] queryParts = query.split("\\s+");
        for (int i = 0; i < queryParts.length; i++) {
            queryParts[i] = "\"" + queryParts[i] + "*\"";
        }

        return String.join(" OR ", queryParts);
    }

    private String getSortOrder(String sortCriteria) {
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
            default:
                return "P.releaseDate DESC";

        }
    }

    public List<Product> getProductsByCondition(int conditionID, String sortCriteria, Map<String, String> filterMap, String condition, String generalCategory, String location) throws SQLException {
        StringBuilder sql = getCTEProductDiscount();
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
                sql.append(getFilterClause(filterOption, selectedFilters.length));

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
        sql.append(getSortOrder(sortCriteria));

        //Execute query
        Object[] params = paramList.toArray();
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), params)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, ""));
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

    private String getFilterClause(String filterOption, int filterCount) {
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
        StringBuilder sql = getCTEProductDiscount(new String[]{"rank"}).append("SELECT P.*,\n"
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

        //Execute query
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql.toString()), null)) {
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs, "").setSalesRank(rs.getInt("salesRank")));
            }
            return productList;

        }
    }

    private StringBuilder getCTEProductDiscount(String... condition) {
        StringBuilder cte = new StringBuilder("WITH ");
        if (condition.length > 0 && condition[0].equals("rank")) {
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
        String sql = "SELECT top 1 Category.categoryID, Category.categoryName, Product.generalCategory\n"
                + "FROM     Product INNER JOIN\n"
                + "                  Category ON Product.categoryID = Category.categoryID\n"
                + "				  where Product.categoryID = ?";
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
                + "GROUP BY c.categoryID, c.categoryName;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<Category, Integer> categoryMap = new HashMap<>();
            while (rs.next()) {
                categoryMap.put(new Category(rs.getInt("categoryID"), rs.getString("categoryName")), rs.getInt("productCount"));
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

    public boolean addNewProducts(Product newProduct) throws SQLException {
        String sql = "";
        Object[] params = {newProduct};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean addNewCreators(Creator newCreator) throws SQLException {
        String sql = "";
        Object[] params = {newCreator};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean assignCreatorsToProduct(int creatorID, int productID) throws SQLException {
        String sql = "";
        Object[] params = {productID, creatorID};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean assignGenresToBook(int genreID, int bookID) throws SQLException {
        String sql = "";
        Object[] params = {bookID, genreID};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean addNewMerchSeries(Series newSeries) throws SQLException {
        String sql = "";
        Object[] params = {newSeries};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean addNewMerchBrand(Brand newBrand) throws SQLException {
        String sql = "";
        Object[] params = {newBrand};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean addNewMerchCharacter(OGCharacter newCharacter) throws SQLException {
        String sql = "";
        Object[] params = {newCharacter};
        return context.exeNonQuery(sql, params) > 0;
    }

    public boolean updateProducts(Product updatedProduct, String type) throws SQLException {
        String sql = generateUpdateStatementBasedOnType(type);
        Object[] params = {updatedProduct};
        return context.exeNonQuery(sql, params) > 0;
    }

    private String generateUpdateStatementBasedOnType(String type) {
        switch (type) {
            case "book":
                return "UPDATE Book...";
            case "merch":
                return "UPDATE Merchandise....";
            default:
                return "UPDATE Product...";
        }
    }

    public boolean changeProductStatus(int productID, boolean newStatus) throws SQLException {
        String sql = "";
        Object[] params = {productID, newStatus};
        return context.exeNonQuery(sql, params) > 0;
    }

    public static void main(String[] args) {
        String sql = "SELECT \n"
                + "    p.publisherID, \n"
                + "    p.publisherName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Publisher p\n"
                + "JOIN Book b ON p.publisherID = b.publisherID\n"
                + "LEFT JOIN Product pr ON b.bookID = pr.productID AND pr.isActive = 1\n"
                + "GROUP BY p.publisherID, p.publisherName;";
        System.out.println(sql);
    }

}
