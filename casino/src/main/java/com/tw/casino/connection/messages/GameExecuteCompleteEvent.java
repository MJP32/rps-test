package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameExecuteCompleteEvent extends BaseGameExecuteEvent implements Serializable
{
    private static final long serialVersionUID = -1241375307277935294L;
    
    private final double playerWinnings;
    private final double houseDeposit;
    
    public GameExecuteCompleteEvent(UUID dealerId, UUID playerId,
            double playerWinnings, double houseDeposit)
    {
        super(dealerId, playerId);
        
        this.playerWinnings = playerWinnings;
        this.houseDeposit = houseDeposit;  
    }

    public double getPlayerWinnings()
    {
        return playerWinnings;
    }

    public double getHouseDeposit()
    {
        return houseDeposit;
    }
}
