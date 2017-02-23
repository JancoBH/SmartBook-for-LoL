package com.jancobh.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;
import com.jancobh.data.ChampionStatsDto;
import com.jancobh.fragments.MatchHistoryFragment;
import com.jancobh.fragments.R;
import com.jancobh.fragments.SummonerChampionsFragment;
import com.jancobh.fragments.SummonerContainerFragment;
import com.jancobh.fragments.SummonerOverviewFragment;
import com.jancobh.fragments.SummonerStatisticsFragment;
import com.jancobh.responseclasses.LeagueInfoResponse;
import com.jancobh.responseclasses.RankedStatsResponse;
import com.jancobh.responseclasses.RecentMatchesResponse;
import com.jancobh.responseclasses.SummonerInfo;

public class SummonerContainerActivity extends AppCompatActivity {

    public static final String EXTRA_RECENTMATCHES = "com.jancobh.activities.SummonerContainerActivity.EXTRA_RECENTMATCHES";
    public static final String EXTRA_SUMMONER_INFO = "com.jancobh.activities.SummonerContainerActivity.EXTRA_SUMMONER_INFO";
    public static final String EXTRA_RANKEDSTATS = "com.jancobh.activities.SummonerContainerActivity.EXTRA_RANKEDSTATS";
    public static final String EXTRA_LEAGUEINFO = "com.jancobh.activities.SummonerContainerActivity.EXTRA_LEAGUEINFO";
    public static final String EXTRA_AVERAGESTATS = "com.jancobh.activities.SummonerContainerActivity.EXTRA_AVERAGESTATS";
    public static final String EXTRA_SUMMONERID = "com.jancobh.activities.SummonerContainerActivity.extrasummonerid";

    private long summonerId;
    private RecentMatchesResponse recentMatchesResponse;
    private SummonerInfo summonerInfo;
    private RankedStatsResponse rankedStatsResponse;
    private LeagueInfoResponse leagueInfoResponse;
    private ChampionStatsDto averageStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_container);
        overridePendingTransition(R.anim.slide_left_in, android.R.anim.fade_out);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            summonerId = extras.getLong(EXTRA_SUMMONERID);
            recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(EXTRA_RECENTMATCHES);
            summonerInfo = (SummonerInfo) extras.getSerializable(EXTRA_SUMMONER_INFO);
            rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(EXTRA_RANKEDSTATS);
            leagueInfoResponse = (LeagueInfoResponse) extras.getSerializable(EXTRA_LEAGUEINFO);
            averageStats = (ChampionStatsDto) extras.getSerializable(EXTRA_AVERAGESTATS);
        }

        setTabhost();

        Toolbar toolbar = (Toolbar) findViewById(R.id.summoner_detail_toolbar);
        setSupportActionBar(toolbar);

    }

    private void setTabhost(){
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new SummonerPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setIndicatorColor(getResources().getColor(R.color.accent));
        tabs.setBackgroundColor(getResources().getColor(R.color.primary));
        tabs.setDividerColor(getResources().getColor(R.color.accent));
        tabs.setTextColor(getResources().getColor(R.color.white));

        tabs.setIndicatorHeight(8);
        tabs.setViewPager(pager);
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
            return 3;
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                Bundle args = new Bundle();
                args.putSerializable(EXTRA_SUMMONER_INFO, summonerInfo);
                args.putSerializable(EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putSerializable(EXTRA_RANKEDSTATS, rankedStatsResponse);
                args.putSerializable(EXTRA_LEAGUEINFO, leagueInfoResponse);
                args.putSerializable(EXTRA_AVERAGESTATS, averageStats);
                SummonerOverviewFragment summonerOverviewFragment = new SummonerOverviewFragment();
                summonerOverviewFragment.setArguments(args);
                return summonerOverviewFragment;
            }/*else if(position == 1){
                Bundle args = new Bundle();
                args.putSerializable(EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putLong(EXTRA_SUMMONERID, summonerInfo.getId());
                MatchHistoryFragment matchHistoryFragment = new MatchHistoryFragment();
                matchHistoryFragment.setArguments(args);
                return matchHistoryFragment;
            }*/else if(position == 1){
                SummonerChampionsFragment summonerChampionsFragment = new SummonerChampionsFragment();
                Bundle args = new Bundle();
                args.putSerializable(EXTRA_RANKEDSTATS, rankedStatsResponse);
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
