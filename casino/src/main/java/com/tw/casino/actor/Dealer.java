package com.tw.casino.actor;

import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import com.tw.casino.IDealer;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameCode;


public class Dealer implements IDealer 
{
    private UUID dealerId;

    private final ConcurrentMap<GameCode, Game> availableGames;
    private final ConcurrentMap<GameCode, Deque<PlayerProfile>> gameCache;

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

    public Map<GameCode, Game> getAvailableGames()
    {
        return availableGames;
    }

    public Map<GameCode, Deque<PlayerProfile>> getGameCache()
    {
        return gameCache;
    }

    @Override
    public void handleGameDataResponse(GameDataResponse gameDataResponse)
    {
        this.availableGames.putAll(gameDataResponse.getGameData());
    }

    @Override
    public Response handleGameRequest(GameRequest gameRequest)
    {
        Response response = null;
        synchronized (this)
        {
            GameCode code = gameRequest.getGameCode();
            Game game = availableGames.get(code);

            // Validate Player
            PlayerProfile playerProfile = gameRequest.getPlayerProfile();
            double entryFee = playerProfile.getEntryFee();
            if (entryFee < game.entryFee())
            {
                response = new GameRejectResponse(playerProfile.getPlayerId());
                return response;
            }

            // Check if gameCache has an entry (game is waiting for players)
            if (!gameCache.containsKey(code))
            {
                Deque<PlayerProfile> requiredPlayers = new ConcurrentLinkedDeque<>();
                requiredPlayers.push(playerProfile);
                gameCache.put(code, requiredPlayers);
                response = new GameWaitResponse(playerProfile.getPlayerId());
            }
            else 
            {
                // The following scenario would be applicable when more than two players are required
                if (gameCache.get(code).contains(playerProfile))
                {
                    response = new GameWaitResponse(playerProfile.getPlayerId());
                }
                else if (gameCache.get(code).size() < game.requiredNumberOfPlayers())
                {
                    gameCache.get(code).push(playerProfile);
                    response = new GameWaitResponse(playerProfile.getPlayerId());
                }
                else
                {
                    game.executeGame(Collections.synchronizedCollection(gameCache.get(code)));
                    gameCache.remove(code);
                }
            }
        }

        return response;
    }

}
