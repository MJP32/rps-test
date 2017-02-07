package com.tw.casino.game.rps;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.tw.casino.actor.PlayerDetails;
import com.tw.casino.game.GameContext;

public final class RPSGameContext implements GameContext
{
    private final UUID matchId;
    private final long timestamp;
    private final Set<PlayerDetails> playerCache;

    private static ThreadLocal<RockPaperScissors> game 
    = new ThreadLocal<RockPaperScissors>() 
    {
        public RockPaperScissors initialValue()
        {
            return new RockPaperScissors(2, 0.0);
        }
    };

    public RPSGameContext(RockPaperScissors rps)
    {
        this.matchId = UUID.randomUUID();
        this.timestamp = System.nanoTime();
        this.playerCache = Collections.synchronizedSet(new LinkedHashSet<>());
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
    public Set<PlayerDetails> getPlayerCache()
    {
        return playerCache;
    }

    @Override
    public void setupMatch(PlayerDetails playerDetails)
    {
        playerCache.add(playerDetails);
    }

    @Override
    public Map<String, List<PlayerDetails>> executeGame(PlayerDetails playerDetails)
    {
        playerCache.add(playerDetails);
        
        PlayerDetails[] gamePlayerDetails = new PlayerDetails[playerCache.size()];
        
        int i = 0;
        Iterator<PlayerDetails> iterator = playerCache.iterator();
        while (iterator.hasNext())
            gamePlayerDetails[i++] = iterator.next();
        
        return game.get().playMatch(gamePlayerDetails);
    }
}
