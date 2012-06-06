package isel.pdm.twitter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Recebido do UpdateStatusService");
			if (msg.getData().containsKey("error")) {
				Log.d(TAG, msg.getData().getString("error"));
				Toast.makeText(StatusActivity.this,
						"An error has ocurred, plz try again!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(StatusActivity.this,
						"Status Updated:\n" + editText.getText().toString(),
						Toast.LENGTH_LONG).show();
				// Cleaning text area
				editText.setText(null);
			}
			// Enable clicks on update button
			updateButton.setClickable(true);
		}
	};
	private Messenger messenger;

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
		messenger = new Messenger(handler);

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
						// Disable clicks on update button
						updateButton.setClickable(false);

						// Starting async task to make the update
						Intent intent = new Intent(StatusActivity.this,
								StatusUpdateService.class);
						intent.putExtra("handler", messenger);
						intent.putExtra("message", editText.getText().toString());
						startService(intent);
					}
				});
		confirmationWindow.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		confirmationWindow.show();
		Log.d(TAG, "onClicked2");
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