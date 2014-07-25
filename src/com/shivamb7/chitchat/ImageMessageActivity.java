package com.shivamb7.chitchat;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class ImageMessageActivity extends Activity {

	ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_image_message);
		mImageView = (ImageView)findViewById(R.id.image);
		Uri imageUri = getIntent().getData();
		setProgressBarIndeterminateVisibility(true);
		Picasso.with(this).load(imageUri.toString()).into(mImageView);
		setProgressBarIndeterminateVisibility(false);
	}
}
