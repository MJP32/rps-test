package com.tw.casino.actor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tw.casino.ICasinoManager;
import com.tw.casino.RequestListener;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.dataloader.DefaultGameDataLoader;
import com.tw.casino.dataloader.GameDataLoader;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameCode;
import com.tw.casino.game.GameDetails;

public class CasinoManager implements ICasinoManager, RequestListener
{
    private static final int DEFAULT = 0;
    
    private final GameDataLoader gameDataLoader;

    private final CopyOnWriteArrayList<UUID> dealerRegistry;
    private final CopyOnWriteArrayList<GameCode> availableGameCodes;
    private final ConcurrentMap<GameCode, List<UUID>> gameDealerCache;
    
    private final ExecutorService executor;

    public CasinoManager()
    {
        this.gameDataLoader = new DefaultGameDataLoader();
        this.dealerRegistry = new CopyOnWriteArrayList<>();
        this.availableGameCodes = new CopyOnWriteArrayList<>();
        this.gameDealerCache = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
        
        initialize();
    }
    
    private void initialize()
    {
        gameDataLoader.loadConfiguredGames();
        // Get the list of game codes
        // Create entries in cache for each of the loaded games
    }

    @Override
    public void registerDealer(UUID dealerId)
    {
        boolean isNewDealer = dealerRegistry.addIfAbsent(dealerId);
        if (isNewDealer)
        {
            synchronized(this)
            {
                // Assign Dealer to one or more games
                GameCode gameCode = availableGameCodes.get(DEFAULT);
                CopyOnWriteArrayList<UUID> dealerList = (CopyOnWriteArrayList<UUID>) gameDealerCache.get(gameCode);
                dealerList.add(dealerId);
            }
        }
    }

    @Override
    public List<GameDetails> getGameListForPlayer()
    {
        return this.gameDataLoader.availableGames();
    }

    @Override
    public UUID assignDealerOnGameRequest(GameRequest gameRequest)
    {
        return gameDealerCache.get(gameRequest.getGameCode()).get(DEFAULT);
    }

    @Override
    public List<Game> getGamesForDealer(UUID dealerId)
    {
        return this.gameDataLoader.getGames();
    }

    @Override
    public void onRequest(Request request)
    {
        // TODO Auto-generated method stub
        
    }
  
}
