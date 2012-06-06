package isel.pdm.twitter;

import java.util.List;

import twitter4j.Status;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimelineAdapter extends ArrayAdapter<Status>{
	private int messageLength;
	private static final String TAG = "TimelineAdapter";

	private static class Holder {
		private TextView createAtTextView;
		private TextView userTextView;
		private TextView messageTextView;
	}

	public void addNewElements(List<Status> tweets) {
		for (int i = tweets.size() - 1; i >= 0; --i) {
			this.insert(tweets.get(i), 0);
		}
		this.notifyDataSetChanged();
	}

	public TimelineAdapter(Context context, List<Status> objects,
			int messageLength) {
		super(context, R.layout.message, R.id.listTimeline, objects);
		Log.d(TAG, "TimelineAdapter1");
		Log.d(TAG, "--" + (objects == null));
		this.messageLength = messageLength;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView");
		View rowView = convertView;
		if (rowView == null) {
			rowView = ((LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.message,
					parent, false);
			Holder holder = new Holder();
			holder.createAtTextView = (TextView) rowView
					.findViewById(R.id.messageCreatedAt);
			holder.userTextView = (TextView) rowView
					.findViewById(R.id.messageUser);
			holder.messageTextView = (TextView) rowView
					.findViewById(R.id.messageUser);
			rowView.setTag(holder);
		}

		Status status = (Status) this.getItem(position);
		Holder holder = (Holder) rowView.getTag();
		holder.createAtTextView.setText(DateUtils
				.getRelativeTimeSpanString(status.getCreatedAt().getTime()));
		holder.userTextView.setText(status.getUser().getScreenName());
		String message = status.getText();
		holder.messageTextView
				.setText(message.length() <= messageLength ? message : message
						.substring(0, messageLength - 1));
		return rowView;
	}

}
