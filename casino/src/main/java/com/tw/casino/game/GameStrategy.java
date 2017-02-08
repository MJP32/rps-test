package com.tw.casino.game;

import java.util.UUID;

public interface GameStrategy
{
    String getName();
    
    GamePlay computeNextPlay(UUID matchId);
}
