package com.tw.casino.connection;

import com.tw.casino.util.CasinoConstants;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class Server
{
    public static void displayStartup()
    {
        System.out.println(CasinoConstants.WELCOME);
    }
    
	public static void main(String[] args) throws InterruptedException
	{
	    displayStartup();
	    
		EventLoopGroup bossGroup = new NioEventLoopGroup();
	    EventLoopGroup workerGroup = new NioEventLoopGroup();
	    ServerBootstrap bootstrap = new ServerBootstrap();
	    bootstrap.group(bossGroup, workerGroup);
	    bootstrap.channel(NioServerSocketChannel.class);
	     
	    // ===========================================================
	    // 1. define a separate thread pool to execute handlers with
	    //    slow business logic. e.g database operation
	    // ===========================================================
	    final EventExecutorGroup group = new DefaultEventExecutorGroup(1500); //thread pool of 1500
	     
	    bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
	      @Override
	      protected void initChannel(SocketChannel ch) throws Exception {
	        ChannelPipeline channelPipeline = ch.pipeline();
	        channelPipeline.addLast(new ObjectEncoder()); // add with name
	        channelPipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null))); // add without name, name auto generated
	         
	        //===========================================================
	        // 2. run handler with slow business logic 
	        //    in separate thread from I/O thread
	        //===========================================================
	        channelPipeline.addLast(group,"serverHandler",new ServerHandler()); 
	      }
	    });
	     
	    bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
	    bootstrap.bind(8100).sync();

	}

}
