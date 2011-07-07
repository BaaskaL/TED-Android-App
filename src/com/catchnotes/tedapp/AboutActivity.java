/*
 * The MIT License

 * Copyright (c) 2010 Peter Ma Modified Catch.com on 5/5/2011

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
import com.tedx.logics.ArchiveLogic;
import com.tedx.logics.SearchResultLogic;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutActivity extends Activity{
	public WebView mWebView;
	private int mEventId;
	public String mContent = "";

    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);        
    	mEventId = ArchiveLogic.GetEventId(getApplicationContext());

        setContentView(R.layout.webview);

        mWebView = (WebView) findViewById(R.id.webview);
		String url = "file:///android_asset/about/index.html";
		
		int ServerEventVersion = SearchResultLogic.getSearchResultVersionByEventId(this.getResources(), mEventId);
		
		//check point to load from cache or web
		if(	ServerEventVersion != 0 &&
			SearchResultLogic.getCurrentVersionByEventIdFromCache(this, mEventId) != ServerEventVersion &&
			SearchResultLogic.getEventAboutByEventIdFromCache(this, mEventId) == "")
		{
			mContent = SearchResultLogic.getEventAboutByEventId(this, mEventId);
		}
		else
		{
			mContent = SearchResultLogic.getEventAboutByEventIdFromCache(this, mEventId);
		}
		
		mWebView.loadUrl(url);
		
		mWebView.setWebViewClient(new WebViewClient(){
			public void onPageFinished (WebView view, String url)
			{
				mWebView.loadUrl("javascript:setContent(" + mContent + ")");
			}
			
		});
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
