/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.catchnotes.tedapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tedx.adapters.ExpandableListAdapter;
import com.tedx.logics.SearchResultLogic;
import com.tedx.objects.SearchResult;
import com.tedx.objects.SessionResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;


/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 * This page is a quick iteration since I have less than 36 hours to complete the entire
 * project, will change this in the future.
 */
public class SessionResultActivity extends Activity {
    private ExpandableListAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions);

        // Retrive the ExpandableListView from the layout
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        
        listView.setOnChildClickListener(new OnChildClickListener()
        {
            
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
            {
                Toast.makeText(getBaseContext(), "Child clicked", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        
        listView.setOnGroupClickListener(new OnGroupClickListener()
        {
            
            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3)
            {
                Toast.makeText(getBaseContext(), "Group clicked", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //Get Data
		int EventId = Integer.valueOf(this.getResources().getString(R.string.eventId));
		int ServerEventVersion = SearchResultLogic.getSearchResultVersionByEventId(this.getResources(), EventId);
		JSONArray speakers;

		//check point to load from cache or web
		if(	ServerEventVersion != 0 &&
			SearchResultLogic.getCurrentVersionByEventIdFromCache(this, EventId) != ServerEventVersion)
		{
			String Url = 
				com.tedx.logics.SearchResultLogic.getSearchResultsByCriteriaURL(
						this.getResources(), EventId, 1);
			
			//Set the version
			SearchResultLogic.setCurrentVersionByEventId(this, EventId, ServerEventVersion);
			
			try {
				speakers = SearchResultLogic.loadSpeakerSearchResultsFromWeb(this, Url, EventId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				speakers = null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				speakers = null;
			}
		}
		else
		{
			try {
				speakers = SearchResultLogic.loadSpeakerSearchResultsFromCache(this, EventId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				speakers = null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				speakers = null;
			}
		}
		String[] groupFrom = new String[] {
				SessionResult.SESSIONNAME
		};

		int[] groupTo = new int[] {
				R.id.tvGroup
		};
		
		String[] childrenFrom = new String[] {
				SearchResult.NAME,
				SearchResult.TITLE,
				SearchResult.PHOTOURL
		};

		int[] childrenTo = new int[] {
				android.R.id.text1,
				android.R.id.text2,
				android.R.id.icon
		};
		
		
        adapter = new ExpandableListAdapter(this, 
        									getSessions(), 
        									R.layout.sessiongroup, 
        									groupFrom, 
        									groupTo, 
        									getSpeakers(speakers), 
        									R.layout.searchresultrow, 
        									childrenFrom, 
        									childrenTo);
      

        // Set this blank adapter to the list view
        listView.setAdapter(adapter);

        // This thread randomly generates some vehicle types
        // At an interval of every 2 seconds
    }
    
    protected List<HashMap<String, String>> getSessions()
    {
		List<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> first = new HashMap<String, String>();
		first.put(SessionResult.SESSION, "1");
		first.put(SessionResult.SESSIONNAME, "Testing");
		ret.add(first);
		
		return ret;
    }
    
    protected List<List<HashMap<String, String>>> getSpeakers(JSONArray speakers)
    {    
		List<List<HashMap<String, String>>> ret = new ArrayList<List<HashMap<String, String>>>();
		int sessions = Integer.valueOf(this.getResources().getString(R.string.eventSessions));
		
		for(int i = 0; i < sessions; i ++)
		{
			List<HashMap<String, String>> Session = new ArrayList<HashMap<String, String>>();
			ret.add(Session);
		}
		
		for(int i = 0; i<speakers.length(); i++)
		{
			HashMap<String, String> SearchResults;
			try {
				SearchResults = loadJSON(speakers.getJSONObject(i));
				int speakersession = Integer.valueOf(SearchResults.get(SearchResult.SESSION)) - 1;
				ret.get(speakersession).add(SearchResults);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
    }
    
	protected HashMap<String, String> loadJSON(JSONObject data) throws JSONException {
		HashMap<String, String> SearchResults = new HashMap<String, String>();
		
		SearchResults.put(SearchResult.NAME, data.getString("FirstName") + " " + data.getString("LastName") );
		SearchResults.put(SearchResult.TITLE, data.getString("Title"));
		SearchResults.put(SearchResult.EMAIL, data.getString("Email"));
		SearchResults.put(SearchResult.FACEBOOK, data.getString("Facebook"));
		SearchResults.put(SearchResult.PHOTOURL, data.getString("PhotoUrl"));
		SearchResults.put(SearchResult.SPEAKERID, data.getString("SpeakerId"));
		SearchResults.put(SearchResult.TWITTER, String.valueOf(data.getString("Twitter")));
		SearchResults.put(SearchResult.EMAIL, String.valueOf(data.getString("Email")));
		SearchResults.put(SearchResult.TOPIC, String.valueOf(data.getString("Topic")));
		SearchResults.put(SearchResult.DESCRIPTION, String.valueOf(data.getString("Description")));
		SearchResults.put(SearchResult.WEBSITE, String.valueOf(data.getString("WebSite")));
		SearchResults.put(SearchResult.SESSION, String.valueOf(data.getInt("Session")));

		return SearchResults;
	}
	
}
