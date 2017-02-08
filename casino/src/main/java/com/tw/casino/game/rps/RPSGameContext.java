package com.tw.casino.game.rps;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

import com.tw.casino.actor.PlayerDetails;
import com.tw.casino.game.GameContext;

public final class RPSGameContext implements GameContext
{
    private final UUID matchId;
    private final long timestamp;
    private final ConcurrentMap<UUID, PlayerDetails> playerCache;

    private static ThreadLocal<TwoPlayerRockPaperScissors> game 
    = new ThreadLocal<TwoPlayerRockPaperScissors>() 
    {
        public TwoPlayerRockPaperScissors initialValue()
        {
            return new TwoPlayerRockPaperScissors(0.0);
        }
    };

    public RPSGameContext(TwoPlayerRockPaperScissors rps)
    {
        this.matchId = UUID.randomUUID();
        this.timestamp = System.nanoTime();
        this.playerCache = new ConcurrentHashMap<>();
        game.set(rps);
    }

    @Override
    public UUID getMatchId()
    {
        return matchId;
    }

    @Override
    public long getTimestamp()
    {
        return timestamp;
    }

    @Override
    public boolean hasPlayer(UUID playerId)
    {
        return playerCache.containsKey(playerId);
    }
    
    @Override
    public int playerCount()
    {
        return playerCache.size();
    }

    @Override
    public void setupMatch(PlayerDetails playerDetails)
    {
        playerCache.putIfAbsent(playerDetails.getPlayerId(), playerDetails);
    }

    @Override
    public Map<String, List<PlayerDetails>> executeGame(PlayerDetails playerDetails)
    {
        playerCache.put(playerDetails.getPlayerId(), playerDetails);
        
        PlayerDetails[] gamePlayerDetails = new PlayerDetails[playerCache.size()];
        
        int i = 0;
        Iterator<PlayerDetails> iterator = playerCache.values().iterator();
        while (iterator.hasNext())
            gamePlayerDetails[i++] = iterator.next();
        
        Map<String, List<PlayerDetails>> results = game.get().playMatch(gamePlayerDetails);
        playerCache.clear();
        
        return results;
    }

    @Override
    public int compareTo(GameContext o)
    {
        return Long.compare(timestamp, o.getTimestamp());
    }
}
