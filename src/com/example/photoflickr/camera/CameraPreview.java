package com.example.photoflickr.camera;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = "CameraPreview";
	private SurfaceHolder mHolder;
	private Camera mCamera;
	public Boolean inPreview;
	public Boolean inCaptured=false;

	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
		super(context);
		if (camera == null)
			return;
		mCamera = camera;
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		Log.i(TAG, "surfaceChanged");
		if (mHolder.getSurface() == null || inCaptured) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			// Get Best PreviewSize
			Camera.Parameters parameters = mCamera.getParameters();
			Camera.Size size = CustomCamera.getBestPreviewSize(width, height, parameters);
			if (size != null) {
				Log.i(TAG,"Camera width: " + size.width + ", height: " + size.height);
				parameters.setPreviewSize(size.width, size.height);
				mCamera.setParameters(parameters);
				mHolder.setFixedSize(size.width, size.height);
				mCamera.startPreview();
				inPreview = true;
			}
		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		Log.i(TAG, "surfaceCreated");
		try {
			if (!inCaptured)
				mCamera.setPreviewDisplay(holder);
			// mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	public void SetCamera(Camera camera) {
		this.mCamera=camera;
	}

}
