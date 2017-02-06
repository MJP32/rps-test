package com.tw.casino.connection.netty;

import java.util.List;

import com.tw.casino.actor.CasinoManager;
import com.tw.casino.connection.messages.GameDataRequest;
import com.tw.casino.connection.messages.GameDataResponse;
import com.tw.casino.connection.messages.GameListRequest;
import com.tw.casino.connection.messages.GameListResponse;
import com.tw.casino.connection.messages.GameRejectResponse;
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
            List<GameDetails> gameDetailsList = casinoManager.getGameDetailsList();
            response = new 
                    GameListResponse(((GameListRequest) request).getPlayerId(), gameDetailsList);      
        }
        else if (request instanceof GameDataRequest)
        {
            List<DealerGameDetails> gameData = casinoManager.getGameData();
            response = new GameDataResponse(((GameDataRequest) request).getDealerId(), gameData);
        }
        
        ChannelFuture future = ctx.write(response);
        future.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception
            {
                //System.out.println("Response sent!");
                
            }});
        
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
