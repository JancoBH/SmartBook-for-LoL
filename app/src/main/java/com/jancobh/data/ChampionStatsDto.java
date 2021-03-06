package com.jancobh.data;

import java.io.Serializable;

/* Created by JancoBH.*/

public class ChampionStatsDto implements Serializable{

    private static final long serialVersionUID = 9L;


    private int id;
    private AggregatedStatsDto stats;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AggregatedStatsDto getStats() {
        return stats;
    }

    public void setStats(AggregatedStatsDto stats) {
        this.stats = stats;
    }
}
