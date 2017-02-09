package com.tw.casino;

import java.util.List;
import java.util.UUID;

import com.tw.casino.connection.messages.CasinoGameCompleteResponse;
import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.Message;

/**
 * Associated with a server instance, the CasinoManager is not meant 
 * for interaction with the user. It manages the forwarding of messages 
 * between the Dealer and Player and maintaining the House account. 
 * 
 * @author siddharths1787
 */
public interface ICasinoManager
{
    /**
     * Start up related tasks such as loading data.
     */
    void initialize();
    
    /**
     * Updates the House account with the deposit
     * @param houseDeposit
     */
    void updateHouseAccountBalance(double houseDeposit);
    
    /**
     * Adds the dealer to local registry.
     * @param dealerId
     */
    void registerDealer(UUID dealerId);
    
    /**
     * Assigns a dealer per game request according to the 
     * configured policy.
     * @param name
     * @return
     */
    UUID assignDealerForGame(String name);
    
    /**
     * Responds to a Player with details that the CasinoManager
     * wishes to share about the various offered games.
     * @param gameListRequest
     * @return
     */
    Message handleGameListRequest(GameListRequest gameListRequest);
    
    /**
     * Responds to a Dealer with all necessary information the 
     * Dealer will require to set up assigned games.
     * @param gameDataRequest
     * @return
     */
    Message handleGameDataRequest(GameDataRequest gameDataRequest);
    
    /**
     * Updates the House account if the Dealer sent a greater than zero
     * deposit and creates responses to be sent out to Players.
     * @param casinoGameCompleteResponse
     * @return
     */
    List<GameCompleteResponse> handleGameCompleteResponse(CasinoGameCompleteResponse 
            casinoGameCompleteResponse);
}

