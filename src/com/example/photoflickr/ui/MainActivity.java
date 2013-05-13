package com.example.photoflickr.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.photoflickr.BuildConfig;
import com.example.photoflickr.R;
import com.example.photoutil.Utils;

public class MainActivity extends SherlockFragmentActivity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*if (BuildConfig.DEBUG) {
			Utils.enableStrictMode();
		}*/
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
	}
}
