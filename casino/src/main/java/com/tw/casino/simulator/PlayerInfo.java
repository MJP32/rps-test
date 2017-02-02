package com.tw.casino.simulator;

import com.tw.casino.game.rps.RPSStrategy;

public abstract class PlayerInfo {

	abstract int getStartingAccountBalance();
	
    abstract RPSStrategy getRPSStrategy();
}
