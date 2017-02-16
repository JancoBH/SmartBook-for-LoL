package com.jancobh.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jancobh.activities.MatchDetailActivity;
import com.jancobh.adapters.MatchHistoryRVAdapter;
import com.jancobh.adapters.VerticalSpaceItemDecoration;
import com.jancobh.commons.Commons;
import com.jancobh.data.Game;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.RecentMatchesResponse;
import com.jancobh.service.ServiceRequest;

import java.util.List;

public class MatchHistoryFragment extends Fragment implements ResponseListener {
    private long summonerId;
    private String region = Commons.SELECTED_REGION;
    private RecyclerView matchHistoryRV;
    private MatchHistoryRVAdapter adapter;
    private List<Game> games;
    private RecentMatchesResponse recentMatchesResponse;

    public static final String EXTRA_SUMMONERID = "com.jancobh.fragments.matchhistoryfragment.extrasummonerid";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_history, container, false);
        Bundle extras = getArguments();
        if (extras != null) {
            recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES);
            summonerId = extras.getLong(EXTRA_SUMMONERID);
        }
        if(recentMatchesResponse != null){
            games = recentMatchesResponse.getGames();
        }
        matchHistoryRV = (RecyclerView) v.findViewById(R.id.matchHistoryRV);
        matchHistoryRV.addItemDecoration(new VerticalSpaceItemDecoration(20));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        matchHistoryRV.setLayoutManager(mLayoutManager);

        if(games == null || games.size() == 0) {
            ServiceRequest.getInstance(getActivity()).makeGetRecentMatchesRequest(
                    Commons.RECENT_MATCHES_REQUEST, region, summonerId + "", null, MatchHistoryFragment.this);
        }else{
            adapter = new MatchHistoryRVAdapter(getContext(), games, R.layout.list_row_match_history_rv, recyclerViewClickListener);
            matchHistoryRV.setAdapter(adapter);
        }

        return  v;
    }

    private MatchHistoryRVAdapter.RecyclerViewClickListener recyclerViewClickListener = new MatchHistoryRVAdapter.RecyclerViewClickListener() {
        @Override
        public void onRecyclerViewItemClicked(Game clickedItem, int position) {
            Intent i = new Intent(getContext(), MatchDetailActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(MatchDetailActivity.EXTRA_GAME, clickedItem);
            b.putLong(MatchDetailActivity.EXTRA_SUMMONER_ID, summonerId);
            b.putString(MatchDetailActivity.EXTRA_REGION, region);
            i.putExtras(b);
            startActivity(i);
        }
    };

    @Override
    public void onSuccess(Object response) {
        if(response instanceof RecentMatchesResponse){
            RecentMatchesResponse recentMatchesResponse = (RecentMatchesResponse) response;
            games = recentMatchesResponse.getGames();
            adapter = new MatchHistoryRVAdapter(getContext(), games, R.layout.list_row_match_history_rv, recyclerViewClickListener);
            matchHistoryRV.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(Object response) {
        if(response instanceof RecentMatchesResponse){
            Toast.makeText(getContext(), "Could not find player", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
