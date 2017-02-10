package com.jancobh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jancobh.commons.Commons;
import com.jancobh.data.Champion;
import com.jancobh.fragments.AboutFragment;
import com.jancobh.fragments.AllChampionsFragment;
import com.jancobh.fragments.CollapsingToolbarFragment;
import com.jancobh.fragments.R;
import com.jancobh.fragments.TabFragment;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.AllChampionsResponse;
import com.jancobh.responseclasses.SummonerSpellsResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, ResponseListener {

    private final static String COLLAPSING_TOOLBAR_FRAGMENT_TAG = "collapsing_toolbar";
    private final static String TAB_FRAGMENT_TAG = "tab";
    private final static String ABOUT_FRAGMENT_TAG = "about";
    private final static String CHAMPIONS_FRAGMENT_TAG = "champions";
    private final static String SELECTED_TAG = "selected_index";
    private final static int COLLAPSING_TOOLBAR = 0;
    private final static int TAB = 1;
    private final static int ABOUT = 2;
    private final static int CHAMPIONS = 3;

    private static int selectedIndex;
    int allchampionsRequestCount = 0, allSpellsRequestCount = 0;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View dialogView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        if(savedInstanceState!=null){
            navigationView.getMenu().getItem(savedInstanceState.getInt(SELECTED_TAG)).setChecked(true);
            return;
        }

        selectedIndex = COLLAPSING_TOOLBAR;

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                new CollapsingToolbarFragment(),COLLAPSING_TOOLBAR_FRAGMENT_TAG).commit();

        makeGetAllChampionsRequest();
        makeGetAllSpellsRequest();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TAG, selectedIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.item_collapsing_toolbar:
                if(!menuItem.isChecked()){
                    selectedIndex = COLLAPSING_TOOLBAR;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new CollapsingToolbarFragment(), COLLAPSING_TOOLBAR_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_tab:
                if(!menuItem.isChecked()){
                    selectedIndex = TAB;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new TabFragment(),TAB_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_about:
                if(!menuItem.isChecked()){
                    selectedIndex = ABOUT;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AboutFragment(),ABOUT_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_champions:
                if(!menuItem.isChecked()){
                    selectedIndex = CHAMPIONS;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AllChampionsFragment(),CHAMPIONS_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
        return false;
    }

    //Click listener for Snackbar UNDO action
    @Override
    public void onClick(View view) {
    }

    public void setupNavigationDrawer(Toolbar toolbar){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void makeGetAllChampionsRequest(){
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("champData", "altimages");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_CHAMPIONS_REQUEST, pathParams, queryParams, null, this);
    }

    private void makeGetAllSpellsRequest(){
        ArrayList<String> pathParams2 = new ArrayList<>();
        pathParams2.add("static-data");
        pathParams2.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams2.add("v1.2");
        pathParams2.add("summoner-spell");
        HashMap<String, String> queryParams2 = new HashMap<String, String>();
        queryParams2.put("spellData", "image");
        queryParams2.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.SUMMONER_SPELLS_REQUEST, pathParams2, queryParams2, null, this);

    }

    public void onSuccess(Object response) {
        if (response instanceof AllChampionsResponse) {
            try {
                AllChampionsResponse resp = (AllChampionsResponse) response;
                Map<String, Map<String, String>> data = resp.getData();
                if (Commons.allChampions != null) {
                    Commons.allChampions.clear();
                } else {
                    Commons.allChampions = new ArrayList<Champion>();
                }
                for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
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
                if (Commons.allChampions != null) {
                    Collections.sort(Commons.allChampions, new Comparator<Champion>() {
                        @Override
                        public int compare(Champion c1, Champion c2) {
                            return c1.getChampionName().compareTo(c2.getChampionName());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (response instanceof SummonerSpellsResponse){
            if(response != null) {
                SummonerSpellsResponse summonerSpellsResponse = (SummonerSpellsResponse) response;
                if (summonerSpellsResponse != null) {
                    Commons.allSpells = summonerSpellsResponse.getSpells();
                }
            }
        }
    }

    public void onFailure (Object response){
        if(response instanceof Integer){
            Integer requestID = (Integer) response;
            if(requestID == Commons.ALL_CHAMPIONS_REQUEST) {
                if (allchampionsRequestCount < 3) {
                    allchampionsRequestCount++;
                    makeGetAllChampionsRequest();
                }
            }else if(requestID == Commons.SUMMONER_SPELLS_REQUEST){
                if(allSpellsRequestCount < 3){
                    allSpellsRequestCount++;
                    makeGetAllSpellsRequest();
                }
            }
        }
    }

    public Context getContext () {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
