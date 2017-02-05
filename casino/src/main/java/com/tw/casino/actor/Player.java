package com.tw.casino.actor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tw.casino.IPlayer;
import com.tw.casino.connection.messages.BaseGameResponse;
import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.game.GameDetails;
import com.tw.casino.game.GameStrategy;

public class Player implements IPlayer 
{
    private UUID playerId;
    private double accountBalance;
    private GameStrategy strategy;
    
    private Map<String, GameDetails> availableGames;
    
    public Player(double startingBalance)
    {
        this.setPlayerId(UUID.randomUUID());
        this.accountBalance = startingBalance;
        this.strategy = null;
        this.availableGames = new HashMap<>();
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
    
    public GameListRequest createGameListRequest()
    {
        GameListRequest request = new GameListRequest(playerId);
        return request;
    }
    
    public GameRequest createGameRequest(String name)
    {
        GameDetails details = availableGames.get(name);
        
        if (accountBalance < details.getEntryFee())
        {
            // Warn account balance has insufficient funds to request a game
            return null;
        }

        double entryFee = details.getEntryFee();
        accountBalance = accountBalance - entryFee;
        
        PlayerProfile profile = new PlayerProfile(playerId, entryFee, strategy);
        GameRequest request = new GameRequest(profile, name);
        
        return request;
    }
    
    public void handleGameListResponse(GameListResponse gameListResponse)
    {
        for (GameDetails details : gameListResponse.getAvailableGames())
            availableGames.put(details.getName(), details);
    }
    
    public void handleGameResponse(BaseGameResponse gameResponse)
    {
        if (gameResponse instanceof GameWaitResponse)
        {
            // TODO 
        }
        else if (gameResponse instanceof GameCompleteResponse)
        {
            GameCompleteResponse response = (GameCompleteResponse) gameResponse;
            if (response.getWinnings() > 0.0)
            {
                accountBalance += response.getWinnings();
                // TODO Win
            }
            else
            {
                // TODO Did not win
            }
        }
        else if (gameResponse instanceof GameRejectResponse)
        {
            // TODO Decide how to handle
        }
    }

}
