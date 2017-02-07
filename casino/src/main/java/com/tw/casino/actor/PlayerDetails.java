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
    private static final long serialVersionUID = -5456813864339299362L;
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
}
