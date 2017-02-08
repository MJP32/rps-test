package com.tw.casino;

import java.util.List;
import java.util.UUID;

import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.data.DealerGameDetails;
import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.game.Game;

public interface ICasinoManager
{
    void initialize();
    
    void updateHouseAccountBalance(double houseDeposit);
    
    List<GameDetails> getGameDetailsList();
    
    List<DealerGameDetails> getGameData();
    
    void registerDealer(UUID dealerId);
    
    UUID assignDealerForGame(String name);
}

