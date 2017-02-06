package com.tw.casino.game;

import java.io.Serializable;

public class GameDetails implements Serializable
{
    private static final long serialVersionUID = -901935564818912216L;
    
    private final String name;
    private final double entryFee;
    private final boolean allowStrategy;

    private static final double MIN_ENTRY_FEE = 0.0;
    private static final boolean HAS_STRATEGY = false;
    
    public GameDetails(String name)
    {
        this(name, MIN_ENTRY_FEE, HAS_STRATEGY);
    }
    
    public GameDetails(String name, double entryFee, boolean allowStrategy)
    {
        this.name = name;
        this.entryFee = entryFee;
        this.allowStrategy = allowStrategy;
    }
    
    public String getName()
    {
        return this.name;
    }

    public double getEntryFee()
    {
        return this.entryFee;
    }

    public boolean isAllowStrategy()
    {
        return this.allowStrategy;
    }
}
