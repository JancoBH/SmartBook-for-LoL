package com.jancobh.responseclasses;

import com.jancobh.data.SummonerNames;

import java.util.List;

/**
 * Created by yrazlik on 1/6/16.
 */
public class SummonerNamesResponse {

    private List<SummonerNames> summonerNames;

    public List<SummonerNames> getSummonerNames() {
        return summonerNames;
    }

    public void setSummonerNames(List<SummonerNames> summonerNames) {
        this.summonerNames = summonerNames;
    }
}
