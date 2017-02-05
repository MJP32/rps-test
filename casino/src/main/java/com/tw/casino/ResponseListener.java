package com.tw.casino;

import com.tw.casino.connection.messages.Response;

public interface ResponseListener
{
    void onResponse(Response response);
}
