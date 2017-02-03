package com.tw.casino;

import com.tw.casino.simulator.DefaultRPSStrategy;

public interface IPlayer {

	public void setStartingAccountBalance(int startingBalance);
	
    public void setRPSStrategy(DefaultRPSStrategy rpsStrategy);
    
}
