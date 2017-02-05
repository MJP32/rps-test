package com.tw.casino.game;

import java.util.List;
import java.util.UUID;

import com.tw.casino.IPlayer;

public interface Game
{
    GameId getGameId();
    
    String getName();
    
    UUID executeGame(List<IPlayer> players);
}
