package com.tw.casino.connection;

import java.util.Scanner;

import com.tw.casino.IPlayer;
import com.tw.casino.actor.Role;
import com.tw.casino.simulator.DefaultRPSStrategy;
import com.tw.casino.util.CasinoConstants;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class Client 
{	

    public static void displayConnectMessage()
    {
        System.out.println(CasinoConstants.AWAIT);
    }
    
    public static void displayErrorAndExit()
    {
        System.out.println("Missing role. Use '-d' for Dealer and '-p' for Player.");
        System.exit(0);
    }

    public static void main(String[] args) 
    {
        // host port
        String host = "localhost";
        int port = 8100;

        if (args.length == 0)
        {
            displayErrorAndExit();
        }

        String value = args[0]; 
        Role role = null;
        if (value.equalsIgnoreCase("-d"))
            role = Role.DEALER;
        else if (value.equals("-p"))
            role = Role.PLAYER;
        else
            displayErrorAndExit();
            
        displayConnectMessage();
        
        final Role actorRole = role;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);

        final EventExecutorGroup group = new DefaultEventExecutorGroup(1500);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() 
                {
            @Override
            public void initChannel(SocketChannel ch) throws Exception 
            {
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(new ObjectEncoder());
                channelPipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                channelPipeline.addLast(group,"clientHandler",new ClientHandler(actorRole)); 
            }
                });

        bootstrap.connect(host, port);
    }


}
