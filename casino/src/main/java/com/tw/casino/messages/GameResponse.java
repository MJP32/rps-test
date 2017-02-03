package com.tw.casino.messages;

import java.util.UUID;

import com.tw.casino.Response;

public class GameResponse implements Response
{
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
