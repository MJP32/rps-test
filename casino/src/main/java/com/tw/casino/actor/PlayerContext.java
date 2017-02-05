package com.tw.casino.actor;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tw.casino.RequestListener;
import com.tw.casino.ResponseListener;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.GameDetails;
import com.tw.casino.util.CasinoConstants;

/**
 * The PlayerContext maintains the context in which a player exists. It allows 
 * maintaining the Player's account balance and communicating with the Casino 
 * server. 
 * 
 * @author siddharths1787
 *
 */
public final class PlayerContext extends ActorContext
{
    private Scanner scanner;
    private Player player;

    // Can be modified to maintain a list of listeners.
    private RequestListener requestListener;

    private final ExecutorService executor;
    private final PrintStream outputStream;

    
    public PlayerContext(RequestListener requestListener)
    {
        this.scanner = new Scanner(System.in);
        this.executor = Executors.newCachedThreadPool();
        this.outputStream = System.out;
        this.requestListener = requestListener;
        this.player = new Player(50);
    }

    @Override
    public void run()
    {
        // Request available games
        GameListRequest gameListRequest = new GameListRequest(this.player.getPlayerId());
        this.requestListener.onRequest(gameListRequest);
        
        /*while (true)
        {
            this.outputStream.println("Choose any of the above games to play or X to quit:");
            String input = this.scanner.next();
            if (input.equalsIgnoreCase("x"))
            {
                break;
            }
            
        }*/
        
        //this.scanner.close();
    }

    public void setup()
    {

        double startingBalance;
        while(true)
        {
            try
            {
                this.outputStream.print(CasinoConstants.PLAYER_START);
                startingBalance = Double.parseDouble(this.scanner.next());
                if (startingBalance >= 0.0)
                {
                    break;
                }
                else
                    this.outputStream.println(CasinoConstants.PLAYER_REDO);
                    
            }
            catch (NumberFormatException e)
            {
                this.outputStream.println(CasinoConstants.PLAYER_REDO);
            }
        }


        this.player = new Player(50);   
    }

    @Override
    public void displayMessage(String message)
    {
        this.outputStream.println(message);      
    }

    @Override
    public void onResponse(final Response response)
    {
        System.out.println("Got it");
        if (response instanceof GameListResponse)
        {
            GameListResponse menu = (GameListResponse) response;
            System.out.println(CasinoConstants.GAME_LIST_AVAILABLE);
            for (GameDetails gd : menu.getAvailableGames())
            {
                System.out.println(gd.getName());
            }
        }  
    }

    public Player getPlayer()
    {
        return this.player;
    }



}
