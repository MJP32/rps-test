package com.tw.casino.connection.netty;

import com.tw.casino.ICasinoManager;
import com.tw.casino.actor.CasinoManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;

public class CasinoServerInitializer extends ChannelInitializer<SocketChannel>
{
    private final SslContext sslCtx;
    private final ICasinoManager casinoManager;

    public CasinoServerInitializer(SslContext sslCtx, ICasinoManager casinoManager)
    {
        this.sslCtx = sslCtx;
        this.casinoManager = casinoManager;
    }
    
    @Override
    protected void initChannel(SocketChannel channel) throws Exception
    {
        ChannelPipeline p = channel.pipeline();
        if (sslCtx != null)
        {
            p.addLast(sslCtx.newHandler(channel.alloc()));
        }
        
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        p.addLast(new ObjectEncoder());
        
        p.addLast(new CasinoServerHandler(casinoManager));
    }
}
