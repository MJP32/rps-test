package com.tw.casino.actor;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.tw.casino.IDealer;
import com.tw.casino.connection.messages.CasinoGameCompleteResponse;
import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.util.Constants;

public class CasinoManagerTest
{
    private CasinoManager casinoManager = new CasinoManager();
    
    @Test
    public void testInitializeGameData()
    {
        casinoManager.initialize();
        
        assertEquals(1, casinoManager.getGameDealerCache().size());
        assertTrue(casinoManager.getGameDealerCache().containsKey(Constants.RPS));
    }
    
    @Test
    public void testDealerAssignmentByPolicy()
    {
        casinoManager.initialize();
        
        IDealer dealerOne = new Dealer();
        IDealer dealerTwo = new Dealer();
        
        casinoManager.registerDealer(dealerOne.getDealerId());
        casinoManager.registerDealer(dealerTwo.getDealerId());
        
        UUID assigned = casinoManager.assignDealerForGame(Constants.RPS);
        assertEquals(dealerTwo.getDealerId(), assigned);
    }
    
    @Test
    public void testHandleGameListRequest()
    {
        Message response = casinoManager.handleGameListRequest(
                new GameListRequest(UUID.randomUUID()));
        
        assertTrue(response instanceof GameListResponse);
        assertEquals(casinoManager.getGameDetailsList(), 
                ((GameListResponse) response).getAvailableGames());      
    }
    
    @Test
    public void testHandleGameDataRequest()
    {
        casinoManager.initialize();
        Message response = casinoManager.handleGameDataRequest(
                new GameDataRequest(UUID.randomUUID()));
        
        assertTrue(response instanceof GameDataResponse);
        assertEquals(casinoManager.getGameData(), 
                ((GameDataResponse) response).getGameData());
        
    }
    
    @Test
    public void testCasinoGameCompleteResponse()
    {
        Map<UUID, Double> playerResults = new HashMap<>();
        UUID playerOne = UUID.randomUUID();
        UUID playerTwo = UUID.randomUUID();
        
        playerResults.put(playerOne, Double.valueOf(0));
        playerResults.put(playerTwo, Double.valueOf(10));
        
        List<GameCompleteResponse> responses = 
                casinoManager.handleGameCompleteResponse(
                        new CasinoGameCompleteResponse(UUID.randomUUID(), 
                                0, playerResults));
        
        assertEquals(0, 
                Double.compare(Double.valueOf(0), casinoManager.getHouseAccountBalance()));
        
        double noWin = Double.valueOf(0);
        double win = Double.valueOf(10);
        
        // Check winnings were correctly assigned.
        for (GameCompleteResponse response : responses)
        {
            if (response.getPlayerId().equals(playerOne))
                assertEquals(0, Double.compare(noWin, response.getWinnings()));
            else if (response.getPlayerId().equals(playerTwo))
                assertEquals(0, Double.compare(win, response.getWinnings()));
        }
        
    }
}
