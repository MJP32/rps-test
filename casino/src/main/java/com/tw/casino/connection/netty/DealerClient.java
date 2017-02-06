package com.tw.casino.connection.netty;

import java.util.Scanner;

import com.tw.casino.actor.Dealer;
import com.tw.casino.actor.Player;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRequest;
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
            .handler(new PlayerClientInitializer(sslCtx));

            Channel channel = b.connect(HOST, PORT).sync().channel();
            PlayerClientHandler handler = channel.pipeline().get(PlayerClientHandler.class);

            // Operate here
            Dealer dealer = new Dealer();
            
            System.out.println(CasinoConstants.WELCOME);
            GameDataRequest request = new GameDataRequest(dealer.getDealerId());
            GameDataResponse response = (GameDataResponse) handler.sendRequestAndGetResponse(request);
            dealer.handleGameDataResponse(response);
            for (DealerGameDetails details : response.getGameData())
                System.out.println(details.getName());

            Scanner scanner = new Scanner(System.in);
            while (true)
            {
                System.out.println("To exit press x:");

                String input = scanner.next();
                if (input.equalsIgnoreCase("x"))
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
