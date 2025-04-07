package com.gamesaver.db;

import java.sql.*;

public class WishlistRepository {
    private Connection conn;

    public WishlistRepository() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:wishlist.db");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS wishlist (title TEXT, store TEXT, price REAL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItem(String title, String store, double price) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO wishlist VALUES (?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, store);
            ps.setDouble(3, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
