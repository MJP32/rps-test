package com.tw.casino;

import java.util.UUID;

import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.GameStrategy;

/**
 * The Player interface that allows maintaining an account balance
 * to play with and various strategies that can be chosen at startup
 * to offer the best move.
 * 
 * @author siddharths1787
 *
 */
public interface IPlayer 
{
    /**
     * System generated unique identifier for the Player.
     * @return
     */
    UUID getPlayerId();
    
    /**
     * Allows to set the account balance.
     * @param accountBalance
     */
    void setAccountBalance(double accountBalance);
    
    /**
     * Returns the current account balance.
     * @return
     */
    double getAccountBalance();

    /**
     * Set the strategy to be employed for the Game.
     * @param strategy
     */
    void setGameStrategy(GameStrategy strategy);
    
    /**
     * Load the strategy at startup. 
     */
    void loadPlayerStrategy();
    
    /**
     * Creates a request for the CasinoManager to query for 
     * the available Games.
     * @return
     */
    Request createGameListRequest();
    
    /**
     * Creates the request to play a game as indicated by the name.
     * @param name
     * @return
     */
    Request createGameRequest(String name);
    
    /**
     * Updates the local cache to store information about available
     * Games.
     * @param response
     * @return
     */
    String handleGameListResponse(Response response);
    
    /**
     * Manages the result of GameRequest which could be to wait,
     * be rejected or update as per the end game result. 
     * @param response
     * @return
     */
    String handleGameResponse(Response response);
}
