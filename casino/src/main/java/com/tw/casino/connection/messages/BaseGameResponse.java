package com.tw.casino.connection.messages;

import java.util.UUID;

public abstract class BaseGameResponse implements Response
{
    private final UUID id;
    private final UUID playerId;
    
    public BaseGameResponse(UUID playerId)
    {
        this.id = UUID.randomUUID();
        this.playerId = playerId;
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
    
}
