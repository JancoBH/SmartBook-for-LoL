package com.jancobh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jancobh.fragments.AboutFragment;
import com.jancobh.fragments.AllChampionsFragment;
import com.jancobh.fragments.CollapsingToolbarFragment;
import com.jancobh.fragments.MatchInfoFragment;
import com.jancobh.fragments.NewItemsFragment;
import com.jancobh.fragments.R;
import com.jancobh.fragments.SummonerChampionsFragment;
import com.jancobh.fragments.SummonerSearchFragment;
import com.jancobh.fragments.TabFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String COLLAPSING_TOOLBAR_FRAGMENT_TAG = "collapsing_toolbar";
    private final static String TAB_FRAGMENT_TAG = "tab";
    private final static String ABOUT_FRAGMENT_TAG = "about";
    private final static String CHAMPIONS_FRAGMENT_TAG = "champions";
    private final static String ITEMS_FRAGMENT_TAG = "items";
    private final static String SUMMONER_FRAGMENT_TAG = "summoner";
    private final static String MATCH_FRAGMENT_TAG = "match";
    private final static String SELECTED_TAG = "selected_index";
    private final static int COLLAPSING_TOOLBAR = 0;
    private final static int TAB = 1;
    private final static int ABOUT = 2;
    private final static int CHAMPIONS = 3;
    private final static int ITEMS = 4;
    private final static int SUMMONER = 5;
    private final static int MATCH = 5;

    private static int selectedIndex;

    private DrawerLayout drawerLayout;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        if(savedInstanceState!=null){
            navigationView.getMenu().getItem(savedInstanceState.getInt(SELECTED_TAG)).setChecked(true);
            return;
        }

        selectedIndex = COLLAPSING_TOOLBAR;

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                new CollapsingToolbarFragment(),COLLAPSING_TOOLBAR_FRAGMENT_TAG).commit();

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

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
            case R.id.item_items:
                if(!menuItem.isChecked()){
                    selectedIndex = ITEMS;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new NewItemsFragment(),ITEMS_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_summoner:
                if(!menuItem.isChecked()){
                    selectedIndex = SUMMONER;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SummonerSearchFragment(),SUMMONER_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.item_match:
                if(!menuItem.isChecked()){
                    selectedIndex = MATCH;
                    menuItem.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MatchInfoFragment(),MATCH_FRAGMENT_TAG).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
        return false;
    }

    public void setupNavigationDrawer(Toolbar toolbar){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer) {
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

    public Context getContext () {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
