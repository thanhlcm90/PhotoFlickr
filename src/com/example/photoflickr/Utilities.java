package com.example.photoflickr;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class Utilities {
	

	public static JSONObject getJSONfromURL(String url){

		//initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		//http post
		final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    final HttpPost getRequest = new HttpPost(url);
		try{
		    
		    HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.e("getJSONfromURL", "Error " + statusCode + " while retrieving bitmap from " + url); 
	            return null;
	        }
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	        	is = entity.getContent();
	        } else {
	        	return null;
	        }
		}catch(Exception e){
			Log.e("getJSONfromURL", "Error in http connection "+e.toString());
			return null;
		}

		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("getJSONfromURL", "Error converting result "+e.toString());
			return null;
		} finally {
	        if (client != null) {
	            client.close();
	        }
	    }

		//try parse the string to a JSON object
		try{
	        	jArray = new JSONObject(result);
		}catch(JSONException e){
			Log.e("getJSONfromURL", "Error parsing data "+e.toString());
			return null;
		}

		return jArray;
	} 
}
