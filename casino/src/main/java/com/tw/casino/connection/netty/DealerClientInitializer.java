package com.tw.casino.connection.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;

public class DealerClientInitializer extends ChannelInitializer<SocketChannel>
{
    private final SslContext sslCtx;
    
    public DealerClientInitializer(SslContext sslCtx)
    {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        if (sslCtx != null)
        {
            pipeline.addLast(sslCtx.newHandler(channel.alloc(), PlayerClient.HOST, PlayerClient.PORT));
        }
        
        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(new ObjectEncoder());
        
        pipeline.addLast(new DealerClientHandler()); 
    }
}
