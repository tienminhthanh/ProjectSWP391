package utils;

/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anhkc
 */
public class DBContext {

    private static final Logger LOGGER = LoggingConfig.getLogger(DBContext.class);
    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=WIBOOKS;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "characterEncoding=UTF-8";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public DBContext() {
        try {
            Class.forName(DRIVER);
            LOGGER.info("SQL Server driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to load SQL Server driver", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            LOGGER.info("Connected successfully!");
            return connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw e;
        }
    }

    public ResultSet exeQuery(String query, Object[] params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        return preparedStatement.executeQuery();
    }

    // Execute INSERT, UPDATE, DELETE
    public int exeNonQuery(String query, Object[] params) throws SQLException {
        try ( Connection connection = getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof String) {
                        preparedStatement.setString(i + 1, (String) params[i]);
                    } else {
                        preparedStatement.setObject(i + 1, params[i]);

                    }
                }
            }
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info(() -> String.format("Query affected %d rows: %s", rowsAffected, query));
            return rowsAffected;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Non-query execution failed: " + query, ex);
            throw ex;
        }
    }

    //Overload
    public int exeNonQuery(Connection connection, String sql, Object[] params, boolean returnGeneratedKeys) throws SQLException {
        try ( PreparedStatement preparedStatement = returnGeneratedKeys 
                ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                : connection.prepareStatement(sql)) {
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof String) {
                        preparedStatement.setString(i + 1, (String) params[i]);
                    } else {
                        preparedStatement.setObject(i + 1, params[i]);

                    }
                }
            }
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, String.format("Query affected %d rows: %s", rowsAffected, sql));

            //Return generated PK for INSERT STATEMENT if any
            if (rowsAffected > 0 && returnGeneratedKeys) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1) > 0 ? rs.getInt(1) : rowsAffected;
                }
            }

            return rowsAffected;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Non-query execution failed: " + sql, ex);
            throw ex;
        }
    }

    // Overload exeQuery with provided Connection
    public ResultSet exeQuery(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        try {
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Query execution failed: {0}", e);
            throw e;
        }
    }
}
