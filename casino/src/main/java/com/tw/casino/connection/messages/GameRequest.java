package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.Request;

public class GameRequest implements Request, Serializable
{
    private static final long serialVersionUID = 2604063109860281443L;
    
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
