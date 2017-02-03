package com.tw.casino.simulator;

import com.tw.casino.game.GameId;
import com.tw.casino.game.rps.RPSMove;

public class DefaultRPSStrategy {
	
	public RPSMove playGame(GameId gameId)
	{
		return RPSMove.ROCK;
	}
}
