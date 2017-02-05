package com.tw.casino.actor;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tw.casino.RequestListener;
import com.tw.casino.ResponseListener;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Request;
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

    public void processRequest(Request request)
    {
        if (request instanceof GameRequest)
        {
            final GameRequest gameRequest = (GameRequest) request;
            Future<?> futureResponse = executor.submit(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Response response = dealer.handleGameRequest(gameRequest);
                            // TODO pass response to ClientHandler
                        }
                    });

        }
    }
}
