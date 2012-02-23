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

package com.catchnotes.tedapp;

import java.util.ArrayList;

import com.catchnotes.tedapp.AboutActivity;
import com.catchnotes.tedapp.R;
import com.tedx.logics.ArchiveLogic;
import com.tedx.utility.IntentIntegrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class Main extends Activity {
    /** Called when the activity is first created. */
	private IntentIntegrator _notesIntent;
	private static final int MENU_CONTACT = 1;
	private static final int MENU_FACEBOOK = 2;
	private static final int MENU_TWITTER = 3;
	
	private static final String TRACKER_PACKAGE_NAME = "com.catchnotes.tedapp"; 

	private ImageView mImgTop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _notesIntent = new IntentIntegrator(this);
        mImgTop = (ImageView)findViewById(R.id.imgTop);
        mImgTop.setImageResource(ArchiveLogic.GetLogo(this));
    }
    
    @Override
    public void onResume()
    {
        mImgTop.setImageResource(ArchiveLogic.GetLogo(this));
    	super.onResume();
    }
    //Loading About
    public void btnabout_Click(View target){
    	Intent intent = new Intent(this, AboutActivity.class);
		this.startActivity(intent);
    }
    
    //Loading up all the Archived Conferences
    public void btnconferences_Click(View target){
    	Intent intent = new Intent(this, ArchiveActivity.class);
		this.startActivity(intent);    	
    }
    
    //Loading Speaker List
    public void btnspeakers_Click(View target){
    	Intent intent = new Intent(this, SpeakerResultTabActivity.class);
		this.startActivity(intent);
    }
    
    //Loading Contact (Email)
    public void btncontact_Click(View target){
    	String emailaddress = this.getString(R.string.email_address);
    	String emailsubject = this.getString(R.string.email_subject);

		String info;
		String version = "x.xx";

		try {
			info = "\n\n----\nDevice: " + Build.MODEL + " (\"" + Build.DEVICE +
				"\")\nBrand: " + Build.BRAND + "\nOS: Android " + Build.VERSION.RELEASE +
				"\nBuild: " + Build.DISPLAY + "";
		} catch (Exception e) {
			info = "";
			e.printStackTrace();
		}
		
		try {
			PackageInfo pinfo = this.getPackageManager().getPackageInfo(
					TRACKER_PACKAGE_NAME, 0);
			version = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	emailIntent.setType("plain/text");
    	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailaddress});
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailsubject + version);    
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, info);    	

    	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    
    //Loading Facebook
    public void btnfacebook_Click(View target){

    	String facebookurl = this.getString(R.string.facebookurl);
		Uri uri = Uri.parse(facebookurl);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		this.startActivity(intent);    
    }
    
    //Loading Facebook
    public void btntwitter_Click(View target){

    	String facebookurl = this.getString(R.string.twitterurl);
		Uri uri = Uri.parse(facebookurl);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		this.startActivity(intent);    
    }
    
    //Loading Note
    public void btnnote_Click(View target)
    {
		_notesIntent.viewNotes(ArchiveLogic.GetEventtag(this).replace("#", ""));
    }
    
    //Loading Map
    public void btnmap_Click(View target)
    {
    	Intent intent = new Intent(this, EventMapActivity.class);

	    ArrayList<String> Latitude = new ArrayList<String>();
	    Latitude.add(ArchiveLogic.GetVenueLatitude(this));
	    ArrayList<String> Longitude = new ArrayList<String>();
	    Longitude.add(ArchiveLogic.GetVenueLongitude(this));
	    ArrayList<String> Name = new ArrayList<String>();
	    Name.add(ArchiveLogic.GetVenueName(this));
	    ArrayList<String> Description = new ArrayList<String>();
	    Description.add(ArchiveLogic.GetVenueAddress(this));
	    
	    Bundle locationBundle = new Bundle();
	    locationBundle.putStringArrayList("Latitude", Latitude);
	    locationBundle.putStringArrayList("Longitude", Longitude);
	    locationBundle.putStringArrayList("Name", Name);
	    locationBundle.putStringArrayList("Description", Description);

	    intent.putExtra("Location", locationBundle);
		this.startActivity(intent);
    }
    
    //Loading up the barcode scanner
    public void btnscan_Click(View target)
    {
    	try
    	{
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
            //startActivity(intent);	
    	}
    	catch (ActivityNotFoundException e) 
    	{
    	    AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
    	    downloadDialog.setTitle("Barcode Scanner needed");
    	    downloadDialog.setMessage("You need Barcode Scanner to scan others" );
    	    downloadDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
    	      public void onClick(DialogInterface dialogInterface, int i) {
    	        Uri uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android");
    	        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    	        Main.this.startActivity(intent);
    	      }
    	    });
    	    downloadDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	      public void onClick(DialogInterface dialogInterface, int i) {}
    	    });
    	    downloadDialog.show();
    	}
    }

    //Loading up the barcode scanner
    public void btnuniversity_Click(View target)
    {
    	Intent intent = new Intent(this, SubEventSpeakerResultTabActivity.class);
		this.startActivity(intent);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		// sets the search menu button key

		menu.add(0, MENU_CONTACT, 0, "Contact")
		.setIcon(R.drawable.mail_menu)
		.setAlphabeticShortcut(SearchManager.MENU_KEY);
		
		menu.add(0, MENU_FACEBOOK, 0, "Facebook")
		.setIcon(R.drawable.facebook_menu)
		.setAlphabeticShortcut(SearchManager.MENU_KEY);	        
	        
		menu.add(0, MENU_TWITTER, 0, "Twitter")
		.setIcon(R.drawable.twitter_menu)
		.setAlphabeticShortcut(SearchManager.MENU_KEY);

		return super.onCreateOptionsMenu(menu);
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {			
			case MENU_CONTACT:
				btncontact_Click(null);
				return true;
			case MENU_FACEBOOK:
				btnfacebook_Click(null);
				return true;
			case MENU_TWITTER:
				btntwitter_Click(null);
				return true;
		}
		return super.onOptionsItemSelected(item);
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