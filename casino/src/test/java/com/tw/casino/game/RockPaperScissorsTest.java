package com.tw.casino.game;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.tw.casino.connection.messages.data.PlayerDetails;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.game.rps.TwoPlayerRockPaperScissors;
import com.tw.casino.util.Constants;

public class RockPaperScissorsTest
{
    private TwoPlayerRockPaperScissors rps = new TwoPlayerRockPaperScissors(5.0);
    
    @Test
    public void testMatchResults()
    {
        RPSPlay onePlay = new RPSPlay(RPSMove.ROCK);
        
        RPSPlay twoPlay = new RPSPlay(RPSMove.PAPER);
        
        PlayerDetails[] players = new PlayerDetails[2];
        players[0] = new PlayerDetails(UUID.randomUUID(), 5.0, onePlay);
        players[1] = new PlayerDetails(UUID.randomUUID(), 5.0, twoPlay);
        
        Map<String, List<PlayerDetails>> results = rps.playMatch(players);
        assertTrue(results.get(Constants.WINNER).contains(players[1]));
    }
}
