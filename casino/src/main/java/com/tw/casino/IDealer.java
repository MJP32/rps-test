package com.tw.casino;

import java.util.List;
import java.util.UUID;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Message;

public interface IDealer 
{
    UUID getDealerId();
    
    void handleGameDataResponse(GameDataResponse gameDataResponse);
    
    Message handleGameExecuteEvent(GameRequest gameExecuteEvent);
}
