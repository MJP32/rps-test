package com.tw.casino.actor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tw.casino.RequestListener;
import com.tw.casino.ResponseListener;
import com.tw.casino.connection.messages.Response;

public class DealerContext extends ActorContext
{
    private Dealer dealer;
    private RequestListener requestListener;
    private final ExecutorService executor;
    
    public DealerContext(RequestListener requestListener)
    {
        this.requestListener = requestListener;
        this.executor = Executors.newCachedThreadPool();
    }
    
    @Override
    public void onResponse(Response response)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void displayMessage(String message)
    {
        // TODO Auto-generated method stub
        
    }
}
