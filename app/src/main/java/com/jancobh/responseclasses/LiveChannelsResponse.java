package com.jancobh.responseclasses;

import com.jancobh.data.Streams;

import java.util.ArrayList;

/* Created by JancoBH.*/

public class LiveChannelsResponse {

    private ArrayList<Streams> streams;

    public ArrayList<Streams> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<Streams> streams) {
        this.streams = streams;
    }
}
