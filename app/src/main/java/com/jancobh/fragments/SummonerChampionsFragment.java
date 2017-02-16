package com.jancobh.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jancobh.adapters.SummonerChampionsAdapter;
import com.jancobh.data.ChampionStatsDto;
import com.jancobh.responseclasses.RankedStatsResponse;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class SummonerChampionsFragment extends Fragment {

    private ExpandableStickyListHeadersListView champList;

    private List<ChampionStatsDto> champions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summoner_champions, container, false);
        TextView noWatchListTV = (TextView) v.findViewById(R.id.noWatchListTV);
        champList = (ExpandableStickyListHeadersListView)v.findViewById(R.id.champList);

        Bundle extras = getArguments();
        if (extras != null) {
            RankedStatsResponse rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS);
            if(rankedStatsResponse != null){
                champions = rankedStatsResponse.getChampions();
            }
        }

        if(champions != null && champions.size() > 0){
            final ArrayList<Long> headerIds = new ArrayList<>();
            for(int i = 0; i < champions.size(); i++){
                ChampionStatsDto dto = champions.get(i);
                if(dto != null && !headerIds.contains(dto.getId())){
                    try {
                        headerIds.add(Long.parseLong(dto.getId() + ""));
                    }catch (Exception ignored){}
                }
            }
            noWatchListTV.setVisibility(View.GONE);
            champList.setVisibility(View.VISIBLE);
            SummonerChampionsAdapter adapter = new SummonerChampionsAdapter(getActivity(), R.layout.list_row_summoner_champions, champions);
            champList.setAdapter(adapter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(Long id : headerIds){
                        if(id != null) {
                            champList.collapse(id);
                        }
                    }
                }
            }, 750);


            champList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
                @Override
                public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                    if (champList.isHeaderCollapsed(headerId)) {
                        champList.expand(headerId);
                        header.findViewById(R.id.downArrow).setBackgroundResource(R.drawable.arrow_up);
                    } else {
                        champList.collapse(headerId);
                        header.findViewById(R.id.downArrow).setBackgroundResource(R.drawable.arrow_down);
                    }
                }
            });
        }else{
            noWatchListTV.setVisibility(View.VISIBLE);
            champList.setVisibility(View.GONE);
        }


        return v;
    }

}
