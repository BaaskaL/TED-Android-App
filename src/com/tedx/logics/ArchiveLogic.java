/*
	Copyright 2011 Catch.com, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License
 */
package com.tedx.logics;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;

import com.catchnotes.tedapp.R;

/**
 * This is the controller for the current conference information
 * @author nyceane
 *
 */
public class ArchiveLogic {
	public static void SetConference(Context context, int rowid)
	{
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventSelectorPreference), android.content.Context.MODE_PRIVATE);		
		SharedPreferences.Editor prefEditor = appSettings.edit();  
		prefEditor.putInt(context.getString(R.string.EventSelectorPreference), rowid);
		prefEditor.commit();
	}
	
	public static int GetConferenceRowId(Context context){
		SharedPreferences appSettings = context.getSharedPreferences(context.getString(R.string.EventSelectorPreference), android.content.Context.MODE_PRIVATE);		
		return appSettings.getInt(context.getString(R.string.EventSelectorPreference), 0);
	}
	
	/**
	 * Getting EventId based on cached event
	 * @param context
	 * @return
	 */
	public static int GetEventId(Context context){
		int row = GetConferenceRowId(context);
		int ret = 0;
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = Integer.valueOf(temp.getAttributeValue(null, "eventid"));	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}

	/**
	 * Getting SubEventId based on cached event
	 * @param context
	 * @return
	 */
	public static int GetSubEventId(Context context){
		int row = GetConferenceRowId(context);
		int ret = 0;
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = Integer.valueOf(temp.getAttributeValue(null, "subeventid"));	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}

	/**
	 * Getting Logo based on cached event
	 * @param context
	 * @return logo's resource id
	 */
	public static int GetLogo(Context context){
		int row = GetConferenceRowId(context);
		int ret = 0;
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = temp.getAttributeResourceValue(null, "logo", 0);
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}
	
	/**
	 * Getting VenueName based on cached event
	 * @param context
	 * @return
	 */
	public static String GetVenueName(Context context){
		int row = GetConferenceRowId(context);
		String ret = "";
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = temp.getAttributeValue(null, "venuename");	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}

	/**
	 * Getting Latitude based on cached event
	 * @param context
	 * @return
	 */
	public static String GetVenueLatitude(Context context){
		int row = GetConferenceRowId(context);
		String ret = "";
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = temp.getAttributeValue(null, "latitude");	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}

	/**
	 * Getting Longitude based on cached event
	 * @param context
	 * @return
	 */
	public static String GetVenueLongitude(Context context){
		int row = GetConferenceRowId(context);
		String ret = "";
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = temp.getAttributeValue(null, "longitude");	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}

	/**
	 * Getting VenueName based on cached event
	 * @param context
	 * @return
	 */
	public static String GetVenueAddress(Context context){
		int row = GetConferenceRowId(context);
		String ret = "";
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = temp.getAttributeValue(null, "address");	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}
	
	/**
	 * Getting EventTag based on cached event
	 * @param context
	 * @return
	 */
	public static String GetEventtag(Context context){
		int row = GetConferenceRowId(context);
		String ret = "";
		int count = 0;
		XmlResourceParser temp = context.getResources().getXml(R.xml.conferences);
	     try {
			while (temp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (temp.getEventType() == XmlResourceParser.START_TAG) {
                	String s = temp.getName();
                	
				    if(s.equals("conference"))
				    {
				    	if(row == count){
					    	ret = temp.getAttributeValue(null, "eventtag");	
				    	}	                	
				    	count++;
				    }
				    				     
                }
			    temp.next();
			 }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.close();
	     
    	return ret;
	}

}
