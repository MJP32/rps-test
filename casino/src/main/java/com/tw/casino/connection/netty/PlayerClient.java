package com.tw.casino.connection.netty;

import java.util.Scanner;

import com.tw.casino.actor.Player;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.GameDetails;
import com.tw.casino.util.CasinoConstants;

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
    
    private static final String ALLOW_STRATEGY = "Allowed";
    private static final String NO_STRATEGY = "Not Allowed";

    public static void main(String[] args) throws Exception
    {
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
            .handler(new PlayerClientInitializer(sslCtx));

            Channel channel = b.connect(HOST, PORT).sync().channel();
            PlayerClientHandler handler = channel.pipeline().get(PlayerClientHandler.class);

            // Operate here
            Scanner scanner = new Scanner(System.in);
            Player player;
            System.out.println(CasinoConstants.WELCOME);
            double startingBalance = 0.0;
            boolean isReady = false;
            while(!isReady)
            {
                try
                {
                    System.out.print(CasinoConstants.PLAYER_START);
                    startingBalance = Double.parseDouble(scanner.next());
                    if (startingBalance >= 0.0)
                    {
                        break;
                    }
                    else
                        System.out.println(CasinoConstants.PLAYER_REDO);

                }
                catch (NumberFormatException e)
                {
                    System.out.println(CasinoConstants.PLAYER_REDO);
                }
            }

            startingBalance = 50;
            player = new Player(startingBalance);

            while (true)
            {
                System.out.println("To request games press G:");
                System.out.println("To play any available game press R:");
                System.out.println("To exit press x:");

                String input = scanner.next();
                if (input.equalsIgnoreCase("G"))
                {
                    GameListRequest request = player.createGameListRequest();
                    GameListResponse response = (GameListResponse) handler.sendRequestAndGetResponse(request);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (GameDetails details : response.getAvailableGames())
                    {
                        stringBuilder.append("Game: ");
                        stringBuilder.append(details.getName());
                        stringBuilder.append("\tEntry Fee: ");
                        stringBuilder.append(details.getEntryFee());
                        stringBuilder.append("\tStrategy: ");
                        String strategy = details.isAllowStrategy() ? ALLOW_STRATEGY : NO_STRATEGY;
                        stringBuilder.append(strategy);
                        stringBuilder.append("\n");
                    }
                    System.out.println(stringBuilder.toString());
                }
                else if (input.equalsIgnoreCase("R"))
                {
                    GameRequest gameRequest = player.createGameRequest(CasinoConstants.RPS);
                }
                else if (input.equalsIgnoreCase("x"))
                {
                    break;
                }
            }

            channel.close();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }
}
