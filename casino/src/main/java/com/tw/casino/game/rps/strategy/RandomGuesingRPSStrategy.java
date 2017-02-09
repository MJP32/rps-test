package com.tw.casino.game.rps.strategy;

import java.util.Random;
import java.util.UUID;

import com.tw.casino.game.GamePlay;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.util.Constants;
import com.tw.casino.util.EmployStrategy;

@EmployStrategy
public class RandomGuesingRPSStrategy implements GameStrategy
{
    private Random random;
    
    public RandomGuesingRPSStrategy()
    {
        random = new Random();
    }
    
    @Override
    public String getName()
    {
        return Constants.RANDOM_GUESSING;
    }

    @Override
    public GamePlay computeNextPlay(UUID matchId)
    {
        int index = random.nextInt() % 3;
        
        RPSMove nextMove;
        
        if (index == 0)
            nextMove = RPSMove.ROCK;
        else if (index == 1)
            nextMove = RPSMove.PAPER;
        else
            nextMove = RPSMove.SCISSORS;
        
        GamePlay play = new RPSPlay(nextMove);
        
        return play;
    }
}
