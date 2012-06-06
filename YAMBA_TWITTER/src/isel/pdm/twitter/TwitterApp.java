package isel.pdm.twitter;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class TwitterApp extends Application implements
		OnSharedPreferenceChangeListener {

	/*
	 * private static final String consumerKey = "Nd9Zqz1Xq3sXiXzIaZDRg";
	 * private static final String consumerSecret =
	 * "KDdhZz3l4uZLHvRfZjn4GOBtS35gKKCajDYCLcbg5hQ"; private static final
	 * String accessToken =
	 * "19740161-cdQDKvmcLwtyJI8UI0GkK2iDQ3Tgu5VB1vOaIaETT"; private static
	 * final String accessTokenSecret =
	 * "qqBUBc9wOPm5ndVYTNvGlk9xaR1M58vecuM1uPswYAs";
	 */

	private static final String TAG = TwitterApp.class.getSimpleName();
	private Twitter twitter;
	protected TimelineAdapter adapter;
	private Intent updateIntent;
	volatile Messenger messenger;

	protected SharedPreferences userPreferences;

	private List<Status> timelineList;
	protected Handler timerHandler = new Handler();

	public List<Status> getTimelineList() {
		return timelineList;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
		// Setup Preferences
		userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		userPreferences.registerOnSharedPreferenceChangeListener(this);
		updateIntent = new Intent(this,TimelineUpdateService.class);
		
		if (userPreferences.getBoolean("updates", false)) {
			startService(updateIntent);
		}

		/*
		 * new Handler(){
		 * 
		 * @Override public void handleMessage(Message msg) {
		 * adapter.add(msg.getData().get) }
		 * 
		 * 
		 * };
		 */
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "onTerminated");
	}

	public synchronized void onSharedPreferenceChanged(SharedPreferences arg0,
			String arg1) {
		
		if (arg1.equals("updates")) {
			if (!userPreferences.getBoolean("updates", false)) {
				stopService(updateIntent);
			}
			else{
				startService(updateIntent);
			}
		}
		else{
			twitter = null;
		}
		
	}

	public synchronized Twitter getTwitter() {
		Log.d(TAG, "getTwitter");
		if (twitter == null) {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey(
							userPreferences.getString("consumerKey", ""))
					.setOAuthConsumerSecret(
							userPreferences.getString("consumerSecret", ""))
					.setOAuthAccessToken(
							userPreferences.getString("accessToken", ""))
					.setOAuthAccessTokenSecret(
							userPreferences.getString("accessTokenSecret", ""))
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

	// método chamado no create da TimeLineActivity
	public synchronized void getTimeline() {// Assync Task
		Log.d(TAG, "getTimeline");
		try {
			timelineList = getTwitter().getUserTimeline();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		// return timelineList;
	}

	// metodo chamado para ir buscar actualizações através do menu
	public synchronized void updateTimeLine() {
		try {
			List<Status> newStatuses = getTwitter().getUserTimeline();
			newStatuses.removeAll(timelineList);
			if (!newStatuses.isEmpty()) {
				Log.d(TAG, "updateTimeLine - Contem novos tweets");
				adapter.addNewElements(newStatuses);
				Log.d(TAG, "updateTimeLine - novos tweets adicionados");
				// adapter.notifyDataSetChanged();
				Log.d(TAG, "updateTimeLine - adapter notificado");
				return;
			}
			Log.d(TAG, "updateTimeLine - Não há novos tweets");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Status> getNewTweets() {
		try {
			List<Status> newStatuses = getTwitter().getUserTimeline();
			newStatuses.removeAll(timelineList);
			if (!newStatuses.isEmpty()) {
				Log.d(TAG, "getNewTweets - Contem novos tweets");
				return newStatuses;
			}
			Log.d(TAG, "getNewTweets - Não há novos tweets");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
