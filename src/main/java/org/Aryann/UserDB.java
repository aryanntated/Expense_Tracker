package org.Atharv1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDB {
    private static final Statement stat;

    static {
        try {
            // Assuming the username is obtained or stored somewhere in your application
            String username = ""; // Set the actual username here
            stat = ConnectDB.connect(username).createStatement();
            stat.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
                    );
                    """);

            stat.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS expenses (
                        expense_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER,
                        name VARCHAR(100),
                        description VARCHAR(255),
                        amount DECIMAL(10, 2),
                        category VARCHAR(50),
                        date DATE,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserDB() {
        // Private constructor to prevent instantiation
    }

    public static boolean isValidUser(String username, String password) {
        String selectSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = ConnectDB.connect(username).prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertUser(String username, String password) {
        // Check if the username already exists
        if (isUsernameTaken(username)) {  
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        // Insert the new user
        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = ConnectDB.connect(username).prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("User registered!");
    }

    private static boolean isUsernameTaken(String username) {
        String selectSQL = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
        try (PreparedStatement pstmt = ConnectDB.connect(username).prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getUserId(String username, String password) {
        String selectSQL = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = ConnectDB.connect(username).prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("user_id") : -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
