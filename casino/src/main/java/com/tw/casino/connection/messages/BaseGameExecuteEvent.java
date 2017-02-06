package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseGameExecuteEvent extends BaseRequest implements
        Serializable
{
    private static final long serialVersionUID = 7310600172625730992L;
    
    private final UUID dealerId;
    private final UUID playerId;
    
    public BaseGameExecuteEvent(UUID dealerId, UUID playerId)
    {
        this.dealerId = dealerId;
        this.playerId = playerId;
    }

    public UUID getDealerId()
    {
        return dealerId;
    }

    public UUID getPlayerId()
    {
        return playerId;
    }
}
