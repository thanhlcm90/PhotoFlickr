package com.example.photoflickr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	ImageView img_photo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		Intent intent = getIntent();
		String username= intent.getStringExtra(MainActivity.EXTRA_ITEM_USERNAME);
		String fullname= intent.getStringExtra(MainActivity.EXTRA_ITEM_FULLNAME);
		String location= intent.getStringExtra(MainActivity.EXTRA_ITEM_LOCATION);
		String postdate= intent.getStringExtra(MainActivity.EXTRA_ITEM_POSTDATE);
		String title= intent.getStringExtra(MainActivity.EXTRA_ITEM_TITLE);
		String description= intent.getStringExtra(MainActivity.EXTRA_ITEM_DESCRIPTION);
		Bitmap avatar = (Bitmap) intent.getParcelableExtra(MainActivity.EXTRA_ITEM_AVATAR );
		String photo = intent.getStringExtra(MainActivity.EXTRA_ITEM_PHOTO_URL);
		// Xu ly lai URL, get Anh lon (240pX)
		photo=photo.replace("_t.jpg", "_m.jpg");
		
		TextView tv_username = (TextView) findViewById(R.id.username);
		TextView tv_fullname = (TextView) findViewById(R.id.fullname);
		TextView tv_location = (TextView) findViewById(R.id.location);
		TextView tv_postdate = (TextView) findViewById(R.id.postdate);
		TextView tv_description = (TextView) findViewById(R.id.description);
		ImageView img_avatar =(ImageView) findViewById(R.id.avatar);
		img_photo = (ImageView) findViewById(R.id.photo);
		
		tv_username.setText(username);
		tv_fullname.setText(fullname);
		tv_location.setText(location);
		tv_postdate.setText(postdate);
		//tv_title.setText(title);
		setTitle(title);
		tv_description.setText(description);
		img_avatar.setImageBitmap(avatar);
		
		new LoadPhotoTask(this).execute(photo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}
	
	private class LoadPhotoTask extends AsyncTask<String, String, Bitmap> {
		private Context context;
		public LoadPhotoTask(Context context) {
			this.context=context;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result!=null) {
				img_photo.setImageBitmap(result);
			} else {
				img_photo.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_available));
			}
		}

		@Override
		protected Bitmap doInBackground(String... params) {
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
