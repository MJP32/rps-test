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
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.GameDetails;
import com.tw.casino.game.GamePlay;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.util.CasinoConstants;

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

    public double getAccountBalance()
    {
        return accountBalance;
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
        
        // TODO Add support to load strategy
        GamePlay play = new RPSPlay(RPSMove.ROCK);
        PlayerDetails profile = new PlayerDetails(playerId, entryFee, play);
        GameRequest request = new GameRequest(profile, name);
        
        return request;
    }
    
    public void handleGameListResponse(GameListResponse gameListResponse)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CasinoConstants.GAME_LIST_AVAILABLE);
        stringBuilder.append("\n");
        int gameIndex = 1;
        for (GameDetails details : gameListResponse.getAvailableGames())
        {
            String name = details.getName();
            availableGames.put(name, details);
            
            stringBuilder.append("Game Code: [");
            stringBuilder.append(gameIndex++);
            stringBuilder.append("]  Game: ");
            stringBuilder.append(name);
            stringBuilder.append("  Entry Fee: ");
            stringBuilder.append(details.getEntryFee());
            stringBuilder.append("  Strategy: ");
            String strategy = details.isAllowStrategy() ? CasinoConstants.ALLOW_STRATEGY
                    : CasinoConstants.NO_STRATEGY;
            stringBuilder.append(strategy);
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }
    
    public void handleGameResponse(Response gameResponse)
    {
        if (gameResponse instanceof GameWaitResponse)
        {
            System.out.println(CasinoConstants.PLAYER_AWAIT);
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
            System.out.println(CasinoConstants.PLAYER_REJECT);
        }
    }

}
