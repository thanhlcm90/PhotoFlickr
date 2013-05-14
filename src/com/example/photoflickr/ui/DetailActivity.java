package com.example.photoflickr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.photoflickr.R;
import com.example.photoflickr.transformer.ZoomOutPageTransformer;
import com.example.photoutil.ImageCache;
import com.example.photoutil.ImageFetcher;
import com.example.photoutil.Utils;

public class DetailActivity extends SherlockFragmentActivity {
	private static final String IMAGE_CACHE_DIR = "images";
	private int mAvatarThumbSize;
	private static ImageFetcher mImageFetcher;
	private ViewPager mPager;
	private DetailPagerAdapter mAdapter;
	public static int current_page;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * if (BuildConfig.DEBUG) { Utils.enableStrictMode(); }
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		mAvatarThumbSize = getResources().getDimensionPixelSize(
				R.dimen.avatar_size);

		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(
				this, IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(this, mAvatarThumbSize);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);

		mAdapter = new DetailPagerAdapter(getSupportFragmentManager(),
				MainFragement.list.size());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(1);
		if (Utils.hasHoneycomb()) {
			mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		}
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				current_page=arg0;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
		});

		final int extraCurrentItem = getIntent().getIntExtra(
				MainFragement.EXTRA_ITEM_POSITION, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
		}

		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * The main adapter that backs the ViewPager. A subclass of
	 * FragmentStatePagerAdapter as there could be a large number of items in
	 * the ViewPager and we don't want to retain them all in memory at once but
	 * create/destroy them on the fly.
	 */
	private class DetailPagerAdapter extends FragmentStatePagerAdapter {
		private final int mSize;

		public DetailPagerAdapter(FragmentManager fm, int size) {
			super(fm);
			mSize = size;
		}

		@Override
		public int getCount() {
			return mSize;
		}

		@Override
		public Fragment getItem(int position) {
			return DetailFragement.newInstance(
					MainFragement.list.get(position), position);
		}
	}

	public static ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	public void onPause() {
		super.onPause();

		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
		mImageFetcher.closeCache();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
