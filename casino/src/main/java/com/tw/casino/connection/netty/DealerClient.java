package com.tw.casino.connection.netty;

import java.util.List;
import java.util.Scanner;

import com.tw.casino.IDealer;
import com.tw.casino.actor.Dealer;
import com.tw.casino.actor.Player;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Message;
import com.tw.casino.connection.messages.TerminateEvent;
import com.tw.casino.connection.messages.data.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.util.Constants;

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

public final class DealerClient
{
    static final boolean SSL = System.getProperty("ssl") != null;
    //static final String HOST = System.getProperty("host", "127.0.0.1");
    //static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));

    public static void displayStartup()
    {
        System.out.println(Constants.STARTUP_DEALER);
    }
    
    public static void main(String[] args) throws Exception
    {
        // Get start up args
        String host = args[0];
        int port = 0;
        try
        {
            port = Integer.parseInt(args[1]);
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
            IDealer dealer = new Dealer();

            System.out.println(Constants.WELCOME);
            GameDataRequest request = new GameDataRequest(dealer.getDealerId());
            GameDataResponse response = (GameDataResponse) handler.sendRequestAndGetResponse(request);
            dealer.handleGameDataResponse(response);

            System.out.println(Constants.DEALER_READY);

            while (true)
            {
                Message event = handler.awaitEvent();
                if (event instanceof GameRequest)
                {
                    final Message gameExecutedEvent = 
                            dealer.handleGameExecuteEvent((GameRequest) event);
                    handler.sendEvent(gameExecutedEvent);
                }
                else if (event instanceof TerminateEvent)
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
