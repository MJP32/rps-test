package com.tw.casino.connection.netty;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.tw.casino.ICasinoManager;
import com.tw.casino.connection.messages.BaseGameResponse;
import com.tw.casino.connection.messages.CasinoGameCompleteResponse;
import com.tw.casino.connection.messages.GameCompleteResponse;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameRequest;
import com.tw.casino.connection.messages.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CasinoServerHandler extends SimpleChannelInboundHandler<Message>
{
    private ICasinoManager casinoManager;

    private static final ConcurrentMap<UUID, Channel> DEALER_CHANNEL_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<UUID, Channel> PLAYER_CHANNEL_CACHE = new ConcurrentHashMap<>();

    public CasinoServerHandler(ICasinoManager casinoManager)
    {
        this.casinoManager = casinoManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message request)
            throws Exception
    {
        Message response = null;
        if (request instanceof GameListRequest)
        {
            GameListRequest gameListRequest = (GameListRequest) request;

            // Store the Channel for Player
            PLAYER_CHANNEL_CACHE.putIfAbsent(gameListRequest.getPlayerId(), ctx.channel());

            response = casinoManager.handleGameListRequest(gameListRequest);
            ctx.write(response);
        }
        else if (request instanceof GameDataRequest)
        {
            GameDataRequest gameDataRequest = (GameDataRequest) request;
            
            // Store the Channel for Dealer
            DEALER_CHANNEL_CACHE.putIfAbsent(gameDataRequest.getDealerId(), ctx.channel());

            response = casinoManager.handleGameDataRequest(gameDataRequest);
            ctx.write(response);
        }
        else if (request instanceof GameRequest)
        {
            GameRequest gameRequest = (GameRequest) request;
            String name = gameRequest.getGameName();
            UUID assignedDealer = casinoManager.assignDealerForGame(name);
            Channel dealerContext = DEALER_CHANNEL_CACHE.get(assignedDealer);
            
            // Forward to Dealer
            dealerContext.writeAndFlush(gameRequest);
        }
        else if (request instanceof BaseGameResponse)
        {
            // Handles Reject or Wait Response
            BaseGameResponse gameResponse = (BaseGameResponse) request;
            UUID playerId = gameResponse.getPlayerId();
            Channel playerChannel = PLAYER_CHANNEL_CACHE.get(playerId);

            // Forward to Player
            playerChannel.writeAndFlush(gameResponse);     
        }
        else if (request instanceof CasinoGameCompleteResponse)
        {
            CasinoGameCompleteResponse gameComplete = (CasinoGameCompleteResponse) request;     
            
            List<GameCompleteResponse> playerResponses = 
                    casinoManager.handleGameCompleteResponse(gameComplete);
            
            // Forward to Player
            for (GameCompleteResponse rsp : playerResponses)
            {
                Channel playerChannel = PLAYER_CHANNEL_CACHE.get(rsp.getPlayerId());

                playerChannel.writeAndFlush(rsp);
            }
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
