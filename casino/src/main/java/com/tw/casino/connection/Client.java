package com.tw.casino.connection;

import java.util.Scanner;

import com.tw.casino.IPlayer;
import com.tw.casino.simulator.DefaultRPSStrategy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Client 
{	
    public static void displayMenu()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to TradeWind Casinos!");
        System.out.println("Enter your starting balance");
        while(true)
        {
            System.out.println("The following games are available for you today...");
            System.out.println("1. Rock-Paper-Scissors");
            System.out.println("2. Blackjack");
            System.out.println("\nChoose one of the following:");
            System.out.println("* Play a game by entering it's item number");
            System.out.println("* Enter X to exit the casino");

            String input = scanner.next();
            if (input.length() == 1)
            {
                char value = input.charAt(0);
                if (Character.isDigit(value))
                {
                    int option = Integer.parseInt(input);
                    System.out.println("Received Game Option: " + option);
                }
                else if (value == 'x' || value == 'X')
                {
                    System.out.println("Goodbye!");
                    break;
                }
                else
                {
                    System.out.println("Invalid input. Please try again.");
                }
            }
            else
            {
                System.out.println("Invalid input. Please try again.");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) 
    {
        // host port
        String host = "localhost";
        int port = 8100;
        
        //displayMenu();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() 
        {
            @Override
            public void initChannel(SocketChannel ch) throws Exception 
            {
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        new ClientHandler());
            }
        });

        bootstrap.connect(host, port);
    }


}
