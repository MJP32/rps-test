package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameCompleteResponse extends BaseGameResponse implements Serializable
{
    private static final long serialVersionUID = 3215255802959634122L;
    
    private final double winnings;
    
    public GameCompleteResponse(UUID playerId, double winnings)
    {
        super(playerId);
        this.winnings = winnings;
    }

    public double getWinnings()
    {
        return this.winnings;
    }

}
