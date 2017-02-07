package com.tw.casino.connection.netty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DealerClientHandler extends SimpleChannelInboundHandler<String>
{
    private volatile Channel channel;
    private final BlockingQueue<String> responseQueue = new LinkedBlockingQueue<>();
    
    public DealerClientHandler()
    {
        super(false);
    }
    
    /**
     * Main send-receive method for both Player and Dealer
     * 
     * @param request
     * @return
     */
    public String sendRequestAndGetResponse(Request request)
    {
        System.out.println("About to write message");
        channel.writeAndFlush(request);
        
        String response = null;
        boolean interrupted = false;
        for (;;)
        {
            try
            {
                System.out.println("Now waiting for message");
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
    public String awaitEvent()
    {
        String response = null;
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
    
    public void sendEvent(Request event)
    {
        channel.writeAndFlush(event);
    }
    
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
    {
        channel = ctx.channel();
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String response)
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
