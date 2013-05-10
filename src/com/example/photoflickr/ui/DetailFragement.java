package com.example.photoflickr.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.photoflickr.CommentArrayAdapter;
import com.example.photoflickr.CommentItem;
import com.example.photoflickr.TaskLoadComment;
import com.example.photoflickr.R;
import com.example.photoflickr.ResultItem;
import com.example.photoutil.ImageFetcher;
import com.example.photoutil.Utils;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class DetailFragement extends Fragment {
	private final String TAG = "DetailFragement";
	public final static String EXTRA_ITEM_POSITION = "com.example.photoflickr.ITEM.POSITION";
	public final static String EXTRA_ITEM_USERNAME = "com.example.photoflickr.ITEM.USERNAME";
	public final static String EXTRA_ITEM_FULLNAME = "com.example.photoflickr.ITEM.FULLNAME";
	public final static String EXTRA_ITEM_LOCATION = "com.example.photoflickr.ITEM.LOCATION";
	public final static String EXTRA_ITEM_TITLE = "com.example.photoflickr.ITEM.TITLE";
	public final static String EXTRA_ITEM_DESCRIPTION = "com.example.photoflickr.ITEM.DESCRIPTION";
	public final static String EXTRA_ITEM_POSTDATE = "com.example.photoflickr.ITEM.POSTDATE";
	public final static String EXTRA_ITEM_VIEWCOUNT = "com.example.photoflickr.ITEM.VIEWCOUNT";
	public final static String EXTRA_ITEM_AVATAR_URL = "com.example.photoflickr.ITEM.AVATAR_URL";
	public final static String EXTRA_ITEM_PHOTO_URL = "com.example.photoflickr.ITEM.PHOTO_URL";
	public final static String EXTRA_ITEM_PHOTO_LARGE_URL = "com.example.photoflickr.ITEM.PHOTO_LARGE_URL";
	public final static String EXTRA_ITEM_PHOTO_ID = "com.example.photoflickr.ITEM.PHOTO_ID";
	private ImageFetcher mImageFetcher;
	ArrayList<CommentItem> list = new ArrayList<CommentItem>();
	CommentArrayAdapter adapter;

	// Animation
	private Animator mCurrentAnimator;

	// Thoi gian Animation, milisecond
	private int mShortAnimationDuration;
	private int current_pos = -1;
	private ResultItem resultitem;

	/**
	 * Factory method to generate a new instance of the fragment given an image
	 * number.
	 * 
	 * @param imageUrl
	 *            The image url to load
	 * @return A new instance of ImageDetailFragment with imageNum extras
	 */
	public static DetailFragement newInstance(ResultItem item, int pos) {
		final DetailFragement f = new DetailFragement();
		final Bundle args = new Bundle();
		args.putString(EXTRA_ITEM_TITLE, item.getTitle());
		args.putString(EXTRA_ITEM_PHOTO_URL, item.getPhotoUrl());
		args.putString(EXTRA_ITEM_PHOTO_ID, item.getPhotoId());
		args.putInt(EXTRA_ITEM_POSITION, pos);
		f.setArguments(args);
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
		View footer_view = inflater.inflate(R.layout.comment_footer, null,
				false);
		ProgressBar loading = (ProgressBar) footer_view
				.findViewById(R.id.loading);
		View header_view = inflater.inflate(R.layout.comment_header, null,
				false);
		int pos = getArguments().getInt(EXTRA_ITEM_POSITION, -1);
		if (pos != -1)
			resultitem = MainFragement.list.get(pos);

		String title = getArguments().getString(EXTRA_ITEM_TITLE);
		final String photo = getArguments().getString(EXTRA_ITEM_PHOTO_URL);
		// Xu ly lai URL, get Anh lon (240pX)
		// photo=photo.replace("_m.jpg", "_m.jpg");
		String photoid = getArguments().getString(EXTRA_ITEM_PHOTO_ID);
		Log.i(TAG, resultitem.isLoaded + "");
		// Tai noi dung chi tiet ve buc anh
		if (resultitem != null && !resultitem.isLoaded) {
			new GetDetailTask(resultitem, header_view).execute("");
		} else {
			SetDetailInformation(resultitem, header_view);
		}

		final ListView comment_list = (ListView) v
				.findViewById(R.id.comment_list);
		adapter = new CommentArrayAdapter(getActivity(), list);
		header_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		comment_list.addFooterView(footer_view);
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

		ImageButton img_photo = (ImageButton) header_view
				.findViewById(R.id.photo);

		getActivity().setTitle(title);
		MainFragement.getPhotoFetcher().loadImage(photo, img_photo);
		new TaskLoadComment(comment_list, list, adapter, loading)
				.execute(photoid);

		img_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String photo_large = photo.replace("_m.jpg", "_b.jpg");
				Intent intent = new Intent(getActivity(),
						ImageShowActivity.class);
				intent.putExtra(EXTRA_ITEM_PHOTO_LARGE_URL, photo_large);
				startActivity(intent);
			}
		});

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

		// Use the parent activity to load the image asynchronously into the
		// ImageView (so a single
		// cache can be used over all pages in the ViewPager
		if (DetailActivity.class.isInstance(getActivity())) {
			mImageFetcher = ((DetailActivity) getActivity()).getImageFetcher();
		}
	}

	public void SetDetailInformation(ResultItem item, View header_view) {
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
		TextView tv_viewcount = (TextView) header_view
				.findViewById(R.id.viewcount);
		ImageView img_avatar = (ImageView) header_view
				.findViewById(R.id.avatar);

		tv_username.setText(item.getUsername());
		tv_fullname.setText(item.getFullname());
		tv_location.setText(item.getLocation());
		tv_postdate.setText(item.getPostDate());
		tv_viewcount.setText("Views: " + item.getViewCount());
		tv_description.setText(item.getDescription());
		MainFragement.getAvatarFetcher().loadImage(item.getAvatarUrl(),
				img_avatar);
	}

	private class GetDetailTask extends AsyncTask<String, String, Boolean> {
		private ResultItem item;
		private View header_view;

		public GetDetailTask(ResultItem item, View header_view) {
			this.item = item;
			this.header_view = header_view;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				JSONObject json = Utils
						.getJSONfromURL("http://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=63a95e8826699c2e7f401a3288bf20cf&photo_id="
								+ item.getPhotoId()
								+ "&secret="
								+ item.getPhotoSecret()
								+ "&format=json&nojsoncallback=1");
				JSONObject photoInfo = json.getJSONObject("photo");
				JSONObject person = photoInfo.getJSONObject("owner");
				String viewcount = photoInfo.getString("views");
				String username = person.getString("username");
				String fullname = person.getString("realname");
				String location = person.getString("location");
				int icon_farm = person.getInt("iconfarm");
				int icon_server = person.getInt("iconserver");
				String avatarurl = "http://farm" + icon_farm
						+ ".staticflickr.com/" + icon_server + "/buddyicons/"
						+ item.getUserid() + ".jpg";

				JSONObject dates = photoInfo.getJSONObject("dates");
				String takendate = dates.getString("taken");
				String title = photoInfo.getJSONObject("title").getString(
						"_content");
				String description = photoInfo.getJSONObject("description")
						.getString("_content");

				// Xu ly lai ngay than theo dinh dang dd/MM/yyyy HH:mm:ss
				if (takendate != null && !takendate.equals("")) {
					String[] datetimes = takendate.split(" ");
					String[] date = datetimes[0].split("-");
					takendate = date[2] + "/" + date[1] + "/" + date[0] + " "
							+ datetimes[1];
				}
				item.setPostDate(takendate);
				item.setUsername(username);
				item.setFullname(fullname);
				item.setLocation(location);
				item.setTitle(title);
				item.setViewCount(viewcount);
				item.setDescription(description);
				item.setAvatarUrl(avatarurl);
				item.isLoaded = true;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				SetDetailInformation(item, header_view);
			}
		}
	}
}
