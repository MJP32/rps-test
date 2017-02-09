package com.tw.casino.connection.netty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.tw.casino.IPlayer;
import com.tw.casino.actor.Player;
import com.tw.casino.connection.messages.BaseGameResponse;
import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.connection.messages.data.GameDetails;
import com.tw.casino.game.GameStrategy;
import com.tw.casino.game.rps.strategy.RandomGuesingRPSStrategy;
import com.tw.casino.game.rps.strategy.SharpRPSStrategy;
import com.tw.casino.util.Constants;
import com.tw.casino.util.EmployStrategy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public final class PlayerClient
{
    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));
    
    private static String MENU;
    static
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nMAIN MENU\n");
        stringBuilder.append("* To request games press G.\n");
        stringBuilder.append("* To play any available game enter game code.\n");
        stringBuilder.append("* Update your account balance press U.\n");
        stringBuilder.append("* View your balance press V.\n");
        stringBuilder.append("* To exit press X.\n");
        stringBuilder.append("Enter your choice: ");
        
        MENU = stringBuilder.toString();       
    }
    
    private static Scanner scanner = new Scanner(System.in);
    
    // Optional approach to a menu driven way to enter your starting balance.
    public static double getStartBalance()
    {
        double startBalance = 0.0;
        boolean isReady = false;
        while(!isReady)
        {
            try
            {
                System.out.print(Constants.START);
                startBalance = Double.parseDouble(scanner.next());
                if (startBalance >= 0.0)
                {
                    break;
                }
                else
                    System.out.println(Constants.RE_ENTER);

            }
            catch (NumberFormatException e)
            {
                System.out.println(Constants.RE_ENTER);
            }
        }
        
        return startBalance;
    }
    
    public static void displayStartup()
    {
        System.out.println(Constants.STARTUP_PLAYER);
    }

    public static void main(String[] args) throws Exception
    {   
        // Get start up args
        String host = args[0];
        double startingBalance = 0;
        int port = 0;
        try
        {
            port = Integer.parseInt(args[1]);
            startingBalance = Double.parseDouble(args[2]);
        }
        catch (NumberFormatException e)
        {
            displayStartup();
            System.exit(0);
        }
        
        final SslContext sslCtx;
        if (SSL)
        {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }
        else
        {
            sslCtx = null;
        }
        

        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            .handler(new CasinoClientInitializer(sslCtx));

            Channel channel = b.connect(host, port).sync().channel();
            CasinoClientHandler handler = channel.pipeline().get(CasinoClientHandler.class);

            // Operate here
            Scanner scanner = new Scanner(System.in);
            IPlayer player;
            System.out.println(Constants.WELCOME + " " + Constants.GAME_LIST_AVAILABLE);
            
            player = new Player(startingBalance);
            // Set Player Strategy
            player.loadPlayerStrategy();

            Message request = null;
            Message response = null;
            
            String menuMessage = null;
            
            // Get List of Games
            request = player.createGameListRequest();
            List<GameDetails> gameDetails = new ArrayList<GameDetails>();
            GameListResponse gameListResponse = (GameListResponse) handler.sendRequestAndGetResponse(request);
            gameDetails.addAll(gameListResponse.getAvailableGames());
            menuMessage = player.handleGameListResponse(gameListResponse);
            System.out.println(menuMessage);
            
            while (true)
            {
                System.out.println(MENU);

                String input = scanner.next();
                char choice = input.charAt(0);

                if (choice == 'g' || choice == 'G')
                {
                    request = player.createGameListRequest();
                    gameListResponse = (GameListResponse) handler.sendRequestAndGetResponse(request);
                    gameDetails.clear();
                    gameDetails.addAll(gameListResponse.getAvailableGames());
                    menuMessage = player.handleGameListResponse(gameListResponse);
                    System.out.println(menuMessage);
                }
                else if (choice == 'v' || choice == 'V')
                {
                    System.out.println(Constants.BALANCE + player.getAccountBalance());
                }
                else if (choice == 'u' || choice == 'U')
                {
                    System.out.print(Constants.BALANCE_ADD);
                    input = scanner.next();
                    Double balance;
                    try
                    {
                        balance = Double.parseDouble(input);
                        double newBalance = player.getAccountBalance() + balance;
                        player.setAccountBalance(newBalance);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(Constants.REDO); 
                    }
                }
                else if (Character.isDigit(choice))
                {
                    try
                    {
                        int gameIndex = Integer.parseInt(input);
                        if (gameIndex > gameDetails.size() || gameIndex < 1)
                        {
                            System.out.println(Constants.REDO);                           
                        }
                        else
                        {
                            GameDetails details = gameDetails.get(gameIndex - 1);
                            request = player.createGameRequest(details.getName());
                            if (request == null)
                                continue;
                            response = handler.sendRequestAndGetResponse(request);
                            if (response instanceof GameWaitResponse)
                            {
                                System.out.println(Constants.AWAIT);
                                response = handler.awaitEvent();
                            }
                            menuMessage = player.handleGameResponse((Response) response);
                            System.out.println(menuMessage);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println(Constants.REDO);
                    }

                }
                else if (choice == 'x' || choice == 'X')
                {
                    break;
                }
                else
                    System.out.println(Constants.REDO);
            }

            scanner.close();
            channel.close();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }
}
