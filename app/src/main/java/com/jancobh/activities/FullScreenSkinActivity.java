package com.jancobh.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.jancobh.commons.Commons;
import com.jancobh.fragments.R;

public class FullScreenSkinActivity extends AppCompatActivity {

    String key;
    int position;
    AQuery aQuery;
    WebView webView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_skin);
        getExtras();
        webView = (WebView)findViewById(R.id.webViewFullScreenSkin);
        progressBar = (ProgressBar)findViewById(R.id.progressImage);
        aQuery = new AQuery(webView);
        aQuery.progress(progressBar).webImage(Commons.URL_CHAMPION_SKIN_BASE + key + "_" + position + ".jpg", true, true, Color.TRANSPARENT);

        /* Toolbar*/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.full_screen_toolbar);
        setSupportActionBar(myToolbar);

        setTitle(key);

        if(getSupportActionBar()!=null){
            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void getExtras() {
        key = getIntent().getStringExtra("EXTRA_SKIN_KEY");
        position = getIntent().getIntExtra("EXTRA_SKIN_POSITION", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
