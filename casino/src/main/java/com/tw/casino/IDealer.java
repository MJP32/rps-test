package com.tw.casino;

import com.tw.casino.connection.messages.Request;

public interface IDealer 
{
    void processRequest(Request request);
    
	String offerGameMenu();
	
	void createRequestedGame(String message);
	
}
