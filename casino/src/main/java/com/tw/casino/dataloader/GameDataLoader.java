package com.tw.casino.dataloader;

import java.util.List;

import com.tw.casino.connection.messages.data.DealerGameDetails;
import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.game.Game;

public interface GameDataLoader
{
    void loadConfiguredGames();
    
    List<DealerGameDetails> getGames();
    
    List<GameDetails> availableGames();
}
