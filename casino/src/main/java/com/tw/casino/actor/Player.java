package com.tw.casino.actor;

import java.util.UUID;

import com.tw.casino.IPlayer;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.simulator.DefaultRPSStrategy;

public class Player implements IPlayer 
{
    private UUID playerId;
    private double accountBalance;
    private GameStrategy strategy;
    
    public Player(double startingBalance)
    {
        this.setPlayerId(UUID.randomUUID());
        this.accountBalance = startingBalance;
        this.strategy = null;
    }

    @Override
    public void setAccountBalance(double accountBalance) 
    {
        this.accountBalance = accountBalance;
    }

    @Override
    public void setGameStrategy(GameStrategy strategy)
    {
        this.strategy = strategy;  
    }

    public UUID getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(UUID playerId)
    {
        this.playerId = playerId;
    }

}
