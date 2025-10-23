/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.interfaces.IClassificationEntityDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import model.Publisher;
import utils.DBContext;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class PublisherDAO implements IClassificationEntityDAO {

    private static final PublisherDAO instance = new PublisherDAO();
    private final DBContext context;

    private PublisherDAO() {
        context = DBContext.getInstance();
    }

    public static PublisherDAO getInstance() {
        return instance;
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
        String sql = "SELECT \n"
                + "    p.publisherID, \n"
                + "    p.publisherName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Publisher p\n"
                + "JOIN Book b ON p.publisherID = b.publisherID\n"
                + "LEFT JOIN Product pr ON b.bookID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY p.publisherID, p.publisherName;";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<IProductClassification, Integer> publisherMap = new HashMap<>();
            while (rs.next()) {
                publisherMap.put(new Publisher(rs.getInt("publisherID"), rs.getString("publisherName")), rs.getInt("productCount"));
            }
            return publisherMap;
        }
    }

    @Override
    public Publisher getById(int id) throws SQLException {
        String sql = "select * from Publisher where publisherID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));
            }
        }
        return null;
    }

}
