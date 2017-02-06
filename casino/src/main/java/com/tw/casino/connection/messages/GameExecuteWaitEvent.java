package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameExecuteWaitEvent extends BaseGameExecuteEvent implements Serializable
{
    private static final long serialVersionUID = 3119266024785709334L;

    public GameExecuteWaitEvent(UUID dealerId, UUID playerId)
    {
        super(dealerId, playerId);
    }
    
}