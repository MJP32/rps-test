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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

import com.tw.casino.IDealer;
import com.tw.casino.connection.messages.CasinoGameCompleteResponse;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameContext;
import com.tw.casino.game.rps.RPSGameContext;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.game.rps.RockPaperScissors;
import com.tw.casino.util.CasinoConstants;


public class Dealer implements IDealer 
{
    private final UUID dealerId;

    private final ConcurrentMap<String, Game> availableGames;
    private final ConcurrentMap<String, PriorityBlockingQueue<GameContext>> liveGameCache;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public Dealer()
    {
        this.dealerId = UUID.randomUUID();
        this.availableGames = new ConcurrentHashMap<>();
        this.liveGameCache = new ConcurrentHashMap<>();
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

    @Override
    public void handleGameDataResponse(GameDataResponse gameDataResponse)
    {
        synchronized(this)
        {
            for (DealerGameDetails gameData : gameDataResponse.getGameData())
            {
                availableGames.put(gameData.getName(), createGame(gameData.getEntryFee(), 
                        gameData.isAllowStrategy()));
                liveGameCache.put(gameData.getName(), new PriorityBlockingQueue<GameContext>());
            }
        }
    }

    private Game createGame(double entryFee, boolean allowStrategy)
    {
        // TODO Use GameFactory here
        return new RockPaperScissors(2, entryFee);
    }

    @Override
    public synchronized Message handleGameExecuteEvent(GameRequest gameRequest)
    {
        System.out.println("Received Game To execute!");
        System.out.println(gameRequest.getGameName());
        System.out.println(gameRequest.getPlayerDetails().getPlayerId());
        System.out.println(((RPSPlay)(gameRequest.getPlayerDetails().getGamePlay())).getMove());

        Message response = null;
        PlayerDetails player = gameRequest.getPlayerDetails();
        String gameName = gameRequest.getGameName();
        Game requestedGame = availableGames.get(gameName);

        // Validate entry fee
        if (player.getEntryFee() < requestedGame.entryFee())
        {
            response = new GameRejectResponse(player.getPlayerId());
            return response;
        }

        PriorityBlockingQueue<GameContext> gameQueue = liveGameCache.get(gameName);
        final GameContext gameContext = gameQueue.poll();
        
        // The following three cases will result in a GameWaitResponse
        // 1. A new game context is created and a player cache is set up for it.
        // 2. If we have received a repeated request from a player.
        // 3. We still have less than the required number of players. This 
        //    is specially useful when there are more than two required 
        //    players.
        if (gameContext == null ||
                (gameContext.getPlayerCache().contains(player)) ||
                (gameContext.getPlayerCache().size() < 
                        (requestedGame.requiredNumberOfPlayers() - 1)))
        {
            Future<UUID> queuedPlayer = executor.submit(new Callable<UUID>(){

                @Override
                public UUID call() throws Exception
                {
                    GameContext gameCtx = null;
                    // TODO Use GameFactory here
                    if (gameContext == null)
                    {
                        gameCtx = 
                            new RPSGameContext((RockPaperScissors) availableGames.get(gameName));
                    }
                    else
                    {
                        gameCtx = gameContext;
                    }
                    
                    gameCtx.setupMatch(player);

                    gameQueue.add(gameCtx);
                    return player.getPlayerId();
                }});

            try
            {
                response = new GameWaitResponse(queuedPlayer.get());
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            // Execute Game Match and return a CasinoGameCompleteResponse
            Future<Response> gameResults = 
                    executor.submit(new Callable<Response>(){

                @Override
                public Response call() throws Exception
                {
                    Map<String, List<PlayerDetails>> finalResults = gameContext.executeGame(player); 
                    return getCasinoGameCompleteResponse(requestedGame, finalResults);
                }});

            try
            {
                response = gameResults.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }
    

    return response;
            
    }
    
    private synchronized Response getCasinoGameCompleteResponse(Game game, 
            Map<String, List<PlayerDetails>> finalResults)
    {
        boolean matchTied = finalResults.containsKey(CasinoConstants.TIE);
        double houseDeposit = matchTied ? game.payOut() : 0;
        Map<UUID, Double> playerResults = 
                getPlayerResults(matchTied, finalResults, game);
        
        return new CasinoGameCompleteResponse(dealerId, houseDeposit, playerResults);
    }

    private synchronized ConcurrentMap<UUID, Double> getPlayerResults(boolean matchTied, 
            Map<String, List<PlayerDetails>> finalResults, Game game)
    {
        ConcurrentMap<UUID, Double> playerResults = new ConcurrentHashMap<>();
        List<PlayerDetails> players = null;
        if (matchTied)
        {
            // Others
            players = finalResults.get(CasinoConstants.OTHERS);
            for (PlayerDetails player : players)
            {
                double playerReturn = player.getEntryFee() - game.entryFee();
                playerResults.put(player.getPlayerId(), playerReturn);
            }
        }
        else
        {
            // Winners
            players = finalResults.get(CasinoConstants.WINNER);
            for (PlayerDetails player : players)
                playerResults.put(player.getPlayerId(), game.payOut());
            
            // Others
            players = finalResults.get(CasinoConstants.OTHERS);
            for (PlayerDetails player : players)
            {
                double playerReturn = player.getEntryFee() - game.entryFee();
                playerResults.put(player.getPlayerId(), playerReturn);
            }
        }
        
        return playerResults;
    }

}
