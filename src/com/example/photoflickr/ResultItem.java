package com.example.photoflickr;

import java.sql.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ResultItem {
	private String avatarurl;
	private Bitmap avatar;
	private String photourl;
	private Bitmap photo;
	private String username;
	private Date postdate;
	private PhotoArrayAdapter adapter;
	private Context context;
	private String userId; 
	
	public ResultItem(Context context, PhotoArrayAdapter adapter){
		this.avatarurl="";
		this.photourl="";
		this.username="";
		this.postdate=null;
		this.adapter = adapter;
		this.context = context;
		
		// Dat Image mac dinh
		//avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_avatar);
		//photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available);
	}
	
	public ResultItem(Context context,PhotoArrayAdapter adapter, String avatarurl, String photourl, String username, Date postdate) {
		this.avatarurl=avatarurl;
		this.photourl=photourl;
		this.username=username;
		this.postdate=postdate;
		this.adapter = adapter;
		this.context = context;
		
		// Dat Image mac dinh
		//avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_avatar);
		//photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available);
	}
	
	public String getUserid() {
		return userId;
	}
	public void setUserid(String userid) {
		this.userId=userid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String value) {
		username=value;
	}
	
	public Date getPostDate() {
		return postdate;
	}
	public void setPostDate(Date value) {
		postdate=value;
	}
	
	public PhotoArrayAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(PhotoArrayAdapter adapter) {
		this.adapter=adapter;
	}

	public Bitmap getAvatarImage() {
		return avatar;
	}
	public String getAvatarUrl() {
		return avatarurl;
	}
	public void setAvatarUrl(String url) {
		avatarurl= url;
	}
	
	public Bitmap getPhotoImage() {
		return photo;
	}
	public String getPhotoUrl() {
		return photourl;
	}
	public void setPhotoUrl(String url) {
		photourl=url;
	}
	
	public void LoadAvatarImage() {
		if (avatarurl != null && !avatarurl.equals("")) {
			new AvatarImageLoadTask().execute(avatarurl);
		}
	}
	
	public void LoadPhotoImage() {
		if (photourl != null && !photourl.equals("")) {
			new PhotoImageLoadTask().execute(photourl);
		}
	}
	
	private class AvatarImageLoadTask extends AsyncTask<String, String, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				Log.i("AvatarImageLoadTask","Load Successfully");
				avatar=result;
			} else {
				Log.i("AvatarImageLoadTask","Load Fail");
				avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_avatar);
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			Log.i("AvatarImageLoadTask", "Loading Image ...");
			try {
				Bitmap b = Utilities.getBitmapFromURL(params[0]);
				return b;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	private class PhotoImageLoadTask extends AsyncTask<String, String, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				Log.i("PhotoImageLoadTask","Load Successfully");
				photo=result;
			} else {
				Log.i("PhotoImageLoadTask","Load Fail");
				photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available);
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			Log.i("PhotoImageLoadTask", "Loading Image ...");
			try {
				Bitmap b = Utilities.getBitmapFromURL(params[0]);
				return b;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
