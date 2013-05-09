package com.example.photoflickr;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentArrayAdapter extends ArrayAdapter<CommentItem> {
	private final List<CommentItem> values;
	private LayoutInflater inflater;

	static class ViewHolder {
		public TextView username;
		public ImageView avatar;
		public TextView comment;
	}

	public CommentArrayAdapter(Context context, List<CommentItem> objects) {
		super(context, R.layout.comment_item, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.values = objects;
	}

	public int getCount() {
		return values.size();
	}

	public CommentItem getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			View rowView = inflater.inflate(R.layout.comment_item, parent, false);
			holder = new ViewHolder();
			holder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
			holder.username = (TextView) rowView.findViewById(R.id.username);
			holder.comment = (TextView) rowView.findViewById(R.id.comment);
			convertView = rowView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CommentItem item = values.get(position);

		if (item != null) {
			holder.username.setText(item.getUsername());
			holder.comment.setText(item.getComment());
			MainActivity.imageDownloader.download(item.getAvatarUrl(), holder.avatar);
			//holder.avatar.setImageBitmap(item.getAvatarImage());
		}
		return convertView;
	}

}
