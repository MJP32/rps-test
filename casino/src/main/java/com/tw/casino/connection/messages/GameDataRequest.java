package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameDataRequest implements Request, Serializable
{
    private static final long serialVersionUID = 8762655582734092210L;
    
    private final UUID id;
    private final UUID dealerId;
    
    public GameDataRequest(UUID dealerId)
    {
        this.id = UUID.randomUUID();
        this.dealerId = dealerId;
    }

    @Override
    public UUID getId()
    {
        return this.id;
    }

    public UUID getDealerId()
    {
        return dealerId;
    }
}
