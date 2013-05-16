package com.example.photoflickr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
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

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.image_show, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
