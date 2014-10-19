package com.shivamb7.chitchat;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageMessageActivity extends Activity {

	ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_image_message);
		mImageView = (ImageView)findViewById(R.id.image);
		Uri imageUri = getIntent().getData();
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    ActionBar ab = getActionBar();
		Resources r = getResources();
		Drawable d = r.getDrawable(android.R.color.transparent);
		ab.setBackgroundDrawable(d);
		ab.hide();
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		TextView abtitle = (TextView)findViewById(titleId);
		abtitle.setTextColor(getResources().getColor(R.color.black));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { 

		    //translucentNavigation = true; 
		    Window w = getWindow();  
		    w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		    w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		}
		
		//setProgressBarIndeterminateVisibility(true);
		Picasso.with(this).load(imageUri.toString()).placeholder(R.drawable.image_loading_2).into(mImageView);
		//setProgressBarIndeterminateVisibility(false);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		}, 60000);
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
