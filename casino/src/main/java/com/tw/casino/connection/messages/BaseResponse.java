package com.tw.casino.connection.messages;

import java.util.UUID;

public abstract class BaseResponse implements Response
{
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
