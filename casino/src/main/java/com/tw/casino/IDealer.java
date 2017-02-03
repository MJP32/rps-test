package com.tw.casino;

public interface IDealer {
	
    void processRequest(Request request);
    
	String offerGameMenu();
	
	void createRequestedGame(String message);
	
}
