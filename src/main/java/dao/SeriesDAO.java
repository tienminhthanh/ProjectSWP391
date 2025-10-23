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
import model.Series;
import utils.DBContext;
import dao.interfaces.IClassificationEntityDAO;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class SeriesDAO implements IClassificationEntityDAO {

    private static final SeriesDAO instance = new SeriesDAO();
    private final DBContext context;

    private SeriesDAO() {
        context = DBContext.getInstance();
    }

    public static SeriesDAO getInstance() {
        return instance;
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
        String sql = "SELECT \n"
                + "    s.seriesID, \n"
                + "    s.seriesName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Series s\n"
                + "JOIN Merchandise m ON m.seriesID = s.seriesID\n"
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY s.seriesID, s.seriesName";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<IProductClassification, Integer> seriesMap = new HashMap<>();
            while (rs.next()) {
                seriesMap.put(new Series(rs.getInt("seriesID"), rs.getString("seriesName")), rs.getInt("productCount"));
            }
            return seriesMap;
        }
    }

    @Override
    public Series getById(int id) throws SQLException {
        String sql = "select * from Series where seriesID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Series(rs.getInt("seriesID"), rs.getString("seriesName"));
            }
        }
        return null;
    }

}
