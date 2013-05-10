package com.example.photoflickr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ResultItem {
	private String avatarurl;
	private String photoid;
	private String photosecret;
	private String photourl;
	private String username;
	private String fullname;
	private String location;
	private String postdate;
	private String userId; 
	private String title;
	private String description;
	private PhotoArrayAdapter adapter;
	private Context context;
	private Bitmap avatar;
	private Bitmap photo;
	private String viewcount;
	public boolean isLoaded;
		
	public ResultItem(Context context, PhotoArrayAdapter adapter){
		this.avatarurl="";
		this.photourl="";
		this.username="";
		this.postdate="";
		this.adapter = adapter;
		this.context = context;
		this.photo=null;
		this.avatar=null;
		isLoaded=false;
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
	
	public String getPostDate() {
		return postdate;
	}
	public void setPostDate(String value) {
		postdate=value;
	}	

	public String getFullname() {
		return fullname;
	}
	public void setFullname(String value) {
		fullname=value;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String value) {
		location=value;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String value) {
		title=value;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String value) {
		description=value;
	}

	public String getViewCount() {
		return viewcount;
	}
	public void setViewCount(String value) {
		viewcount=value;
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
	public String getPhotoId() {
		return photoid;
	}
	public void setPhotoId(String photoid) {
		this.photoid=photoid;
	}
	public String getPhotoSecret() {
		return photosecret;
	}
	public void setPhotoSecret(String photosecret) {
		this.photosecret=photoid;
	}
	public String getPhotoUrl() {
		return photourl;
	}
	public void setPhotoUrl(String url) {
		photourl=url;
	}
	
	/*public void LoadAvatarImage() {
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
	}*/
}
