package com.tw.casino;

import com.tw.casino.connection.messages.Request;

public interface RequestListener
{
    void onRequest(Request request);
}
