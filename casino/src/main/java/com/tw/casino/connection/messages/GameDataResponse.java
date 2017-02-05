package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import com.tw.casino.game.Game;

public class GameDataResponse extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = -1815787314020848248L;
    
    private final UUID dealerId;
    private final Map<String, Game> gameData;
    
    public GameDataResponse(UUID dealerId, Map<String, Game> gameData)
    {
        this.dealerId = dealerId;
        this.gameData = gameData;
    }
    
    public UUID getDealerId()
    {
        return this.dealerId;
    }

    public Map<String, Game> getGameData()
    {
        return this.gameData;
    }
}
