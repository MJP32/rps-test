package com.tw.casino;

import com.tw.casino.game.rps.RPSStrategy;

public interface IPlayer {

	public void setStartingAccountBalance(int startingBalance);
	
    public void setRPSStrategy(RPSStrategy rpsStrategy);
    
}
