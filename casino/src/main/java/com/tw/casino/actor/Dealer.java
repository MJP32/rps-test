package com.tw.casino.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import com.tw.casino.IDealer;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameExecuteEvent;
import com.tw.casino.connection.messages.GameExecuteRejectEvent;
import com.tw.casino.connection.messages.GameExecuteWaitEvent;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.rps.RockPaperScissors;


public class Dealer implements IDealer 
{
    private UUID dealerId;

    private final ConcurrentMap<String, Game> availableGames;
    private final ConcurrentMap<String, Deque<PlayerProfile>> gameCache;

    public Dealer()
    {
        this.dealerId = UUID.randomUUID();
        this.availableGames = new ConcurrentHashMap<>();
        this.gameCache = new ConcurrentHashMap<>();
    }

    @Override
    public UUID getDealerId()
    {
        return dealerId;
    }

    public Map<String, Game> getAvailableGames()
    {
        return availableGames;
    }

    public Map<String, Deque<PlayerProfile>> getGameCache()
    {
        return gameCache;
    }

    @Override
    public void handleGameDataResponse(GameDataResponse gameDataResponse)
    {
        synchronized(this)
        {
            for (DealerGameDetails game : gameDataResponse.getGameData())
                availableGames.put(game.getName(), createGame(game.getEntryFee(), game.isAllowStrategy()));
        }
    }

    private Game createGame(double entryFee, boolean allowStrategy)
    {
        return new RockPaperScissors(2, entryFee);
    }

    @Override
    public List<Request> handleGameExecuteEvent(GameExecuteEvent gameExecuteEvent)
    {
        List<Request> eventList = new ArrayList<Request>();
        Request event = null;
        synchronized (this)
        {
            String name = gameExecuteEvent.getGameName();
            Game game = availableGames.get(name);

            // Validate Player
            PlayerProfile playerProfile = gameExecuteEvent.getPlayerProfile();
            double entryFee = playerProfile.getEntryFee();
            if (entryFee < game.entryFee())
            {
                event = new GameExecuteRejectEvent(dealerId, playerProfile.getPlayerId());

                eventList.add(event);
                return eventList;
            }

            // Check if gameCache has an entry (game is waiting for players)
            if (!gameCache.containsKey(name))
            {
                Deque<PlayerProfile> requiredPlayers = new ConcurrentLinkedDeque<>();
                requiredPlayers.push(playerProfile);
                gameCache.put(name, requiredPlayers);
                event = new GameExecuteWaitEvent(dealerId, playerProfile.getPlayerId());

                eventList.add(event);
                return eventList;
            }

            // Player is already been added to wait list
            if (gameCache.get(name).contains(playerProfile))
            {
                event = new GameExecuteWaitEvent(dealerId, playerProfile.getPlayerId());

                eventList.add(event);
                return eventList;
            }
            
            // Awaiting more players
            if (gameCache.get(name).size() < game.requiredNumberOfPlayers())
            {
                gameCache.get(name).push(playerProfile);
                event = new GameExecuteWaitEvent(dealerId, playerProfile.getPlayerId());

                eventList.add(event);
                return eventList;
            }
            
            if (gameCache.get(name).size() == game.requiredNumberOfPlayers())
            {
                PlayerProfile[] players = new PlayerProfile[game.requiredNumberOfPlayers()];
                Deque<PlayerProfile> requiredPlayers = gameCache.get(name);
                int i = 0;
                for (PlayerProfile player : requiredPlayers)
                    players[i++] = player;

                PlayerProfile winner = game.executeGame(players);
                //response = new GameExecuteCompleteEvent(dealerId, winn)
                gameCache.remove(name);
            }
        }

        return eventList;
    }

}
