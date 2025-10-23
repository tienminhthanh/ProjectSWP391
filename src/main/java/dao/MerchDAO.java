/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.interfaces.ISpecificProductDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import model.Book;
import model.Brand;
import model.Category;
import model.Merchandise;
import model.OGCharacter;
import model.Product;
import model.Publisher;
import model.Series;
import utils.Utility;

/**
 *
 * @author anhkc
 */
public class MerchDAO extends ProductDAO implements ISpecificProductDAO {

    private static final MerchDAO instance = new MerchDAO();

    private MerchDAO() {
    }

    public static MerchDAO getInstance() {
        return instance;
    }

    @Override
    protected Merchandise mapResultSetToProduct(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

        LocalDate eventEndDate = Utility.getLocalDate(rs.getDate("eventDateStarted"), rs.getInt("eventDuration"));
        int discountPercentage = 0;
        if (eventEndDate != null) {
            discountPercentage = LocalDate.now().isAfter(eventEndDate) ? 0 : rs.getInt("discountPercentage");
        }

        LocalDate rlsDate = Utility.getLocalDate(rs.getDate("releaseDate"), 0);

        LocalDateTime lastMdfTime = Utility.getLocalDateTime(rs.getTimestamp("lastModifiedTime"));

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
                eventEndDate,
                rs.getInt("salesRank"));
    }


    @Override
    public Merchandise getProductById(int productID, boolean isManagement) throws SQLException {
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
                return mapResultSetToProduct(rs);
            }
        }
        return null;
    }
    
   

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean addNewProduct(Product product) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void main(String[] args) {
        try {
            System.out.println(MerchDAO.getInstance().getProductById(107, true));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
