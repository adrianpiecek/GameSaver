package com.gamesaver.model;

public class GameDeal {
    private String store;
    private String savings;
    private String price;
    private String title;

    public GameDeal(String store, String savings, String price, String title) {
        this.store = store;
        this.savings = savings;
        this.price = price;
        this.title = title;
    }

    public String getStore() {
        return store;
    }
    public String getSavings() {
        return savings;
    }
    public String getPrice() {
        return price;
    }
    public String getTitle() {
        return title;
    }

    public void setStore(String store) {
        this.store = store;
    }
    public void setSavings(String savings) {
        this.savings = savings;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
