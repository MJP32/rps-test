package com.tw.casino.connection.messages;

import java.util.UUID;

public abstract class BaseRequest implements Request
{
    private final UUID id;
    private final long timestamp;
    
    public BaseRequest()
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
