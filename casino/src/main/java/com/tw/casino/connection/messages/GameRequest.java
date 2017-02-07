package com.tw.casino.connection.messages;

import java.io.Serializable;

import com.tw.casino.actor.PlayerDetails;

public class GameRequest extends BaseRequest implements Serializable
{
    private static final long serialVersionUID = -4840163434885437234L;
    
    private final PlayerDetails playerDetails;
    private final String gameName;
    
    
    public GameRequest(PlayerDetails playerDetails, String gameName)
    {
        this.playerDetails = playerDetails;
        this.gameName = gameName;
    }

    public PlayerDetails getPlayerProfile()
    {
        return playerDetails;
    }

    public String getGameName()
    {
        return gameName;
    }

}
