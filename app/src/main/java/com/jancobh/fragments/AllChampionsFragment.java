package com.jancobh.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.jancobh.MainActivity;
import com.jancobh.adapters.GridViewAdapter;
import com.jancobh.commons.Commons;
import com.jancobh.data.Champion;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.AllChampionsResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AllChampionsFragment extends Fragment implements ResponseListener, AdapterView.OnItemClickListener, TextWatcher {


    private MainActivity mainActivity;
    private Toolbar toolbar;
    private GridView gridView;
    private GridViewAdapter adapter;
    private TextView noChampsFoundTV;

    public AllChampionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_champions, container, false);
        initUI(view);
        toolbar = (Toolbar)view.findViewById(R.id.champions_toolbar);
        setupToolbar();

        if(Commons.allChampions == null || Commons.allChampions.size() == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> pathParams = new ArrayList<>();
                    pathParams.add("static-data");
                    pathParams.add(Commons.getRegion());
                    pathParams.add("v1.2");
                    pathParams.add("champion");
                    HashMap<String, String> queryParams = new HashMap<>();
                    queryParams.put("locale", Commons.getLocale());
                    queryParams.put("version", Commons.LATEST_VERSION);
                    queryParams.put("champData", "altimages");
                    queryParams.put("api_key", Commons.API_KEY);
                    ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_CHAMPIONS_REQUEST, pathParams, queryParams, null, AllChampionsFragment.this);
                }
            }, 400);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(adapter == null || adapter.getCount() == 0) {
                        adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
                        gridView.setAdapter(adapter);
                    }
                }
            }, 400);
        }
        return view;
    }

    private void initUI(View v){
        noChampsFoundTV = (TextView)v.findViewById(R.id.noChampsFoundTV);
        gridView = (GridView)v.findViewById(R.id.gridview_champions);
        EditText searchBar = (EditText) v.findViewById(R.id.edittextSearchBar);
        searchBar.addTextChangedListener(this);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar(){
        toolbar.setTitle(getString(R.string.champions_fragment_title));
        mainActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        noChampsFoundTV.setVisibility(View.GONE);
        if(s.length() >= 2){
            ArrayList<Champion> searchResultChampions = new ArrayList<>();
            for(Champion c : Commons.allChampions){
                if(containsIgnoreCase(c.getChampionName(), String.valueOf(s))){
                    searchResultChampions.add(c);
                }
            }
            adapter = new GridViewAdapter(getContext(), R.layout.row_grid, searchResultChampions);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if(searchResultChampions.size() <= 0){
                noChampsFoundTV.setVisibility(View.VISIBLE);
            }else if(searchResultChampions.size() > 0){
                noChampsFoundTV.setVisibility(View.GONE);
            }
        }else{
            if(Commons.allChampions != null && Commons.allChampions.size() > 0) {
                adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
                if(gridView != null) {
                    try {
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }catch (Exception ignored){}
                }
            }
        }
    }

    private void hideKeyboard() {
        try{
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch(Exception ignored){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideKeyboard();
        Champion c = (Champion)gridView.getItemAtPosition(position);
        int champId = c.getId();
        ChampionDetailFragment fragment = new ChampionDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
        args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, c.getChampionImageUrl());
        args.putString(ChampionDetailFragment.EXTRA_CHAMPION_NAME, c.getKey());
        fragment.setArguments(args);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
        ft.replace(R.id.fragment_container, fragment).addToBackStack(Commons.CHAMPION_DETAILS_FRAGMENT).commit();
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    @Override
    public void onSuccess(Object response) {
        try{
            if(response instanceof AllChampionsResponse){
                AllChampionsResponse resp = (AllChampionsResponse) response;
                Map<String, Map<String, String>> data = resp.getData();
                if(Commons.allChampions != null){
                    Commons.allChampions.clear();
                }else{
                    Commons.allChampions = new ArrayList<>();
                }
                for(Map.Entry<String, Map<String, String>> entry : data.entrySet()){
                    String key = entry.getKey();
                    String imageUrl = Commons.CHAMPION_IMAGE_BASE_URL + key + ".png";
                    Champion c = new Champion();
                    c.setChampionImageUrl(imageUrl);
                    c.setChampionName(entry.getValue().get("name"));
                    c.setId(Integer.parseInt(entry.getValue().get("id")));
                    c.setKey(entry.getValue().get("key"));
                    c.setTitle("\"" + entry.getValue().get("title") + "\"");
                    Commons.allChampions.add(c);
                }
                if(Commons.allChampions != null) {
                    Collections.sort(Commons.allChampions, new Comparator<Champion>() {
                        @Override
                        public int compare(Champion c1, Champion c2) {
                            return c1.getChampionName().compareTo(c2.getChampionName());
                        }
                    });
                }
                adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Object response) {
        try {
            String errorMessage = String.valueOf(response);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }catch (Exception ignored){}
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }
}
