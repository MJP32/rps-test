package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameExecuteEvent extends BaseResponse implements Serializable
{   
    private static final long serialVersionUID = 7252203561687047487L;
    
    private final UUID dealerId;
    private final String gameName;
    
    public GameExecuteEvent(UUID dealerId, String gameName)
    {
        this.dealerId = dealerId;
        this.gameName = gameName;
    }

    public UUID getDealerId()
    {
        return dealerId;
    }
    
    public String getGameName()
    {
        return gameName;
    }
}
