package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tw.casino.game.Game;

public class GameDataResponse extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 2832907221729261051L;
    
    private final UUID dealerId;
    private final List<Game> gameData;
    
    public GameDataResponse(UUID dealerId, List<Game> gameData)
    {
        this.dealerId = dealerId;
        this.gameData = gameData;
    }
    
    public UUID getDealerId()
    {
        return this.dealerId;
    }

    public List<Game> getGameData()
    {
        return this.gameData;
    }
}
