package isel.pdm.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class StatusUpdateService extends Service {

	private static final String TAG = StatusUpdateService.class.getSimpleName();
	private Messenger messenger;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand!");
		
		if(intent.hasExtra("handler")){
			messenger = (Messenger) intent.getExtras().get("handler");
			String msg = intent.getExtras().getString("message");
			new PostToTwitterTask().execute(msg);		
		}
		
		return START_NOT_STICKY;

	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private class PostToTwitterTask extends AsyncTask<String, String, Void> {
		private final String TAGInnerClass = TAG + " "
				+ PostToTwitterTask.class.getSimpleName();

		@Override
		protected Void doInBackground(String... statuses) {
			Log.d(TAGInnerClass, "doInBackground!");
			Message msg = new Message();

			try {
				Twitter twitter = ((TwitterApp) getApplication()).getTwitter();
				if (twitter != null) {
					twitter.updateStatus(statuses[0]);
				}
				
			} catch (TwitterException e) {
				Log.e(TAGInnerClass,
						"Twitter update status failed: " + e.getMessage());
				Bundle bundle = new Bundle();
				bundle.putString("error", String.format("%s: %s", e.getClass(), e.getMessage()));
				msg.setData(bundle);
			}
			try {
				messenger.send(msg);
			} catch (RemoteException e) {}
			return null;
		}

		
	}

}
