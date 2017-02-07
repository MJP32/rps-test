package com.tw.casino.connection.netty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tw.casino.connection.messages.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CasinoClientHandler extends SimpleChannelInboundHandler<Message>
{
    private volatile Channel channel;
    private final BlockingQueue<Message> responseQueue = new LinkedBlockingQueue<>();
    
    public CasinoClientHandler()
    {
        super(false);
    }
    
    /**
     * Main send-receive method for both Player and Dealer
     * 
     * @param request
     * @return
     */
    public Message sendRequestAndGetResponse(Message request)
    {
        channel.writeAndFlush(request);
        
        Message response = null;
        boolean interrupted = false;
        for (;;)
        {
            try
            {
                response = responseQueue.take();
                break;
            }
            catch (InterruptedException e)
            {
                interrupted = true;
            }
        }
        
        if (interrupted)
        {
            Thread.currentThread().interrupt();
        }
        
        return response;
    }
    
    /**
     * For Dealer Client
     * 
     * @return
     */
    public Message awaitEvent()
    {
        Message response = null;
        boolean interrupted = false;
        for (;;)
        {
            try
            {
                response = responseQueue.take();
                break;
            }
            catch (InterruptedException e)
            {
                interrupted = true;
            }
        }
        
        if (interrupted)
        {
            Thread.currentThread().interrupt();
        }
        
        return response;
    }
    
    public void sendEvent(Message event)
    {
        channel.writeAndFlush(event);
    }
    
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
    {
        channel = ctx.channel();
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message response)
            throws Exception
    {
        responseQueue.add(response);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
