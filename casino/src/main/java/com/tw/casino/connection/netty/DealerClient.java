package com.tw.casino.connection.netty;

import com.tw.casino.IDealer;
import com.tw.casino.actor.Dealer;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Message;
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
        Channel channel = null;
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            .handler(new CasinoClientInitializer(sslCtx));

            channel = b.connect(host, port).sync().channel();
            CasinoClientHandler handler = channel.pipeline().get(CasinoClientHandler.class);

            // Operate here
            IDealer dealer = new Dealer();

            System.out.println(Constants.WELCOME);
            
            Message request = dealer.createGameDataRequest();;
            Message response = handler.sendRequestAndGetResponse(request);
            dealer.handleGameDataResponse((GameDataResponse) response);

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
            }

        }
        finally
        {
            channel.close();
            group.shutdownGracefully();
        }
    }
}
