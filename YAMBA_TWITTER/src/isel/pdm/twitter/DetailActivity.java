package isel.pdm.twitter;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {
	private static final String TAG = DetailActivity.class.getSimpleName();
	private	TextView username, post, timeSincePost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.detail);

		Bundle extras = getIntent().getExtras();
		this.username = (TextView) findViewById(R.id.usernameDetail);
		this.post = (TextView) findViewById(R.id.postDetail);
		this.timeSincePost = (TextView) findViewById(R.id.timesincepostDetail);

		if (extras != null) {
			this.username.setText(extras.getString("username"));
			this.post.setText(extras.getString("post"));
			this.timeSincePost.setText(extras.getString("timesincepost"));
			Log.d(TAG, extras.getString("username"));
			Log.d(TAG, extras.getString("post"));
			Log.d(TAG, extras.getString("timesincepost"));
		} else {
			this.username.setText("username");
			this.post.setText("post");
			this.timeSincePost.setText("timesincepost");

		}
	}
}
