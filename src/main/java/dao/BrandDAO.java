/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import model.Brand;
import model.Genre;
import model.OGCharacter;
import model.Publisher;
import model.Series;
import utils.DBContext;
import dao.interfaces.IClassificationEntityDAO;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class BrandDAO implements IClassificationEntityDAO {

    private static final BrandDAO instance = new BrandDAO();
    private final DBContext context;

    private BrandDAO() {
        context = DBContext.getInstance();
    }

    public static BrandDAO getInstance() {
        return instance;
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
        String sql = "SELECT \n"
                + "    br.brandID, \n"
                + "    br.brandName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Brand br\n"
                + "JOIN Merchandise m ON m.brandID = br.brandID\n"
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY br.brandID, br.brandName";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<IProductClassification, Integer> brandMap = new HashMap<>();
            while (rs.next()) {
                brandMap.put(new Brand(rs.getInt("brandID"), rs.getString("brandName")), rs.getInt("productCount"));
            }
            return brandMap;
        }
    }

    @Override
    public Brand getById(int id) throws SQLException {
        String sql = "select * from Brand where brandID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Brand(rs.getInt("brandID"), rs.getString("brandName"));
            }
        }
        return null;
    }

}
