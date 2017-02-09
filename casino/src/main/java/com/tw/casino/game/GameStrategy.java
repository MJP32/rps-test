package com.tw.casino.game;

import java.util.UUID;

/**
 * This interface must be implemented by Players 
 * interested in setting up their own strategies to play
 * at the Casino.
 * 
 * @author siddharths1787
 *
 */
public interface GameStrategy
{
    String getName();
    
    GamePlay computeNextPlay(UUID matchId);
}
