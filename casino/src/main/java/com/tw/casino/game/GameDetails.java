package com.tw.casino.game;

public final class GameDetails
{
    private final String name;
    private final String code;
    private final double entryFee;
    private final boolean allowStrategy;

    private static final double MIN_ENTRY_FEE = 0.0;
    private static final boolean HAS_STRATEGY = false;
    
    public GameDetails(String name, String code)
    {
        this(name, code, MIN_ENTRY_FEE, HAS_STRATEGY);
    }
    
    public GameDetails(String name, String code, double entryFee, boolean allowStrategy)
    {
        this.name = name;
        this.code = code;
        this.entryFee = entryFee;
        this.allowStrategy = allowStrategy;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getCode()
    {
        return this.code;
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
