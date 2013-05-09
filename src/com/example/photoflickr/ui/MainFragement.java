package com.example.photoflickr.ui;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.example.photoflickr.PhotoArrayAdapter;
import com.example.photoflickr.R;
import com.example.photoflickr.ResultItem;
import com.example.photoflickr.SearchTask;
import com.example.photoutil.ImageCache.ImageCacheParams;
import com.example.photoutil.ImageFetcher;

public class MainFragement extends Fragment {
	public final static String EXTRA_ITEM_POSITION = "com.example.photoflickr.ITEM.POSITION";

	private static ImageFetcher mAvatarFetcher, mPhotoFetcher;
	private static final String IMAGE_CACHE_DIR = "thumbs";
	private int mAvatarThumbSize;
	private int mPhotoThumbSize;

	public static Boolean loadingMore = false;
	int per_page = 5;
	static int page = 0;
	public static int currentFirstVisibleItem;
	static String searchText = "";
	static ArrayList<ResultItem> list = new ArrayList<ResultItem>();
	ListView photoView;
	PhotoArrayAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Lay kich thuoc max thumb cua Avatar va Photo
		mAvatarThumbSize = getResources().getDimensionPixelSize(
				R.dimen.avatar_size);
		mPhotoThumbSize = getResources().getDimensionPixelSize(
				R.dimen.photo_size);

		// Khoi tao Cache
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// Khoi tao Avatar Fetch
		mAvatarFetcher = new ImageFetcher(getActivity(), mAvatarThumbSize);
		mAvatarFetcher.setLoadingImage(R.drawable.no_avatar);
		mAvatarFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
		
		// Khoi tao Photo Fetch
		mPhotoFetcher= new ImageFetcher(getActivity(), mPhotoThumbSize);
		mPhotoFetcher.setLoadingImage(R.drawable.no_image_available);
		mPhotoFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragement_main, container,
				false);
		// Bat su kien key Enter cua Edit Text
		final EditText keyword = (EditText) v.findViewById(R.id.keyword);
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
					new SearchTask(photoView, list, adapter).execute(url);
				}
				return false;
			}
		});

		photoView = (ListView) v.findViewById(R.id.photoView);
		View footer_view = inflater.inflate(R.layout.list_footer, null, false);
		photoView.addFooterView(footer_view);
		adapter = new PhotoArrayAdapter(this.getActivity(), list);
		photoView.setAdapter(adapter);

		photoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final ResultItem item = (ResultItem) parent
						.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra(EXTRA_ITEM_POSITION, position);
				startActivity(intent);
			}
		});

		photoView.setOnScrollListener(new OnScrollListener() {
			int currentVisibleItemCount, currentTotalItemCount,
					currentScrollState;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				this.currentScrollState = scrollState;
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mAvatarFetcher.setPauseWork(true);
                    mPhotoFetcher.setPauseWork(true);
                } else {
                	mAvatarFetcher.setPauseWork(false);
                    mPhotoFetcher.setPauseWork(false);
                }
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
					new SearchTask(photoView, list, adapter).execute(url);
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

		return v;
	}

	/*@Override
    public void onResume() {
        super.onResume();
        mAvatarFetcher.setExitTasksEarly(false);
        mPhotoFetcher.setExitTasksEarly(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        
        mAvatarFetcher.setPauseWork(false);
        mAvatarFetcher.setExitTasksEarly(true);
        mAvatarFetcher.flushCache();
        
        mPhotoFetcher.setPauseWork(false);
        mPhotoFetcher.setExitTasksEarly(true);
        mPhotoFetcher.flushCache();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAvatarFetcher.closeCache();
        mPhotoFetcher.closeCache();
    }
    
    public static ImageFetcher getAvatarFetcher() {
        return mAvatarFetcher;
    }
    
    public static ImageFetcher getPhotoFetcher() {
        return mPhotoFetcher;
    }

}
