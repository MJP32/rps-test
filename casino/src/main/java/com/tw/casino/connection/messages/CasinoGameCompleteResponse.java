package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class CasinoGameCompleteResponse extends Response implements
        Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -1185384339503122751L;
    private final UUID dealerId;
    private final double houseDeposit;
    private final Map<UUID, Double> playerResults;
    
    public CasinoGameCompleteResponse(UUID dealerId, 
            double houseDeposit, Map<UUID, Double> playerResults)
    {
        this.dealerId = dealerId;
        this.houseDeposit = houseDeposit;
        this.playerResults = playerResults;
    }

    public UUID getDealerId()
    {
        return dealerId;
    }

    public double getHouseDeposit()
    {
        return houseDeposit;
    }

    public Map<UUID, Double> getPlayerResults()
    {
        return playerResults;
    }
    
}
