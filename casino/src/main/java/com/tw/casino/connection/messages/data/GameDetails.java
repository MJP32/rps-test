package com.tw.casino.connection.messages.data;

import java.io.Serializable;

public class GameDetails implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1502648592242221307L;
    private final String name;
    private final double entryFee;

    private static final double MIN_ENTRY_FEE = 0.0;
    
    public GameDetails(String name)
    {
        this(name, MIN_ENTRY_FEE);
    }
    
    public GameDetails(String name, double entryFee)
    {
        this.name = name;
        this.entryFee = entryFee;
    }
    
    public String getName()
    {
        return this.name;
    }

    public double getEntryFee()
    {
        return this.entryFee;
    }

}
