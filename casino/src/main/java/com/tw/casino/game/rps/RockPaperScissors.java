package com.tw.casino.game.rps;

import java.util.List;
import java.util.UUID;

import com.tw.casino.IPlayer;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameId;
import com.tw.casino.util.CasinoConstants;

public class RockPaperScissors implements Game
{
    private static final String NAME = CasinoConstants.RPS;
    private GameId id;
    private int requiredPlayers;
    
    public RockPaperScissors(int requiredNumberOfPlayers)
    {
        id = new GameId(UUID.randomUUID());
        this.requiredPlayers = requiredNumberOfPlayers;
    }

    @Override
    public GameId getGameId()
    {
        return this.id;
    }
    
    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public UUID executeGame(List<IPlayer> players)
    {
        if (players.size() < this.requiredPlayers)
            throw new IllegalStateException("Attempting to start game with less than required players.");
        
        return null;
    }

}
