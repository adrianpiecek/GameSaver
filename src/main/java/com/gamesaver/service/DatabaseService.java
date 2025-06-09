package com.gamesaver.service;

import com.gamesaver.db.DatabaseHelper;
import com.gamesaver.model.GameDeal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private static DatabaseService instance;
    private final DatabaseHelper dbHelper;

    private DatabaseService() {
        dbHelper = DatabaseHelper.getInstance();
    }

    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public List<GameDeal> getAllWishlistDeals() {
        List<GameDeal> deals = new ArrayList<>();
        String sql = "SELECT title, store_id, savings, price, deal_id FROM wishlist";

        // pobierz connection, ale NIE w try-with-resources!
        Connection conn = dbHelper.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                GameDeal deal = new GameDeal(
                        rs.getString("title"),
                        rs.getString("store_id"),
                        rs.getString("savings"),
                        rs.getString("price"),
                        rs.getString("deal_id")
                );
                deals.add(deal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deals;
    }

    public void addDealToWishlist(GameDeal deal) {
        dbHelper.insertDeal(
                deal.getTitle(),
                deal.getStoreId(),
                deal.getSavings(),
                deal.getPrice(),
                deal.getDealID()
        );
    }

    public void deleteDealById(String dealID) {
        dbHelper.deleteDealById(dealID);
    }

}
