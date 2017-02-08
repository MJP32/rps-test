package com.tw.casino.game.rps;

import java.io.Serializable;
import java.util.UUID;

import com.tw.casino.connection.messages.data.PlayerDetails;
import com.tw.casino.game.GamePlay;

public class RPSPlay implements GamePlay, Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1124697691657816925L;

    private volatile int hashCode = 0;
    
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
    
    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        
        if (!(other instanceof RPSPlay))
        {
            return false;
        }
        
        RPSPlay otherMove = (RPSPlay) other;
        
        if (!this.id.equals(otherMove.getId()))
            return false;
        
        if (!this.move.equals(otherMove.getMove()))
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        int result = hashCode;
        if (result == 0)
        {
            result = 17;
            result = 31 * result + id.hashCode();
            result = 31 * result + move.hashCode();
        }
        
        return result;
    }
}
