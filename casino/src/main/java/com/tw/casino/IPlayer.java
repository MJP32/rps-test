package com.tw.casino;

import com.tw.casino.game.GameStrategy;

public interface IPlayer 
{
    public void setAccountBalance(double accountBalance);

    public void setGameStrategy(GameStrategy strategy);

}
