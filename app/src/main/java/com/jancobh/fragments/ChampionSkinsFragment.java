package com.jancobh.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jancobh.activities.ChampionDetailActivity;
import com.jancobh.activities.FullScreenSkinActivity;
import com.jancobh.adapters.ChampionSkinsAdapter;
import com.jancobh.commons.Commons;
import com.jancobh.data.Skin;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.ChampionSkinsResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ChampionSkinsFragment extends Fragment implements ResponseListener, AdapterView.OnItemClickListener {

    ListView skinsList;
    ChampionSkinsAdapter adapter;
    int champId;
    String key;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_champion_skins, container, false);
        getExtras();
        skinsList = (ListView)v.findViewById(R.id.skinsList);
        skinsList.setOnItemClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> pathParams = new ArrayList<String>();
                pathParams.add("static-data");
                pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
                pathParams.add("v1.2");
                pathParams.add("champion");
                pathParams.add(String.valueOf(champId));
                HashMap<String, String> queryParams = new HashMap<String, String>();
                queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
                queryParams.put("version", Commons.LATEST_VERSION);
                queryParams.put("champData", "skins");
                queryParams.put("api_key", Commons.API_KEY);
                ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_SKINS_REQUEST, pathParams, queryParams, null, ChampionSkinsFragment.this);
            }
        }, 350);
        return v;
    }

    private void getExtras() {
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(ChampionDetailActivity.EXTRA_CHAMPION_ID);
        }
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ChampionSkinsResponse) {
            ChampionSkinsResponse resp = (ChampionSkinsResponse) response;
            ArrayList<Skin> skins = resp.getSkins();
            key = resp.getKey();
            adapter = null;
            adapter = new ChampionSkinsAdapter(getContext(), R.layout.listrow_champion_skins, skins, key);
            skinsList.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(Object response) {
        try {
            String errorMessage = (String) response;
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }catch (Exception ignored){

        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Skin s = (Skin)skinsList.getItemAtPosition(position);
        Intent i = new Intent(getActivity(), FullScreenSkinActivity.class);
        i.putExtra("EXTRA_SKIN_KEY", key);
        i.putExtra("EXTRA_SKIN_POSITION", position);
        startActivity(i);

    }
}
