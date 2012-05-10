package isel.pdm.twitter;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {
	TextView username, post, timeSincePost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		} else {
			this.username.setText("username");
			this.post.setText("post");
			this.timeSincePost.setText("timesincepost");

		}
	}
}
