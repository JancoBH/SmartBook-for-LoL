package com.jancobh.responseclasses;

import java.util.Map;

/* Created by JancoBH.*/

public class SummonerByNameResponse {

    private Map<String, SummonerInfo> summonerInfo;

    public Map<String, SummonerInfo> getSummonerInfo() {
        return summonerInfo;
    }

    public void setSummonerInfo(Map<String, SummonerInfo> summonerInfo) {
        this.summonerInfo = summonerInfo;
    }
}
