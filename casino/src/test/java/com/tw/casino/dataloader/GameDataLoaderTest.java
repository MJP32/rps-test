package com.tw.casino.dataloader;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.util.Constants;

public class GameDataLoaderTest
{
    private GameDataLoader gameDataLoader = new DefaultGameDataLoader();
    
    @Test
    public void testLoadData()
    {
        gameDataLoader.loadConfiguredGames();
        
        assertTrue(gameDataLoader.availableGames().size() > 0);
        
        for (GameDetails details : gameDataLoader.availableGames())
        {
            assertEquals(Constants.RPS, details.getName());
            assertEquals(0, Double.compare(5.0, details.getEntryFee()));
        }
        
    }
}
