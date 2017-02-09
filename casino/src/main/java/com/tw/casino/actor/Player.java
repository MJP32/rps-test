package com.tw.casino.actor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.tw.casino.IPlayer;
import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.connection.messages.data.PlayerDetails;
import com.tw.casino.game.GamePlay;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.game.rps.strategy.SharpRPSStrategy;
import com.tw.casino.util.Constants;
import com.tw.casino.util.EmployStrategy;
import com.tw.casino.util.VisibleForTest;

public class Player implements IPlayer 
{
    private static final GameStrategy DEFAULT_STRATEGY = new SharpRPSStrategy();
    
    private UUID playerId;
    private double accountBalance;
    private GameStrategy strategy;
    
    private Map<String, GameDetails> availableGames;
    
    public Player(double startingBalance)
    {
        this.playerId = UUID.randomUUID();
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
    public double getAccountBalance()
    {
        return accountBalance;
    }

    @Override
    public void setGameStrategy(GameStrategy strategy)
    {
        this.strategy = strategy;  
    }
    
    @VisibleForTest
    public GameStrategy getGameStrategy()
    {
        return strategy;  
    }

    @Override
    public UUID getPlayerId()
    {
        return playerId;
    }
    
    @VisibleForTest
    public Map<String, GameDetails> getAvailableGames()
    {
        return availableGames;
    }
    
    @Override
    public void loadPlayerStrategy()
    {
        Reflections reflections = new Reflections(Constants.GAME_LOCATION, new SubTypesScanner());
        Set<Class<? extends GameStrategy>> strategies = reflections.getSubTypesOf(GameStrategy.class);
        GameStrategy gameStrategy = null;
        
        for (Class<? extends GameStrategy> strategy : strategies)
        {
            if (strategy.getAnnotation(EmployStrategy.class) != null)
            {
                Constructor<? extends GameStrategy> constructor; 
                try
                {
                    constructor = strategy.getConstructor((Class<?>[])null);
                    gameStrategy = constructor.newInstance((Object[])null);
                }
                catch (InstantiationException | NoSuchMethodException | SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }

        this.strategy = gameStrategy != null ? gameStrategy : DEFAULT_STRATEGY;
    }
    
    @Override
    public Request createGameListRequest()
    {
        GameListRequest request = new GameListRequest(playerId);
        return request;
    }
    
    @Override
    public Request createGameRequest(String name)
    {
        GameDetails details = availableGames.get(name);
        
        if (accountBalance <= 0.0)
        {
            // Warn account balance has insufficient funds to request a game
            System.out.println(Constants.INSUFFICIENT_FUNDS);
            return null;
        }

        double expectedFee = details.getEntryFee();
        double entryFeeToPay = (accountBalance >= expectedFee) ? expectedFee : accountBalance;
        
        // Update account balance
        accountBalance -= entryFeeToPay;

        GameStrategy strategy = this.strategy != null ? this.strategy : DEFAULT_STRATEGY;
        GamePlay play = strategy.computeNextPlay(UUID.randomUUID());

        PlayerDetails profile = new PlayerDetails(playerId, entryFeeToPay, play);
        GameRequest request = new GameRequest(profile, name);
        
        return request;
    }
    
    @Override
    public String handleGameListResponse(Response response)
    {
        GameListResponse gameListResponse = (GameListResponse) response;
        
        StringBuilder stringBuilder = new StringBuilder();
        int gameCode = 1;
        for (GameDetails details : gameListResponse.getAvailableGames())
        {
            String name = details.getName();
            
            availableGames.put(name, details);
            
            stringBuilder.append("Game Code: ");
            stringBuilder.append(gameCode++);
            stringBuilder.append("  Name: ");
            stringBuilder.append(name);
            stringBuilder.append("  Entry Fee: ");
            stringBuilder.append(details.getEntryFee());
        }
        
        return stringBuilder.toString();
    }
    
    @Override
    public String handleGameResponse(Response response)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if (response instanceof GameWaitResponse)
        {
            return Constants.AWAIT;
        }
        
        if (response instanceof GameCompleteResponse)
        {
            GameCompleteResponse gameCompleteResponse = (GameCompleteResponse) response;
            if (gameCompleteResponse.getWinnings() > 0.0)
            {
                accountBalance += gameCompleteResponse.getWinnings();
                stringBuilder.append(Constants.CONGRATULATIONS);
                stringBuilder.append(gameCompleteResponse.getWinnings());
            }
            else
            {
                stringBuilder.append(Constants.REGRET);
            }
        }
        else if (response instanceof GameRejectResponse)
        {
            stringBuilder.append(Constants.REJECT);
        }
        
        return stringBuilder.toString();
    }

}
