package com.jancobh.responseclasses;
/* Created by Janco.*/

import com.jancobh.data.Data;

import java.util.Map;

public class SpellsResponse {
    private Map<String, Data> data;

    public Map<String, Data> getData() {
        return data;
    }

    public void setData(Map<String, Data> data) {
        this.data = data;
    }
}
