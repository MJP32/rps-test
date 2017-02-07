package com.tw.casino.actor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tw.casino.dataloader.DefaultGameDataLoader;
import com.tw.casino.dataloader.GameDataLoader;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameDetails;
import com.tw.casino.util.CasinoConstants;

public class CasinoManager
{
    private static final String DEFAULT_GAME = CasinoConstants.RPS;
    private static final double DEFAULT_INITIAL_HOUSE_BALANCE = 0;
    
    private double houseAccountBalance;
    private GameDataLoader gameDataLoader;
    private CopyOnWriteArrayList<UUID> dealerRegistry;
    private ConcurrentMap<String, List<UUID>> gameDealerCache;
    
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
            gameDealerCache.putIfAbsent(name, new CopyOnWriteArrayList<UUID>());
        }
    }
    
    public double getHouseAccountBalance()
    {
        return houseAccountBalance;
    }

    public void updateHouseAccountBalance(double houseDeposit)
    {
        this.houseAccountBalance += houseDeposit;
    }

    public List<GameDetails> getGameDetailsList()
    {
        return gameDataLoader.availableGames();
    }
    
    public List<DealerGameDetails> getGameData()
    {
        return gameDataLoader.getGames();
    }
    
    public void registerDealer(UUID dealerId)
    {
        dealerRegistry.addIfAbsent(dealerId);
        CopyOnWriteArrayList<UUID> dealerList = 
                (CopyOnWriteArrayList<UUID>) gameDealerCache.get(DEFAULT_GAME);
        
        dealerList.addIfAbsent(dealerId);
    }
    
    public UUID assignDealerForGame(String name)
    {
        return gameDealerCache.get(name).get(0);
    }
}
