package com.tw.casino.connection;

import com.tw.casino.component.Player;
import com.tw.casino.component.SimpleRequest;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelDuplexHandler 
{
    private Player player;
    
    public ClientHandler()
    {
        player = new Player();
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        //SimpleRequest request = new SimpleRequest();
        //request.setMessage("Hi, I am a new user: " + request.getId());
        //ctx.writeAndFlush(request);
        System.out.println("I'm a player!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception 
    {
        String message = (String) msg;
        System.out.println(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
    {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    
    public String getMessage()
    {
        return "Hello World, Casino!";
    }

}
