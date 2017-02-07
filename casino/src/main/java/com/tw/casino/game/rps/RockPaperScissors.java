package com.tw.casino.game.rps;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.tw.casino.IPlayer;
import com.tw.casino.actor.PlayerDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameId;
import com.tw.casino.util.CasinoConstants;

public class RockPaperScissors implements Game, Serializable
{
    private static final long serialVersionUID = -675644357093471300L;
    
    private static final String NAME = CasinoConstants.RPS;
    private final GameId id;
    private final int requiredPlayers;
    private final double entryFee;
    
    public RockPaperScissors(int requiredNumberOfPlayers, double entryFee)
    {
        id = new GameId(UUID.randomUUID());
        this.requiredPlayers = requiredNumberOfPlayers;
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
        return requiredPlayers;
    }

    @Override
    public double entryFee()
    {
        return entryFee;
    }
    

    @Override
    public double payOut()
    {
        return (requiredPlayers * entryFee);
    }
    
    @Override
    public Map<String, List<PlayerDetails>> playMatch(PlayerDetails[] players)
    {
        // TODO FIX THIS
        if (players.length != this.requiredPlayers)
            throw new IllegalStateException("Invalid number of players in the game.");
        
        ConcurrentHashMap<String, List<PlayerDetails>> results = 
                new ConcurrentHashMap<String, List<PlayerDetails>>();
        
        PlayerDetails playerOne = players[0];
        PlayerDetails playerTwo = players[1];
        
        RPSMove playerOneMove = RPSMove.PAPER;
        RPSMove playerTwoMove = RPSMove.SCISSORS;
        
        PlayerDetails winner = null;
        if (playerOneMove == RPSMove.PAPER && playerTwoMove == RPSMove.ROCK)
            winner = playerOne;
        else if (playerOneMove == RPSMove.ROCK && playerTwoMove == RPSMove.SCISSORS)
            winner = playerOne;
        else if (playerOneMove == RPSMove.SCISSORS && playerTwoMove == RPSMove.PAPER)
            winner = playerOne;
        else if (playerOneMove == RPSMove.ROCK && playerTwoMove == RPSMove.PAPER)
            winner = playerTwo;
        else if (playerOneMove == RPSMove.SCISSORS && playerTwoMove == RPSMove.ROCK)
            winner = playerTwo;
        else if (playerOneMove == RPSMove.PAPER && playerTwoMove == RPSMove.SCISSORS)
            winner = playerTwo;
        else if (playerOneMove == playerTwoMove)
            winner = null;

        return results;
    }

}
