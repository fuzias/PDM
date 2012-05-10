package isel.pdm.twitter;

import java.util.List;

import twitter4j.Status;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TimelineAdapter extends ArrayAdapter<Status> implements
		ListAdapter {
	private Context context;
	private int messageLength;
	private static final String TAG = "TimelineAdapter";
	private List<Status> statuses;

	private static class Holder {
		private TextView createAtTextView;
		private TextView userTextView;
		private TextView messageTextView;
	}

	public TimelineAdapter(Context context, List<Status> objects,
			int messageLength) {
		super(context, R.layout.message, R.id.listTimeline, objects);
		Log.d(TAG, "TimelineAdapter1");
		Log.d(TAG, "--" + (objects == null));
		this.context = context;
		this.messageLength = messageLength;
		this.statuses = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView");
		View rowView = convertView;
		if (rowView == null) {
			rowView = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.message, parent, false);
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

	public int getCount() {
		return statuses.size();
	}

	public Status getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.statuses.get(arg0);
	}

	public long getItemId(int arg0) {
		return statuses.get(arg0).getCreatedAt().getTime();
	}

	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getViewTypeCount() {
		return getCount();
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return statuses.size() > 0;
	}

	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled(int arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
