package com.jancobh.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jancobh.adapters.MatchInfoAdapter;
import com.jancobh.commons.Commons;
import com.jancobh.data.Champion;
import com.jancobh.data.Summoner;
import com.jancobh.fragments.R;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.AllChampionsResponse;
import com.jancobh.responseclasses.MatchInfoResponse;
import com.jancobh.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MatchInfoActivity extends AppCompatActivity implements ResponseListener, AdapterView.OnItemClickListener{

    private MatchInfoResponse response;
    private ListView team1LV, team2LV;
    private ArrayList<Summoner> team1Summoners, team2Summoners;
    private TextView matchTime;
    private long counter;
    private ProgressBar loadingProgress;
    private ScrollView scrollContent;
    private String selectedRegion;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            setContentView(R.layout.activity_match_info);
        } else {
            setContentView(R.layout.activity_match_info);
        }
        matchTime = (TextView)findViewById(R.id.matchTime);
        response = (MatchInfoResponse) getIntent().getSerializableExtra("MATCH_INFO_RESPONSE");
        selectedRegion = getIntent().getStringExtra("SELECTED_REGION");
        loadingProgress = (ProgressBar)findViewById(R.id.loadingProgress);
        scrollContent = (ScrollView)findViewById(R.id.scrollContent);

        setTitle(response.getGameMode().toLowerCase()+" mode");

        if(response != null){
            team1LV = (ListView) findViewById(R.id.team1LV);
            team2LV = (ListView) findViewById(R.id.team2LV);
            team1LV.setOnItemClickListener(this);
            team2LV.setOnItemClickListener(this);
            ArrayList<Summoner>allSummoners = response.getParticipants();
            ArrayList<Long> teamIds = new ArrayList<>();
            for(Summoner s : allSummoners){
                if(!teamIds.contains(s.getTeamId())){
                    teamIds.add(s.getTeamId());
                }
            }

            if(teamIds.size() != 2){
                Toast.makeText(getApplicationContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
                finish();
            }else {
                team1Summoners = new ArrayList<>();
                team2Summoners = new ArrayList<>();
                for (Summoner s : allSummoners) {
                    if(s.getTeamId() == teamIds.get(0)){
                        team1Summoners.add(s);
                    }else if(s.getTeamId() == teamIds.get(1)){
                        team2Summoners.add(s);
                    }
                }

                ArrayList<String> pathParams = new ArrayList<>();
                pathParams.add("static-data");
                pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
                pathParams.add("v1.2");
                pathParams.add("champion");
                HashMap<String, String> queryParams = new HashMap<>();
                queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
                queryParams.put("version", Commons.LATEST_VERSION);
                queryParams.put("champData", "altimages");
                queryParams.put("api_key", Commons.API_KEY);

                if(Commons.allChampions == null || Commons.allChampions.size() <= 0) {
                    ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_CHAMPIONS_REQUEST, pathParams, queryParams, null, this);
                }else{
                    setAdapters();
                }
            }

        }else{
            Toast.makeText(getApplicationContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.match_info_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(response.getGameMode()+" Mode");

        if(getSupportActionBar()!=null){
            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void startTimer(long secs) {
        Timer t = new Timer();
        counter = secs;
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter++;
                        matchTime.setText(getTime(counter));
                    }
                });

            }
        }, 0, 1000);
    }

    private void setAdapters(){
        //  ArrayList<Summoner>team1SummonersWithNames = new ArrayList<Summoner>();
        //  ArrayList<Summoner>team2SummonersWithNames = new ArrayList<Summoner>();

        for(Summoner s : team1Summoners){
            for(Champion c : Commons.allChampions){
                if(c.getId() == s.getChampionId()){
                    s.setChampName(c.getChampionName());
                    s.setKey(c.getKey());
                    break;
                }
            }
        }

        for(Summoner s : team2Summoners){
            for(Champion c : Commons.allChampions){
                if(c.getId() == s.getChampionId()){
                    s.setChampName(c.getChampionName());
                    s.setKey(c.getKey());
                    break;
                }
            }
        }

        MatchInfoAdapter team1Adapter = new MatchInfoAdapter(MatchInfoActivity.this, R.layout.match_info_detail_listrow, team1Summoners);
        MatchInfoAdapter team2Adapter = new MatchInfoAdapter(MatchInfoActivity.this, R.layout.match_info_detail_listrow, team2Summoners);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px1 = (team1Summoners.size()*60+20) * (metrics.densityDpi / 160f);
        float px2 = (team2Summoners.size()*60+20) * (metrics.densityDpi / 160f);
        team1LV.getLayoutParams().height = Math.round(px1);
        team2LV.getLayoutParams().height = Math.round(px2);
        team1LV.setAdapter(team1Adapter);
        team2LV.setAdapter(team2Adapter);
        matchTime.setText(getTime(response.getGameLength()));
        try{
            loadingProgress.setVisibility(View.GONE);
            scrollContent.setVisibility(View.VISIBLE);
        }catch (Exception ignored){}

        startTimer(response.getGameLength());

    }

    private String getTime(long secs){

        if(secs < 0){
            secs = (-1)*secs;
        }

        String hours =  String.valueOf(secs / 3600);
        String minutes = String.valueOf((secs/60));
        String seconds = String.valueOf(secs % 60);


        if(minutes.length() == 1){
            minutes = "0" + minutes;
        }

        if(seconds.length() == 1){
            seconds = "0" + seconds;
        }

        if(hours.equals("0")){
            return minutes + ":" + seconds;
        }

        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    public void onSuccess(Object response) {

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

            setAdapters();

        }

    }

    @Override
    public void onFailure(Object response) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == team1LV.getId() || parent.getId() == team2LV.getId()){

            Summoner s = (Summoner)parent.getAdapter().getItem(position);
            Intent i = new Intent(MatchInfoActivity.this, PlayerMatchInfoDetailActivity.class);
            i.putExtra("EXTRA_USERNAME", s.getSummonerName());
            i.putExtra("EXTRA_USERID", s.getSummonerId());
            i.putExtra("EXTRA_CHAMP_IMAGE_URL", "http://ddragon.leagueoflegends.com/cdn/" + Commons.LATEST_VERSION + "/img/champion/" + s.getKey() + ".png");
            i.putExtra("SELECTED_REGION", selectedRegion);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
