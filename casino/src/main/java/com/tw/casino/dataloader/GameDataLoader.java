package com.tw.casino.dataloader;

import java.util.List;

import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameDetails;

public interface GameDataLoader
{
    void loadConfiguredGames();
    
    List<DealerGameDetails> getGames();
    
    List<GameDetails> availableGames();
}
