package com.jancobh.responseclasses;

import com.jancobh.data.Skin;

import java.util.ArrayList;

/* Created by JancoBH.*/

public class ChampionSkinsResponse {

    public ArrayList<Skin> skins;
    public String key;

    public ArrayList<Skin> getSkins() {
        return skins;
    }

    public void setSkins(ArrayList<Skin> skins) {
        this.skins = skins;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
