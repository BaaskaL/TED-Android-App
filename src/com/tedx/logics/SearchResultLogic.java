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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;

import com.catchnotes.tedapp.R;
import com.tedx.utility.FusionTableReader;


public class SearchResultLogic 
{
	/**
	 * Getting Event About Page Information from Google Fusion Table by Providing EventId
	 * @param context
	 * @param EventId
	 * @return
	 */
	public static String getEventAboutByEventId(Context context, int EventId)
	{
		JSONObject responseJSON = null;
		try {
			responseJSON = FusionTableReader.getSearchResultsByUrl(context.getString(R.string.GetEventAbout).replace("{eventid}", String.valueOf(EventId)) + 
					context.getString(R.string.GetEventAboutCallBack), 
					context.getString(R.string.GetEventAboutCallBack)).getJSONObject(0);
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(responseJSON != null)
		{
			try {
				if(responseJSON.getString("About") != null)
				{
					SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventAboutPreference), android.content.Context.MODE_PRIVATE);		
					SharedPreferences.Editor prefEditor = appSettings.edit();  
					prefEditor.putString(context.getString(R.string.EventAboutPreference_EventAbout) + String.valueOf(EventId), responseJSON.getString("About"));
					prefEditor.commit();
				}
				return responseJSON.getString("About");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return "";
			}
		}
		else return "";
	}
	
	//Get Current Version from Cache
	public static String getEventAboutByEventIdFromCache(Context context, int EventId)
	{
		//If Stored in Preference
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventAboutPreference), android.content.Context.MODE_PRIVATE);		
		return appSettings.getString(context.getString(R.string.EventAboutPreference_EventAbout) + String.valueOf(EventId), "");
	}
	
	public static int getSearchResultVersionByEventId(Resources res, int EventId)
	{
		JSONObject responseJSON = null;
		try {
			responseJSON = FusionTableReader.getSearchResultsByUrl(	res.getString(R.string.GetEventVersion).replace("{eventid}", String.valueOf(EventId)) + 
					res.getString(R.string.GetEventVersionCallBack), 
					res.getString(R.string.GetEventVersionCallBack)).getJSONObject(0);
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(responseJSON != null)
		{
			try {
				return responseJSON.getInt("EventVersion");
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
	
	/**
	 * Loading Entire Speaker from the web, mainly going through gogole fusion table
	 * @param context
	 * @param url
	 * @param EventId
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray loadSpeakerSearchResultsFromWeb(Context context, int EventId) throws IOException, JSONException
	{
		JSONArray collection = FusionTableReader.getSearchResultsByUrl(	context.getResources().getString(R.string.GetEventSpeakers).replace("{eventid}", String.valueOf(EventId)) + 
				context.getResources().getString(R.string.GetEventSpeakersCallBack), 
				context.getResources().getString(R.string.GetEventSpeakersCallBack));
		
		//Caching it
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		SharedPreferences.Editor prefEditor = appSettings.edit();  
		prefEditor.putString(context.getString(R.string.EventVersionPreference_Speakers) + String.valueOf(EventId), collection.toString());
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
	
	/**
	 * Loading Session Variable from the web
	 * @param context
	 * @param EventId
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray loadEventSessionsFromWeb(Context context, int EventId) throws IOException, JSONException
	{
		JSONArray collection = FusionTableReader.getSearchResultsByUrl(	context.getResources().getString(R.string.GetEventSessions).replace("{eventid}", String.valueOf(EventId)) + 
				context.getResources().getString(R.string.GetEventSessionsCallBack), 
				context.getResources().getString(R.string.GetEventSessionsCallBack));
				
		//Caching it
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventVersionPreference), android.content.Context.MODE_PRIVATE);		
		SharedPreferences.Editor prefEditor = appSettings.edit();  
		prefEditor.putString(context.getString(R.string.EventVersionPreference_Sessions) + String.valueOf(EventId), collection.toString());
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
	
	/**
	 * Local method for pulling session names for the speaker
	 * @param context
	 * @param EventId
	 * @param Session
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getSessionNameBySession(Context context, int EventId, int Session) throws IOException, JSONException
	{
		JSONArray sessions = loadEventSessionsFromCache(context, EventId);
		
		String ret = "";
		for(int i = 0; i < sessions.length(); i++)
		{
			if(sessions.getJSONObject(i).getInt("SessionId") == Session)
			{
				ret = "Session " + String.valueOf(Session) + ": " + sessions.getJSONObject(i).getString("SessionName");
				break;
			}
		}
		
		return ret;
	}
}