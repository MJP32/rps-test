package com.tw.casino.connection.messages;

import java.util.UUID;

public interface Message
{
    UUID getId();
    
    long getTimestamp();
}
