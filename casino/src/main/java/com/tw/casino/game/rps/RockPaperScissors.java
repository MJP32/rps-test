package com.tw.casino.game.rps;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.tw.casino.IPlayer;
import com.tw.casino.actor.PlayerProfile;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameId;
import com.tw.casino.util.CasinoConstants;

public class RockPaperScissors implements Game
{
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
    public PlayerProfile executeGame(PlayerProfile[] players)
    {
        if (players.length != this.requiredPlayers)
            throw new IllegalStateException("Invalid number of players in the game.");
        
        PlayerProfile playerOne = players[0];
        PlayerProfile playerTwo = players[1];
        
        RPSMove playerOneMove = RPSMove.PAPER;
        RPSMove playerTwoMove = RPSMove.SCISSORS;
        
        PlayerProfile winner = null;
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

        return winner;
    }

}
