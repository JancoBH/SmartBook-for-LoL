package com.jancobh.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jancobh.adapters.StatisticsAdapter;
import com.jancobh.commons.Commons;
import com.jancobh.data.Champion;
import com.jancobh.data.Game;
import com.jancobh.data.Player;
import com.jancobh.data.Statistics;
import com.jancobh.data.Stats;
import com.jancobh.data.SummonerNames;
import com.jancobh.fragments.R;
import com.jancobh.listener.ResponseListener;
import com.jancobh.responseclasses.SummonerNamesResponse;
import com.jancobh.service.ServiceRequest;
import com.jancobh.view.FadeInNetworkImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class MatchDetailActivity extends AppCompatActivity implements ResponseListener {

    public static final String EXTRA_GAME = "com.jancobh.activities.EXTRA_GAME";
    public static final String EXTRA_SUMMONER_ID = "com.jancobh.activities.EXTRA_SUMMONER_ID";
    public static final String EXTRA_REGION = "com.jancobh.activities.EXTRA_REGION";

    private int summonerNamesRequestCount = 0;
    private LinearLayout team1LL, team2LL;
    private List<SummonerNames> summonerNames;
    private String region;

    private Game game;
    private long summonerId;
    private String summonerIds = "";
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_detail);
        if(getIntent() != null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                game = (Game) extras.getSerializable(EXTRA_GAME);
                summonerId = extras.getLong(EXTRA_SUMMONER_ID);
                region = extras.getString(EXTRA_REGION);
            }

        }
        if(game != null){
            List<Player> players = game.getFellowPlayers();
            if(players != null && players.size() > 0) {
                players.add(new Player(game.getChampionId(), summonerId, game.getTeamId()));
                for (Player p : players) {
                    summonerIds += p.getSummonerId() + ",";
                }
                if (summonerIds.length() > 0) {
                    summonerIds = summonerIds.substring(0, summonerIds.length() - 1);
                }
                makeGetPlayerNamesRequest(summonerIds);
            }else{
                initUI();
            }
        }

        toolbar = (Toolbar) findViewById(R.id.match_detail_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Game Detail");

        if(getSupportActionBar()!=null){
            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void makeGetPlayerNamesRequest(String summonerIds){
        ServiceRequest.getInstance(this).makeGetSummonerIdsRequest(Commons.SUMMONER_NAMES_REQUEST, region, summonerIds, null, this);
    }

    private void initUI() {
        ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        ScrollView parent = (ScrollView) findViewById(R.id.parent);
        RelativeLayout winLoseLabel = (RelativeLayout) findViewById(R.id.winLoseLabel);
        FadeInNetworkImageView champIV = (FadeInNetworkImageView) findViewById(R.id.champIV);
        TextView levelTV = (TextView) findViewById(R.id.levelTV);
        TextView matchTypeTV = (TextView) findViewById(R.id.matchTypeTV);
        TextView matchModeTV = (TextView) findViewById(R.id.matchModeTV);
        TextView kdaTV = (TextView) findViewById(R.id.kdaTV);
        TextView goldTV = (TextView) findViewById(R.id.goldTV);
        TextView matchTimeTV = (TextView) findViewById(R.id.matchTimeTV);
        TextView matchDateTV = (TextView) findViewById(R.id.matchDateTV);
        FadeInNetworkImageView spell1 = (FadeInNetworkImageView) findViewById(R.id.spell1);
        FadeInNetworkImageView spell2 = (FadeInNetworkImageView) findViewById(R.id.spell2);
        FadeInNetworkImageView item0 = (FadeInNetworkImageView) findViewById(R.id.item0);
        FadeInNetworkImageView item1 = (FadeInNetworkImageView) findViewById(R.id.item1);
        FadeInNetworkImageView item2 = (FadeInNetworkImageView) findViewById(R.id.item2);
        FadeInNetworkImageView item3 = (FadeInNetworkImageView) findViewById(R.id.item3);
        FadeInNetworkImageView item4 = (FadeInNetworkImageView) findViewById(R.id.item4);
        FadeInNetworkImageView item5 = (FadeInNetworkImageView) findViewById(R.id.item5);
        FadeInNetworkImageView item6 = (FadeInNetworkImageView) findViewById(R.id.item6);
        ListView statisticsLV = (ListView) findViewById(R.id.statisticsLV);

        LinearLayout teamsContainer = (LinearLayout) findViewById(R.id.teamsContainer);
        team1LL = (LinearLayout) findViewById(R.id.team1LL);
        team2LL = (LinearLayout) findViewById(R.id.team2LL);

        if (game != null) {

            String gameMode = game.getGameMode();
            String gameType = game.getGameType();
            String subType = game.getSubType();

            String gameModeText = getGameModeText(gameMode);
            String gameTypeText = getGameTypeText(gameType, subType);

            matchModeTV.setText(gameModeText);
            matchTypeTV.setText(gameTypeText);

            int summonerSpell1 = game.getSpell1();
            int summonerSpell2 = game.getSpell2();

            int Intspell21 = 21, Intspell30 = 30, Intspell1 = 1, Intspell12 = 12, Intspell4 = 4, Intspell32 = 32, Intspell7 = 7,
                    Intspell13 = 13, Intspell31 = 31, Intspell11 = 11, Intspell3 = 3, Intspell14 = 14, Intspell6 = 6;

            String spell21Name = "SummonerBarrier.png", spell30Name = "SummonerPoroRecall.png", spell1Name = "SummonerBoost.png",
                    spell12Name = "SummonerTeleport.png", spell4Name = "SummonerFlash.png", spell32Name = "SummonerSnowball.png",
                    spell7Name = "SummonerHeal.png", spell13Name = "SummonerMana.png", spell31Name = "SummonerPoroThrow.png",
                    spell11Name = "SummonerSmite.png", spell3Name = "SummonerExhaust.png", spell14Name = "SummonerDot.png",
                    spell6Name = "SummonerHaste.png";

            if(summonerSpell1 == Intspell21){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell21Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell30){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell30Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell1){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell1Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell12){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell12Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell4){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell4Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell32){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell32Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell7){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell7Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell13){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell13Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell31){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell31Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell11){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell11Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell3){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell3Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell14){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell14Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell1 == Intspell6){spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell6Name, ServiceRequest.getInstance((this)).getImageLoader());}

            if(summonerSpell2 == Intspell21){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell21Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell30){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell30Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell1){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell1Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell12){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell12Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell4){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell4Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell32){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell32Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell7){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell7Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell13){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell13Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell31){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell31Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell11){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell11Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell3){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell3Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell14){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell14Name, ServiceRequest.getInstance((this)).getImageLoader());}
            else if(summonerSpell2 == Intspell6){spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell6Name, ServiceRequest.getInstance((this)).getImageLoader());}

            /*if (Commons.allSpells != null) {
                String spell1Name = null, spell2Name = null;
                for (SummonerSpell sp : Commons.allSpells) {
                    if (sp.getId() == summonerSpell1) {
                        if (sp.getImage() != null) {
                            spell1Name = sp.getImage().getFull();
                        }
                    }

                    if (sp.getId() == summonerSpell2) {
                        if (sp.getImage() != null) {
                            spell2Name = sp.getImage().getFull();
                        }
                    }
                }

                if (spell1Name != null) {
                    spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell1Name, ServiceRequest.getInstance((this)).getImageLoader());
                } else {
                    spell1.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    spell1.setBackgroundResource(R.drawable.question_mark);
                }

                if (spell2Name != null) {
                    spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell2Name, ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    spell2.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    spell2.setBackgroundResource(R.drawable.question_mark);
                }
            }*/

            long createDate = game.getCreateDate();

            Calendar c = Calendar.getInstance();
            c.setTime(new Date(createDate));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            matchDateTV.setText(dateFormat.format(c.getTime()));

            int championID = game.getChampionId();
            String champImageUrl = null;
            if (Commons.allChampions != null && Commons.allChampions.size() > 0) {
                for (Champion champ : Commons.allChampions) {
                    if (champ.getId() == championID) {
                        champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                        break;
                    }
                }
            }

            if (champImageUrl != null) {
                champIV.setImageUrl(champImageUrl, ServiceRequest.getInstance(this).getImageLoader());
            } else {
                champIV.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                champIV.setBackgroundResource(R.drawable.question_mark);
            }

            Stats stats = game.getStats();
            if (stats != null) {
                if (stats.isWin()) {
                    winLoseLabel.setBackgroundColor(this.getResources().getColor(R.color.green));
                } else {
                    winLoseLabel.setBackgroundColor(this.getResources().getColor(R.color.red));
                }

                levelTV.setText(stats.getLevel() + "");
                kdaTV.setText(stats.getChampionsKilled() + " / " + stats.getNumDeaths() + " / " + stats.getAssists());
                goldTV.setText(format(stats.getGoldEarned()));
                String timeString = convertSecondsToHoursMinutes(stats.getTimePlayed());
                if (timeString != null) {
                    matchTimeTV.setText(timeString);
                }

                int summonerItem0 = stats.getItem0();
                int summonerItem1 = stats.getItem1();
                int summonerItem2 = stats.getItem2();
                int summonerItem3 = stats.getItem3();
                int summonerItem4 = stats.getItem4();
                int summonerItem5 = stats.getItem5();
                int summonerItem6 = stats.getItem6();

                if (summonerItem0 != 0) {
                    item0.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem0 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item0.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item0.setBackgroundResource(R.drawable.nothing);
                }
                if (summonerItem1 != 0) {
                    item1.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem1 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item1.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item1.setBackgroundResource(R.drawable.nothing);
                }
                if (summonerItem2 != 0) {
                    item2.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem2 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item2.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item2.setBackgroundResource(R.drawable.nothing);
                }
                if (summonerItem3 != 0) {
                    item3.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem3 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item3.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item3.setBackgroundResource(R.drawable.nothing);
                }
                if (summonerItem4 != 0) {
                    item4.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem4 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item4.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item4.setBackgroundResource(R.drawable.nothing);
                }
                if (summonerItem5 != 0) {
                    item5.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem5 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item5.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item5.setBackgroundResource(R.drawable.nothing);
                }
                if (summonerItem6 != 0) {
                    item6.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem6 + ".png", ServiceRequest.getInstance(this).getImageLoader());
                } else {
                    item6.setImageUrl(null, ServiceRequest.getInstance(this).getImageLoader());
                    item6.setBackgroundResource(R.drawable.nothing);
                }

                ArrayList<Statistics> statistics = new ArrayList<>();

                int totalDamageDealt = stats.getTotalDamageDealt();
                statistics.add(new Statistics(getResources().getString(R.string.totalDamageDealt), totalDamageDealt + ""));
                int totalDamageTaken = stats.getTotalDamageTaken();
                statistics.add(new Statistics(getResources().getString(R.string.totalDamageTaken), totalDamageTaken + ""));
                int goldEarned = stats.getGoldEarned();
                statistics.add(new Statistics(getResources().getString(R.string.goldEarned), goldEarned + ""));
                int totalHeal = stats.getTotalHeal();
                statistics.add(new Statistics(getResources().getString(R.string.totalHeal), totalHeal + ""));
                int lagestKillingSpree = stats.getLargestKillingSpree();
                statistics.add(new Statistics(getResources().getString(R.string.lagestKillingSpree), lagestKillingSpree + ""));
                int largestMultiKill = stats.getLargestMultiKill();
                statistics.add(new Statistics(getResources().getString(R.string.largestMultiKill), largestMultiKill + ""));
                int magicDamageDealtPlayer = stats.getMagicDamageDealtPlayer();
                statistics.add(new Statistics(getResources().getString(R.string.magicDamageDealtPlayer), magicDamageDealtPlayer + ""));
                int physicalDamageDealtPlayer = stats.getPhysicalDamageDealtPlayer();
                statistics.add(new Statistics(getResources().getString(R.string.physicalDamageDealtPlayer), physicalDamageDealtPlayer + ""));
                int minionsKilled = stats.getMinionsKilled();
                statistics.add(new Statistics(getResources().getString(R.string.minionsKilled), minionsKilled + ""));
                int neutralMinionsKilled = stats.getNeutralMinionsKilled();
                statistics.add(new Statistics(getResources().getString(R.string.neutralMinionsKilled), neutralMinionsKilled + ""));
                int magicDamageTaken = stats.getMagicDamageTaken();
                statistics.add(new Statistics(getResources().getString(R.string.magicDamageTaken), magicDamageTaken + ""));
                int physicalDamageTaken = stats.getPhysicalDamageTaken();
                statistics.add(new Statistics(getResources().getString(R.string.physicalDamageTaken), physicalDamageTaken + ""));
                int totalTimeCrowdControlDealt = stats.getTotalTimeCrowdControlDealt();
                statistics.add(new Statistics(getResources().getString(R.string.totalTimeCrowdControlDealt), totalTimeCrowdControlDealt + ""));
                int turretsKilled = stats.getTurretsKilled();
                statistics.add(new Statistics(getResources().getString(R.string.turretsKilled), turretsKilled + ""));
                int wardPlaced = stats.getWardPlaced();
                statistics.add(new Statistics(getResources().getString(R.string.wardPlaced), wardPlaced + ""));
                int wardKilled = stats.getWardKilled();
                statistics.add(new Statistics(getResources().getString(R.string.wardKilled), wardKilled + ""));
                int ipEarned = game.getIpEarned();
                statistics.add(new Statistics(getResources().getString(R.string.ipEarned), ipEarned + ""));

                StatisticsAdapter statisticsAdapter = new StatisticsAdapter(this, R.layout.list_row_statistics, statistics);
                statisticsLV.setAdapter(statisticsAdapter);

            }
            List<Player> players = game.getFellowPlayers();
            if(players != null){
                teamsContainer.setVisibility(View.VISIBLE);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                ArrayList<Integer> teamIds = new ArrayList<>();
                for(Player p : players){
                    if(!teamIds.contains(p.getTeamId())){
                        teamIds.add(p.getTeamId());
                    }
                }

                if(teamIds.size() != 2){
                    teamsContainer.setVisibility(View.GONE);
                }else {
                    for (Player p : players) {
                        if(p.getSummonerId() == 0){
                            createPlayerLayoutAndAdd(summonerId, p.getChampionId(), inflater, p.getTeamId(), teamIds);
                        }else {
                            createPlayerLayoutAndAdd(p.getSummonerId(), p.getChampionId(), inflater, p.getTeamId(), teamIds);
                        }
                    }
                }
            }else{
                teamsContainer.setVisibility(View.GONE);
            }
        }

        progress.setVisibility(View.GONE);
        parent.setVisibility(View.VISIBLE);
    }

    private void createPlayerLayoutAndAdd(long playerId, int championID, LayoutInflater inflater, int teamID, ArrayList<Integer> teamIds){
        RelativeLayout summonerLayout = (RelativeLayout) inflater.inflate(R.layout.view_team_player, null);
        FadeInNetworkImageView champIV = (FadeInNetworkImageView) summonerLayout.findViewById(R.id.champIV);
        TextView summonerNameTV = (TextView) summonerLayout.findViewById(R.id.summonerNameTV);

        String champImageUrl = null;
        if(Commons.allChampions != null && Commons.allChampions.size() > 0){
            for(Champion champ : Commons.allChampions){
                if(champ.getId() == championID){
                    champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                    break;
                }
            }
        }

        if(summonerNames != null && summonerNames.size() > 0){
            String summonerName = "???";
            for(SummonerNames sn : summonerNames){
                if(sn != null){
                    if(playerId == sn.getId()){
                        summonerName = sn.getName();
                        break;
                    }
                }
            }
            if(summonerName != null){
                summonerNameTV.setText(summonerName);
            }
        }

        if(champImageUrl != null) {
            champIV.setImageUrl(champImageUrl, ServiceRequest.getInstance(MatchDetailActivity.this).getImageLoader());
        }else{
            champIV.setImageUrl(null, ServiceRequest.getInstance(MatchDetailActivity.this).getImageLoader());
            champIV.setBackgroundResource(R.drawable.question_mark);
        }

        if (teamID == teamIds.get(0)){
            team1LL.addView(summonerLayout);
        } else if (teamID == teamIds.get(1)){
            team2LL.addView(summonerLayout);
        }
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = ((truncated / 10d) != (truncated / 10));
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public String convertSecondsToHoursMinutes(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString;
        try {
            if (hours == 0) {
                timeString = String.format(Locale.US,"%02d:%02d", minutes, seconds);
            } else {
                timeString = String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds);
            }
        } catch (Exception e) {
            timeString = null;
        }
        return timeString;
    }

    private String getGameModeText(String gameMode) {
        String gameTypeText = "";
        if (gameMode != null && (gameMode.length() > 0)) {
            if (gameMode.equalsIgnoreCase("classic")) {
                gameTypeText += this.getResources().getString(R.string.summoners_rift);
            } else if (gameMode.equalsIgnoreCase("odin")) {
                gameTypeText += this.getResources().getString(R.string.dominion);
            } else if (gameMode.equalsIgnoreCase("aram")) {
                gameTypeText += this.getResources().getString(R.string.aram);
            } else if (gameMode.equalsIgnoreCase("tutorial")) {
                gameTypeText += this.getResources().getString(R.string.tutorial);
            } else if (gameMode.equalsIgnoreCase("oneforall")) {
                gameTypeText += this.getResources().getString(R.string.oneforall);
            } else if (gameMode.equalsIgnoreCase("ascension")) {
                gameTypeText += this.getResources().getString(R.string.ascension);
            } else if (gameMode.equalsIgnoreCase("firstblood")) {
                gameTypeText += this.getResources().getString(R.string.firstblood);
            } else if (gameMode.equalsIgnoreCase("kingporo")) {
                gameTypeText += this.getResources().getString(R.string.kingporo);
            } else if (gameMode.equalsIgnoreCase("Assassinate")){
                gameTypeText += this.getResources().getString(R.string.summoners_rift);
            }
        }
        return gameTypeText;
    }

    private String getGameTypeText(String gameType, String subType) {
        String gameTypeText = "";
        if (gameType != null && gameType.length() > 0) {
            if (gameType.equalsIgnoreCase("custom_game")) {
                gameTypeText += this.getResources().getString(R.string.custom_game);
            } else if (gameType.equalsIgnoreCase("matched_game")) {
                if (subType != null) {
                    if (subType.equalsIgnoreCase("none")) {
                        gameTypeText += this.getResources().getString(R.string.none);
                    } else if (subType.contains("normal") || subType.contains("NORMAL")) {
                        gameTypeText += this.getResources().getString(R.string.normal);
                    } else if (subType.contains("odin") || subType.contains("ODIN")) {
                        gameTypeText += this.getResources().getString(R.string.odin);
                    } else if (subType.contains("aram") || subType.contains("ARAM")) {
                        gameTypeText += this.getResources().getString(R.string.aram_aram);
                    } else if (subType.equalsIgnoreCase("bot")) {
                        gameTypeText += this.getResources().getString(R.string.bot);
                    } else if (subType.contains("oneforall") || subType.contains("ONEFORALL")) {
                        gameTypeText += this.getResources().getString(R.string.oneforall);
                    } else if (subType.contains("firstblood") || subType.contains("FIRSTBLOOD")) {
                        gameTypeText += this.getResources().getString(R.string.firstblood);
                    } else if (subType.equalsIgnoreCase("SR_6x6")) {
                        gameTypeText += this.getResources().getString(R.string.sr6);
                    } else if (subType.equalsIgnoreCase("CAP_5x5")) {
                        gameTypeText += this.getResources().getString(R.string.cap5);
                    } else if (subType.equalsIgnoreCase("urf")) {
                        gameTypeText += this.getResources().getString(R.string.urf);
                    } else if (subType.equalsIgnoreCase("nightmare_bot")) {
                        gameTypeText += this.getResources().getString(R.string.nightmare);
                    } else if (subType.equalsIgnoreCase("ascension")) {
                        gameTypeText += this.getResources().getString(R.string.ascension);
                    } else if (subType.equalsIgnoreCase("hexakill")) {
                        gameTypeText += this.getResources().getString(R.string.sr6);
                    } else if (subType.equalsIgnoreCase("king_poro")) {
                        gameTypeText += this.getResources().getString(R.string.kingporo);
                    } else if (subType.equalsIgnoreCase("counter_pick")) {
                        gameTypeText += this.getResources().getString(R.string.counter_pick);
                    } else if (subType.equalsIgnoreCase("bilgewater")) {
                        gameTypeText += this.getResources().getString(R.string.bilgewater);
                    }else if (subType.equalsIgnoreCase("Assassinate")) {
                        gameTypeText += this.getResources().getString(R.string.assassinate);
                    } else if (subType.contains("ranked") || subType.contains("RANKED")) {
                        gameTypeText += this.getResources().getString(R.string.ranked);
                    } else {
                        gameTypeText += this.getResources().getString(R.string.unknown);
                    }
                }
            } else if (gameType.equalsIgnoreCase("tutorial_game")) {
                gameTypeText += getResources().getString(R.string.tutorial);
            }
        }
        return gameTypeText;
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof SummonerNamesResponse){
            SummonerNamesResponse summonerNamesResponse = (SummonerNamesResponse) response;
            summonerNames = summonerNamesResponse.getSummonerNames();
            initUI();
        }
    }

    @Override
    public void onFailure(Object response) {
        if(response instanceof Integer){
            Integer requestID = (Integer) response;
            if(requestID == Commons.SUMMONER_NAMES_REQUEST) {
                if (summonerNamesRequestCount < 3) {
                    summonerNamesRequestCount++;
                    makeGetPlayerNamesRequest(summonerIds);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
