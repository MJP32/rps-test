package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseGameResponse extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = -8929002370966992436L;
    
    private final UUID playerId;
    
    public BaseGameResponse(UUID playerId)
    {
        this.playerId = playerId;
    }

    public UUID getPlayerId()
    {
        return this.playerId;
    }
    
}
