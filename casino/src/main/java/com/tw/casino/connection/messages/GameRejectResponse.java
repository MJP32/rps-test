package com.tw.casino.connection.messages;

import java.io.Serializable;
import java.util.UUID;

public class GameRejectResponse extends BaseGameResponse implements Serializable
{
    private static final long serialVersionUID = -8058765059332237439L;

    public GameRejectResponse(UUID playerId)
    {
        super(playerId);
    }
}
