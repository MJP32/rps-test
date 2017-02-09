package com.tw.casino.actor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.connection.messages.data.PlayerDetails;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.game.rps.strategy.RandomGuesingRPSStrategy;
import com.tw.casino.util.Constants;

public class PlayerTest
{
    private Player player = new Player(50);
    private static final List<GameDetails> gameList = new ArrayList<GameDetails>();
    public static GameDetails gameDetails = new GameDetails(Constants.RPS, 5);
    
    static
    {
        gameList.add(gameDetails);
    }
    
    public GameListResponse gameListResponse;
    public GameWaitResponse gameWaitResponse;
    public GameRejectResponse gameRejectResponse;
    public GameCompleteResponse gameCompleteResponse;
    
    @Test
    public void testCreateGameRequest()
    {
        player.handleGameListResponse(new GameListResponse(player.getPlayerId(), gameList));
        
        Message request = player.createGameRequest(Constants.RPS);
        assertTrue(request instanceof GameRequest);
        
        GameRequest gameRequest = (GameRequest) request;
        PlayerDetails details = gameRequest.getPlayerDetails();
        assertEquals(player.getPlayerId(), details.getPlayerId());
        assertEquals(0, Double.compare(gameDetails.getEntryFee(), details.getEntryFee()));
        assertEquals(RPSMove.SCISSORS, (((RPSPlay) details.getGamePlay()).getMove()));
    }
    
    @Test
    public void testGameWinUpdatesBalance()
    {
        gameCompleteResponse = new GameCompleteResponse(player.getPlayerId(), 10);
        player.handleGameResponse(gameCompleteResponse);
        
        assertEquals(0, Double.compare(60, player.getAccountBalance()));
    }
    
    @Test
    public void testGameWaitResponse()
    {
        gameWaitResponse = new GameWaitResponse(player.getPlayerId());
        String message = player.handleGameResponse(gameWaitResponse);
        
        assertEquals(Constants.AWAIT, message);
    }
    
    @Test
    public void testGameRejectResponse()
    {
        gameRejectResponse = new GameRejectResponse(player.getPlayerId());
        String message = player.handleGameResponse(gameRejectResponse);
        
        assertEquals(Constants.REJECT, message);
    }
    
    @Test
    public void testPlayerUsesAnnotatedStrategy()
    {
        player.loadPlayerStrategy();
        GameStrategy strategy = player.getGameStrategy();
        
        assertTrue(strategy instanceof RandomGuesingRPSStrategy);
    }
}
