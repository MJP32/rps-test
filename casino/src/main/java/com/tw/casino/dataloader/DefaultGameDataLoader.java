package com.tw.casino.dataloader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tw.casino.game.Game;
import com.tw.casino.game.GameCode;
import com.tw.casino.game.GameDetails;
import com.tw.casino.game.rps.RockPaperScissors;

public class DefaultGameDataLoader implements GameDataLoader
{
    private final List<Game> availableGames;
    
    private final List<GameDetails> availableGameDetails;
    
    public DefaultGameDataLoader()
    {
        this.availableGames = new CopyOnWriteArrayList<>();
        this.availableGameDetails = new CopyOnWriteArrayList<>();
    }
    
    @Override
    public void loadConfiguredGames()
    {
        RockPaperScissors rps = new RockPaperScissors(2);
        GameDetails gameDetails = new GameDetails(rps.getName(), GameCode.RPS, 5.0, true);
        
        this.availableGames.add(rps);
        this.availableGameDetails.add(gameDetails);    
    }

    @Override
    public List<Game> getGames()
    {
        return this.availableGames;
    }

    @Override
    public List<GameDetails> availableGames()
    {
        return this.availableGameDetails;
    }
}
