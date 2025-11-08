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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
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
public class BookDAO extends ProductDAO implements ISpecificProductDAO {

    private static final BookDAO instance = new BookDAO();

    private BookDAO() {
    }

    public static BookDAO getInstance() {
        return instance;
    }

    @Override
    protected Book mapResultSetToProduct(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("categoryID"), rs.getString("categoryName"));

        LocalDate eventEndDate = Utility.getLocalDate(rs.getDate("eventDateStarted"), rs.getInt("eventDuration"));
        int discountPercentage = 0;
        if (eventEndDate != null) {
            discountPercentage = LocalDate.now().isAfter(eventEndDate) ? 0 : rs.getInt("discountPercentage");
        }

        LocalDate rlsDate = Utility.getLocalDate(rs.getDate("releaseDate"), 0);

        LocalDateTime lastMdfTime = Utility.getLocalDateTime(rs.getTimestamp("lastModifiedTime"));

        Publisher publisher = new Publisher(rs.getInt("publisherID"), rs.getString("publisherName"));
        return new Book(publisher, rs.getString("bookDuration"),
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
    public Book getProductById(int productID, boolean isManagement) throws SQLException {

        StringBuilder sql = getCTETables("rank").append("SELECT\n"
                + "P.*, C.categoryName, B.publisherID, B.bookDuration,\n"
                + "Pub.publisherName, PD.discountPercentage,PD.eventDateStarted,PD.eventDuration,TS.salesRank\n"
                + "FROM Product AS P\n"
                + "JOIN Book AS B ON P.productID = B.bookID\n"
                + "LEFT JOIN TopSale TS ON TS.productID = P.productID\n"
                + "LEFT JOIN ProductDiscount PD ON P.productID = PD.productID AND PD.rn = 1\n"
                + "LEFT JOIN Category AS C ON P.categoryID = C.categoryID\n"
                + "LEFT JOIN Publisher AS Pub ON B.publisherID = Pub.publisherID\n"
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
                return this.mapResultSetToProduct(rs);
            }
        }
        return null;
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        if (product instanceof Book == false) {
            return false;
        }
        Book updatedProduct = (Book) product;
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
                throw new SQLException("Failed to update this book!");
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
                        throw new SQLException(String.format("Error assigning creators to book {bookID : %d}", productID));
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

            Set<Integer> associatedGenreIDs = new LinkedHashSet<>();
            boolean deleteAllGenreAssociations = false;

            List<Genre> genreList = updatedProduct.getGenreList();
            if (!genreList.isEmpty()) {
                int associationCount = 0;

                for (Genre genre : genreList) {
                    int genreID = genre.getGenreID();
                    String genreName = genre.getGenreName();

                    if (genreID == 0 && genreName.trim().endsWith("deleteAll")) {
                        deleteAllGenreAssociations = true;
                        break;
                    }

                    if (genreID > 0 && genreName.trim().endsWith("associated")) {
                        associatedGenreIDs.add(genreID);
                        continue;
                    }

                    if (genreID > 0 && !associatedGenreIDs.contains(genreID)) {
                        associationCount++;
                        paramList.add(productID);
                        paramList.add(genreID);
                        associatedGenreIDs.add(genreID);
                    }
                }

                if (associationCount > 0) {
                    sqlBuilder.append("INSERT INTO [dbo].[Book_Genre]\n"
                            + "           ([bookID]\n"
                            + "           ,[genreID])\n"
                            + "     VALUES\n")
                            .append(String.join(",", Collections.nCopies(associationCount, "(?,?)")));

                    if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                        throw new SQLException(String.format("Error assigning genres to book {bookID : %d}", productID));
                    }

                    sqlBuilder.setLength(0);
                    paramList.clear();
                }

                sqlBuilder.append("DELETE FROM Book_Genre\n")
                        .append("WHERE bookID = ?\n");
                paramList.add(productID);

                if (!deleteAllGenreAssociations) {
                    sqlBuilder.append("AND genreID NOT IN (")
                            .append(String.join(",", Collections.nCopies(associatedGenreIDs.size(), "?")))
                            .append(")\n");
                    paramList.addAll(associatedGenreIDs);
                }

                context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false);

                sqlBuilder.setLength(0);
                paramList.clear();

            }

            Publisher publisher = updatedProduct.getPublisher();
            sqlBuilder.append("UPDATE Book SET\n");
            if (publisher != null && publisher.getPublisherID() > 0) {
                sqlBuilder.append("publisherID = ?,\n");
                paramList.add(publisher.getPublisherID());
            }
            sqlBuilder.append("bookDuration = ? WHERE bookID = ?\n");
            paramList.add(updatedProduct.getDuration());
            paramList.add(productID);

            //Update Book
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
        if (product instanceof Book == false) {
            return false;
        }

        Book newProduct = (Book) product;
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
                throw new SQLException("Failed to add this book!");
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
                    throw new SQLException(String.format("Error assigning creators to book {bookID : %d}", insertedProductID));
                }

                sqlBuilder.setLength(0);
                paramList.clear();
            }

            List<Genre> genreList = newProduct.getGenreList();
            if (!genreList.isEmpty()) {
                String placeHolder = String.join(",", Collections.nCopies(genreList.size(), "(?,?)"));
                sqlBuilder.append("INSERT INTO [dbo].[Book_Genre]\n"
                        + "           ([bookID]\n"
                        + "           ,[genreID])\n"
                        + "     VALUES\n").append(placeHolder);

                genreList.forEach(genre -> {
                    int genreID = genre.getGenreID();
                    if (genreID > 0) {
                        paramList.add(insertedProductID);
                        paramList.add(genreID);
                    }
                });
                if (context.exeNonQuery(connection, sqlBuilder.toString(), paramList.toArray(), false) == 0) {
                    throw new SQLException(String.format("Error assigning genres to book {bookID : %d}", insertedProductID));
                }

                sqlBuilder.setLength(0);
                paramList.clear();
            }
            Publisher publisher = newProduct.getPublisher();
            sqlBuilder.append("UPDATE Book SET\n");
            if (publisher != null && publisher.getPublisherID() > 0) {
                sqlBuilder.append("publisherID = ?,\n");
                paramList.add(publisher.getPublisherID());
            }
            sqlBuilder.append("bookDuration = ? WHERE bookID = ?\n");
            paramList.add(newProduct.getDuration());
            paramList.add(insertedProductID);

            //Update Book
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
            System.out.println(BookDAO.getInstance().getProductById(5, true));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
