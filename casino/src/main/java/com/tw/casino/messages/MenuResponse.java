package com.tw.casino.messages;

import java.util.List;
import java.util.UUID;

import com.tw.casino.Response;
import com.tw.casino.game.GameDetails;

public class MenuResponse implements Response
{
    private final UUID id;
    private final List<GameDetails> availableGames;
    
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
