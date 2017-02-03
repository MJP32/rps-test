package com.tw.casino.connection;

import com.tw.casino.Response;
import com.tw.casino.actor.PlayerManager;
import com.tw.casino.connection.messages.SimpleRequest;
import com.tw.casino.util.CasinoConstants;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.CombinedChannelDuplexHandler;

public class ClientHandler extends CombinedChannelDuplexHandler<ChannelInboundHandlerAdapter, ChannelOutboundHandlerAdapter>  
{
    private PlayerManager playerManager;

    public ClientHandler()
    {
        this.init(new ChannelInboundHandlerAdapter(), new ChannelOutboundHandlerAdapter());
        playerManager = new PlayerManager();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println(CasinoConstants.PLAYER_START);
        playerManager.setupPlayer();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception 
    {
        playerManager.onResponse((Response) msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
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
