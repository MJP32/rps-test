package com.tw.casino;

import java.util.UUID;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Response;

public interface IDealer 
{
    UUID getDealerId();
    
    void handleGameDataResponse(GameDataResponse gameDataResponse);
    
    Response handleGameRequest(GameRequest gameRequest);
}
