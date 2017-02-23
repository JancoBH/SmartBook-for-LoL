package com.jancobh.responseclasses;

import com.jancobh.data.PlayerStatsSummaryDto;

import java.util.ArrayList;

/* Created by JancoBH.*/

public class StatsResponse {

    private ArrayList<PlayerStatsSummaryDto> playerStatSummaries;
    private long summonerId;

    public ArrayList<PlayerStatsSummaryDto> getPlayerStatSummaries() {
        return playerStatSummaries;
    }

    public void setPlayerStatSummaries(ArrayList<PlayerStatsSummaryDto> playerStatSummaries) {
        this.playerStatSummaries = playerStatSummaries;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
