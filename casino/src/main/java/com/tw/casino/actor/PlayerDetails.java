package com.tw.casino.actor;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.game.GamePlay;
import com.tw.casino.game.rps.RPSMove;

public class PlayerDetails implements Serializable
{
    private static final long serialVersionUID = 1390543613319213716L;
    private final UUID playerId;
    private final double entryFee;
    private final RPSMove rpsMove;
    
    public PlayerDetails(UUID playerId, double entryFee, RPSMove rpsMove)
    {
        this.playerId = playerId;
        this.entryFee = entryFee;
        this.rpsMove = rpsMove;
    }

    public UUID getPlayerId()
    {
        return playerId;
    }
    
    public double getEntryFee()
    {
        return entryFee;
    }

    public RPSMove getMove()
    {
        return rpsMove;
    }
}
