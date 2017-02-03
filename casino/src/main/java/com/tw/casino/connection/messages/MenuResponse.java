package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tw.casino.Response;
import com.tw.casino.game.GameDetails;

public class MenuResponse implements Response, Serializable
{
    private static final long serialVersionUID = 1789522163045682429L;
    
    private final UUID id;
    private final List<GameDetails> availableGames;
    
    public MenuResponse()
    {
        this(new ArrayList<GameDetails>());
    }
    
    public MenuResponse(List<GameDetails> availableGames)
    {
        this.id = UUID.randomUUID();
        this.availableGames = availableGames;
    }

    @Override
    public UUID getId()
    {
        return this.id;
    }

    public List<GameDetails> getAvailableGames()
    {
        return availableGames;
    }
    
}
