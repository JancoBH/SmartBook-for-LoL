package com.jancobh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class ChampionDetailFragment extends Fragment {

    public static String EXTRA_CHAMPION_ID = "com.jancobh.fragments.championdetailfragment.extrachampionid";
    public static String EXTRA_CHAMPION_IMAGE_URL ="com.jancobh.fragments.championdetailfragment.extrachampionimageurl";
    public static String EXTRA_CHAMPION_NAME = "com.jancobh.fragments.championdetailfragment.extrachampionname";

    private int champId;
    private String champLogoImageUrl;
    private String splashImageUrl;

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_champion_detail, container,
                false);
        getExtras();
        setTabhost(v);

        return v;
    }

    private void getExtras(){
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(EXTRA_CHAMPION_ID);
            champLogoImageUrl = args.getString(EXTRA_CHAMPION_IMAGE_URL);
            splashImageUrl = args.getString(EXTRA_CHAMPION_NAME);
        }
    }

    private void setTabhost(View v){
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new ChampionDetailAdapter(getChildFragmentManager()));

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabs.setIndicatorColor(getResources().getColor(R.color.accent));
        tabs.setBackgroundColor(getResources().getColor(R.color.primary));
        /*tabs.setDividerColor(getResources().getColor(R.color.white));*/
        tabs.setTextColor(getResources().getColor(R.color.white));
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getActivity().getResources().getDisplayMetrics());
        tabs.setTextSize(textSize);

        tabs.setIndicatorHeight(8);
        tabs.setViewPager(pager);
    }

    public class ChampionDetailAdapter extends FragmentPagerAdapter {

        public ChampionDetailAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return getResources().getString(R.string.general);
            }else if(position == 1){
                return getResources().getString(R.string.abilities);
            }else if(position == 2){
                return getResources().getString(R.string.legend);
            }else{
                return getResources().getString(R.string.strategy);
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
                LegendFragment legendFragment = new LegendFragment();
                legendFragment.setArguments(args);
                return legendFragment;
            }else{
                StrategyFragment strategyFragment = new StrategyFragment();
                strategyFragment.setArguments(args);
                return strategyFragment;
            }
        }
    }

}
