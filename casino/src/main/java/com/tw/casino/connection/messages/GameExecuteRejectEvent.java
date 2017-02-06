package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameExecuteRejectEvent extends BaseGameExecuteEvent implements
        Serializable
{
    private static final long serialVersionUID = -1742665792920521242L;

    public GameExecuteRejectEvent(UUID dealerId, UUID playerId)
    {
        super(dealerId, playerId);
    }
}
