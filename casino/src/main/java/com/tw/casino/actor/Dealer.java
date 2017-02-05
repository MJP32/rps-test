package com.tw.casino.actor;

import com.tw.casino.IDealer;
import com.tw.casino.connection.messages.Request;


public class Dealer implements IDealer {

	@Override
	public String offerGameMenu() {
		return "Today you can play:\n1.Rock Paper Scissors";

	}

	@Override
	public void createRequestedGame(String message) {
		System.out.println(message);
	}

    @Override
    public void processRequest(Request request)
    {

        
    }

}
