package com.tw.casino;

import java.util.List;
import java.util.UUID;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameExecuteEvent;
import com.tw.casino.connection.messages.Request;

public interface IDealer 
{
    UUID getDealerId();
    
    void handleGameDataResponse(GameDataResponse gameDataResponse);
    
    List<Request> handleGameExecuteEvent(GameExecuteEvent gameExecuteEvent);
}
