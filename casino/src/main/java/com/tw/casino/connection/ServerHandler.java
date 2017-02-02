package com.tw.casino.connection;

import com.tw.casino.IDealer;
import com.tw.casino.IRequest;
import com.tw.casino.component.Dealer;
import com.tw.casino.simulator.PlayerInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter 
{

    private IDealer dealer;

    ServerHandler()
    {
        dealer = new Dealer();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        String menu = dealer.offerGameMenu();
        ctx.writeAndFlush(menu);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception 
    {
        dealer.processRequest((IRequest) msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
    {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
