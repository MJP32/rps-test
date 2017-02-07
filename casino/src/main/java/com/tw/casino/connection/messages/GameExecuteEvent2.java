package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.actor.PlayerDetails;

public class GameExecuteEvent2 extends BaseResponse implements Serializable
{   
    private static final long serialVersionUID = -6665230396679542188L;
    
    private final UUID dealerId;
    private final PlayerDetails playerDetails;
    private final String gameName;
    
    public GameExecuteEvent2(UUID dealerId, PlayerDetails playerDetails, String gameName)
    {
        this.dealerId = dealerId;
        this.playerDetails = playerDetails;
        this.gameName = gameName;
    }

    public UUID getDealerId()
    {
        return dealerId;
    }

    public PlayerDetails getPlayerDetails()
    {
        return playerDetails;
    }

    public String getGameName()
    {
        return gameName;
    }
}
