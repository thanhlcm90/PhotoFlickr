package com.example.photoflickr;

import java.lang.ref.WeakReference;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.photoflickr.ui.MainFragement;
import com.example.photoutil.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class SearchTask extends AsyncTask<String, String, Boolean> {
	private final WeakReference<List<ResultItem>> listRef;
	PhotoArrayAdapter adapter;
	private ProgressDialog dialog;
	private Context context;
	private ListView listview;

	public SearchTask(ListView listview, List<ResultItem> list,
			PhotoArrayAdapter adapter) {
		this.listRef = new WeakReference<List<ResultItem>>(list);
		this.adapter = adapter;
		this.listview = listview;
		this.context = listview.getContext();
		this.dialog = new ProgressDialog(this.context);
	}

	@Override
	protected void onPreExecute() {
		MainFragement.loadingMore = true;
		this.dialog.setMessage("Seaching...");
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result && listRef != null) {
			List<ResultItem> list = listRef.get();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					ResultItem item = list.get(i);
					//item.LoadPhotoImage();
					new GetDetailTask(item).execute("");
				}
				adapter.notifyDataSetChanged();
				listview.setSelectionFromTop(
						MainFragement.currentFirstVisibleItem, 0);
			}
		}
		if (this.dialog.isShowing())
			this.dialog.dismiss();
		MainFragement.loadingMore = false;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			if (listRef != null) {
				List<ResultItem> list = listRef.get();
				JSONObject result = Utils.getJSONfromURL(params[0]);
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
								+ ".staticflickr.com/" + server_id + "/" + id
								+ "_" + secret + "_m.jpg";

						// Lay thong tin nguoi post anh
						String owner = e.getString("owner");
						item.setPhotoSecret(secret);
						item.setUserid(owner);
						item.setPhotoId(id);
						item.setPhotoUrl(photourl);
						list.add(item);
					}
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private class GetDetailTask extends AsyncTask<String, String, Boolean> {
		ResultItem item;

		public GetDetailTask(ResultItem item) {
			this.item = item;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				JSONObject json = Utils
						.getJSONfromURL("http://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=63a95e8826699c2e7f401a3288bf20cf&photo_id="
								+ item.getPhotoId()
								+ "&secret="
								+ item.getPhotoSecret()
								+ "&format=json&nojsoncallback=1");
				JSONObject photoInfo = json.getJSONObject("photo");
				JSONObject person = photoInfo.getJSONObject("owner");
				String username = person.getString("username");
				String fullname = person.getString("realname");
				String location = person.getString("location");
				int icon_farm = person.getInt("iconfarm");
				int icon_server = person.getInt("iconserver");
				String avatarurl = "http://farm" + icon_farm
						+ ".staticflickr.com/" + icon_server + "/buddyicons/"
						+ item.getUserid() + ".jpg";

				JSONObject dates = photoInfo.getJSONObject("dates");
				String takendate = dates.getString("taken");
				String title = photoInfo.getJSONObject("title").getString(
						"_content");
				String description = photoInfo.getJSONObject("description")
						.getString("_content");

				// Xu ly lai ngay than theo dinh dang dd/MM/yyyy HH:mm:ss
				if (takendate != null && !takendate.equals("")) {
					String[] datetimes = takendate.split(" ");
					String[] date = datetimes[0].split("-");
					takendate=date[2] + "/" + date[1] + "/" + date[0] + " " + datetimes[1];
				}
				item.setPostDate(takendate);
				item.setUsername(username);
				item.setFullname(fullname);
				item.setLocation(location);
				item.setTitle(title);
				item.setDescription(description);
				item.setAvatarUrl(avatarurl);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				//item.LoadAvatarImage();
				adapter.notifyDataSetChanged();
			}

		}
	}
}
