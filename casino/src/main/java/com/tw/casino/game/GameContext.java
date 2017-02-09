package com.tw.casino.game;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tw.casino.connection.messages.data.PlayerDetails;

/**
 * Provides a context to store and execute Game matches by 
 * the Dealer.
 *      
 * @author siddharths1787
 *
 */
public interface GameContext extends Comparable<GameContext>
{
    /**
     * This would be useful in multi-round Games.
     * @return
     */
    UUID getMatchId();
    
    /**
     * The time at which the context was created.
     * @return
     */
    long getTimestamp();
    
    /**
     * Checks if the Player has already been added to the wait
     * list for the Game.
     * @param playerId
     * @return
     */
    boolean hasPlayer(UUID playerId);
    
    /**
     * Returns the number of Players ready to play the Game.
     * @return
     */
    int playerCount();
    
    /**
     * This method adds players to the player cache only once per match for
     * a game.
     * @param playerDetails
     */
    void setupMatch(PlayerDetails playerDetails);
    
    /**
     * Executes the game when the final player is received.
     * 
     * @param playerDetails
     * @return
     */
    Map<String, List<PlayerDetails>> executeGame(PlayerDetails playerDetails);
}
