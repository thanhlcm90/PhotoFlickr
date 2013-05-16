package com.example.photoflickr.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

@SuppressLint("NewApi")
public class CustomCamera {
	private static final String TAG="CustomCamera";
	/** Check if this device has a camera */
	@SuppressLint("NewApi")
	public static boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	/**
	 * Mo Camera
	 * @return
	 */
	public static Camera OpenCamera(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        Log.i(TAG,"Camera is not available");
	    }
	    return c; // returns null if camera is unavailable
	}
	
	/** Mo Camera
	 * @param cameraId : Id Camera (Camera truoc hay sau)
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Camera OpenCamera(int cameraId){
	    Camera c = null;
	    try {
	        c = Camera.open(cameraId); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        Log.i(TAG,"Camera is not available");
	    }
	    return c; // returns null if camera is unavailable
	}
		
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	@SuppressLint("SimpleDateFormat")
	public static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir;
	    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "PhotoFlickr");
		
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("getOutputMediaFile", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@SuppressLint("NewApi")
	public static Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return (result);
	}
}
