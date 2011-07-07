package com.catchnotes.tedapp;


import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.catchnotes.tedapp.R;
import com.tedx.logics.ArchiveLogic;
import com.tedx.logics.SearchResultLogic;
import com.tedx.objects.SearchResult;
import com.tedx.activities.GroupLazyActivity;

public class SubEventSessionResultActivity extends GroupLazyActivity{
	private int mEventId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	mEventId = ArchiveLogic.GetSubEventId(getApplicationContext());
    	
		mFrom = new String[] {
				SearchResult.SESSIONNAME,
				SearchResult.NAME,
				SearchResult.TITLE,
				SearchResult.PHOTOURL
		};

		mTo = new int[] {
				R.id.groupheader,
				android.R.id.text1,
				android.R.id.text2,
				android.R.id.icon
		};
		
		super.onCreate(savedInstanceState, R.layout.searchresults, R.layout.searchresultrow);
	}
	

	public void onItemClick(AdapterView<?> list, View row, int position, long id) {
		startActivityForPosition(SpeakerActivity.class, position);
	}

	@Override
	protected LoadTask newLoadTask() {
		return new LoadSearchResultTask();
	}

	@Override
	protected void setTaskActivity() {
		mLoadTask.activity = this;
	}

	protected static class LoadSearchResultTask extends LoadTask {
		@Override
		protected Boolean doInBackground(String... params) {
			SubEventSessionResultActivity activity = (SubEventSessionResultActivity) super.activity;

			int ServerEventVersion = SearchResultLogic.getSearchResultVersionByEventId(activity.getResources(), activity.mEventId);
			JSONArray speakers;

			//check point to load from cache or web
			if(	ServerEventVersion != 0 &&
				SearchResultLogic.getCurrentVersionByEventIdFromCache(activity, activity.mEventId) != ServerEventVersion)
			{
				//Set the version
				SearchResultLogic.setCurrentVersionByEventId(activity, activity.mEventId, ServerEventVersion);
				
				try {
					speakers = SearchResultLogic.loadSpeakerSearchResultsFromWeb(activity, activity.mEventId);
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
					speakers = SearchResultLogic.loadSpeakerSearchResultsFromCache(activity, activity.mEventId);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					speakers = null;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					speakers = null;
				}
			}
			
			if(speakers != null)
			{
				speakers = SortSpeakersBySessionId(speakers);
			}
			return loadSpeakerResultsByCollection(speakers);	

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				activity.showDialog(DIALOG_ERROR_LOADING);
			}
		}
	}
	
	/**
	 * Method for sorting speakers by SessionId
	 * This method is not efficient at all and will be updated in the future
	 * @param speakers
	 */
	private static JSONArray SortSpeakersBySessionId(JSONArray speakers)
	{
		JSONArray ret = speakers;
		
	    int n = speakers.length();
	    for (int pass=1; pass < n; pass++) {  // count how many times
	        // This next loop becomes shorter and shorter
	        for (int i=0; i < n-pass; i++) {
	            try {
					if (ret.getJSONObject(i).getInt("Session") > ret.getJSONObject(i + 1).getInt("Session")) {
					    // exchange elements
						JSONObject temp = ret.getJSONObject(i);
						ret.put(i, ret.getJSONObject(i + 1));
						ret.put(i + 1, temp);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	    
	    return ret;
	}

	@Override
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
		SearchResults.put(SearchResult.WEBSITE, String.valueOf(data.getString("Website")));
		SearchResults.put(SearchResult.SESSION, String.valueOf(data.getInt("Session")));
		SearchResults.put(SearchResult.EVENTID, String.valueOf(data.getInt("EventId")));

		String SessionName = null;
		try {
			SessionName = SearchResultLogic.getSessionNameBySession(this, mEventId, data.getInt("Session"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("SessionName", SessionName);
		
		SearchResults.put(SearchResult.SESSIONNAME, SessionName);
		
		return SearchResults;
	}
	
    //Back Button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    
    @Override
    public void onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
		finish();
        return;
    }
}
