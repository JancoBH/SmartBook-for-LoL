package com.jancobh.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jancobh.commons.Commons;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.ChampionLegendResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class LegendFragment extends Fragment implements ResponseListener {
    TextView legend;
    private int champId;
    private Typeface typeFace;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_legend, container, false);
        initUI(v);
        getExtras();

        ArrayList<String> pathParams = new ArrayList<>();
        pathParams.add("static-data");
        pathParams.add(Commons.getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        pathParams.add(String.valueOf(champId));
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("locale", Commons.getLocale());
        queryParams.put("champData", "lore");
        queryParams.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_LEGEND_REQUEST, pathParams, queryParams, null, this);

        return v;
    }

    private void getExtras(){
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
        }
    }

    private void initUI(View v){
        legend = (TextView)v.findViewById(R.id.textviewLegend);
    }

    private String formatLoreString(String lore) {
        if(lore.contains("<br>")){
            lore = lore.replaceAll("<br>", "\n");
        }
        return lore;
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ChampionLegendResponse){
            ChampionLegendResponse resp = (ChampionLegendResponse) response;
            String lore = resp.getLore();
            lore = formatLoreString(lore);
            legend.setText(lore);
        }

    }

    @Override
    public void onFailure(Object response) {
        String errorMessage = (String)response;
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
