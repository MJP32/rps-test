package com.tw.casino;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Response;

public interface IDealer 
{
    void handleGameDataResponse(GameDataResponse gameDataResponse);
    
    Response handleGameRequest(GameRequest gameRequest);
}
