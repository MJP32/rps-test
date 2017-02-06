package com.tw.casino.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.Game;
import com.tw.casino.game.rps.RPSStrategy;
import com.tw.casino.game.rps.RockPaperScissors;
import com.tw.casino.util.CasinoConstants;

import junit.framework.TestCase;

public class DealerTest extends TestCase
{
    private Dealer dealer = new Dealer();
    
    private static List<Game> games = new ArrayList<>();
    static
    {
        games.add(new RockPaperScissors(2, 5.0));
    }
    private GameDataResponse gameDataResponse = new GameDataResponse(dealer.getDealerId(), games);

    private GameRequest gameRequest;
    
    private PlayerProfile playerOne;
    //private PlayerProfile playerTwo;
    private PlayerProfile playerThree;
    
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
        playerOne = new PlayerProfile(UUID.randomUUID(), 5.0, new RPSStrategy());
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        
        Response response = dealer.handleGameRequest(gameRequest);
        assertTrue(response instanceof GameWaitResponse);
        assertEquals(playerOne.getPlayerId(), ((GameWaitResponse) response).getPlayerId());
    }
    
    @Test
    public void testHandleGameRequestAsksRepeatedRequestsToWait()
    {
        dealer.handleGameDataResponse(new GameDataResponse(dealer.getDealerId(), games));
        playerOne = new PlayerProfile(UUID.randomUUID(), 5.0, new RPSStrategy());      
        gameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        
        dealer.handleGameRequest(gameRequest);
        
        GameRequest anotherGameRequest = new GameRequest(playerOne, CasinoConstants.RPS);
        Response response = dealer.handleGameRequest(anotherGameRequest);
        
        assertTrue(response instanceof GameWaitResponse);
        assertEquals(playerOne.getPlayerId(), ((GameWaitResponse) response).getPlayerId());
    }
    
    @Test
    public void testHandleGameRequestValidatesEntryFee()
    {
        dealer.handleGameDataResponse(new GameDataResponse(dealer.getDealerId(), games));

        playerThree = new PlayerProfile(UUID.randomUUID(), 4.0, new RPSStrategy());
        gameRequest = new GameRequest(playerThree, CasinoConstants.RPS);
        Response response = dealer.handleGameRequest(gameRequest);
        
        assertTrue(response instanceof GameRejectResponse);
        assertEquals(playerThree.getPlayerId(), ((GameRejectResponse) response).getPlayerId());
    }
}
