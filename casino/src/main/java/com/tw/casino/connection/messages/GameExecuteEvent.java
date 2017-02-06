package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.actor.PlayerProfile;

public class GameExecuteEvent extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 6329608648028501816L;
    
    private final UUID dealerId;
    private final PlayerProfile playerProfile;
    private final String gameName;
    
    public GameExecuteEvent(UUID dealerId, PlayerProfile playerProfile, String gameName)
    {
        this.dealerId = dealerId;
        this.playerProfile = playerProfile;
        this.gameName = gameName;
    }

    public UUID getDealerId()
    {
        return dealerId;
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
