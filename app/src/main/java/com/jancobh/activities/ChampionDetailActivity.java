package com.jancobh.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.jancobh.fragments.ChampionOverviewFragment;
import com.jancobh.fragments.ChampionSkinsFragment;
import com.jancobh.fragments.ChampionSpellsFragment;
import com.jancobh.fragments.R;
import com.jancobh.fragments.StrategyFragment;

public class ChampionDetailActivity extends AppCompatActivity {

    public static String EXTRA_CHAMPION_ID = "com.jancobh.activities.championdetailactivity.extrachampionid";
    public static String EXTRA_CHAMPION_IMAGE_URL ="com.jancobh.activities.championdetailactivity.extrachampionimageurl";
    public static String EXTRA_CHAMPION_NAME = "com.jancobh.activities.championdetailactivity.extrachampionname";

    private int champId;
    private String champLogoImageUrl;
    private String splashImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);
        overridePendingTransition(R.anim.slide_left_in, android.R.anim.fade_out);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras.getInt(EXTRA_CHAMPION_ID) > 0 && extras.getString(EXTRA_CHAMPION_IMAGE_URL) != null && extras.getString(EXTRA_CHAMPION_NAME) != null){

            champId = extras.getInt(EXTRA_CHAMPION_ID);
            champLogoImageUrl = extras.getString(EXTRA_CHAMPION_IMAGE_URL);
            splashImageUrl = extras.getString(EXTRA_CHAMPION_NAME);
            setTitle(splashImageUrl);
        }

        setTabhost();

        Toolbar toolbar = (Toolbar) findViewById(R.id.champion_detail_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setTabhost(){
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new ChampionDetailAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setIndicatorColor(getResources().getColor(R.color.accent));
        tabs.setBackgroundColor(getResources().getColor(R.color.primary));
        tabs.setDividerColor(getResources().getColor(R.color.accent));
        tabs.setTextColor(getResources().getColor(R.color.white));
        tabs.setIndicatorHeight(8);
        tabs.setViewPager(pager);
    }

    private class ChampionDetailAdapter extends FragmentPagerAdapter {

        ChampionDetailAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return getResources().getString(R.string.general);
            }else if(position == 1){
                return getResources().getString(R.string.abilities);
            }else if(position == 2){
                return getResources().getString(R.string.strategy);
            }else{
                return getResources().getString(R.string.skins);
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt(EXTRA_CHAMPION_ID, champId);
            args.putString(EXTRA_CHAMPION_IMAGE_URL, champLogoImageUrl);
            args.putString(EXTRA_CHAMPION_NAME, splashImageUrl);
            if(position == 0){
                ChampionOverviewFragment championOverviewFragment = new ChampionOverviewFragment();
                championOverviewFragment.setArguments(args);
                return championOverviewFragment;
            }else if(position == 1){
                ChampionSpellsFragment championSpellsFragment = new ChampionSpellsFragment();
                championSpellsFragment.setArguments(args);
                return championSpellsFragment;
            }else if(position == 2){
                StrategyFragment strategyFragment = new StrategyFragment();
                strategyFragment.setArguments(args);
                return strategyFragment;
            }else{
                ChampionSkinsFragment championSkinsFragment = new ChampionSkinsFragment();
                championSkinsFragment.setArguments(args);
                return championSkinsFragment;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
