package com.example.photoflickr;

import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoArrayAdapter extends ArrayAdapter<ResultItem> {
	private final Context context;
	private final List<ResultItem> values;
	private LayoutInflater inflater;

	static class ViewHolder {
		public TextView username;
		public TextView postdate;
		public ImageView avatar;
		public ImageView photo;
	}

	public PhotoArrayAdapter(Context context, List<ResultItem> objects) {
		super(context, R.layout.list_item, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.values = objects;
	}

	public int getCount() {
		return values.size();
	}

	public ResultItem getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			View rowView = inflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder();
			holder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
			holder.username = (TextView) rowView.findViewById(R.id.username);
			holder.postdate = (TextView) rowView.findViewById(R.id.postdate);
			holder.photo = (ImageView) rowView.findViewById(R.id.photo);
			convertView=rowView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ResultItem item = values.get(position);

		if (item != null) {
			holder.username.setText(item.getUsername());
			if (item.getPostDate() != null) {
				holder.postdate.setText(DateFormat.format("dd/MM/yyyy",
						item.getPostDate()));
			}
				holder.avatar.setImageBitmap(item.getAvatarImage());
				holder.photo.setImageBitmap(item.getPhotoImage());
		}
		return convertView;
	}

}
