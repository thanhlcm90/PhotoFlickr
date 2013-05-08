package com.example.photoflickr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class CommentItem {
	private String avatarurl;
	private String username;
	private String comment;
	private String userId;
	private CommentArrayAdapter adapter;
	private Context context;
	private Bitmap avatar;
	
	public CommentItem(Context context, CommentArrayAdapter adapter){
		this.avatarurl="";
		this.username="";
		this.comment="";
		this.adapter = adapter;
		this.context = context;
		this.avatar=null;
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
	
	public String getComment() {
		return comment;
	}
	public void setComment(String value) {
		comment=value;
	}	

	public CommentArrayAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(CommentArrayAdapter adapter) {
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

	public void LoadAvatarImage() {
		if (avatarurl != null && !avatarurl.equals("")) {
			new AvatarImageLoadTask().execute(avatarurl);
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
}
