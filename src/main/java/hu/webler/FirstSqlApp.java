package hu.webler;

import hu.webler.bootstrap.DataLoader;
import hu.webler.config.DatabaseConnection;

import java.sql.Connection;

public class FirstSqlApp {

    public static void main(String[] args) {

        DataLoader loader = new DataLoader();

        // Initialize the database connection and create the database and table if they don't exist
        Connection connection = DatabaseConnection.getConnection();
        DatabaseConnection.createDatabaseAndTable(connection);

        // Load data
        loader.loadData();

        // Close the database connection when done (if necessary)
        DatabaseConnection.closeConnection();
    }
}