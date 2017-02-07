package com.tw.casino.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameExecuteEvent;
import com.tw.casino.connection.messages.GameExecuteRejectEvent;
import com.tw.casino.connection.messages.GameExecuteWaitEvent;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GamePlay;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.game.rps.RPSStrategy;
import com.tw.casino.game.rps.RockPaperScissors;
import com.tw.casino.util.CasinoConstants;

import junit.framework.TestCase;

public class DealerTest extends TestCase
{
    private Dealer dealer = new Dealer();
    
    private static List<DealerGameDetails> games = new ArrayList<>();
    static
    {
        games.add(new DealerGameDetails(CasinoConstants.RPS, 5.0, true, 2));
    }
    private GameDataResponse gameDataResponse = new GameDataResponse(dealer.getDealerId(), games);

    private GameExecuteEvent gameExecuteEvent;
    
    private PlayerDetails playerOne;
    //private PlayerProfile playerTwo;
    private PlayerDetails playerThree;
    
    @Test
    public void testHandleGameDataResponseCachesGames()
    {   
        dealer.handleGameDataResponse(gameDataResponse);
        assertEquals(1, dealer.getAvailableGames().size());
    }
    
    @Test
    public void testHandleGameRequestCreatesNewGame()
    {
        dealer.handleGameDataResponse(new GameDataResponse(dealer.getDealerId(), games));
        playerOne = new PlayerDetails(UUID.randomUUID(), 5.0, RPSMove.ROCK);
        gameExecuteEvent = null; //new GameExecuteEvent(dealer.getDealerId(), playerOne, CasinoConstants.RPS);
        
        List<Request> responses = dealer.handleGameExecuteEvent(gameExecuteEvent);
        Request executedEvent = responses.get(0);
        assertTrue(executedEvent instanceof GameExecuteWaitEvent);
        assertEquals(playerOne.getPlayerId(), ((GameExecuteWaitEvent) executedEvent).getPlayerId());
    }
    
    @Test
    public void testHandleGameRequestAsksRepeatedRequestsToWait()
    {
        dealer.handleGameDataResponse(new GameDataResponse(dealer.getDealerId(), games));
        playerOne = new PlayerDetails(UUID.randomUUID(), 5.0, RPSMove.ROCK);      
        gameExecuteEvent = null; //new GameExecuteEvent(dealer.getDealerId(), playerOne, CasinoConstants.RPS);
        
        dealer.handleGameExecuteEvent(gameExecuteEvent);
        
        GameExecuteEvent anotherGameRequest = null;//new GameExecuteEvent(dealer.getDealerId(), playerOne, CasinoConstants.RPS);
        List<Request> responses = dealer.handleGameExecuteEvent(anotherGameRequest);
        Request executedEvent = responses.get(0);
        
        assertTrue(executedEvent instanceof GameExecuteWaitEvent);
        assertEquals(playerOne.getPlayerId(), ((GameExecuteWaitEvent) executedEvent).getPlayerId());
    }
    
    @Test
    public void testHandleGameRequestValidatesEntryFee()
    {
        dealer.handleGameDataResponse(new GameDataResponse(dealer.getDealerId(), games));

        playerThree = new PlayerDetails(UUID.randomUUID(), 4.0, RPSMove.ROCK);
        gameExecuteEvent = null;//new GameExecuteEvent(dealer.getDealerId(), playerThree, CasinoConstants.RPS);
        List<Request> responses = dealer.handleGameExecuteEvent(gameExecuteEvent);
        Request executedEvent = responses.get(0);
        
        assertTrue(executedEvent instanceof GameExecuteRejectEvent);
        assertEquals(playerThree.getPlayerId(), ((GameExecuteRejectEvent) executedEvent).getPlayerId());
    }
}
