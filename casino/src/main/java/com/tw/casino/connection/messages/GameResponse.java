package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.Response;

public class GameResponse implements Response, Serializable
{
    private static final long serialVersionUID = 548828943632143549L;
    
    private final UUID id;
    private final UUID playerId;
    private final double winnings;
    
    public GameResponse(UUID playerId, double winnings)
    {
        this.id = UUID.randomUUID();
        this.playerId = playerId;
        this.winnings = winnings;
    }
   
    @Override
    public UUID getId()
    {
        return this.id;
    }

    public UUID getPlayerId()
    {
        return this.playerId;
    }

    public double getWinnings()
    {
        return this.winnings;
    }

}
