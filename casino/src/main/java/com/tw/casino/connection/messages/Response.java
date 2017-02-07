package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class Response implements Message, Serializable
{   
    /**
     * 
     */
    private static final long serialVersionUID = -4813813447157666028L;
    private final UUID id;
    private final long timestamp;
    
    public Response()
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
