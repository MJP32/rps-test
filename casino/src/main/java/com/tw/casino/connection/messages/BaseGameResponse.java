package com.tw.casino.connection.messages;

import java.util.UUID;

public abstract class BaseGameResponse extends BaseResponse
{
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
