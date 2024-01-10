package org.Atharv1;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    private static final String DB_DIRECTORY = "./user_databases/";
    private static Connection conn = null;

    // Private constructor to prevent instantiation
    private ConnectDB() {
    }

    private static void startConnection(String username) {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create a directory for user databases if it doesn't exist
            String userDbDirectory = DB_DIRECTORY + username + "/";
            createDirectory(userDbDirectory);

            // Check if the database file exists, if not, create it
            String dbFilePath = userDbDirectory + "expense.db";
            if (!fileExists(dbFilePath)) {
                createNewDatabase(dbFilePath);
            }

            String url = "jdbc:sqlite:" + dbFilePath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to user database!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to the user database: " + e.getMessage());
        }
    }

    private static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean directoryCreated = directory.mkdirs();
            if (directoryCreated) {
                System.out.println("User database directory created: " + directoryPath);
            }
        }
    }

    private static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private static void createNewDatabase(String dbFilePath) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username VARCHAR(100) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL);";

        try (Connection newDbConn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             Statement stmt = newDbConn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("New user database created: " + dbFilePath);
        } catch (SQLException e) {
            System.out.println("Error creating new user database: " + e.getMessage());
        }
    }

    public static Connection connect(String username) {
        ConnectDB connectDB = new ConnectDB();
         connectDB.startConnection(username);
        return connectDB.conn;
    }

    public static Statement createStatement(String username) throws SQLException {
        if (conn == null || conn.isClosed()) {
            startConnection(username);
        }
        return conn.createStatement();
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }
}
