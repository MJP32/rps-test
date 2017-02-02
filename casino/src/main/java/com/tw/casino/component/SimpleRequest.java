package com.tw.casino.component;

import java.util.UUID;

import com.tw.casino.IRequest;

public class SimpleRequest implements IRequest
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