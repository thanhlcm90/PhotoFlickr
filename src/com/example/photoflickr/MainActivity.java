package com.example.photoflickr;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	public final static String EXTRA_ITEM_USERNAME = "com.example.photoflickr.ITEM.USERNAME";
	public final static String EXTRA_ITEM_FULLNAME = "com.example.photoflickr.ITEM.FULLNAME";
	public final static String EXTRA_ITEM_LOCATION = "com.example.photoflickr.ITEM.LOCATION";
	public final static String EXTRA_ITEM_TITLE = "com.example.photoflickr.ITEM.TITLE";
	public final static String EXTRA_ITEM_DESCRIPTION = "com.example.photoflickr.ITEM.DESCRIPTION";
	public final static String EXTRA_ITEM_POSTDATE = "com.example.photoflickr.ITEM.POSTDATE";
	public final static String EXTRA_ITEM_AVATAR = "com.example.photoflickr.ITEM.AVATAR";
	public final static String EXTRA_ITEM_PHOTO_URL = "com.example.photoflickr.ITEM.PHOTO_URL";
	public final static String EXTRA_ITEM_PHOTO_ID = "com.example.photoflickr.ITEM.PHOTO_ID";
	
	static Boolean loadingMore = false;
	int per_page = 5;
	static int page = 0;
	static int currentFirstVisibleItem;
	static String searchText = "";
	static ArrayList<ResultItem> list = new ArrayList<ResultItem>();
	ListView photoView;
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
					list.clear();
					// Lay ket qua JSON tu URL
					searchText = keyword.getText().toString().replace(" ", "+");
					String url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=63a95e8826699c2e7f401a3288bf20cf&text="
							+ searchText
							+ "&per_page="
							+ per_page
							+ "&page="
							+ page + "&format=json&nojsoncallback=1";
					new SearchTask(photoView, list, adapter)
							.execute(url);
				}
				return false;
			}
		});

		photoView = (ListView) findViewById(R.id.photoView);
		View footer_view = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_footer, null, false);
		photoView.addFooterView(footer_view);
		adapter = new PhotoArrayAdapter(this, list);
		photoView.setAdapter(adapter);

		photoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final ResultItem item = (ResultItem) parent
						.getItemAtPosition(position);
				Intent intent = new Intent(MainActivity.this,
						DetailActivity.class);
				intent.putExtra(EXTRA_ITEM_USERNAME, item.getUsername());
				intent.putExtra(EXTRA_ITEM_FULLNAME, item.getFullname());
				intent.putExtra(EXTRA_ITEM_LOCATION, item.getLocation());
				intent.putExtra(EXTRA_ITEM_TITLE, item.getTitle());
				intent.putExtra(EXTRA_ITEM_DESCRIPTION, item.getDescription());
				intent.putExtra(EXTRA_ITEM_POSTDATE, item.getPostDate());
				intent.putExtra(EXTRA_ITEM_AVATAR, item.getAvatarImage());
				intent.putExtra(EXTRA_ITEM_PHOTO_URL, item.getPhotoUrl());
				intent.putExtra(EXTRA_ITEM_PHOTO_ID, item.getPhotoId());
				startActivity(intent);
			}
		});

		photoView.setOnScrollListener(new OnScrollListener() {
			int currentVisibleItemCount,
					currentTotalItemCount, currentScrollState;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				this.currentScrollState = scrollState;
				this.isScrollCompleted();
			}

			private void isScrollCompleted() {

				// Vi tri Item cuoi cung cua List tren man hinh
				int lastInScreen = currentFirstVisibleItem
						+ currentVisibleItemCount;
				if (lastInScreen >= currentTotalItemCount && !loadingMore
						&& searchText != "" && this.currentVisibleItemCount > 0
						&& this.currentScrollState == SCROLL_STATE_IDLE) {

					page++;
					String url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=63a95e8826699c2e7f401a3288bf20cf&text="
							+ searchText
							+ "&per_page="
							+ per_page
							+ "&page="
							+ page + "&format=json&nojsoncallback=1";
					new SearchTask(photoView, list, adapter)
							.execute(url);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				currentFirstVisibleItem = firstVisibleItem;
				this.currentVisibleItemCount = visibleItemCount;
				this.currentTotalItemCount = totalItemCount;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
