package com.tw.casino.simulator;


public abstract class PlayerInfo {

	abstract int getStartingAccountBalance();
	
    abstract DefaultRPSStrategy getRPSStrategy();
}
