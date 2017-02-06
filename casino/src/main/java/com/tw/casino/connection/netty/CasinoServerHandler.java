package com.tw.casino.connection.netty;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.tw.casino.actor.CasinoManager;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameExecuteEvent;
import com.tw.casino.connection.messages.GameExecuteRejectEvent;
import com.tw.casino.connection.messages.GameExecuteWaitEvent;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.GameWaitResponse;
import com.tw.casino.connection.messages.Request;
import com.tw.casino.connection.messages.Response;
import com.tw.casino.game.DealerGameDetails;
import com.tw.casino.game.Game;
import com.tw.casino.game.GameDetails;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CasinoServerHandler extends SimpleChannelInboundHandler<Request>
{
    private CasinoManager casinoManager;

    private static final ConcurrentMap<UUID, ChannelHandlerContext> DEALER_CONTEXT_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<UUID, ChannelHandlerContext> PLAYER_CONTEXT_CACHE = new ConcurrentHashMap<>();

    public CasinoServerHandler(CasinoManager casinoManager)
    {
        this.casinoManager = casinoManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request)
            throws Exception
    {
        Response response = null;
        if (request instanceof GameListRequest)
        {
            GameListRequest gameListRequest = (GameListRequest) request;
            List<GameDetails> gameDetailsList = casinoManager.getGameDetailsList();

            // Store the ChannelHandlerContext for player
            PLAYER_CONTEXT_CACHE.putIfAbsent(gameListRequest.getPlayerId(), ctx);

            response = new 
                    GameListResponse(gameListRequest.getPlayerId(), gameDetailsList);
            
            ctx.write(response);
        }
        else if (request instanceof GameDataRequest)
        {
            GameDataRequest gameDataRequest = (GameDataRequest) request;

            casinoManager.registerDealer(gameDataRequest.getDealerId());

            // Store the ChannelHandlerContext for dealer
            DEALER_CONTEXT_CACHE.putIfAbsent(gameDataRequest.getDealerId(), ctx);

            List<DealerGameDetails> gameData = casinoManager.getGameData();
            response = new GameDataResponse(gameDataRequest.getDealerId(), gameData);
            
            ctx.write(response);
        }
        else if (request instanceof GameRequest)
        {
            GameRequest gameRequest = (GameRequest) request;
            String name = gameRequest.getGameName();
            UUID assignedDealer = casinoManager.assignDealerForGame(name);
            ChannelHandlerContext dealerContext = DEALER_CONTEXT_CACHE.get(assignedDealer);
            
            response = new GameExecuteEvent(assignedDealer, gameRequest.getPlayerProfile(), name);
            
            // Forward to Dealer
            ChannelFuture future = dealerContext.write(response);
            future.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture arg0) throws Exception
                {
                    dealerContext.flush();
                    
                }});
        }
        else if (request instanceof GameExecuteWaitEvent)
        {
            GameExecuteWaitEvent waitEvent = (GameExecuteWaitEvent) request;
            UUID playerId = waitEvent.getPlayerId();
            ChannelHandlerContext playerContext = PLAYER_CONTEXT_CACHE.get(playerId);
            
            response = new GameWaitResponse(playerId);
            
            // Forward to Player
            ChannelFuture future = playerContext.write(response);
            future.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture arg0) throws Exception
                {
                    playerContext.flush();
                    
                }});           
        }
        else if (request instanceof GameExecuteRejectEvent)
        {
            GameExecuteRejectEvent waitEvent = (GameExecuteRejectEvent) request;
            UUID playerId = waitEvent.getPlayerId();
            ChannelHandlerContext playerContext = PLAYER_CONTEXT_CACHE.get(playerId);
            
            response = new GameRejectResponse(playerId);
            
            // Forward to Player
            ChannelFuture future = playerContext.write(response);
            future.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture arg0) throws Exception
                {
                    playerContext.flush();
                    
                }});       
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
        cause.printStackTrace();
        ctx.close();
    }
}
