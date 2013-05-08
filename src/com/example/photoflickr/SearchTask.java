package com.example.photoflickr;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class SearchTask extends AsyncTask<String, String, Boolean> {
	List<ResultItem> list;
	PhotoArrayAdapter adapter;
	List<ResultItem> listTemp;
	private ProgressDialog dialog;
	private Context context;
	private ListView listview;

	public SearchTask(ListView listview, List<ResultItem> list,
			PhotoArrayAdapter adapter) {
		this.list = list;
		this.adapter = adapter;
		this.listview = listview;
		this.context = listview.getContext();
		this.dialog = new ProgressDialog(this.context);
		listTemp = new ArrayList<ResultItem>();
	}

	@Override
	protected void onPreExecute() {
		MainActivity.loadingMore = true;
		this.dialog.setMessage("Seaching...");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			for (int i = 0; i < listTemp.size(); i++) {
				ResultItem item = listTemp.get(i);
				item.LoadAvatarImage();
				item.LoadPhotoImage();
				list.add(item);
			}
			adapter.notifyDataSetChanged();
			listview.setSelectionFromTop(MainActivity.currentFirstVisibleItem,
					0);
		}
		if (this.dialog.isShowing())
			this.dialog.dismiss();
		MainActivity.loadingMore = false;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			JSONObject result = Utilities.getJSONfromURL(params[0]);
			if (result != null) {
				Log.i("SearchApiCall", "Call Successfully");
				JSONObject photos = result.getJSONObject("photos");
				// int page = photos.getInt("page");
				// int pages = photos.getInt("pages");
				// int total = photos.getInt("total");
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
							+ ".staticflickr.com/" + server_id + "/" + id + "_"
							+ secret + "_t.jpg";

					// Lay thong tin nguoi post anh
					String owner = e.getString("owner");
					JSONObject json = Utilities
							.getJSONfromURL("http://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=63a95e8826699c2e7f401a3288bf20cf&photo_id="
									+ id
									+ "&secret="
									+ secret
									+ "&format=json&nojsoncallback=1");
					JSONObject photoInfo = json.getJSONObject("photo");
					JSONObject person = photoInfo.getJSONObject("owner");
					String username = person.getString("username");
					String fullname = person.getString("realname");
					String location = person.getString("location");
					int icon_farm = person.getInt("iconfarm");
					int icon_server = person.getInt("iconserver");
					String avatarurl = "http://farm" + icon_farm
							+ ".staticflickr.com/" + icon_server
							+ "/buddyicons/" + owner + ".jpg";

					JSONObject dates = photoInfo.getJSONObject("dates");
					String takendate = dates.getString("taken");
					String title = photoInfo.getJSONObject("title").getString(
							"_content");
					String description = photoInfo.getJSONObject("description")
							.getString("_content");
					item.setPostDate(takendate);
					item.setUsername(username);
					item.setFullname(fullname);
					item.setLocation(location);
					item.setTitle(title);
					item.setDescription(description);
					item.setAvatarUrl(avatarurl);
					item.setPhotoId(id);
					item.setPhotoUrl(photourl);
					listTemp.add(item);
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
