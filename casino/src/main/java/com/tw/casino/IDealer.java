package com.tw.casino;

public interface IDealer {
	
    void processRequest(IRequest request);
    
	String offerGameMenu();
	
	void createRequestedGame(String message);
	
}
