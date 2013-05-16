package com.example.photoflickr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.photoflickr.R;

public class MainActivity extends SherlockFragmentActivity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * if (BuildConfig.DEBUG) { Utils.enableStrictMode(); }
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.fragment_container, new MainFragement());
			ft.commit();

		}

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent ;
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			break;
		case R.id.action_upload:
			intent = new Intent(this, CameraActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
