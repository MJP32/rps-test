package com.tw.casino;

import java.util.List;
import java.util.UUID;

import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameDetails;

public interface ICasinoManager
{
    void registerDealer(UUID dealerId);
    
    List<Game> getGamesForDealer(UUID dealerId);
    
    List<GameDetails> getGameListForPlayer();
    
    UUID assignDealerOnGameRequest(GameRequest gameRequest);
}

