package com.tw.casino.messages;

import java.util.UUID;

import com.tw.casino.Request;

public class GameRequest implements Request
{
    private final UUID id;
    private final UUID playerId;
    private final String gameCode;
    
    
    public GameRequest(UUID playerId, String gameCode)
    {
        this.id = UUID.randomUUID();
        this.playerId = playerId;
        this.gameCode = gameCode;
    }

    @Override
    public UUID getId()
    {
        return this.id;
    }

}
