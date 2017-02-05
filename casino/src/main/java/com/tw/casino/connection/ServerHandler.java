package com.tw.casino.connection;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.tw.casino.ICasinoManager;
import com.tw.casino.ResponseListener;
import com.tw.casino.actor.CasinoManager;

import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.GameDetails;

import com.tw.casino.util.CasinoConstants;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.CombinedChannelDuplexHandler;


public class ServerHandler extends CombinedChannelDuplexHandler<ChannelInboundHandlerAdapter, ChannelOutboundHandlerAdapter> implements ResponseListener
{
    private static boolean isLive;

    private static final ConcurrentHashMap<UUID, ChannelHandlerContext> PLAYER_CONTEXT_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, ChannelHandlerContext> DEALER_CONTEXT_CACHE = new ConcurrentHashMap<>();

    private final ICasinoManager casinoManager;

    ServerHandler()
    {
        this.init(new ChannelInboundHandlerAdapter(), new ChannelOutboundHandlerAdapter());
        this.casinoManager = new CasinoManager();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        if (!isLive)
        {
            System.out.println(CasinoConstants.SERVER_READY);
            isLive = true;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception 
    {
        if (msg instanceof GameDataRequest)
        {
            GameDataRequest gameRequest = (GameDataRequest) msg;
            UUID playerId = gameRequest.getId();
            DEALER_CONTEXT_CACHE.putIfAbsent(playerId, ctx);

            // Get Game Data from GameDataLoader
        }
        else if (msg instanceof GameListRequest)
        {
            System.out.println("Got Game List Request");
            GameListRequest request = (GameListRequest) msg;
            UUID playerId = request.getId();
            PLAYER_CONTEXT_CACHE.putIfAbsent(playerId, ctx);

            // Get Game Details from GameDataLoader
            List<GameDetails> gameList = this.casinoManager.getGameListForPlayer();
            //GameListResponse gameListResponse = new GameListResponse(request.getPlayerId(), gameList);
            System.out.println("Got to sending response...");
            //ctx.writeAndFlush(gameListResponse);           
            System.out.println("Sent a response");
        }
        if (msg instanceof GameRequest)
        {
            // Forward to Dealer
        }
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
    public void onResponse(Response response)
    {
        // TODO

    }
}
