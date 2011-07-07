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

import com.catchnotes.tedapp.R;
import com.tedx.utility.IntentIntegrator;
import com.tedx.logics.ArchiveLogic;
import com.tedx.objects.SearchResult;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SpeakerActivity extends Activity{
	public WebView mWebView;
	private IntentIntegrator _notesIntent;
	private Bundle mSpeaker;

    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speakerview);
        _notesIntent = new IntentIntegrator(this);

        mSpeaker = getIntent().getExtras();            
   
        mWebView = (WebView) findViewById(R.id.webview);   
        mWebView.setInitialScale(120);
        mWebView.getSettings().setJavaScriptEnabled(true);

        /*
        StringBuilder querystring = new StringBuilder();
        querystring.append("description=");
        querystring.append(mSpeaker.get("Description").toString());
        
        querystring.append("&speakername=");
        querystring.append(mSpeaker.get("ProfileName").toString());

        querystring.append("&speakertitle=");
        querystring.append(mSpeaker.get("Title").toString());
        
        querystring.append("&speakerphoto=");
        querystring.append(mSpeaker.get("PhotoUrl").toString());
        */
        
		mWebView.setWebViewClient(new WebViewClient(){
			public void onPageFinished (WebView view, String url)
			{
				mWebView.loadUrl("javascript:setSpeakerDescription('" + mSpeaker.get("Description").toString().replace("'", "").replace(System.getProperty("line.separator"), "").replace("\r", "<br />") + "')");
				mWebView.loadUrl("javascript:setSpeakerName('" + mSpeaker.get("ProfileName").toString().replace("'", "").replace(System.getProperty("line.separator"), "").replace("\r", "<br />") + "')");
				mWebView.loadUrl("javascript:setSpeakerPhoto('" + mSpeaker.get("PhotoUrl").toString().replace("'", "").replace(System.getProperty("line.separator"), "").replace("\r", "<br />") + "')");
				mWebView.loadUrl("javascript:setSpeakerTitle('" + mSpeaker.get("Title").toString().replace("'", "").replace(System.getProperty("line.separator"), "").replace("\r", "<br />") + "')");
			}
		});
        
        String url = "file:///android_asset/speaker/index.html";
		
		mWebView.loadUrl(url);
		
    }
    
    //Writing Notes with this specific speaker
    public void btnWrite_Click(View target)
    {
    	String speakerNote 	= 	"Speaker: " + mSpeaker.getString(SearchResult.NAME) + "\n" +
    							"Title: " + mSpeaker.getString(SearchResult.TITLE) + "\n" + 
    							"Bio: " + mSpeaker.getString(SearchResult.DESCRIPTION) + "\n" + 
    							"\nWebsite: " + mSpeaker.getString(SearchResult.WEBSITE) + "\n" +
    							"\nTwitter: " + mSpeaker.getString(SearchResult.TWITTER) + "\n" +
    							"\nMy Notes: " + "\n\n" + 
    							"\n________________________________\nTags:" +
    							ArchiveLogic.GetEventtag(this);
		_notesIntent.createNote(speakerNote);
    }
    
    //Reading the Notes
    public void btnRead_Click(View target)
    {
		_notesIntent.viewNotes(ArchiveLogic.GetEventtag(this).replace("#", ""));
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
