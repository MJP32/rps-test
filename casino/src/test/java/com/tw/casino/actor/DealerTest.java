package com.tw.casino.actor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.tw.casino.connection.messages.CasinoGameCompleteResponse;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.util.CasinoConstants;

public class DealerTest
{
    private Dealer dealer = new Dealer();
    private static List<DealerGameDetails> gameDetails = new ArrayList<DealerGameDetails>();
    static
    {
        gameDetails.add(new DealerGameDetails(CasinoConstants.RPS, 5.0, 2));
    }
    private GameDataResponse gameDataResponse;
    private GameRequest gameRequest;
    
    private PlayerDetails playerOne;
    private PlayerDetails playerTwo;
    
    @Test
    public void testGameLoad()
    {
        gameDataResponse = new GameDataResponse(dealer.getDealerId(), gameDetails);
        dealer.handleGameDataResponse(gameDataResponse);
        
        assertEquals(1, dealer.getAvailableGames().size());
    }
    
    @Test
    public void testGameCreation()
    {
        gameDataResponse = new GameDataResponse(dealer.getDealerId(), gameDetails);
        dealer.handleGameDataResponse(gameDataResponse);
        
        playerOne = new PlayerDetails(UUID.randomUUID(), 5.0, new RPSPlay(RPSMove.ROCK));
        
        Message response = null;
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        response = dealer.handleGameExecuteEvent(gameRequest);
        assertTrue(response instanceof GameWaitResponse);
        assertEquals(playerOne.getPlayerId(), ((GameWaitResponse) response).getPlayerId());
        
        assertEquals(1, dealer.getLiveGameCache().get(CasinoConstants.RPS).size());
    }

    @Test
    public void testGameExecution()
    {
        gameDataResponse = new GameDataResponse(dealer.getDealerId(), gameDetails);
        dealer.handleGameDataResponse(gameDataResponse);
        
        playerOne = new PlayerDetails(UUID.randomUUID(), 5.0, new RPSPlay(RPSMove.ROCK));
        playerTwo = new PlayerDetails(UUID.randomUUID(), 5.0, new RPSPlay(RPSMove.ROCK));
        
        Message response = null;
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        response = dealer.handleGameExecuteEvent(gameRequest);
        assertTrue(response instanceof GameWaitResponse);
        
        gameRequest = new GameRequest(playerTwo, CasinoConstants.RPS);
        response = dealer.handleGameExecuteEvent(gameRequest);
        assertTrue(response instanceof CasinoGameCompleteResponse);
    }
    
    @Test
    public void testRepeatedRequest()
    {
        gameDataResponse = new GameDataResponse(dealer.getDealerId(), gameDetails);
        dealer.handleGameDataResponse(gameDataResponse);
        
        playerOne = new PlayerDetails(UUID.randomUUID(), 5.0, new RPSPlay(RPSMove.ROCK));
        
        Message response = null;
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        response = dealer.handleGameExecuteEvent(gameRequest);
        assertTrue(response instanceof GameWaitResponse);
        
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        response = dealer.handleGameExecuteEvent(gameRequest);
        assertTrue(response instanceof GameWaitResponse);
        
    }
    
    @Test
    public void testGameRejectExecution()
    {
        gameDataResponse = new GameDataResponse(dealer.getDealerId(), gameDetails);
        dealer.handleGameDataResponse(gameDataResponse);
        
        playerOne = new PlayerDetails(UUID.randomUUID(), 2.0, new RPSPlay(RPSMove.ROCK));
        
        Message response = null;
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        response = dealer.handleGameExecuteEvent(gameRequest);
        assertTrue(response instanceof GameRejectResponse);
        
    }
}
