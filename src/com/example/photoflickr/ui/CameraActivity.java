package com.example.photoflickr.ui;

import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.photoflickr.GetOAuthTokenTask;
import com.example.photoflickr.OAuthTask;
import com.example.photoflickr.R;
import com.example.photoflickr.UploadPhotoTask;
import com.example.photoflickr.camera.CameraPreview;
import com.example.photoflickr.camera.CustomCamera;
import com.example.photoflickr.ui.PhotoUploadDialog.PhotoUploadDialogListener;
import com.example.photoflickr.util.SystemUiHider;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.people.User;
import com.gmail.yuyang226.flickr.uploader.UploadMetaData;

public class CameraActivity extends SherlockFragmentActivity implements
		PhotoUploadDialogListener {
	private static final String TAG = "CameraActivity";
	public static final String CALLBACK_SCHEME = "photoflickr-android-sample-oauth"; //$NON-NLS-1$
	public static final String PREFS_NAME = "photoflickr-android-sample-pref"; //$NON-NLS-1$
	public static final String KEY_OAUTH_TOKEN = "photoflickr-android-oauthToken"; //$NON-NLS-1$
	public static final String KEY_TOKEN_SECRET = "photoflickr-android-tokenSecret"; //$NON-NLS-1$
	public static final String KEY_USER_NAME = "photoflickr-android-userName"; //$NON-NLS-1$
	public static final String KEY_USER_ID = "photoflickr-android-userId"; //$NON-NLS-1$
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_FULLSCREEN;
	private Camera mCamera;
	private CameraPreview mPreview;
	private byte[] uploadData;
	private String photoTitle, photoDescription;
	private PhotoUploadDialog dialog;
	private Boolean captured = false;
	private Button uploadButton, captureButton, reCaptureButton;
	private SystemUiHider mSystemUiHider;
	private int iOrientation;

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			camera.stopPreview();
			// new SavePhotoTask(CameraActivity.this).execute(data);
			uploadData = data;
			captured = true;
			camera.release();
			camera = null;
			uploadButton.setEnabled(true);
			captureButton.setVisibility(View.INVISIBLE);
			reCaptureButton.setVisibility(View.VISIBLE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		// Show the Up button in the action bar.
		setupActionBar();
		Log.i(TAG, "onCreate");
		/*
		 * if (!CustomCamera.checkCameraHardware(this)) {
		 * org.holoeverywhere.widget.Toast.makeText(this, "Camera not support",
		 * org.holoeverywhere.widget.Toast.LENGTH_SHORT).show(); return; }
		 */

		// Create an instance of Camera
		mCamera = CustomCamera.OpenCamera(0);
		if (mCamera == null)
			mCamera = CustomCamera.OpenCamera(1);
		if (mCamera == null) {
			org.holoeverywhere.widget.Toast.makeText(this,
					"Camera not support",
					org.holoeverywhere.widget.Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		LayoutParams previewLayout = (LayoutParams) preview.getLayoutParams();
		int width = preview.getWidth();
		int height = preview.getHeight();
		Log.i(TAG, "width: " + width + ", height: " + height);
		Camera.Parameters parameters = mCamera.getParameters();
		Size size = CustomCamera.getBestPreviewSize(width, height, parameters);
		if (size != null) {
			previewLayout.width = size.width;
			previewLayout.height = size.height;
			preview.setLayoutParams(previewLayout);
		}
		preview.addView(mPreview);

		// Add a listener to the Capture button
		captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mPreview.inPreview = false;
				mCamera.takePicture(shutterCallback, rawCallback, mPicture);
			}
		});

		// Add a listener to the Upload button
		uploadButton = (Button) findViewById(R.id.button_upload);
		uploadButton.setEnabled(false);
		uploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new PhotoUploadDialog();
				dialog.show(getSupportFragmentManager(), "PhotoUploadDialog");
			}
		});

		// Add a listener to the Recapture button
		reCaptureButton = (Button) findViewById(R.id.button_recapture);
		reCaptureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captured = false;
				captureButton.setVisibility(View.VISIBLE);
				reCaptureButton.setVisibility(View.INVISIBLE);

				// Open Camera Again
				mCamera = CustomCamera.OpenCamera(0);
				if (mCamera == null)
					mCamera = CustomCamera.OpenCamera(1);

				mPreview = new CameraPreview(CameraActivity.this, mCamera);
				preview.removeAllViews();
				preview.addView(mPreview);

			}
		});

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		/*mSystemUiHider = SystemUiHider.getInstance(this, preview, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();*/

		final RelativeLayout groupButton = (RelativeLayout)findViewById(R.id.button_capture_group);
		// Rotation Sensor
		OrientationEventListener myOrientationEventListener;
		myOrientationEventListener = new OrientationEventListener(this,
				SensorManager.SENSOR_DELAY_NORMAL) {

			@Override
			public void onOrientationChanged(int iAngle) { // 0 15 30 45 60 75,
															// 90 105, 120, 135,
															// 150, 165, 180,
															// 195, 210, 225,
															// 240, 255, 270,
															// 285, 300, 315,
															// 330, 345
				final int iLookup[] = { 0, 0, 0, 90, 90, 90, 90, 90, 90, 180,
						180, 180, 180, 180, 180, 270, 270, 270, 270, 270, 270,
						0, 0, 0 }; // 15-degree increments
				final int iButtonDegress[] = { 0, 0, 0, 270, 270, 270, 270, 270, 270, -180,
						-180, -180, -180, -180, -180, 90, 90, 90, 90, 90, 90,
						0, 0, 0 }; // 15-degree increments
				if (iAngle != ORIENTATION_UNKNOWN) {
					int iNewOrientation = iLookup[iAngle / 15];
					if (iOrientation != iNewOrientation) {
						iOrientation = iNewOrientation;
						Log.i(TAG, "Change orientation: " + iLookup[iAngle/15]);
						int width = groupButton.getWidth();
						int height = groupButton.getWidth();
						RotateAnimation a = new RotateAnimation(iButtonDegress[iAngle/15], iButtonDegress[iAngle/15],width/2,height/2);
						a.setFillAfter(true);
						a.setDuration(0);
						groupButton.startAnimation(a);
					}
				}
			}
		};

		// To display if orientation detection will work and enable it
		if (myOrientationEventListener.canDetectOrientation()) {
			myOrientationEventListener.enable();
		} else {
			Toast.makeText(this, "Can't DetectOrientation", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().hide();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		if (!captured) {
			if (mCamera == null)
				mCamera = CustomCamera.OpenCamera(0);
			if (mCamera == null)
				mCamera = CustomCamera.OpenCamera(1);
			if (mPreview == null && mCamera != null)
				mPreview = new CameraPreview(this, mCamera);
		}
		mPreview.inCaptured = captured;

		Intent intent = getIntent();
		String scheme = intent.getScheme();
		OAuth savedToken = getOAuthToken();

		if (CALLBACK_SCHEME.equals(scheme)
				&& (savedToken == null || savedToken.getUser() == null)) {
			Uri uri = intent.getData();
			String query = uri.getQuery();
			Log.i(TAG, "Returned Query: " + query); //$NON-NLS-1$
			String[] data = query.split("&"); //$NON-NLS-1$
			if (data != null && data.length == 2) {
				String oauthToken = data[0].substring(data[0].indexOf("=") + 1); //$NON-NLS-1$
				String oauthVerifier = data[1]
						.substring(data[1].indexOf("=") + 1); //$NON-NLS-1$
				Log.i(TAG,
						"OAuth Token: " + oauthToken + ", OAuth Verifier: " + oauthVerifier); //$NON-NLS-1$

				OAuth oauth = getOAuthToken();
				if (oauth != null && oauth.getToken() != null
						&& oauth.getToken().getOauthTokenSecret() != null) {
					GetOAuthTokenTask task = new GetOAuthTokenTask(this);
					task.execute(oauthToken, oauth.getToken()
							.getOauthTokenSecret(), oauthVerifier);
				}
			}
		}
	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause");
		if (mCamera == null || mPreview == null)
			return;
		if (mPreview.inPreview) {
			mCamera.stopPreview();
		}
		mPreview.inPreview = false;
		mCamera.release();
		mCamera = null;
		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// this is very important, otherwise you would get a null Scheme in the
		// onResume later on.
		setIntent(intent);
	}

	Handler h = new Handler();
	Runnable init = new Runnable() {

		@Override
		public void run() {
			OAuth oauth = getOAuthToken();
			if (oauth == null || oauth.getUser() == null) {
				OAuthTask task = new OAuthTask(getContext());
				task.execute();
			} else {
				load(oauth);
			}
		}
	};

	public OAuth getOAuthToken() {
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
		String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
		if (oauthTokenString == null && tokenSecret == null) {
			//			logger.warn("No oauth token retrieved"); //$NON-NLS-1$
			return null;
		}
		OAuth oauth = new OAuth();
		String userName = settings.getString(KEY_USER_NAME, null);
		String userId = settings.getString(KEY_USER_ID, null);
		if (userId != null) {
			User user = new User();
			user.setUsername(userName);
			user.setId(userId);
			oauth.setUser(user);
		}
		OAuthToken oauthToken = new OAuthToken();
		oauth.setToken(oauthToken);
		oauthToken.setOauthToken(oauthTokenString);
		oauthToken.setOauthTokenSecret(tokenSecret);
		// logger.debug(
		//				"Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret); //$NON-NLS-1$
		return oauth;
	}

	private void load(OAuth oauth) {
		if (oauth != null) {
			UploadMetaData uploadMetaData = new UploadMetaData();
			Log.i(TAG, "photoTitle:" + photoTitle);
			uploadMetaData.setTitle(photoTitle);
			uploadMetaData.setDescription(photoDescription);
			UploadPhotoTask taskUpload = new UploadPhotoTask(this, uploadData,
					uploadMetaData);
			taskUpload.setOnUploadDone(new UploadPhotoTask.onUploadDone() {

				@Override
				public void onComplete() {
					finish();
				}
			});

			taskUpload.execute(oauth);
		}
	}

	public void saveOAuthToken(String userName, String userId, String token,
			String tokenSecret) {
		// logger.debug(
		//				"Saving userName=%s, userId=%s, oauth token={}, and token secret={}", new String[] { userName, userId, token, tokenSecret }); //$NON-NLS-1$
		SharedPreferences sp = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(KEY_OAUTH_TOKEN, token);
		editor.putString(KEY_TOKEN_SECRET, tokenSecret);
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
	}

	public void onOAuthDone(OAuth result) {
		if (result == null) {
			Toast.makeText(this, "Authorization failed", //$NON-NLS-1$
					Toast.LENGTH_LONG).show();
		} else {
			User user = result.getUser();
			OAuthToken token = result.getToken();
			if (user == null || user.getId() == null || token == null
					|| token.getOauthToken() == null
					|| token.getOauthTokenSecret() == null) {
				Toast.makeText(this, "Authorization failed", //$NON-NLS-1$
						Toast.LENGTH_LONG).show();
				return;
			}
			String message = String
					.format(Locale.US,
							"Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s", //$NON-NLS-1$
							user.getUsername(), user.getId(),
							token.getOauthToken(), token.getOauthTokenSecret());
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			saveOAuthToken(user.getUsername(), user.getId(),
					token.getOauthToken(), token.getOauthTokenSecret());
			load(result);
		}
	}

	private Context getContext() {
		return this;

	}

	@Override
	public void onDialogPositiveClick(SherlockDialogFragment dialog) {
		photoTitle = ((PhotoUploadDialog) dialog).GetPhotoTitle();
		photoDescription = ((PhotoUploadDialog) dialog).GetPhotoDescription();
		Log.i(TAG, "PhotoTitle:" + photoTitle);
		new Thread() {
			public void run() {
				h.post(init);
			};
		}.start();
	}

	@Override
	public void onDialogNegativeClick(SherlockDialogFragment dialog) {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
		outState.putString("PhotoTitle", photoTitle);
		outState.putString("PhotoDescription", photoDescription);
		outState.putByteArray("UploadData", uploadData);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		Log.i(TAG, "onRestoreInstanceState");
		photoTitle = savedInstanceState.getString("PhotoTitle");
		photoDescription = savedInstanceState.getString("PhotoDescription");
		uploadData = savedInstanceState.getByteArray("UploadData");
	}
}
