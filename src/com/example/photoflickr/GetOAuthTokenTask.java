/**
 * 
 */
package com.example.photoflickr;

import android.os.AsyncTask;

import com.example.photoflickr.ui.CameraActivity;
import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class GetOAuthTokenTask extends AsyncTask<String, Integer, OAuth> {

	private CameraActivity activity;

	public GetOAuthTokenTask(CameraActivity context) {
		this.activity = context;
	}

	@Override
	protected OAuth doInBackground(String... params) {
		String oauthToken = params[0];
		String oauthTokenSecret = params[1];
		String verifier = params[2];

		Flickr f = FlickrHelper.getInstance().getFlickr();
		OAuthInterface oauthApi = f.getOAuthInterface();
		try {
			return oauthApi.getAccessToken(oauthToken, oauthTokenSecret,
					verifier);
		} catch (Exception e) {
			// logger.error(e.getLocalizedMessage(), e);
			return null;
		}

	}

	@Override
	protected void onPostExecute(OAuth result) {
		if (activity != null) {
			activity.onOAuthDone(result);
		}
	}

}