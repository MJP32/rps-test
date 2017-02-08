package com.tw.casino.connection.netty;

import com.tw.casino.ICasinoManager;
import com.tw.casino.actor.CasinoManager;
import com.tw.casino.actor.CasinoManager;
import com.tw.casino.util.CasinoConstants;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public final class CasinoServer
{
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));
    
    public static void displayStartup()
    {
        System.out.println(CasinoConstants.STARTUP_SERVER);
    }
    
    public static void main(String[] args) throws Exception
    {
        // Get start up args
        int port = 0;
        try
        {
            port = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        {
            displayStartup();
            
        }
        
        final SslContext sslCtx;
        if (SSL)
        {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }
        else
        {
            sslCtx = null;
        }
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        ICasinoManager casinoManager = new CasinoManager();
        
        // Run startup configuration tasks
        casinoManager.initialize();
        
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new CasinoServerInitializer(sslCtx, casinoManager));
            
            b.bind(port).sync().channel().closeFuture().sync();
            
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
