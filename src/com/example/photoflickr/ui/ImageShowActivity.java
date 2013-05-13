package com.example.photoflickr.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.photoflickr.R;

public class ImageShowActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);  
		setContentView(webView);  
		String urlImage = getIntent().getStringExtra(DetailFragement.EXTRA_ITEM_PHOTO_LARGE_URL);
		String yourData = "<body style='display:table; height:100%; width:100%;'><div style='display:table-cell;vertical-align:middle;text-align:center'><img src='" + urlImage + "' />";
		  
		try {  
			webView.loadData(yourData, "text/html", "UTF-8");
		    webView.getSettings().setLoadsImagesAutomatically(true);
		    webView.getSettings().setBuiltInZoomControls(true); // zooming  
		                                                        // controls  
		    //webView.setInitialScale(100); // sets the scale to 25% you may  
		                                    // specify whatever suits you best  
		} catch (Exception e) {  
		    e.printStackTrace();  
		}  
	}

}
