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
import model.OGCharacter;
import utils.DBContext;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
public class CharacterDAO implements IClassificationEntityDAO {

    private static final CharacterDAO instance = new CharacterDAO();
    private final DBContext context;

    private CharacterDAO() {
        context = DBContext.getInstance();
    }

    public static CharacterDAO getInstance() {
        return instance;
    }

    @Override
    public Map<IProductClassification, Integer> getAllClassficationEntitiesWithCount() throws SQLException {
        String sql = "SELECT \n"
                + "    ch.characterID, \n"
                + "    ch.characterName, \n"
                + "    COUNT(pr.productID) AS productCount\n"
                + "FROM Character ch\n"
                + "JOIN Merchandise m ON m.characterID = ch.characterID\n"
                + "LEFT JOIN Product pr ON m.merchandiseID = pr.productID AND pr.productIsActive = 1\n"
                + "GROUP BY ch.characterID, ch.characterName";

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), null)) {
            Map<IProductClassification, Integer> characterMap = new HashMap<>();
            while (rs.next()) {
                characterMap.put(new OGCharacter(rs.getInt("characterID"), rs.getString("characterName")), rs.getInt("productCount"));
            }
            return characterMap;
        }

    }

    @Override
    public OGCharacter getById(int id) throws SQLException {
        String sql = "select * from Character where characterID = ?";
        Object[] params = {id};

        try ( Connection connection = context.getConnection();  ResultSet rs = context.exeQuery(connection.prepareStatement(sql), params)) {
            if (rs.next()) {
                return new OGCharacter(rs.getInt("characterID"), rs.getString("characterName"));
            }
        }
        return null;
    }

}
