package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tw.casino.game.GameDetails;

public class GameListResponse extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 708050025747084291L;
    
    private final UUID playerId;
    private final List<GameDetails> availableGames;
    
    public GameListResponse(UUID playerId, List<GameDetails> availableGames)
    {
        this.playerId = playerId;
        this.availableGames = availableGames;
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
