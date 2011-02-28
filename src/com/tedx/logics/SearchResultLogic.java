/*
 * The MIT License

 * Copyright (c) 2010 Peter Ma

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
*/

package com.tedx.logics;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;

import com.catchnotes.tedapp.R;
import com.tedx.helpers.Common;
import com.tedx.webservices.WebServices;


public class SearchResultLogic 
{
	// get url for getting search results by search string
	// for background loading
	public static String getSearchResultsByCriteriaURL(Resources res, int EventId, int Page) {		
		String Action = "GetSpeakersByEventId";
		String URL = res.getString(R.string.WebServiceAddress) + Action;		
		
		Uri u = Uri.parse(URL);
		Uri.Builder builder = u.buildUpon();		
		builder.appendQueryParameter("eventid", String.valueOf(EventId));
		builder.appendQueryParameter("page", String.valueOf(Page));
		URL = builder.build().toString();
		
		return URL;
	}
	
	public static String getSessionsByCriteriaURL(Resources res, int EventId)
	{
		String Action = "GetSessionsByEventId";
		String URL = res.getString(R.string.WebServiceAddress) + Action;		
		
		Uri u = Uri.parse(URL);
		Uri.Builder builder = u.buildUpon();		
		builder.appendQueryParameter("eventid", String.valueOf(EventId));
		URL = builder.build().toString();
		
		return URL;
	}
	
	public static int getSearchResultVersionByEventId(Resources res, int EventId)
	{
		String Action = res.getString(R.string.WebService_GetEventVersion);
		
		JSONObject requestJSONParameters = new JSONObject();
		try {
			requestJSONParameters.put("EventId", Integer.valueOf(EventId));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return 0;
		}		
		
		String URL = res.getString(R.string.WebServiceAddress) + Action;
		
		JSONObject responseJSON = WebServices.SendHttpPost(URL, requestJSONParameters);
		
		if(responseJSON != null)
		{
			try {
				if(responseJSON.getBoolean("IsSuccessful"))
				{
					return responseJSON.getInt("EventVersion");
				}
				else return 0;
				} catch (JSONException e) {
				// TODO Auto-generated catch block
					return 0;
			}
		}
		else return 0;
	}	
	
	//Get Current Version from Cache
	public static int getCurrentVersionByEventIdFromCache(Context context, int EventId)
	{
		//If Stored in Preference
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		return appSettings.getInt(context.getString(R.string.EventVersionPreference_EventVersion) + String.valueOf(EventId), 0);
	}
	
	public static void setCurrentVersionByEventId(Context context, int EventId, int EventVersion)
	{
		//If Stored in Preference
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		SharedPreferences.Editor prefEditor = appSettings.edit();  
		prefEditor.putInt(context.getString(R.string.EventVersionPreference_EventVersion) + String.valueOf(EventId), EventVersion);
		prefEditor.commit();	
	}
	
	public static JSONArray loadSpeakerSearchResultsFromWeb(Context context, String url, int EventId) throws IOException, JSONException
	{
		URL request = new URL(url);
		String jsonRaw = Common.getContent((InputStream) request.getContent());
		JSONArray collection = new JSONArray(jsonRaw);
		
		//Caching it
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		SharedPreferences.Editor prefEditor = appSettings.edit();  
		prefEditor.putString(context.getString(R.string.EventVersionPreference_Speakers) + String.valueOf(EventId), jsonRaw);
		prefEditor.commit();	
		
		//Load up the Sessions
		loadEventSessionsFromWeb(context, EventId);
		
		return collection;
	}
	
	public static JSONArray loadSpeakerSearchResultsFromCache(Context context, int EventId) throws IOException, JSONException
	{		
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		
		String jsonRaw = appSettings.getString(context.getString(R.string.EventVersionPreference_Speakers) + String.valueOf(EventId), "");
		JSONArray collection = new JSONArray(jsonRaw);		
		
		return collection;
	}
	
	public static JSONArray loadEventSessionsFromWeb(Context context, int EventId) throws IOException, JSONException
	{
		String Url = getSessionsByCriteriaURL(context.getResources(), EventId);
		URL request = new URL(Url);
		String jsonRaw = Common.getContent((InputStream) request.getContent());
		JSONArray collection = new JSONArray(jsonRaw);
		
		//Caching it
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		SharedPreferences.Editor prefEditor = appSettings.edit();  
		prefEditor.putString(context.getString(R.string.EventVersionPreference_Sessions) + String.valueOf(EventId), jsonRaw);
		prefEditor.commit();
		
		return collection;
	}
	
	public static JSONArray loadEventSessionsFromCache(Context context, int EventId) throws IOException, JSONException
	{		
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		
		String jsonRaw = appSettings.getString(context.getString(R.string.EventVersionPreference_Sessions) + String.valueOf(EventId), "");
		JSONArray collection = new JSONArray(jsonRaw);		
		
		return collection;
	}
	
	public static String getSessionNameBySession(Context context, int EventId, int Session) throws IOException, JSONException
	{
		JSONArray sessions = loadEventSessionsFromCache(context, EventId);
		
		String ret = "";
		for(int i = 0; i < sessions.length(); i++)
		{
			if(sessions.getJSONObject(i).getInt("Session") == Session)
			{
				ret = "Session " + String.valueOf(sessions.getJSONObject(i).getString("Session")) + ": " + sessions.getJSONObject(i).getString("SessionName");
				break;
			}
		}
		
		return ret;
	}
}