package com.tw.casino.dataloader;

import java.util.List;

import com.tw.casino.connection.messages.data.DealerGameDetails;
import com.tw.casino.connection.messages.data.GameDetails;

/**
 * Loads up all data necessary for the Casino operations. As it 
 * is expected to handle the I/O at startup it doesn't affect the
 * runtime performance of the Casino.
 * 
 * @author siddharths1787
 *
 */
public interface GameDataLoader
{
    /**
     * Initialize data as provided or configured.
     */
    void loadConfiguredGames();
    
    /**
     * Provide a list of games suitable for Dealer discretion.
     * @return
     */
    List<DealerGameDetails> getGames();
    
    /**
     * Provide a list of game appropriate for Players.
     * @return
     */
    List<GameDetails> availableGames();
}
