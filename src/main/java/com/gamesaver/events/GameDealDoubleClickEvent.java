package com.gamesaver.events;

import com.gamesaver.model.GameDeal;

public class GameDealDoubleClickEvent {
    private final GameDeal deal;

    public GameDealDoubleClickEvent(GameDeal deal) {
        this.deal = deal;
    }

    public GameDeal getDeal() {
        return deal;
    }
}
