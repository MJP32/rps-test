package com.tw.casino.actor;

import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tw.casino.ICasinoManager;
import com.tw.casino.connection.messages.data.DealerGameDetails;
import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.dataloader.DefaultGameDataLoader;
import com.tw.casino.dataloader.GameDataLoader;
import com.tw.casino.game.Game;
import com.tw.casino.util.Constants;

public class CasinoManager implements ICasinoManager
{
    private static final String DEFAULT_GAME = Constants.RPS;
    private static final double DEFAULT_INITIAL_HOUSE_BALANCE = 0;
    
    private double houseAccountBalance;
    private GameDataLoader gameDataLoader;
    private CopyOnWriteArrayList<UUID> dealerRegistry;
    private ConcurrentMap<String, Deque<UUID>> gameDealerCache;
    
    public CasinoManager()
    {
        
        this.houseAccountBalance = DEFAULT_INITIAL_HOUSE_BALANCE;
        this.gameDataLoader = new DefaultGameDataLoader();
        this.dealerRegistry = new CopyOnWriteArrayList<>();
        this.gameDealerCache = new ConcurrentHashMap<>();
    }
    
    public void initialize()
    {
        gameDataLoader.loadConfiguredGames();
        for (GameDetails gameDetails : gameDataLoader.availableGames())
        {
            String name = gameDetails.getName();
            gameDealerCache.putIfAbsent(name, new ConcurrentLinkedDeque<UUID>());
        }
    }
    
    public double getHouseAccountBalance()
    {
        return houseAccountBalance;
    }

    @Override
    public void updateHouseAccountBalance(double houseDeposit)
    {
        this.houseAccountBalance += houseDeposit;
    }

    @Override
    public List<GameDetails> getGameDetailsList()
    {
        return gameDataLoader.availableGames();
    }
    
    @Override
    public List<DealerGameDetails> getGameData()
    {
        return gameDataLoader.getGames();
    }
    
    /**
     * Maintains a registry of dealers and assigns them to 
     * various loaded games with a LIFO policy for retrieving
     * them for games.
     * @param dealerId
     */
    @Override
    public void registerDealer(UUID dealerId)
    {
        dealerRegistry.addIfAbsent(dealerId);
        ConcurrentLinkedDeque<UUID> dealerList = 
                (ConcurrentLinkedDeque<UUID>) gameDealerCache.get(DEFAULT_GAME);
        
        dealerList.addFirst(dealerId);
    }
    
    /**
     * Uses a LIFO policy to retrieve the most recently registered dealer.
     * @param name
     * @return
     */
    @Override
    public UUID assignDealerForGame(String name)
    {
        return gameDealerCache.get(name).getFirst();
    }

}
