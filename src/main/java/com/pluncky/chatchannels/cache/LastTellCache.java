package com.pluncky.chatchannels.cache;

import java.util.HashMap;

public class LastTellCache {
    private final HashMap<String, String> lastTellMap = new HashMap<>();

    public void setLastTell(String sender, String receiver) {
        lastTellMap.put(receiver, sender);
    }

    public String getLastTell(String receiver) {
        return lastTellMap.get(receiver);
    }

    public boolean hasLastTell(String receiver) {
        return lastTellMap.containsKey(receiver);
    }
}
