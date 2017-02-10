package com.jancobh.responseclasses;

import com.jancobh.data.Data;

import java.util.Map;

/**
 * Created by yrazlik on 8/6/15.
 */
public class ItemsResponse {

    private Map<String, Data> data;

    public Map<String, Data> getData() {
        return data;
    }

    public void setData(Map<String, Data> data) {
        this.data = data;
    }
}
