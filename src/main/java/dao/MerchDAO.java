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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import model.Book;
import model.Brand;
import model.Category;
import model.Creator;
import model.Genre;
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
        if (product instanceof Merchandise == false) {
            return false;
        }
        Merchandise updatedProduct = (Merchandise) product;
        int productID = updatedProduct.getProductID();
        Connection connection = null;
        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);

            StringBuilder sqlBuilder = new StringBuilder();
            List<Object> paramList = new ArrayList<>();

            sqlBuilder.append("UPDATE Product SET categoryID = ?, adminID = ?, keywords = ?, generalCategory = ?, "
                    + "productIsActive = ?, imageURL = ?, description = ?, releaseDate = ?, specialFilter = ?, "
                    + "productName = ?, price = ?, stockCount = ? "
                    + "WHERE productID = ?");
            paramList.add(updatedProduct.getSpecificCategory().getCategoryID());
            paramList.add(updatedProduct.getAdminID());
            paramList.add(updatedProduct.getKeywords());
            paramList.add(updatedProduct.getGeneralCategory());
            paramList.add(updatedProduct.isIsActive());
            paramList.add(updatedProduct.getImageURL());
            paramList.add(updatedProduct.getDescription());
            paramList.add(java.sql.Date.valueOf(updatedProduct.getReleaseDate()));
            paramList.add(updatedProduct.getSpecialFilter());
            paramList.add(updatedProduct.getProductName());
            paramList.add(updatedProduct.getPrice());
            paramList.add(updatedProduct.getStockCount());
            paramList.add(productID);

            boolean updateSuccess = context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) > 0;
            //Update failed
            if (!updateSuccess) {
                throw new SQLException("Failed to update this merchandise!");
            }

            sqlBuilder.setLength(0);
            paramList.clear();

            Set<Integer> associatedCreatorIDs = new LinkedHashSet<>();
            boolean deleteAllCreatorAssociations = false;

            List<Creator> creatorList = updatedProduct.getCreatorList();
            if (!creatorList.isEmpty()) {
                int associationCount = 0;

                for (Creator creator : creatorList) {
                    int creatorID = creator.getCreatorID();
                    String creatorName = creator.getCreatorName();

                    if (creatorID == 0 && creatorName.trim().endsWith("deleteAll")) {
                        deleteAllCreatorAssociations = true;
                        break;
                    }

                    if (creatorID > 0 && creatorName.trim().endsWith("associated")) {
                        associatedCreatorIDs.add(creatorID);
                        continue;
                    }

                    if (creatorID > 0 && !associatedCreatorIDs.contains(creatorID)) {
                        associationCount++;
                        paramList.add(productID);
                        paramList.add(creatorID);
                        associatedCreatorIDs.add(creatorID);
                    }
                }

                if (associationCount > 0) {
                    sqlBuilder.append("INSERT INTO [dbo].[Product_Creator]\n"
                            + "           ([productID]\n"
                            + "           ,[creatorID])\n"
                            + "     VALUES\n");
                    sqlBuilder.append(String.join(",", Collections.nCopies(associationCount, "(?,?)")));

                    if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                        throw new SQLException(String.format("Error assigning creators to merchandise {merchandiseID : %d}", productID));
                    }

                    sqlBuilder.setLength(0);
                    paramList.clear();
                }
                
                sqlBuilder.append("DELETE FROM Product_Creator\n")
                        .append("WHERE productID = ?\n");
                paramList.add(productID);

                if (!deleteAllCreatorAssociations) {
                    sqlBuilder.append("AND creatorID NOT IN (")
                            .append(String.join(",", Collections.nCopies(associatedCreatorIDs.size(), "?")))
                            .append(")\n");
                    paramList.addAll(associatedCreatorIDs);
                }

                context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false);

                sqlBuilder.setLength(0);
                paramList.clear();

            }

            Series series = updatedProduct.getSeries();
            OGCharacter character = updatedProduct.getCharacter();
            Brand brand = updatedProduct.getBrand();

            sqlBuilder.append("UPDATE [dbo].[Merchandise] SET\n");

            if (series != null && series.getSeriesID() > 0) {
                sqlBuilder.append("[seriesID] = ?,\n");
                paramList.add(series.getSeriesID());
            }

            if (character != null && character.getCharacterID() > 0) {
                sqlBuilder.append("[characterID] = ?,\n");
                paramList.add(character.getCharacterID());
            }

            if (brand != null && brand.getBrandID() > 0) {
                sqlBuilder.append("[brandID] = ?,\n");
                paramList.add(brand.getBrandID());
            }

            sqlBuilder.append("[size] = ?, [scaleLevel] = ?, [material] = ? WHERE [merchandiseID] = ?\n");
            paramList.add(updatedProduct.getSize());
            paramList.add(updatedProduct.getScaleLevel());
            paramList.add(updatedProduct.getMaterial());
            paramList.add(productID);

            //Update Merch
            if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                    connection.close();
                } catch (SQLException e) {
                    throw e;
                }
            }

        }
    }

    @Override
    public boolean addNewProduct(Product product) throws SQLException {
        if (product instanceof Merchandise == false) {
            return false;
        }

        Merchandise newProduct = (Merchandise) product;
        Connection connection = null;
        try {
            connection = context.getConnection();
            connection.setAutoCommit(false);

            StringBuilder sqlBuilder = new StringBuilder();
            List<Object> paramList = new ArrayList<>();

            sqlBuilder.append("INSERT INTO Product (categoryID, adminID, keywords, generalCategory, productIsActive, imageURL, description, releaseDate, specialFilter, productName, price, stockCount) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            paramList.add(newProduct.getSpecificCategory().getCategoryID());
            paramList.add(newProduct.getAdminID());
            paramList.add(newProduct.getKeywords());
            paramList.add(newProduct.getGeneralCategory());
            paramList.add(newProduct.isIsActive());
            paramList.add(newProduct.getImageURL());
            paramList.add(newProduct.getDescription());
            paramList.add(java.sql.Date.valueOf(newProduct.getReleaseDate()));
            paramList.add(newProduct.getSpecialFilter());
            paramList.add(newProduct.getProductName());
            paramList.add(newProduct.getPrice());
            paramList.add(newProduct.getStockCount());

            int insertedProductID = context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), true);
            //Insert failed
            if (insertedProductID == 0) {
                throw new SQLException("Failed to add this merchandise!");
            }

            newProduct.setProductID(insertedProductID);

            sqlBuilder.setLength(0);
            paramList.clear();

            List<Creator> creatorList = newProduct.getCreatorList();
            if (!creatorList.isEmpty()) {
                String placeHolder = String.join(",", Collections.nCopies(creatorList.size(), "(?,?)"));
                sqlBuilder.append("INSERT INTO [dbo].[Product_Creator]\n"
                        + "           ([productID]\n"
                        + "           ,[creatorID])\n"
                        + "     VALUES\n").append(placeHolder);

                creatorList.forEach(creator -> {
                    int creatorID = creator.getCreatorID();
                    if (creatorID > 0) {
                        paramList.add(insertedProductID);
                        paramList.add(creatorID);
                    }
                });
                if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                    throw new SQLException(String.format("Error assigning creators to merchandise {merchandiseID : %d}", insertedProductID));
                }

                sqlBuilder.setLength(0);
                paramList.clear();
            }

            Series series = newProduct.getSeries();
            OGCharacter character = newProduct.getCharacter();
            Brand brand = newProduct.getBrand();

            sqlBuilder.append("UPDATE [dbo].[Merchandise] SET\n");

            if (series != null && series.getSeriesID() > 0) {
                sqlBuilder.append("[seriesID] = ?,\n");
                paramList.add(series.getSeriesID());
            }

            if (character != null && character.getCharacterID() > 0) {
                sqlBuilder.append("[characterID] = ?,\n");
                paramList.add(character.getCharacterID());
            }

            if (brand != null && brand.getBrandID() > 0) {
                sqlBuilder.append("[brandID] = ?,\n");
                paramList.add(brand.getBrandID());
            }

            sqlBuilder.append("[size] = ?, [scaleLevel] = ?, [material] = ? WHERE [merchandiseID] = ?\n");
            paramList.add(newProduct.getSize());
            paramList.add(newProduct.getScaleLevel());
            paramList.add(newProduct.getMaterial());
            paramList.add(insertedProductID);

            //Update Merch
            if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore auto-commit
                    connection.close();
                } catch (SQLException e) {
                    throw e;
                }
            }

        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(MerchDAO.getInstance().getProductById(107, true));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
