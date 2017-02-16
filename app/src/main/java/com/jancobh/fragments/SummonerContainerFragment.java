package com.jancobh.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.jancobh.MainActivity;
import com.jancobh.data.ChampionStatsDto;
import com.jancobh.responseclasses.LeagueInfoResponse;
import com.jancobh.responseclasses.RankedStatsResponse;
import com.jancobh.responseclasses.RecentMatchesResponse;
import com.jancobh.responseclasses.SummonerInfo;

public class SummonerContainerFragment extends Fragment {

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;
    private Toolbar toolbar;

    private RecentMatchesResponse recentMatchesResponse;
    private SummonerInfo summonerInfo;
    private RankedStatsResponse rankedStatsResponse;
    private LeagueInfoResponse leagueInfoResponse;
    private ChampionStatsDto averageStats;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_summoner_container, container, false);
        Bundle extras = getArguments();

        if (extras != null) {
            recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES);
            summonerInfo = (SummonerInfo) extras.getSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO);
            rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS);
            leagueInfoResponse = (LeagueInfoResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO);
            averageStats = (ChampionStatsDto) extras.getSerializable(SummonerOverviewFragment.EXTRA_AVERAGESTATS);
        }

        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pager.setAdapter(new SummonerPagerAdapter(getChildFragmentManager()));
                tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
                tabs.setIndicatorColor(getResources().getColor(R.color.accent));
                tabs.setBackgroundColor(getResources().getColor(R.color.primary));
                tabs.setDividerColor(getResources().getColor(R.color.white));
                tabs.setTextColor(getResources().getColor(R.color.white));
                int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getActivity().getResources().getDisplayMetrics());
                tabs.setTextSize(textSize);

                tabs.setIndicatorHeight(8);
                tabs.setViewPager(pager);
            }
        }, 300);

        return v;
    }

    private class SummonerPagerAdapter extends FragmentPagerAdapter {

        SummonerPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return getResources().getString(R.string.overview);
            }else if(position == 1){
                return getResources().getString(R.string.match_history);
            }else if(position == 2){
                return getResources().getString(R.string.champions);
            }else{
                return getResources().getString(R.string.statistics);
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO, summonerInfo);
                args.putSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS, rankedStatsResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO, leagueInfoResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_AVERAGESTATS, averageStats);
                SummonerOverviewFragment summonerOverviewFragment = new SummonerOverviewFragment();
                summonerOverviewFragment.setArguments(args);
                return summonerOverviewFragment;
            }else if(position == 1){
                MatchHistoryFragment matchHistoryFragment = new MatchHistoryFragment();
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putLong(MatchHistoryFragment.EXTRA_SUMMONERID, summonerInfo.getId());
                matchHistoryFragment.setArguments(args);
                return matchHistoryFragment;
            }else if(position == 2){
                SummonerChampionsFragment summonerChampionsFragment = new SummonerChampionsFragment();
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS, rankedStatsResponse);
                summonerChampionsFragment.setArguments(args);
                return summonerChampionsFragment;
            }else{
                SummonerStatisticsFragment statisticsFragment = new SummonerStatisticsFragment();
                Bundle args = new Bundle();
                args.putSerializable(SummonerStatisticsFragment.EXTRA_STATISTICS, rankedStatsResponse);
                args.putSerializable(SummonerStatisticsFragment.EXTRA_AVERAGE_STATS, averageStats);
                statisticsFragment.setArguments(args);
                return statisticsFragment;
            }
        }
    }

}
