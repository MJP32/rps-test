package com.tw.casino.game;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.tw.casino.connection.messages.data.PlayerDetails;

public interface GameContext extends Comparable<GameContext>
{
    UUID getMatchId();
    
    long getTimestamp();
    
    boolean hasPlayer(UUID playerId);
    
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
