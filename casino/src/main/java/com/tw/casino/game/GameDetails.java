package com.tw.casino.game;

public final class GameDetails
{
    private final String name;
    private final GameCode code;
    private final double entryFee;
    private final boolean allowStrategy;

    private static final double MIN_ENTRY_FEE = 0.0;
    private static final boolean HAS_STRATEGY = false;
    
    public GameDetails(String name, GameCode code)
    {
        this(name, code, MIN_ENTRY_FEE, HAS_STRATEGY);
    }
    
    public GameDetails(String name, GameCode code, double entryFee, boolean allowStrategy)
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
    
    public GameCode getCode()
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
