package com.tw.casino.actor;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tw.casino.Response;
import com.tw.casino.ResponseListener;
import com.tw.casino.connection.messages.MenuResponse;
import com.tw.casino.util.CasinoConstants;

public final class PlayerManager implements ResponseListener
{
    private Scanner scanner;
    private Player player;
    
    private final ExecutorService executor;
    private final PrintStream outputStream;

    public PlayerManager()
    {
        this.scanner = new Scanner(System.in);
        this.executor = Executors.newCachedThreadPool();
        this.outputStream = System.out;
    }

    public void setupPlayer()
    {
        double startingBalance;
        while(true)
        {
            try
            {
                this.outputStream.print(CasinoConstants.PLAYER_SETUP);
                startingBalance = Double.parseDouble(scanner.next());
                if (startingBalance >= 0.0)
                {
                    break;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println(CasinoConstants.PLAYER_REDO);
            }
        }
        
        player = new Player(startingBalance);
        this.outputStream.println(CasinoConstants.PLAYER_READY);
    }

    @Override
    public void onResponse(final Response response)
    {
        executor.submit(new Callable<Response>(){

            @Override
            public Response call() throws Exception
            {
                if (response instanceof MenuResponse)
                {
                    MenuResponse menu = (MenuResponse) response;
                    outputStream.println("Got the Menu!");
                }
                
                return response;
            }});        
    }
}
