package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.actor.PlayerProfile;

public class GameRequest extends BaseRequest implements Serializable
{
    private static final long serialVersionUID = 1436436035219299645L;
    
    private final PlayerProfile playerProfile;
    private final String gameName;
    
    
    public GameRequest(PlayerProfile playerProfile, String gameName)
    {
        this.playerProfile = playerProfile;
        this.gameName = gameName;
    }

    public PlayerProfile getPlayerProfile()
    {
        return playerProfile;
    }

    public String getGameName()
    {
        return gameName;
    }

}
