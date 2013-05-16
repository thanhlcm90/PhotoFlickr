/**
 * 
 */
package com.example.photoflickr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.photoflickr.ui.CameraActivity;
import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.uploader.UploadMetaData;

public class UploadPhotoTask extends AsyncTask<OAuth, Void, String> {
	/**
	 * 
	 */
	private final static String TAG="UploadPhotoTask";
	private final CameraActivity flickrjAndroidSampleActivity;
	private byte[] file;
	private UploadMetaData uploadMetaData;

	// private final Logger logger = LoggerFactory
	// .getLogger(UploadPhotoTask.class);

	public UploadPhotoTask(CameraActivity flickrjAndroidSampleActivity,
			byte[] file, UploadMetaData uploadMetaData) {
		this.flickrjAndroidSampleActivity = flickrjAndroidSampleActivity;
		this.file = file;
		this.uploadMetaData=uploadMetaData;
	}

	/**
	 * The progress dialog before going to the browser.
	 */
	private ProgressDialog mProgressDialog;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(flickrjAndroidSampleActivity,
				"", "Uploading..."); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				UploadPhotoTask.this.cancel(true);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(OAuth... params) {
		OAuth oauth = params[0];
		OAuthToken token = oauth.getToken();

		try {
			Flickr f = FlickrHelper.getInstance().getFlickrAuthed(
					token.getOauthToken(), token.getOauthTokenSecret());
			uploadMetaData.setPublicFlag(true);
			uploadMetaData.setContentType("7");
			return f.getUploader().upload(uploadMetaData.getTitle(),
					file, uploadMetaData);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String response) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

		if (monUploadDone != null) {
			monUploadDone.onComplete();
		}

		if (response!=null) {
		Toast.makeText(flickrjAndroidSampleActivity.getApplicationContext(),
				"Upload success.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(flickrjAndroidSampleActivity.getApplicationContext(),
					"Upload fail.", Toast.LENGTH_SHORT).show();	
		}

	}

	onUploadDone monUploadDone;

	public void setOnUploadDone(onUploadDone monUploadDone) {
		this.monUploadDone = monUploadDone;
	}

	public interface onUploadDone {
		void onComplete();
	}

}