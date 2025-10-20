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
import model.product_related.Genre;
import utils.DBContext;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class GenreDAO implements IProductExtraAttributesDAO<Genre, Integer>, IClassificationEntityDAO {

    private static final GenreDAO instance = new GenreDAO();
    private final DBContext context;

    private GenreDAO() {
        context = DBContext.getInstance();
    }

    public static GenreDAO getInstance() {
        return instance;
    }

    @Override
    public List<Genre> getExtraAttributesByProductID(Integer bookID) throws SQLException {
        String sql = "SELECT BG.genreID, G.genreName\n"
                + "FROM Book_Genre AS BG\n"
                + "JOIN Genre AS G ON BG.genreID = G.genreID\n"
                + "WHERE BG.bookID = ?";
        int id = bookID;
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            List<Genre> genreList = new ArrayList<>();
            while (rs.next()) {
                genreList.add(new Genre(rs.getInt(1), rs.getString(2)));
            }
            return genreList;
        }
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
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
            Map<IProductClassification, Integer> genreMap = new HashMap<>();
            while (rs.next()) {
                genreMap.put(new Genre(rs.getInt("genreID"), rs.getString("genreName")), rs.getInt("productCount"));
            }
            return genreMap;
        }

    }

    @Override
    public Genre getById(int id) throws SQLException {
        String sql = "select * from Genre where genreID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new Genre(rs.getInt("genreID"), rs.getString("genreName"));
            }
        }
        return null;
    }

}
