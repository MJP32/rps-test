package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.actor.PlayerProfile;
import com.tw.casino.game.GameCode;

public class GameRequest implements Request, Serializable
{
    private static final long serialVersionUID = 8209786112068043094L;
    
    private final UUID id;
    private final PlayerProfile playerProfile;
    private final GameCode gameCode;
    
    
    public GameRequest(PlayerProfile playerProfile, GameCode gameCode)
    {
        this.id = UUID.randomUUID();
        this.playerProfile = playerProfile;
        this.gameCode = gameCode;
    }

    @Override
    public UUID getId()
    {
        return id;
    }

    public PlayerProfile getPlayerProfile()
    {
        return playerProfile;
    }

    public GameCode getGameCode()
    {
        return gameCode;
    }

}
