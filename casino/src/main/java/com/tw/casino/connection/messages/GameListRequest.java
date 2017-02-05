package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameListRequest implements Request, Serializable
{
    private static final long serialVersionUID = 7089637787007821040L;
    
    private final UUID id;
    private final UUID playerId;
    
    public GameListRequest(UUID playerId)
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
