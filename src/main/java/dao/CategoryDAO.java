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
import model.Category;
import utils.DBContext;
import dao.interfaces.IClassificationEntityDAO;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class CategoryDAO implements IClassificationEntityDAO {

    private static final CategoryDAO instance = new CategoryDAO();
    private final DBContext context;

    private CategoryDAO() {
        context = DBContext.getInstance();
    }

    public static CategoryDAO getInstance() {
        return instance;
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
        String sql = "SELECT c.*, COUNT(p.productID) AS productCount  \n"
                + "FROM Category AS c  \n"
                + "LEFT JOIN Product AS p  \n"
                + "    ON p.categoryID = c.categoryID AND p.productIsActive = 1  \n"
                + "GROUP BY c.categoryID, c.categoryName, c.generalCategory;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<IProductClassification, Integer> categoryMap = new HashMap<>();
            while (rs.next()) {
                categoryMap.put(new Category(rs.getInt("categoryID"), rs.getString("categoryName"), rs.getString("generalCategory")), rs.getInt("productCount"));
            }
            return categoryMap;
        }
    }

    @Override
    public Category getById(int id) throws SQLException {
        String sql = "SELECT *\n" + "FROM Category\n" + "WHERE categoryID = ?";
        Object[] params = {id};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Category(rs.getInt("categoryID"), rs.getString("categoryName"), rs.getString("generalCategory"));
            }
        }
        return null;
    }

}
