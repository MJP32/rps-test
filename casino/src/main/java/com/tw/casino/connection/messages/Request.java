package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class Request implements Message, Serializable
{   
    /**
     * 
     */
    private static final long serialVersionUID = -6890365311760503140L;
    private final UUID id;
    private final long timestamp;
    
    public Request()
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
