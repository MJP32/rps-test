package com.tw.casino.game.rps;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tw.casino.connection.messages.data.PlayerDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameId;
import com.tw.casino.util.Constants;

public class TwoPlayerRockPaperScissors implements Game
{   
    private static final String NAME = Constants.RPS;
    private static final int REQUIRED_PLAYERS = 2;
    
    private final GameId id;
    private final double entryFee;
    
    public TwoPlayerRockPaperScissors(double entryFee)
    {
        id = new GameId(UUID.randomUUID());
        this.entryFee = entryFee;
    }

    @Override
    public GameId getGameId()
    {
        return this.id;
    }
    
    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public int requiredNumberOfPlayers()
    {
        return REQUIRED_PLAYERS;
    }

    @Override
    public double entryFee()
    {
        return entryFee;
    }
    

    @Override
    public double payOut()
    {
        return (REQUIRED_PLAYERS * entryFee);
    }
    
    @Override
    public synchronized Map<String, List<PlayerDetails>> playMatch(PlayerDetails[] players)
    {
        if (players.length != REQUIRED_PLAYERS)
            throw new IllegalStateException("Invalid number of players in the game.");
        
        RPSPlay onePlay = (RPSPlay) players[0].getGamePlay();
        RPSPlay twoPlay = (RPSPlay) players[1].getGamePlay();
        
        RPSMove playerOneMove = onePlay.getMove();
        RPSMove playerTwoMove = twoPlay.getMove();      
        
        PlayerDetails winner = null;
        PlayerDetails other = null;
        if (playerOneMove == RPSMove.PAPER && playerTwoMove == RPSMove.ROCK)
        {
            winner = players[0];
            other = players[1];
        }
        else if (playerOneMove == RPSMove.ROCK && playerTwoMove == RPSMove.SCISSORS)
        {
            winner = players[0];
            other = players[1];
        }
        else if (playerOneMove == RPSMove.SCISSORS && playerTwoMove == RPSMove.PAPER)
        {
            winner = players[0];
            other = players[1];
        }
        else if (playerOneMove == RPSMove.ROCK && playerTwoMove == RPSMove.PAPER)
        {
            winner = players[1];
            other = players[0];
        }
        else if (playerOneMove == RPSMove.SCISSORS && playerTwoMove == RPSMove.ROCK)
        {
            winner = players[1];
            other = players[0];
        }
        else if (playerOneMove == RPSMove.PAPER && playerTwoMove == RPSMove.SCISSORS)
        {
            winner = players[1];
            other = players[0];
        }
        else if (playerOneMove == playerTwoMove)
            winner = null; // Empty player for tied result

        ConcurrentHashMap<String, List<PlayerDetails>> results = 
                new ConcurrentHashMap<String, List<PlayerDetails>>();
        
        List<PlayerDetails> playerDetails;
        if (winner != null)
        {
            playerDetails = new CopyOnWriteArrayList<>();
            playerDetails.add(winner);
            results.put(Constants.WINNER, playerDetails);
            
            playerDetails = new CopyOnWriteArrayList<>();
            playerDetails.add(other);
            results.put(Constants.OTHERS, playerDetails);
        }
        else
        {
            playerDetails = new CopyOnWriteArrayList<>();
            results.put(Constants.TIE, playerDetails);
            
            playerDetails = new CopyOnWriteArrayList<>();
            playerDetails.add(players[0]);
            playerDetails.add(players[1]);
            results.put(Constants.OTHERS, playerDetails);
        }
        
        return results;
    }

}
