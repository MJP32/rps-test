package com.tw.casino.game;

import java.io.Serializable;

public class DealerGameDetails extends GameDetails implements Serializable
{   
    /**
     * 
     */
    private static final long serialVersionUID = 2477947355126706971L;
    private int requiredNumberOfPlayers;

    public DealerGameDetails(String name, double entryFee, int requiredPlayers)
    {
        super(name, entryFee);
        
        this.requiredNumberOfPlayers = requiredPlayers;
    }
    
    public int getRequiredNumberOfPlayers()
    {
        return requiredNumberOfPlayers;
    }
}
