package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.tw.casino.connection.messages.data.DealerGameDetails;

public class GameDataResponse extends Response implements Serializable
{
    private static final long serialVersionUID = 2832907221729261051L;
    private final UUID dealerId;
    private final List<DealerGameDetails> gameData;
    
    public GameDataResponse(UUID dealerId, List<DealerGameDetails> gameData)
    {
        this.dealerId = dealerId;
        this.gameData = gameData;
    }
    
    public UUID getDealerId()
    {
        return this.dealerId;
    }

    public List<DealerGameDetails> getGameData()
    {
        return this.gameData;
    }
}
