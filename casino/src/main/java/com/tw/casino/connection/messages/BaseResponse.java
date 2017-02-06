package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseResponse implements Response, Serializable
{
    private static final long serialVersionUID = -8054460330445873023L;
    
    private final UUID id;
    private final long timestamp;
    
    public BaseResponse()
    {
        this.id = UUID.randomUUID();
        this.timestamp = System.nanoTime();
    }
    
    @Override
    public UUID getId()
    {
        return id;
    }

    @Override
    public long getTimestamp()
    {
        return timestamp;
    }
}
