package com.tw.casino.actor;

import com.tw.casino.IDealer;
import com.tw.casino.Request;
import com.tw.casino.connection.messages.SimpleRequest;

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
        if (request instanceof SimpleRequest)
        {
            SimpleRequest sr = (SimpleRequest) request;
            System.out.println(sr.getMessage());
        }
        
    }

}
