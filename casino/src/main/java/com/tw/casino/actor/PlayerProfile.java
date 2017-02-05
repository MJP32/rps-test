package com.tw.casino.actor;

import java.util.UUID;

import com.tw.casino.game.GameStrategy;

public class PlayerProfile
{
    private final UUID playerId;
    private final double entryFee;

    private final GameStrategy gameStrategy;
    
    public PlayerProfile(UUID playerId, double entryFee, GameStrategy gameStrategy)
    {
        this.playerId = playerId;
        this.entryFee = entryFee;
        this.gameStrategy = gameStrategy;
    }

    public UUID getPlayerId()
    {
        return playerId;
    }
    
    public double getEntryFee()
    {
        return entryFee;
    }

    public GameStrategy getGameStrategy()
    {
        return gameStrategy;
    }
}
