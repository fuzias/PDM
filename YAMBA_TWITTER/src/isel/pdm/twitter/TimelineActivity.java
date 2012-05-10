package isel.pdm.twitter;

import twitter4j.Status;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TimelineActivity extends BaseActivity implements
		OnItemClickListener {
	private static final String TAG = "TimelineActivity";
	private ListView listViewTimelime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		Log.d(TAG, "onCreate2");
		listViewTimelime = (ListView) findViewById(R.id.listTimeline);
		Log.d(TAG, "onCreate3");
		if (twitterApp.userPreferences.getString("username", null) == null)
			startActivityForResult(new Intent(this, UserPrefActivity.class), 1);
		//listViewTimelime.setOnClickListener(this);

		((TwitterApp) getApplication()).getTimeline();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		updateTimeline();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
		Log.d(TAG, "onItemClick");
		Status status = twitterApp.adapter.getItem(pos);
		/*
		 * Toast.makeText( this, status.getUser().getScreenName() + ": \n" +
		 * status.getText() + "\n" + DateUtils.getRelativeTimeSpanString(status
		 * .getCreatedAt().getTime()), Toast.LENGTH_LONG);
		 */
		// TODO Start DetailActivity
		Intent detailActivity = new Intent(TimelineActivity.this,
				DetailActivity.class);
		detailActivity.putExtra("username", status.getUser().getScreenName());
		detailActivity.putExtra("post", status.getText());
		detailActivity.putExtra("username", DateUtils
				.getRelativeTimeSpanString(status.getCreatedAt().getTime()));
		this.startActivity(detailActivity);
	}

	protected void updateTimeline() {
		Log.d(TAG, "UpdateTimeliner");
		Log.d(TAG, (((TwitterApp) getApplication()).userPreferences.getString(
				"charactersPerMessageShownTimeline", "50")));
		
		Log.d(TAG, "UpdateTimeline");
		twitterApp.adapter = new TimelineAdapter(
				this,
				((TwitterApp) getApplication()).getTimeline(),
				Integer.parseInt(((TwitterApp) getApplication()).userPreferences
						.getString("charactersPerMessageShownTimeline",
								"50")));
		listViewTimelime.setAdapter(twitterApp.adapter);
		/*listViewTimelime
				.setAdapter(new TimelineAdapter(
						this,
						((TwitterApp) getApplication()).getTimeline(),
						Integer.parseInt(((TwitterApp) getApplication()).userPreferences
								.getString("charactersPerMessageShownTimeline",
										"50"))));*/
	}
	
	
}
