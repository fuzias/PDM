package isel.pdm.twitter;

import java.util.List;

import twitter4j.Status;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TimelineActivity extends BaseActivity implements
		OnItemClickListener {
	private static final int HasNewTweets = 1;
	private static final int NoNewTweets = 2;
	private static final int ErrorOcurred = -1;
	private static final String TAG = "TimelineActivity";
	private ListView listViewTimelime;
	private Handler periodicUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Recebido do TimeLineUpdateService");
			if (msg.what == ErrorOcurred) {
				Log.d(TAG, msg.getData().getString("error"));
				Toast.makeText(TimelineActivity.this,
						"An error has ocurred, plz try again!",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == HasNewTweets) {
				((TwitterApp) getApplication()).adapter
						.addNewElements((List<Status>) msg.getData()
								.getSerializable("newTweets"));
			}
		}
	};

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
		listViewTimelime.setOnItemClickListener(this);
		// new GetUserTimelineFromTwitterTask().doInBackground();
		((TwitterApp) getApplication()).getTimeline();
		// deve chamar um metodo que vai ao twitter buscar os tweets
		if (((TwitterApp) getApplication()).userPreferences.getBoolean(
				"updates", false)) {
			((TwitterApp) getApplication()).messenger = new Messenger(periodicUpdateHandler);
		}

	}

	/*
	 * private class GetUserTimelineFromTwitterTask extends AsyncTask<Void,
	 * Void, Void> { private final String TAGInnerClass = TAG + " " +
	 * GetUserTimelineFromTwitterTask.class.getSimpleName();
	 * 
	 * @Override protected Void doInBackground(Void... params) {
	 * Log.d(TAGInnerClass, "doInBackground! - getTimelineFromTwitterTask");
	 * Message msg = new Message();
	 * 
	 * try { ((TwitterApp) getApplication()).getTimeline(); } catch (Exception
	 * e) { Log.e(TAG, e.getMessage()); //Toast.makeText(this, e.getMessage(),
	 * Toast.LENGTH_LONG).show(); } return null; } }
	 */

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		getTimelineInMemory();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

		Log.d(TAG, "onItemClick");
		Status status = twitterApp.adapter.getItem(pos);

		Intent detailActivity = new Intent(TimelineActivity.this,
				DetailActivity.class);
		detailActivity.putExtra("username", status.getUser().getScreenName());
		detailActivity.putExtra("post", status.getText());
		detailActivity.putExtra("timesincepost", DateUtils
				.getRelativeTimeSpanString(status.getCreatedAt().getTime()));
		this.startActivity(detailActivity);
	}

	// get timeline list of twitterapp
	protected void getTimelineInMemory() {
		Log.d(TAG, "UpdateTimeliner");
		Log.d(TAG, (((TwitterApp) getApplication()).userPreferences.getString(
				"charactersPerMessageShownTimeline", "50")));

		Log.d(TAG, "UpdateTimeline");
		twitterApp.adapter = new TimelineAdapter(
				this,
				twitterApp.getTimelineList(),
				Integer.parseInt(((TwitterApp) getApplication()).userPreferences
						.getString("charactersPerMessageShownTimeline", "50")));
		listViewTimelime.setAdapter(twitterApp.adapter);
	}

}
