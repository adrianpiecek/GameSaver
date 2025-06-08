package com.gamesaver.events;

import com.google.common.eventbus.EventBus;

public class GlobalEventBus {
    private static final EventBus eventBus = new EventBus();

    public static EventBus getInstance() {
        return eventBus;
    }
}
