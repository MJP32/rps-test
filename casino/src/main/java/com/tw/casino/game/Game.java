package com.tw.casino.game;

import java.util.Collection;

import com.tw.casino.actor.PlayerProfile;

public interface Game
{
    GameId getGameId();
    
    String getName();
    
    int requiredNumberOfPlayers();
    
    double entryFee();
    
    PlayerProfile executeGame(Collection<PlayerProfile> players);
}
