package com.tw.casino;

import com.tw.casino.connection.messages.Request;

public interface RequestHandler
{
    void handleRequest(Request request);
}
