package com.tw.casino.game.rps;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.game.GamePlay;

public class RPSPlay implements GamePlay, Serializable
{
    private static final long serialVersionUID = -7993385018617643348L;
    
    private final UUID id;
    private final RPSMove move;
    
    public RPSPlay(RPSMove move)
    {
        this.id = UUID.randomUUID();
        this.move = move;
    }
    
    @Override
    public UUID getId()
    {
        return id;
    }

    public RPSMove getMove()
    {
        return move;
    }
}
