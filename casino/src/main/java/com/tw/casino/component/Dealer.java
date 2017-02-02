package com.tw.casino.component;

import com.tw.casino.IDealer;
import com.tw.casino.IRequest;

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
    public void processRequest(IRequest request)
    {
        if (request instanceof SimpleRequest)
        {
            SimpleRequest sr = (SimpleRequest) request;
            System.out.println(sr.getMessage());
        }
        
    }

}
