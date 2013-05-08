package com.example.photoflickr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class SearchApiCall extends AsyncTask<String, String, Boolean> {
	List<ResultItem> list;
	PhotoArrayAdapter adapter;
	List<ResultItem> listTemp;
	private ProgressDialog dialog;
	private Context context;

	public SearchApiCall(Context context, List<ResultItem> list,
			PhotoArrayAdapter adapter) {
		this.list = list;
		this.adapter = adapter;
		this.dialog = new ProgressDialog(context);
		this.context = context;
		listTemp = new ArrayList<ResultItem>();
	}

	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			for (int i = 0; i < listTemp.size(); i++) {
				new LoadDetailResultTask().execute(listTemp.get(i).getUserid(),
						listTemp.get(i).getPhotoUrl());
			}
		}
		if (this.dialog.isShowing())
			this.dialog.dismiss();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			JSONObject result = Utilities.getJSONfromURL(params[0]);
			if (result != null) {
				Log.i("SearchApiCall", "Call Successfully");
				JSONObject photos = result.getJSONObject("photos");
				if (photos != null) {
					int page = photos.getInt("page");
					int pages = photos.getInt("pages");
					if (photos.getString("total")=="null") return false;
					int total = photos.getInt("total");
					JSONArray photo = photos.getJSONArray("photo");

					for (int i = 0; i < photo.length(); i++) {
						ResultItem item = new ResultItem(context, adapter);
						JSONObject e = photo.getJSONObject(i);

						// Lay dia chi url cua anh
						int farm_id = e.getInt("farm");
						String server_id = e.getString("server");
						String id = e.getString("id");
						String secret = e.getString("secret");
						String photourl = "http://farm" + farm_id
								+ ".staticflickr.com/" + server_id + "/" + id
								+ "_" + secret + "_t.jpg";

						// Lay thong tin nguoi post anh
						String owner = e.getString("owner");
						item.setUserid(owner);
						item.setPhotoUrl(photourl);
						listTemp.add(item);
					}
					return true;
				}
			}
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private class LoadDetailResultTask extends
			AsyncTask<String, String, ResultItem> {

		@Override
		protected void onPostExecute(ResultItem result) {
			if (result != null) {
				result.LoadAvatarImage();
				result.LoadPhotoImage();
				list.add(result);
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		protected ResultItem doInBackground(String... params) {
			try {
				ResultItem item = new ResultItem(context, adapter);
				String owner = params[0];
				JSONObject jsonowner = Utilities
						.getJSONfromURL("http://api.flickr.com/services/rest/?method=flickr.people.getInfo&api_key=63a95e8826699c2e7f401a3288bf20cf&user_id="
								+ owner + "&format=json&nojsoncallback=1");
				JSONObject person = jsonowner.getJSONObject("person");
				String username = person.getJSONObject("username").getString(
						"_content");
				int icon_farm = person.getInt("iconfarm");
				int icon_server = person.getInt("iconserver");
				String avatarurl = "http://farm" + icon_farm
						+ ".staticflickr.com/" + icon_server + "/buddyicons/"
						+ owner + ".jpg";
				item.setPhotoUrl(params[1]);
				item.setUsername(username);
				item.setAvatarUrl(avatarurl);
				return item;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

	}
}
