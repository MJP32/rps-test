package com.tw.casino.actor;

import java.util.List;

import com.tw.casino.dataloader.DefaultGameDataLoader;
import com.tw.casino.dataloader.GameDataLoader;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameDetails;

public class CasinoManager
{
    private GameDataLoader gameDataLoader;
    
    public CasinoManager()
    {
        this.gameDataLoader = new DefaultGameDataLoader();
    }
    
    public void initialize()
    {
        gameDataLoader.loadConfiguredGames();
    }
    
    public List<GameDetails> getGameDetailsList()
    {
        return gameDataLoader.availableGames();
    }
    
    public List<Game> getGameData()
    {
        return gameDataLoader.getGames();
    }
}
