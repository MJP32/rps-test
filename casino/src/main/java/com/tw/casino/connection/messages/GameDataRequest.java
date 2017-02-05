package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameDataRequest extends BaseRequest implements Serializable
{
    private static final long serialVersionUID = -2700235029873272433L;
    
    private final UUID dealerId;
    
    public GameDataRequest(UUID dealerId)
    {
        this.dealerId = dealerId;
    }

    public UUID getDealerId()
    {
        return dealerId;
    }
}
