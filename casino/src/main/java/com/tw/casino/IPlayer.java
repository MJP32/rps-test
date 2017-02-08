package com.tw.casino;

import java.util.UUID;

import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.GameStrategy;

public interface IPlayer 
{
    UUID getPlayerId();
    
    void setAccountBalance(double accountBalance);
    
    double getAccountBalance();

    void setGameStrategy(GameStrategy strategy);
    
    Request createGameListRequest();
    
    Request createGameRequest(String name);
    
    String handleGameListResponse(Response response);
    
    String handleGameResponse(Response response);
}
