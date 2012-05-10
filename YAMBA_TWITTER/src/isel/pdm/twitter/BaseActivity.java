package isel.pdm.twitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {
	private static final String TAG = "BaseActivity";
	protected TwitterApp twitterApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		twitterApp = (TwitterApp) getApplication();
	}

	// Called first time a user clicks on the menu button
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
		case R.id.itemUserPrefs:
			startActivity(new Intent(this, UserPrefActivity.class));
			break;
		case R.id.itemStatus:
			startActivity(new Intent(this, StatusActivity.class));
			break;
		case R.id.itemTimeline:
			startActivity(new Intent(this, TimelineActivity.class));
			break;
		case R.id.itemUpdateTimeline:
			Toast.makeText(this, "Geting Tweets from Twitter",
					Toast.LENGTH_SHORT);
			// TODO Async get tweets from twitter
			break;
		}
		return true;
	}

}
