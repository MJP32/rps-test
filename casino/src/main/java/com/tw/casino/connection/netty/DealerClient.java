package com.tw.casino.connection.netty;

import java.util.List;
import java.util.Scanner;

import com.tw.casino.IDealer;
import com.tw.casino.actor.Dealer;
import com.tw.casino.actor.Player;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameExecuteEvent;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
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

public final class DealerClient
{
    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));

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
            .handler(new CasinoClientInitializer(sslCtx));

            Channel channel = b.connect(HOST, PORT).sync().channel();
            CasinoClientHandler handler = channel.pipeline().get(CasinoClientHandler.class);

            // Operate here
            IDealer dealer = new Dealer();

            System.out.println(CasinoConstants.WELCOME);
            GameDataRequest request = new GameDataRequest(dealer.getDealerId());
            GameDataResponse response = (GameDataResponse) handler.sendRequestAndGetResponse(request);
            dealer.handleGameDataResponse(response);

            Scanner scanner = new Scanner(System.in);
            System.out.println(CasinoConstants.DEALER_READY);
            
            while (true)
            {
                GameExecuteEvent gameExecuteEvent = (GameExecuteEvent) handler.awaitEvent();
                final List<Request> gameExecutedEvents = dealer.handleGameExecuteEvent(gameExecuteEvent);
                for (Request event : gameExecutedEvents)
                    handler.sendEvent(event);

                String input = scanner.next();
                if (input.equalsIgnoreCase("x"))
                {
                    break;
                }
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
