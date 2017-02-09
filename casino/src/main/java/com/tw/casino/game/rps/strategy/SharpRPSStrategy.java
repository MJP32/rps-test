package com.tw.casino.game.rps.strategy;

import java.util.UUID;

import com.tw.casino.game.GamePlay;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.game.rps.RPSMove;
import com.tw.casino.game.rps.RPSPlay;
import com.tw.casino.util.Constants;

public class SharpRPSStrategy implements GameStrategy
{
    private RPSPlay play;
    
    public SharpRPSStrategy()
    {
        play = new RPSPlay(RPSMove.SCISSORS);
    }
    
    @Override
    public String getName()
    {
        return Constants.ALWAYS_SHARP;
    }

    @Override
    public GamePlay computeNextPlay(UUID matchId)
    {
        return play;
    }

}
