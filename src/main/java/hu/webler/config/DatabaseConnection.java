package hu.webler.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Using Singleton pattern to create a single database connection
public class DatabaseConnection {

    private static Connection connection;
    // "jdbc:mysql://localhost:3306/first_database_with_java?createDatabaseIfNotExist=true&useSSL=true&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false"
    // perfect if port and database name is coming from Environment variables or config file!
    // általában egy konfig file lehet: valami.properties pl. appication.properties vagy yaml vagy json is!

    // This in case if url is from config file!
    private static final String JDBC_URL = DatabaseConfig.getJdbcUrl();
    // your user name -> root (default)

    // This in case if all is from Environment variables!
    // private static final String JDBC_URL = "jdbc:mysql://localhost:" + System.getenv("DB_PORT_MYSQL") + "/" + System.getenv("DB_URL_MYSQL") + "?createDatabaseIfNotExist=true&useSSL=true&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";

    private static final String USERNAME = System.getenv("DB_USER_MYSQL");
    // your password -> we set it up in MySQL Server!!!
    private static final String PASSWORD = System.getenv("DB_PASSWORD_MYSQL");

    // Method to get the database connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                createDatabaseAndTable(connection);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Method to close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createDatabaseAndTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS webler_first_database_with_java");
            statement.executeUpdate("USE webler_first_database_with_java");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," + // this made it creating autoincrement id!
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," + // add constraint to email -> UNIQUE
                    "first_name VARCHAR(50) NOT NULL," +
                    "last_name VARCHAR(50) NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Private constructor to prevent instantiation from other classes
    private DatabaseConnection() {

    }
}