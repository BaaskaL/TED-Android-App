package com.catchnotes.tedapp;

import com.catchnotes.tedapp.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class SpeakerResultTabActivity extends TabActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);

    	super.onCreate(savedInstanceState);

        final TabHost tabHost = getTabHost();


        tabHost.addTab(tabHost.newTabSpec("Speakers")
                .setIndicator("Speakers", getResources().getDrawable(R.drawable.ic_tab_speakers))
                .setContent(new Intent(this, SpeakerResultActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("Sessions")
                .setIndicator("Sessions", getResources().getDrawable(R.drawable.ic_tab_sessions))
                .setContent(new Intent(this, SessionResultActivity.class)));
    }
}
