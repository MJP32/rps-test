package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.game.GameCode;

public class GameRequest implements Request, Serializable
{
    private static final long serialVersionUID = 2604063109860281443L;
    
    private final UUID id;
    private final UUID playerId;
    private final GameCode gameCode;
    
    
    public GameRequest(UUID playerId, GameCode gameCode)
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

    public UUID getPlayerId()
    {
        return playerId;
    }

    public GameCode getGameCode()
    {
        return gameCode;
    }

}
