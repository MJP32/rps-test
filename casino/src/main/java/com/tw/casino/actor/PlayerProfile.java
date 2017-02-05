package com.tw.casino.actor;

import java.util.UUID;

import com.tw.casino.game.GameStrategy;

public class PlayerProfile
{
    private volatile int hashCode = 0;
    
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
    
    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        
        if (!(other instanceof PlayerProfile))
        {
            return false;
        }
        
        PlayerProfile otherProfile = (PlayerProfile) other;
        
        if (!this.playerId.equals(otherProfile.getPlayerId()))
            return false;
        
        if (Double.compare(this.entryFee, otherProfile.getEntryFee()) != 0)
            return false;
        
        if (!this.gameStrategy.equals(otherProfile.gameStrategy))
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        int result = hashCode;
        if (result == 0)
        {
            result = 17;
            result = 31 * result + playerId.hashCode();
            result = 31 * result + Double.valueOf(entryFee).hashCode();
            result = 31 * result + gameStrategy.hashCode();
        }
        
        return result;
    }
    
    
}
