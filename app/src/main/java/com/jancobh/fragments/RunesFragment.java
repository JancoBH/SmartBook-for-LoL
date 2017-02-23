package com.jancobh.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.jancobh.MainActivity;
import com.jancobh.adapters.RuneAdapter;
import com.jancobh.base.BaseFragment;
import com.jancobh.commons.Commons;
import com.jancobh.data.Rune;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.RuneResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RunesFragment extends BaseFragment implements ResponseListener {

    public RunesFragment() {
        // Required empty public constructor
    }

    private MainActivity mainActivity;
    private Toolbar toolbar;
    ListView list;
    RuneAdapter adapter;
    ArrayList<Rune> runes;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_runes, container, false);

        toolbar = (Toolbar)v.findViewById(R.id.runes_toolbar);

        setupToolbar();

        initUI(v);
        ArrayList<String> pathParams = new ArrayList<>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("rune");
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("runeListData", "image,sanitizedDescription");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_RUNES_REQUEST, pathParams, queryParams, null, this);

        return v;

    }

    private void initUI(View v) {
        list = (ListView) v.findViewById(R.id.listViewRunes);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar(){
        toolbar.setTitle(getString(R.string.runes_fragment_title));
        mainActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onSearch(String key) {

    }

    @Override
    public void onSuccess(Object response) {
        if (response instanceof RuneResponse) {
            RuneResponse resp = (RuneResponse)response;
            if(runes != null){
                runes.clear();
            }else{
                runes = new ArrayList<>();
            }

            Map<String, Map<String, Object>> data = resp.getData();
            for(Map.Entry<String, Map<String, Object>> entry : data.entrySet()){
                String imageUrl = (String) ((LinkedTreeMap<String, Object>)entry.getValue().get("image")).get("full");
                String sanitizedDescription = (String) entry.getValue().get("sanitizedDescription");
                String name = (String) entry.getValue().get("name");
                Rune rune = new Rune();
                rune.setName(name);
                rune.setSanitizedDescription(sanitizedDescription);
                rune.setImageUrl(imageUrl);
                runes.add(rune);
            }
            try{
                if(getContext() != null){
                    adapter = new RuneAdapter(getContext(), R.layout.rune_row, runes);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }catch (Exception ignored){}

        }
    }

    @Override
    public void onFailure(Object response) {
        String errorMessage = (String) response;
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
