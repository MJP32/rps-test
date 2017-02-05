package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tw.casino.game.GameDetails;

public class GameListResponse implements Response, Serializable
{
    private static final long serialVersionUID = -4844126834959583483L;
    
    private final UUID id;
    private final UUID playerId;
    private final List<GameDetails> availableGames;
    
    public GameListResponse(UUID playerId, List<GameDetails> availableGames)
    {
        this.id = UUID.randomUUID();
        this.playerId = playerId;
        this.availableGames = availableGames;
    }

    @Override
    public UUID getId()
    {
        return this.id;
    }

    public UUID getPlayerId()
    {
        return playerId;
    }
    
    public List<GameDetails> getAvailableGames()
    {
        return availableGames;
    }
    
}
