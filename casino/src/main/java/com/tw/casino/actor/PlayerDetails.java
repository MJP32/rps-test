package com.tw.casino.actor;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.game.GamePlay;
import com.tw.casino.game.rps.RPSMove;

public class PlayerDetails implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 4754908319114689558L;

    /**
     * 
     */
    
    private volatile int hashCode = 0;
    
    private final UUID playerId;
    private final double entryFee;
    private final GamePlay gamePlay;
    
    public PlayerDetails(UUID playerId, double entryFee, GamePlay gamePlay)
    {
        this.playerId = playerId;
        this.entryFee = entryFee;
        this.gamePlay = gamePlay;
    }

    public UUID getPlayerId()
    {
        return playerId;
    }
    
    public double getEntryFee()
    {
        return entryFee;
    }

    public GamePlay getGamePlay()
    {
        return gamePlay;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        
        if (!(other instanceof PlayerDetails))
        {
            return false;
        }
        
        PlayerDetails otherProfile = (PlayerDetails) other;
        
        if (!this.playerId.equals(otherProfile.getPlayerId()))
            return false;
        
        if (Double.compare(this.entryFee, otherProfile.getEntryFee()) != 0)
            return false;
        
        if (!this.gamePlay.equals(otherProfile.gamePlay))
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
            result = 31 * result + gamePlay.hashCode();
        }
        
        return result;
    }
}
