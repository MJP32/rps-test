package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameListRequest extends BaseRequest implements Serializable
{
    private static final long serialVersionUID = -7565128141274554404L;
    
    private final UUID playerId;
    
    public GameListRequest(UUID playerId)
    {
        this.playerId = playerId;
    }

    public UUID getPlayerId()
    {
        return this.playerId;
    }
}
