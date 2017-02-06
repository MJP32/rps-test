package com.tw.casino.game;

import java.io.Serializable;

public class DealerGameDetails extends GameDetails implements Serializable
{
    private static final long serialVersionUID = -1936776591894404368L;
    
    private int requiredNumberOfPlayers;

    public DealerGameDetails(String name, double entryFee, boolean allowStrategy, int requiredPlayers)
    {
        super(name, entryFee, allowStrategy);
        
        this.requiredNumberOfPlayers = requiredPlayers;
    }
    
    public int getRequiredNumberOfPlayers()
    {
        return requiredNumberOfPlayers;
    }
}
