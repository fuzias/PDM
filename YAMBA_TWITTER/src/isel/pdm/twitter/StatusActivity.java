package isel.pdm.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	private static final String TAG = "StatusActivity";
	private EditText editText;
	private Button updateButton;
	private TextView textCount;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		// find views
		editText = (EditText) findViewById(R.id.editText);
		updateButton = (Button) findViewById(R.id.buttonUpdate);

		// TextWatcher
		textCount = (TextView) findViewById(R.id.textCount);
		textCount.setText(Integer.toString(140));
		textCount.setTextColor(Color.GREEN);

		// Set listeners
		updateButton.setOnClickListener(this);
		editText.addTextChangedListener(this);
	}

	// Para efeitos de debug
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	// Para efeitos de debug
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	// Called when a button is clicked
	public void onClick(View v) {
		Log.d(TAG, "onClicked1");
		// confirmation window
		AlertDialog.Builder confirmationWindow = new AlertDialog.Builder(this);
		confirmationWindow.setTitle("Confirm");
		confirmationWindow.setMessage("Update Status?");
		confirmationWindow.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Starting async task to make the update
						new PostToTwitterTask().execute(editText.getText()
								.toString());
					}
				});
		confirmationWindow.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		confirmationWindow.show();

		new PostToTwitterTask().execute(editText.getText().toString());
		Log.d(TAG, "onClicked2");
	}

	// Asynchronous post on twitter
	private class PostToTwitterTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... statuses) {
			Log.d(TAG, "doInBackground!");

			// Disable clicks on update button
			updateButton.setClickable(false);

			try {
				Twitter twitter = ((TwitterApp) getApplication()).getTwitter();
				if (twitter != null) {
					twitter.updateStatus(statuses[0]);
				} else {
					// TODO Tell user to update preferences
				}
			} catch (TwitterException e) {
				Log.e(TAG, "Twitter update status failed: " + e.getMessage());
				// Enable update button ?? Is this necessary here ??
				updateButton.setEnabled(true);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void d) {
			Log.d(TAG, "onPostExecute!");
			Toast.makeText(StatusActivity.this,
					"Status Updated:\n" + editText.getText().toString(),
					Toast.LENGTH_LONG).show();
			// Cleaning text area
			editText.setText(null);
			// Enable clicks on update button
			updateButton.setClickable(true);
		}
	}

	public void afterTextChanged(Editable s) {
		Log.d(TAG, "afterTextChanged");
		// Values to switch color when x characters are left to write
		final int YELLOW_COLOR_VALUE = 40, RED_COLOR_VALUE = 0;
		int count = 140 - editText.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		if (count < RED_COLOR_VALUE) {
			textCount.setTextColor(Color.RED);
		} else if (count < YELLOW_COLOR_VALUE) {
			textCount.setTextColor(Color.YELLOW);
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

}