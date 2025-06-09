package com.gamesaver.events;

import com.gamesaver.model.GameDeal;

public class GameDealDoubleClickEvent {
    private final GameDeal gameDeal;
    private final boolean fromWishlist; // true jeśli klik z wishlisty, false jeśli z wyszukiwania

    public GameDealDoubleClickEvent(GameDeal gameDeal, boolean fromWishlist) {
        this.gameDeal = gameDeal;
        this.fromWishlist = fromWishlist;
    }

    public GameDeal getDeal() {
        return gameDeal;
    }

    public boolean isFromWishlist() {
        return fromWishlist;
    }
}

