package com.gamesaver.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDeal {
    @JsonProperty("storeID")
    private String storeId;

    @JsonProperty("savings")
    private String savings;

    @JsonProperty("salePrice")
    private String price;

    @JsonProperty("title")
    private String title;

    public GameDeal() {}

    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }

    public String getSavings() { return savings; }
    public void setSavings(String savings) { this.savings = savings; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

}
