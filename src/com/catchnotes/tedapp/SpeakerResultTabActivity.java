package com.catchnotes.tedapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class SpeakerResultTabActivity extends TabActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("Speakers")
                .setIndicator("Speakers")
                .setContent(new Intent(this, SpeakerResultActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("Sessions")
                .setIndicator("Sessions")
                .setContent(new Intent(this, SessionResultActivity.class)));
    }
}
