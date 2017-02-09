package com.tw.casino.game;

import java.util.UUID;

public class GameId 
{
    private UUID gameId;
    
    public GameId(UUID id)
    {
        this.gameId = id;
    }
    
	public UUID getGameId()
	{
		return this.gameId;
	}
}
