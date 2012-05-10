package isel.pdm.twitter;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class TwitterApp extends Application implements
		OnSharedPreferenceChangeListener {

	private static final String consumerKey = "Nd9Zqz1Xq3sXiXzIaZDRg";
	private static final String consumerSecret = "KDdhZz3l4uZLHvRfZjn4GOBtS35gKKCajDYCLcbg5hQ";
	private static final String accessToken = "19740161-cdQDKvmcLwtyJI8UI0GkK2iDQ3Tgu5VB1vOaIaETT";
	private static final String accessTokenSecret = "qqBUBc9wOPm5ndVYTNvGlk9xaR1M58vecuM1uPswYAs";

	private static final String TAG = TwitterApp.class.getSimpleName();
	private Twitter twitter;
	protected SharedPreferences userPreferences;
	protected List<Status> timelineList;

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
		// Setup Preferences
		userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		userPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "onTerminated");
	}

	public synchronized void onSharedPreferenceChanged(SharedPreferences arg0,
			String arg1) {
		twitter = null;
	}

	public synchronized Twitter getTwitter() {
		Log.d(TAG, "getTwitter");
		if (twitter == null) {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey(consumerKey)
					.setOAuthConsumerSecret(consumerSecret)
					.setOAuthAccessToken(accessToken)
					.setOAuthAccessTokenSecret(accessTokenSecret)
					.setOAuthRequestTokenURL(
							"https://api.twitter.com/oauth/request_token")
					.setOAuthAuthorizationURL(
							"https://api.twitter.com/oauth/authorize")
					.setOAuthAccessTokenURL(
							"https://api.twitter.com/oauth/access_token")

			;
			TwitterFactory tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();
			if (twitter == null)
				Log.e(TAG, "Twitter instance == null");
		}
		return twitter;
	}

	public synchronized List<Status> getTimeline() {
		Log.d(TAG, "getTimeline");

		if (timelineList == null) {
			try {
				timelineList = getTwitter().getUserTimeline();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		return timelineList;
	}
}
