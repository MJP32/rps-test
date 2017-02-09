package com.tw.casino;

import java.util.UUID;

import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.game.Game;

/**
 * Creates and executes games for incoming Player requests. It requests
 * the CasinoManager to provide it a list of Games it can execute once
 * online. The Dealer also validates users asking them to wait or receive
 * payout as per the game results. Whenever a game ends in a tie, the 
 * Dealer sends the game payout to the CasinoManager for adding to the 
 * House account.
 * 
 * @author siddharths1787
 *
 */
public interface IDealer 
{
    /**
     * Unique system generated identifier for Dealer
     * @return
     */
    UUID getDealerId();
    
    /**
     * Creates a Game instance as requested.
     * @param name
     * @param entryFee
     * @return
     */
    Game createGame(String name, double entryFee);
    
    /**
     * Creates the message to query the CasinoManager for 
     * information about Games to offer.
     * @return
     */
    Message createGameDataRequest();
    
    /**
     * Updates its local cache of Games in anticipation of Player 
     * requests.
     * @param gameDataResponse
     */
    void handleGameDataResponse(GameDataResponse gameDataResponse);
    
    /**
     * Validates if a user has paid the required entry fee and can
     * be added to the Player list for the Game. Once complete sends 
     * a message to the CasinoManager with player results and any 
     * deposit for the House account.
     * @param gameExecuteEvent
     * @return
     */
    Message handleGameExecuteEvent(GameRequest gameExecuteEvent);
}
