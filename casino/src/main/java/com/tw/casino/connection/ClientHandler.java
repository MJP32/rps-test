package com.tw.casino.connection;

import java.util.UUID;

import com.tw.casino.RequestListener;
import com.tw.casino.actor.ActorContext;
import com.tw.casino.actor.DealerContext;
import com.tw.casino.actor.PlayerContext;
import com.tw.casino.actor.Role;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.util.CasinoConstants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.CombinedChannelDuplexHandler;

public class ClientHandler extends CombinedChannelDuplexHandler<ChannelInboundHandlerAdapter, ChannelOutboundHandlerAdapter> implements RequestListener
{
    private Channel channel;
    private ChannelHandlerContext channelHandlerContext;
    
    private Role role;
    private PlayerContext actorContext;

    public ClientHandler(Role role)
    {
        this.init(new ChannelInboundHandlerAdapter(), new ChannelOutboundHandlerAdapter());
        this.role = role;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        this.channel = ctx.channel();
        this.channelHandlerContext = ctx;
        
        if (role == Role.DEALER)
        {
            //actorContext = new DealerContext(this);
        }
        else
            actorContext = new PlayerContext(this);
        
        this.actorContext.displayMessage(CasinoConstants.WELCOME);
        //this.actorContext.run();
        
        GameListRequest gameListRequest = new GameListRequest(UUID.randomUUID());
        //this.onRequest(gameListRequest);
        
        ctx.writeAndFlush(gameListRequest);
        
        System.out.println("Okay active now.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception 
    {
        System.out.print("Got the response");
        this.actorContext.onResponse((Response) msg);   
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

    @Override
    public void onRequest(Request request)
    {   
        //ChannelPromise promise = this.channel.newPromise();
        //promise.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        
        try
        {
            this.channelHandlerContext.writeAndFlush(request);
            //this.channelHandlerContext.writeAndFlush(request);
            System.out.println("Sent request");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }

}
