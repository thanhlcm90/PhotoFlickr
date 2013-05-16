package com.example.photoflickr.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.holoeverywhere.app.ProgressDialog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SavePhotoTask extends AsyncTask<byte[], String, Boolean> {
	private static final String TAG = "SavePhotoTask";
	private Context context;
	private org.holoeverywhere.app.ProgressDialog dialog;
	
	public SavePhotoTask(Context context) {
		this.context=context;
	}
	
	@Override
	protected void onPreExecute() {
		dialog=ProgressDialog.show(context,"","Saving Photo");
	}

	@Override
	protected Boolean doInBackground(byte[]... params) {
		File pictureFile = CustomCamera
				.getOutputMediaFile(CustomCamera.MEDIA_TYPE_IMAGE);
		if (pictureFile == null) {
			Log.d(TAG,
					"Error creating media file, check storage permissions");
			return false;
		}

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(params[0]);
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
			return false;
		}
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onPostExecute(Boolean result) {
		if (result)
			org.holoeverywhere.widget.Toast.makeText(context, "Saved successful.", org.holoeverywhere.widget.Toast.LENGTH_SHORT).show();
		else
			org.holoeverywhere.widget.Toast.makeText(context, "Saved unsuccessful.", org.holoeverywhere.widget.Toast.LENGTH_SHORT).show();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	

}
