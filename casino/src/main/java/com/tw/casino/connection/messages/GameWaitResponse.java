package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameWaitResponse extends BaseGameResponse implements Serializable
{
    private static final long serialVersionUID = 548828943632143549L;
    
    public GameWaitResponse(UUID playerId)
    {
        super(playerId);
    }

}
