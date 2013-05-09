package com.example.photoflickr.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.example.photoflickr.CommentArrayAdapter;
import com.example.photoflickr.CommentItem;
import com.example.photoflickr.LoadCommentTask;
import com.example.photoflickr.R;
import com.example.photoflickr.ResultItem;
import com.example.photoflickr.SearchTask;
import com.example.photoutil.ImageCache;
import com.example.photoutil.ImageFetcher;

public class DetailFragement extends Fragment {
	public final static String EXTRA_ITEM_USERNAME = "com.example.photoflickr.ITEM.USERNAME";
	public final static String EXTRA_ITEM_FULLNAME = "com.example.photoflickr.ITEM.FULLNAME";
	public final static String EXTRA_ITEM_LOCATION = "com.example.photoflickr.ITEM.LOCATION";
	public final static String EXTRA_ITEM_TITLE = "com.example.photoflickr.ITEM.TITLE";
	public final static String EXTRA_ITEM_DESCRIPTION = "com.example.photoflickr.ITEM.DESCRIPTION";
	public final static String EXTRA_ITEM_POSTDATE = "com.example.photoflickr.ITEM.POSTDATE";
	public final static String EXTRA_ITEM_AVATAR_URL = "com.example.photoflickr.ITEM.AVATAR_URL";
	public final static String EXTRA_ITEM_PHOTO_URL = "com.example.photoflickr.ITEM.PHOTO_URL";
	public final static String EXTRA_ITEM_PHOTO_ID = "com.example.photoflickr.ITEM.PHOTO_ID";
    private ImageFetcher mImageFetcher;
	static ArrayList<CommentItem> list = new ArrayList<CommentItem>();
	CommentArrayAdapter adapter;

    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static DetailFragement newInstance(ResultItem item, int pos) {
        final DetailFragement f = new DetailFragement();
        final Bundle args = new Bundle();
        args.putString(EXTRA_ITEM_USERNAME, item.getUsername());
		args.putString(EXTRA_ITEM_FULLNAME, item.getFullname());
		args.putString(EXTRA_ITEM_LOCATION, item.getLocation());
		args.putString(EXTRA_ITEM_TITLE, item.getTitle());
		args.putString(EXTRA_ITEM_DESCRIPTION, item.getDescription());
		args.putString(EXTRA_ITEM_POSTDATE, item.getPostDate());
		args.putString(EXTRA_ITEM_AVATAR_URL, item.getAvatarUrl());
		args.putString(EXTRA_ITEM_PHOTO_URL, item.getPhotoUrl());
		args.putString(EXTRA_ITEM_PHOTO_ID, item.getPhotoId());
        f.setArguments(args);
        Log.i("Position",pos + " " );
        return f;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragement_detail, container,
				false);
		String username = getArguments().getString(EXTRA_ITEM_USERNAME);
		String fullname = getArguments().getString(EXTRA_ITEM_FULLNAME);
		String location = getArguments().getString(EXTRA_ITEM_LOCATION);
		String postdate = getArguments().getString(EXTRA_ITEM_POSTDATE);
		String title = getArguments().getString(EXTRA_ITEM_TITLE);
		String description = getArguments().getString(EXTRA_ITEM_DESCRIPTION);
		String avatar = getArguments().getString(EXTRA_ITEM_AVATAR_URL);
		String photo = getArguments().getString(EXTRA_ITEM_PHOTO_URL);
		// Xu ly lai URL, get Anh lon (240pX)
		// photo=photo.replace("_m.jpg", "_m.jpg");
		String photoid = getArguments().getString(EXTRA_ITEM_PHOTO_ID);

		final ListView comment_list = (ListView) v.findViewById(R.id.comment_list);
		adapter = new CommentArrayAdapter(getActivity(), list);
		View header_view = inflater.inflate(R.layout.comment_header, null,
				false);
		header_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		comment_list.addHeaderView(header_view);
		comment_list.setAdapter(adapter);
		comment_list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
                } else {
                	mImageFetcher.setPauseWork(false);
                }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		TextView tv_username = (TextView) header_view
				.findViewById(R.id.username);
		TextView tv_fullname = (TextView) header_view
				.findViewById(R.id.fullname);
		TextView tv_location = (TextView) header_view
				.findViewById(R.id.location);
		TextView tv_postdate = (TextView) header_view
				.findViewById(R.id.postdate);
		TextView tv_description = (TextView) header_view
				.findViewById(R.id.description);
		ImageView img_avatar = (ImageView) header_view
				.findViewById(R.id.avatar);
		ImageView img_photo = (ImageView) header_view.findViewById(R.id.photo);

		tv_username.setText(username);
		tv_fullname.setText(fullname);
		tv_location.setText(location);
		tv_postdate.setText(postdate);
		// tv_title.setText(title);
		getActivity().setTitle(title);
		tv_description.setText(description);
		MainFragement.getAvatarFetcher().loadImage(avatar, img_avatar);
		Log.i("Photo", photo);
		MainFragement.getPhotoFetcher().loadImage(photo, img_photo);
		new LoadCommentTask(comment_list, list, adapter).execute(photoid);
		return v;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		// Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        if (DetailActivity.class.isInstance(getActivity())) {
            mImageFetcher = ((DetailActivity) getActivity()).getImageFetcher();
        }
	}    
}
