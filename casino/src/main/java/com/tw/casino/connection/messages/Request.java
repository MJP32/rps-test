package com.tw.casino.connection.messages;

import java.util.UUID;

public interface Request
{
    UUID getId();
    
    long getTimestamp();
}
