package com.example.photoflickr;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.photoutil.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

public class LoadCommentTask extends AsyncTask<String, String, Boolean> {
	private final WeakReference<List<CommentItem>> listRef;
	CommentArrayAdapter adapter;
	private Context context;

	public LoadCommentTask(ListView listview, List<CommentItem> list,
			CommentArrayAdapter adapter) {
		this.listRef = new WeakReference<List<CommentItem>>(list);
		this.adapter = adapter;
		this.context = listview.getContext();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result && listRef != null) {
			/*
			 * List<CommentItem> list = listRef.get(); for (int i = 0; i <
			 * list.size(); i++) { CommentItem item = listTemp.get(i);
			 * //item.LoadAvatarImage(); list.add(item); }
			 */
			adapter.notifyDataSetChanged();
			// listview.setSelectionFromTop(0,0);
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			if (listRef != null) {
				List<CommentItem> list = listRef.get();
				String photoId = params[0];
				String url = "http://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=63a95e8826699c2e7f401a3288bf20cf&photo_id="
						+ photoId + "&format=json&nojsoncallback=1";
				JSONObject result = Utils.getJSONfromURL(url);
				if (result != null) {
					JSONObject comments = result.getJSONObject("comments");
					JSONArray commentArray = comments.getJSONArray("comment");
					for (int i = 0; i < commentArray.length(); i++) {
						CommentItem item = new CommentItem(context, adapter);
						JSONObject e = commentArray.getJSONObject(i);
						String owner = e.getString("author");
						String authorname = e.getString("authorname");
						String icon_server = e.getString("iconserver");
						int icon_farm = e.getInt("iconfarm");
						String avatarurl = "http://farm" + icon_farm
								+ ".staticflickr.com/" + icon_server
								+ "/buddyicons/" + owner + ".jpg";
						String comment = e.getString("_content");
						item.setUserid(owner);
						item.setUsername(authorname);
						item.setAvatarUrl(avatarurl);
						item.setComment(comment);
						list.add(item);
					}
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
