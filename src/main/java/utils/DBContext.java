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

        private static String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=WIBOOKS;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
        private static final String USERNAME = "sa";
        private static final String PASSWORD = "sa123456";

        protected Connection conn;

        public DBContext() {
            try {
                String url = "jdbc:sqlserver://localhost:1433;databaseName=WIBOOKS";
                String username = "sa";
                String password = "sa123456";
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e);
            }
        }

        public Connection getConnection() throws SQLException {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
            return DriverManager.getConnection(url, USERNAME, PASSWORD);
        }

        public void testConnection() {
            try {
                Connection connection = getConnection();
                if (connection != null) {
                    System.out.println("Connected successfully!");
                } else {
                    System.out.println("Connection failed!");
                }
                connection.close();
            } catch (SQLException e) {
            }
        }



        public ResultSet exeQuery(String query, Object[] params) throws SQLException {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            System.out.println("Connected successfully!");

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            return preparedStatement.executeQuery();
        }



        public int exeNonQuery(String query, Object[] params) throws SQLException {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            System.out.println("Connected successfully!");
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            return preparedStatement.executeUpdate();
        }

    }
