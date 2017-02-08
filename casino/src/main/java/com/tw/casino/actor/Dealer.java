package com.tw.casino.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.tw.casino.connection.messages.data.DealerGameDetails;
import com.tw.casino.connection.messages.data.PlayerDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameContext;
import com.tw.casino.game.rps.RPSGameContext;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.game.rps.TwoPlayerRockPaperScissors;
import com.tw.casino.util.Constants;


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

    // For Test
    public Map<String, Game> getAvailableGames()
    {
        return availableGames;
    }
    

    // For Test
    public ConcurrentMap<String, PriorityBlockingQueue<GameContext>> getLiveGameCache()
    {
        return liveGameCache;
    }

    @Override
    public void handleGameDataResponse(GameDataResponse gameDataResponse)
    {
        synchronized(this)
        {
            for (DealerGameDetails gameData : gameDataResponse.getGameData())
            {
                availableGames.put(gameData.getName(), createGame(gameData.getEntryFee()));
                liveGameCache.put(gameData.getName(), new PriorityBlockingQueue<GameContext>());
            }
        }
    }

    private Game createGame(double entryFee)
    {
        // TODO Use GameFactory here
        return new TwoPlayerRockPaperScissors(entryFee);
    }

    @Override
    public Message handleGameExecuteEvent(GameRequest gameRequest)
    {
        Message response = null;
        PlayerDetails player = gameRequest.getPlayerDetails();
        String gameName = gameRequest.getGameName();
        Game requestedGame = availableGames.get(gameName);

        // Validate entry fee
        if (player.getEntryFee() < requestedGame.entryFee())
        {
            System.out.println("Player paid: " + player.getEntryFee());
            response = new GameRejectResponse(player.getPlayerId());
            return response;
        }

        PriorityBlockingQueue<GameContext> gameQueue = liveGameCache.get(gameName);
        final GameContext gameContext = gameQueue.peek();

        // The following three cases will result in a GameWaitResponse
        // 1. A new game context is created and a player cache is set up for it.
        // 2. If we have received a repeated request from a player.
        // 3. We still have less than the required number of players. This 
        //    is specially useful when there are more than two required 
        //    players.
        if (gameContext == null ||
                (gameContext.hasPlayer(player.getPlayerId())) ||
                (gameContext.playerCount() < 
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
                                new RPSGameContext((TwoPlayerRockPaperScissors) availableGames.get(gameName));
                    }
                    else
                    {
                        gameCtx = gameContext;
                    }                  
                    gameCtx.setupMatch(player);
                    gameQueue.offer(gameCtx);

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

                            boolean matchTied = finalResults.containsKey(Constants.TIE);
                            double houseDeposit = matchTied ? requestedGame.payOut() : 0;
                            Map<UUID, Double> playerResults = 
                                    GameExecutorUtil.getPlayerResults(matchTied, finalResults, requestedGame);
                            Response resp = new CasinoGameCompleteResponse(dealerId, houseDeposit, playerResults);

                            gameQueue.remove(gameContext);
                            return resp;
                        }});

            try
            {
                response = gameResults.get();
                gameQueue.remove(gameContext);
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        return response;
    }

    static class GameExecutorUtil
    {
        private static ConcurrentMap<UUID, Double> getPlayerResults(boolean matchTied, 
                Map<String, List<PlayerDetails>> finalResults, Game game)
        {
            ConcurrentMap<UUID, Double> playerResults = new ConcurrentHashMap<>();
            List<PlayerDetails> players = null;
            if (matchTied)
            {
                // Others
                players = finalResults.get(Constants.OTHERS);
                for (PlayerDetails player : players)
                {
                    double playerReturn = player.getEntryFee() - game.entryFee();
                    playerResults.put(player.getPlayerId(), playerReturn);
                }
            }
            else
            {
                // Winners
                players = finalResults.get(Constants.WINNER);
                for (PlayerDetails player : players)
                    playerResults.put(player.getPlayerId(), game.payOut());

                // Others
                players = finalResults.get(Constants.OTHERS);
                for (PlayerDetails player : players)
                {
                    double playerReturn = player.getEntryFee() - game.entryFee();
                    playerResults.put(player.getPlayerId(), playerReturn);
                }
            }

            return playerResults;
        }


    }

}
