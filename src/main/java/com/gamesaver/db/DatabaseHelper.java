package com.gamesaver.db;

import java.sql.*;

public class DatabaseHelper {

    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {
        try {
            // np. SQLite
            connection = DriverManager.getConnection("jdbc:sqlite:data/gamesaver.db");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private void initializeDatabase() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS wishlist (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                store_id TEXT,
                savings TEXT,
                price TEXT,
                deal_id TEXT UNIQUE
            )
            """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void insertDeal(String title, String store_id, String savings, String price, String deal_id) {
        String sql = "INSERT OR IGNORE INTO wishlist (title, store_id, savings, price, deal_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, store_id);
            pstmt.setString(3, savings);
            pstmt.setString(4, price);
            pstmt.setString(5, deal_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDealById(String dealId) {
            String sql = "DELETE FROM wishlist WHERE deal_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, dealId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
