package com.shivamb7.chitchat;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageMessageActivity extends Activity {

	ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_message);
		mImageView = (ImageView)findViewById(R.id.image);
		Uri imageUri = getIntent().getData();
		Picasso.with(this).load(imageUri.toString()).into(mImageView);
	}
}
