package com.tw.casino.simulator;

public abstract class EventInfo {

    public abstract int getTimestampInMS();
    
    // A unique id for this event.
    public abstract java.util.UUID getEventID();
    
    public abstract EventType getEventType();
    
    // Only meaningful for the JOIN_GAME event type. Otherwise returns null.
    // Indicates the game which the player want's to join by pointing to the event that
    // resulted in that game being established.
    public abstract java.util.UUID getEstablishNewGameEventId();
    
    // Only meaningful for the JOIN_GAME event type. Otherwise returns null.
    public abstract PlayerInfo getPlayer();
}
