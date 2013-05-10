package com.example.photoflickr.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.photoflickr.R;
import com.example.photoflickr.transformer.ZoomOutPageTransformer;
import com.example.photoutil.ImageCache;
import com.example.photoutil.ImageFetcher;
import com.example.photoutil.Utils;

public class DetailActivity extends FragmentActivity {
	private static final String IMAGE_CACHE_DIR = "images";
	private int mAvatarThumbSize;
	private static ImageFetcher mImageFetcher;
	private ViewPager mPager;
	private DetailPagerAdapter mAdapter;
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
		
		final int extraCurrentItem = getIntent().getIntExtra(
				MainFragement.EXTRA_ITEM_POSITION, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
		}
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
}
