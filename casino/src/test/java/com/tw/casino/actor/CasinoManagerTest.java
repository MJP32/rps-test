package com.tw.casino.actor;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import com.tw.casino.IDealer;
import com.tw.casino.util.Constants;

public class CasinoManagerTest
{
    private CasinoManager casinoManager = new CasinoManager();
    
    @Test
    public void testDealerAssignmentByPolicy()
    {
        casinoManager.initialize();
        
        IDealer dealerOne = new Dealer();
        IDealer dealerTwo = new Dealer();
        
        casinoManager.registerDealer(dealerOne.getDealerId());
        casinoManager.registerDealer(dealerTwo.getDealerId());
        
        UUID assigned = casinoManager.assignDealerForGame(Constants.RPS);
        assertEquals(dealerTwo.getDealerId(), assigned);
    }
}
