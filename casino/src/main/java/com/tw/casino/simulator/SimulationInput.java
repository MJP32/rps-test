package com.tw.casino.simulator;

import java.util.List;
import java.util.Set;

public abstract class SimulationInput {

	abstract Set<PlayerInfo> getPlayers();
    // A list of timestamped events, ordered by timestamp. Note: there may be more than one
    // event in a given timestamp.
    abstract List<EventInfo> getEvents();
    
}
