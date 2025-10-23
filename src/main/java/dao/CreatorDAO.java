/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.interfaces.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Creator;
import utils.DBContext;
import utils.Utility;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class CreatorDAO implements IProductExtraAttributesDAO<Creator, Integer>, IClassificationEntityDAO {

    private static final CreatorDAO instance = new CreatorDAO();
    private final DBContext context;

    private CreatorDAO() {
        context = DBContext.getInstance();
    }

    public static CreatorDAO getInstance() {
        return instance;
    }

    @Override
    public List<Creator> getExtraAttributesByProductID(Integer productID) throws SQLException {
        String sql = "SELECT PC.creatorID, C.creatorName, C.creatorRole\n"
                + "FROM Creator AS C\n"
                + "JOIN Product_Creator AS PC ON C.creatorID = PC.creatorID\n"
                + "WHERE PC.productID = ?";
        int id = productID;

        Object[] params = {id};
        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            List<Creator> creatorList = new ArrayList<>();
            while (rs.next()) {
                creatorList.add(new Creator(rs.getInt("creatorID"), rs.getString("creatorName"), rs.getString("creatorRole")));
            }
            return creatorList;
        }
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
        String sql = "SELECT \n"
                + "    c.creatorID, \n"
                + "    c.creatorName, \n"
                + "    c.creatorRole, \n"
                + "    p.generalCategory, \n"
                + "    COUNT(p.productID) AS productCount\n"
                + "FROM Creator c\n"
                + " JOIN Product_Creator pc \n"
                + "    ON c.creatorID = pc.creatorID\n"
                + "LEFT JOIN Product p\n"
                + "    ON pc.productID = p.productID \n"
                + "    AND p.productIsActive = 1  \n"
                + "GROUP BY c.creatorID, c.creatorName, c.creatorRole, p.generalCategory";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<IProductClassification, Integer> creatorMap = new HashMap<>();
            while (rs.next()) {

                creatorMap.put(new Creator(rs.getInt("creatorID"), rs.getString("creatorName"),
                        Utility.toTitleCase(rs.getString("creatorRole"))).setGeneralCategory(rs.getString("generalCategory")),
                        rs.getInt("productCount"));
            }
            return creatorMap;
        }

    }

    @Override
    public Creator getById(int id) throws SQLException {
        String sql = "SELECT top 1 Creator.creatorID, Creator.creatorName, Creator.creatorRole, Product.generalCategory\n"
                + "FROM     Creator INNER JOIN\n"
                + "                  Product_Creator ON Creator.creatorID = Product_Creator.creatorID INNER JOIN\n"
                + "                  Product ON Product_Creator.productID = Product.productID\n"
                + "WHERE  Product_Creator.creatorID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Creator(rs.getInt("creatorID"), rs.getString("creatorName"),
                        Utility.toTitleCase(rs.getString("creatorRole")), rs.getString("generalCategory"));
            }
        }
        return null;
    }

}
