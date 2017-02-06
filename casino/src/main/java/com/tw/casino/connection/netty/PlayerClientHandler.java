package com.tw.casino.connection.netty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerClientHandler extends SimpleChannelInboundHandler<Response>
{
    private volatile Channel channel;
    private final BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
    
    public PlayerClientHandler()
    {
        super(false);
    }
    
    public Response sendRequestAndGetResponse(Request request)
    {
        channel.writeAndFlush(request);
        
        Response response = null;
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
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
    {
        channel = ctx.channel();
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Response response)
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
