package com.jancobh.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.jancobh.activities.ChampionDetailActivity;
import com.jancobh.adapters.GridViewItemsAdapter;
import com.jancobh.commons.Commons;
import com.jancobh.data.Blocks;
import com.jancobh.data.Items;
import com.jancobh.data.Recommended;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.ChampionLegendResponse;
import com.jancobh.responseclasses.ChampionOverviewResponse;
import com.jancobh.responseclasses.RecommendedItemsResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class ChampionOverviewFragment extends Fragment implements ResponseListener, AdapterView.OnItemClickListener {

    private static final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();

    private int champId;
    private String champLogoImageUrl;
    TextView legend;

    private TextView champName;
    private TextView champTitle;
    private RelativeLayout barAttack;
    private RelativeLayout barDefense;
    private RelativeLayout barMagic;
    private RelativeLayout barDifficulty;
    private TextView tags;
    public static int lastSelectedChampionId;
    private GridView gridviewStartingItems, gridviewEssentialItems, gridviewOffensiveItems, gridviewDeffensiveItems;
    private ProgressBar progressStartingItems;
    private ProgressBar progressEssentialItems;
    private ProgressBar progressOffensiveItems;
    private ProgressBar progressDeffensiveItems;
    private TextView textViewStartingItems, textViewEssentialItems, textViewOffensiveItems, textViewDeffensiveItems;

    private ChampionOverviewResponse championOverviewResponse;
    private RecommendedItemsResponse recommendedItemsResponse;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_champion_overview, container, false);
        getExtras();
        initUI(v);
        getFragmentData();
        showInterstitial();
        return v;
    }

    private void getFragmentData() {

        ArrayList<String> pathParamsL = new ArrayList<>();
        pathParamsL.add("static-data");
        pathParamsL.add(Commons.getRegion());
        pathParamsL.add("v1.2");
        pathParamsL.add("champion");
        pathParamsL.add(String.valueOf(champId));
        HashMap<String, String> queryParamsL = new HashMap<>();
        queryParamsL.put("locale", Commons.getLocale());
        queryParamsL.put("champData", "lore");
        queryParamsL.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_LEGEND_REQUEST, pathParamsL, queryParamsL, null, this);

        if(championOverviewResponse == null) {
            ArrayList<String> pathParams = new ArrayList<>();
            pathParams.add("static-data");
            pathParams.add(Commons.getRegion());
            pathParams.add("v1.2");
            pathParams.add("champion");
            pathParams.add(String.valueOf(champId));
            HashMap<String, String> queryParams = new HashMap<>();
            queryParams.put("locale", Commons.getLocale());
            queryParams.put("version", Commons.LATEST_VERSION);
            queryParams.put("champData", "info,tags");
            queryParams.put("api_key", Commons.API_KEY);
            ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_OVERVIEW_REQUEST, pathParams, queryParams, null, this);
        } else {
            handleChampionOverviewResponse();
        }

        if(recommendedItemsResponse == null) {
            ArrayList<String> pathParams2 = new ArrayList<>();
            pathParams2.add("static-data");
            pathParams2.add(Commons.getRegion());
            pathParams2.add("v1.2");
            pathParams2.add("champion");
            pathParams2.add(String.valueOf(champId));
            HashMap<String, String> queryParams2 = new HashMap<>();
            queryParams2.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
            queryParams2.put("version", Commons.RECOMMENDED_ITEMS_VERSION);
            queryParams2.put("champData", "recommended");
            queryParams2.put("api_key", Commons.API_KEY);
            ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.RECOMMENDED_ITEMS_REQUEST, pathParams2, queryParams2, null, this);
        } else {
            handleRecommendedItemsResponse();
        }
    }

    private void showInterstitial(){
        /*try {
            if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
                ((LolApplication) (getActivity().getApplication())).showInterstitial();
            }
        }catch (Exception ignored){}*/
    }
    private void getExtras(){
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(ChampionDetailActivity.EXTRA_CHAMPION_ID);
            lastSelectedChampionId = champId;
            champLogoImageUrl = args.getString(ChampionDetailActivity.EXTRA_CHAMPION_IMAGE_URL);
        }
    }

    private void initUI(View v){
        gridviewStartingItems = (GridView)v.findViewById(R.id.gridviewStartingItems);
        gridviewEssentialItems = (GridView)v.findViewById(R.id.gridviewEssentialItems);
        gridviewOffensiveItems = (GridView)v.findViewById(R.id.gridviewOffensiveItems);
        gridviewDeffensiveItems = (GridView)v.findViewById(R.id.gridviewDeffensiveItems);
        gridviewStartingItems.setOnItemClickListener(this);
        gridviewEssentialItems.setOnItemClickListener(this);
        gridviewOffensiveItems.setOnItemClickListener(this);
        gridviewDeffensiveItems.setOnItemClickListener(this);
        ImageView champLogo = (ImageView) v.findViewById(R.id.imageViewChampionImage);
        AQuery aq = new AQuery(champLogo);
        ProgressBar progress = (ProgressBar) v.findViewById(R.id.imageProgress);
        progressStartingItems = (ProgressBar)v.findViewById(R.id.progressStartingItems);
        progressEssentialItems = (ProgressBar)v.findViewById(R.id.progressEssentialItems);
        progressOffensiveItems = (ProgressBar)v.findViewById(R.id.progressOffensiveItems);
        progressDeffensiveItems = (ProgressBar)v.findViewById(R.id.progressDeffensiveItems);
        textViewDeffensiveItems = (TextView)v.findViewById(R.id.textViewDeffensiveItems);
        textViewEssentialItems = (TextView)v.findViewById(R.id.textViewEssentialItems);
        textViewOffensiveItems = (TextView)v.findViewById(R.id.textViewOffensiveItems);
        textViewStartingItems = (TextView)v.findViewById(R.id.textViewStartingItems);
        aq.progress(progress).image(champLogoImageUrl, true, true);
        champName = (TextView)v.findViewById(R.id.textViewChampName);
        champTitle = (TextView)v.findViewById(R.id.textViewChampTitle);
        tags = (TextView)v.findViewById(R.id.textviewTags);
        TextView tagsTitle = (TextView) v.findViewById(R.id.textviewTagsTitle);
        tagsTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        barAttack = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarAttack);
        barDefense = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarDefense);
        barMagic = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarMagic);
        barDifficulty = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarDifficulty);
        legend = (TextView)v.findViewById(R.id.tvLore);
    }

    private String formatLoreString(String lore) {
        if(lore.contains("<br>")){
            lore = lore.replaceAll("<br>", "\n");
        }
        return lore;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideKeyboard();
        Items i = null;
        int itemId = 0;
        if(parent.getId() == R.id.gridviewStartingItems){
            i = (Items)gridviewStartingItems.getItemAtPosition(position);
            itemId = i.getId();
        }else if(parent.getId() == R.id.gridviewEssentialItems){
            i = (Items)gridviewEssentialItems.getItemAtPosition(position);
            itemId = i.getId();
        }else if(parent.getId() == R.id.gridviewOffensiveItems){
            i = (Items)gridviewOffensiveItems.getItemAtPosition(position);
            itemId = i.getId();
        }else if(parent.getId() == R.id.gridviewDeffensiveItems){
            i = (Items)gridviewDeffensiveItems.getItemAtPosition(position);
            itemId = i.getId();
        }
        if(i != null){
            ItemDetailFragment fragment = new ItemDetailFragment();
            Bundle args = new Bundle();
            args.putInt(ItemDetailFragment.EXTRA_ITEM_ID, itemId);
            args.putString(ItemDetailFragment.EXTRA_ITEM_IMAGE_URL, Commons.ITEM_IMAGES_BASE_URL + String.valueOf(i.getId()) + ".png");
            fragment.setArguments(args);
            FragmentManager fm = getParentFragment().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
            ft.addToBackStack(Commons.ITEM_DETAIL_FRAGMENT).replace(R.id.fragment_container, fragment).commit();
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


    private void setBarLength(View v, int length){
        int pixels;
        try {
            try {
                pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        length, getContext().getResources().getDisplayMetrics()) * 25;
            } catch (Exception e) {
                pixels = 300;
            }
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = pixels;
            v.setLayoutParams(params);
        }catch (Exception ignored){

        }
    }

    private void setAbilityBars(ChampionOverviewResponse resp) {

        setBarLength(barAttack, resp.getInfo().getAttack());
        setBarLength(barDefense, resp.getInfo().getDefense());
        setBarLength(barMagic, resp.getInfo().getMagic());
        setBarLength(barDifficulty, resp.getInfo().getDifficulty());
        stretchBar(barAttack);
        stretchBar(barDefense);
        stretchBar(barMagic);
        stretchBar(barDifficulty);

    }

    private void setTags(ChampionOverviewResponse resp) {
        String tagsString="";

        ArrayList<String> tagsResponse = resp.getTags();
        for(String tag : tagsResponse){
            tagsString = tagsString+ "- " + Commons.getTurkishTag(tag) + "\n";
        }
        tags.setText(tagsString);

    }

    private void stretchBar(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            long animationDuration = (long) (1000);
            view.setPivotX(view.getWidth());

            PropertyValuesHolder pvhSX;
            pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, .1f, 1f);
            ObjectAnimator stretchAnim =
                    ObjectAnimator.ofPropertyValuesHolder(view, pvhSX);
            stretchAnim.setInterpolator(sDecelerator);
            stretchAnim.setDuration(animationDuration);


            AnimatorSet set = new AnimatorSet();
            set.playSequentially(stretchAnim);
            set.start();
        }
    }

    private void handleChampionOverviewResponse() {
        if(championOverviewResponse != null) {
            champName.setText(championOverviewResponse.getName());
            champTitle.setText(championOverviewResponse.getTitle());
            setAbilityBars(championOverviewResponse);
            setTags(championOverviewResponse);
        }
    }

    private void handleRecommendedItemsResponse() {
        if(recommendedItemsResponse != null) {
            boolean startingOK = false, essentialOK = false, offensiveOK = false, defensiveOK = false;
            if (recommendedItemsResponse.getRecommended() != null) {
                for (Recommended r : recommendedItemsResponse.getRecommended()) {
                    if (r.getMode().equals("CLASSIC")) {
                        if (r.getBlocks() != null && r.getBlocks().size() > 0) {
                            for (Blocks b : r.getBlocks()) {
                                ArrayList<Items> items = b.getItems();
                                if (items != null && items.size() > 0) {
                                    switch (b.getType()) {
                                        case "starting":
                                            startingOK = true;
                                            GridViewItemsAdapter startingItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewStartingItems.setAdapter(startingItemsAdapter);
                                            startingItemsAdapter.notifyDataSetChanged();
                                            progressStartingItems.setVisibility(View.GONE);
                                            break;
                                        case "essential":
                                            essentialOK = true;
                                            GridViewItemsAdapter essentialItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewEssentialItems.setAdapter(essentialItemsAdapter);
                                            essentialItemsAdapter.notifyDataSetChanged();
                                            progressEssentialItems.setVisibility(View.GONE);
                                            break;
                                        case "offensive":
                                            offensiveOK = true;
                                            GridViewItemsAdapter offensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewOffensiveItems.setAdapter(offensiveItemsAdapter);
                                            offensiveItemsAdapter.notifyDataSetChanged();
                                            progressOffensiveItems.setVisibility(View.GONE);
                                            break;
                                        case "defensive":
                                            defensiveOK = true;
                                            GridViewItemsAdapter deffensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewDeffensiveItems.setAdapter(deffensiveItemsAdapter);
                                            deffensiveItemsAdapter.notifyDataSetChanged();
                                            progressDeffensiveItems.setVisibility(View.GONE);
                                            break;
                                        case "ability_scaling":
                                            offensiveOK = true;
                                            offensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewOffensiveItems.setAdapter(offensiveItemsAdapter);
                                            offensiveItemsAdapter.notifyDataSetChanged();
                                            progressOffensiveItems.setVisibility(View.GONE);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }

                if(!startingOK){
                    textViewStartingItems.setVisibility(View.GONE);
                    progressStartingItems.setVisibility(View.GONE);
                    gridviewStartingItems.setVisibility(View.GONE);
                }

                if(!essentialOK){
                    textViewEssentialItems.setVisibility(View.GONE);
                    progressEssentialItems.setVisibility(View.GONE);
                    gridviewEssentialItems.setVisibility(View.GONE);
                }

                if(!defensiveOK){
                    textViewDeffensiveItems.setVisibility(View.GONE);
                    progressDeffensiveItems.setVisibility(View.GONE);
                    gridviewDeffensiveItems.setVisibility(View.GONE);
                }

                if(!offensiveOK){
                    textViewOffensiveItems.setVisibility(View.GONE);
                    progressOffensiveItems.setVisibility(View.GONE);
                    gridviewOffensiveItems.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ChampionOverviewResponse){
            championOverviewResponse = (ChampionOverviewResponse)response;
            handleChampionOverviewResponse();
        }else if(response instanceof RecommendedItemsResponse){
            try {
                recommendedItemsResponse = (RecommendedItemsResponse) response;
                handleRecommendedItemsResponse();
            }catch (Exception ignored){}
        } else if(response instanceof ChampionLegendResponse){
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

