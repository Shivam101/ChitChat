package com.shivamb7.chitchat;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageMessageActivity extends Activity {

	ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_image_message);
		mImageView = (ImageView)findViewById(R.id.image);
		Uri imageUri = getIntent().getData();
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		//setProgressBarIndeterminateVisibility(true);
		Picasso.with(this).load(imageUri.toString()).placeholder(R.drawable.ic_action_picture_orange).into(mImageView);
		//setProgressBarIndeterminateVisibility(false);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		}, 20000);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
