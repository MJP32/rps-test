package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import com.tw.casino.game.Game;
import com.tw.casino.game.GameCode;

public class GameDataResponse implements Response, Serializable
{
    private static final long serialVersionUID = -8515495192760215987L;
    
    private final UUID id;
    private final UUID dealerId;
    private final Map<GameCode, Game> gameData;
    
    public GameDataResponse(UUID dealerId, Map<GameCode, Game> gameData)
    {
        this.id = UUID.randomUUID();
        this.dealerId = dealerId;
        this.gameData = gameData;
    }
    
    @Override
    public UUID getId()
    {
        return this.id;
    }

    public UUID getDealerId()
    {
        return this.dealerId;
    }

    public Map<GameCode, Game> getGameData()
    {
        return this.gameData;
    }
}
