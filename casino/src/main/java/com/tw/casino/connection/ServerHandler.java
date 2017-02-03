package com.tw.casino.connection;

import com.tw.casino.IDealer;
import com.tw.casino.Request;
import com.tw.casino.Response;
import com.tw.casino.actor.Dealer;
import com.tw.casino.connection.messages.MenuResponse;
import com.tw.casino.simulator.PlayerInfo;
import com.tw.casino.util.CasinoConstants;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends CombinedChannelDuplexHandler<ChannelInboundHandlerAdapter, ChannelOutboundHandlerAdapter> 
{
    private IDealer dealer;
    private static boolean isLive;

    ServerHandler()
    {
        this.init(new ChannelInboundHandlerAdapter(), new ChannelOutboundHandlerAdapter());
        dealer = new Dealer();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        if (!isLive)
        {
            System.out.println(CasinoConstants.DEALER_READY);
            isLive = true;
        }
        
        Response menu = new MenuResponse();
        ctx.writeAndFlush(menu);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception 
    {
        dealer.processRequest((Request) msg);
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
}
