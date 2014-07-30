package com.shivamb7.chitchat;

import java.util.Currency;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.shivamb7.chitchat.workers.Constants;
import com.shivamb7.chitchat.workers.FileHelper;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends Activity {

	ImageView mProfileImage;
	int PICK_PICTURE = 100;
	Uri pictureUri;
	String fileType = "ProfilePic";
	ParseUser currentUser = ParseUser.getCurrentUser();
	TextView mUsername, mName, mFriendCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.profile_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mProfileImage = (ImageView) findViewById(R.id.profile_picture);
		mUsername = (TextView) findViewById(R.id.profile_username);
		mName = (TextView) findViewById(R.id.profile_name);
		ActionBar ab = getActionBar();
		Resources r = getResources();
		Drawable d = r.getDrawable(android.R.color.transparent);
		ab.setBackgroundDrawable(d);
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		TextView abtitle = (TextView)findViewById(titleId);
		abtitle.setTextColor(getResources().getColor(R.color.orange_800));
		
		mUsername.setText(currentUser.getUsername());
		mName.setText(currentUser.getString("Name"));
		mProfileImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent choosePictureIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				choosePictureIntent.setType("image/*");
				startActivityForResult(choosePictureIntent,
						PICK_PICTURE);

			}
		});
		
		

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
		{

			if(requestCode == PICK_PICTURE)
			{
				if(data==null)
				{
					Toast.makeText(ProfileActivity.this, "Try picking a file again", Toast.LENGTH_SHORT).show();
				}
				else
				{
					pictureUri = data.getData();
					Picasso.with(ProfileActivity.this).load(pictureUri.toString()).into(mProfileImage);
					byte[] fileData = FileHelper.getByteArrayFromFile(
							ProfileActivity.this, pictureUri);
							//if (pictureUri.equals(Constants.TYPE_PICTURE)) {
							fileData = FileHelper.reduceImageForUpload(fileData);
							Toast.makeText(ProfileActivity.this, "Saved to Parse", Toast.LENGTH_SHORT).show();

						//}
						String fileName = FileHelper.getFileName(ProfileActivity.this,
								pictureUri,fileType );
						ParseFile mFile = new ParseFile(fileName, fileData);
						currentUser.put("profilePicture", mFile);
						
					
				}
	}}

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
