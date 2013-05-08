package com.example.photoflickr;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	ArrayList<ResultItem> list = new ArrayList<ResultItem>();
	PhotoArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Bat su kien key Enter cua Edit Text
		final EditText keyword = (EditText) findViewById(R.id.keyword);
		keyword.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					// Lay ket qua JSON tu URL
					String text = keyword.getText().toString();
					String url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=63a95e8826699c2e7f401a3288bf20cf&text="
							+ text
							+ "&per_page=5&page=0&format=json&nojsoncallback=1";
					new SearchApiCall(MainActivity.this,list,adapter).execute(url);					
				}
				return false;
			}
		});

		final ListView photoView = (ListView) findViewById(R.id.photoView);
		adapter = new PhotoArrayAdapter(this, list);
		photoView.setAdapter(adapter);

//		photoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@SuppressLint("NewApi")
//			@Override
//			public void onItemClick(AdapterView<?> parent, final View view,
//					int position, long id) {
//				final String item = (String) parent.getItemAtPosition(position);
//				view.animate().setDuration(2000).alpha(0)
//						.withEndAction(new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								list.remove(item);
//								adapter.notifyDataSetChanged();
//								view.setAlpha(1);
//							}
//						});
//			}
//		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
