package com.tw.casino.connection.messages;

import java.util.UUID;

import com.tw.casino.Request;

public class SimpleRequest implements Request
{
    private final UUID id;
    
    private String message;
    
    public SimpleRequest()
    {
        this.id = UUID.randomUUID();
        this.setMessage(null);
        
    }
    
    @Override
    public UUID getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
