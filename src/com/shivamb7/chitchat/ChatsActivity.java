package com.shivamb7.chitchat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.adapters.LevelAdapter;
import com.shivamb7.chitchat.fragments.ChatsFragment;
import com.shivamb7.chitchat.fragments.ContactsFragment;
import com.shivamb7.chitchat.fragments.ProfileFragment;
import com.shivamb7.chitchat.workers.Fab;
import com.shivamb7.chitchat.workers.Level;

public class ChatsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	int PICTURE_INTENT_CODE = 1;
	int VIDEO_INTENT_CODE = 2;
	int MEDIA_TYPE_IMAGE = 3;
	int MEDIA_TYPE_VIDEO = 4;
	int TEXT_INTENT_CODE = 5;
	int MEDIA_TYPE_TEXT = 6;
	static int flag = 0;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	Uri mMediaUri;
	ParseUser currentUser = ParseUser.getCurrentUser();
	DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

			//if (which == 0) {
				//flag = 1;
				//Intent textIntent = new Intent(ChatsActivity.this,ComposeTextActivity.class);
				//startActivity(textIntent);
				
			 if (which == 0) {
				// do picture stuff here
				flag = 0;
				Intent pictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				mMediaUri = getOutputUri(MEDIA_TYPE_IMAGE);
				if (mMediaUri == null) {
					Toast.makeText(ChatsActivity.this,
							R.string.storage_access_error, Toast.LENGTH_SHORT)
							.show();
				} else {
					pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
					startActivityForResult(pictureIntent, PICTURE_INTENT_CODE);
				}
			} else if (which == 1) {
				// do video stuff here
				flag = 0;
				Intent videoIntent = new Intent(
						MediaStore.ACTION_VIDEO_CAPTURE);
				mMediaUri = getOutputUri(MEDIA_TYPE_VIDEO);
				if (mMediaUri == null) {
					Toast.makeText(ChatsActivity.this,
							R.string.storage_access_error, Toast.LENGTH_SHORT)
							.show();
				}
				else
				{
					videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
					videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
					videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
					startActivityForResult(videoIntent, VIDEO_INTENT_CODE);
				}
			}

		}

		private Uri getOutputUri(int mediaType) {
			// TODO Auto-generated method stub
			if (hasExternalStorage()) {
				// 1. get external storage directory
				String appName = ChatsActivity.this.getString(R.string.app_name);
				File extStorageDir = new File(
						Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appName);
				
				// 2. create subdirectory
				if(!extStorageDir.exists())
				{
					if(!extStorageDir.mkdirs())
					{
						Toast.makeText(ChatsActivity.this, "Failed to create directory", Toast.LENGTH_SHORT).show();
					}
				}
				
				// 3. create a filename
				// 4. create a file
				File mFile;
				Date mCurrentDate = new Date();
				String mTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(mCurrentDate);
				String path = extStorageDir.getPath() + File.separator;
				if(mediaType == MEDIA_TYPE_IMAGE)
				{
					mFile = new File(path+"CHATIMG_"+mTimestamp+".jpg");
				}
				else if(mediaType == MEDIA_TYPE_VIDEO)
				{
					mFile = new File(path+"CHATVID"+mTimestamp+".mp4");
				}
				else
				{
					return null;
				}
				// 5. return the file's URI
				return Uri.fromFile(mFile);
						} else {
				return null;
			}
		}

		private boolean hasExternalStorage() {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats);
		ParseAnalytics.trackAppOpened(getIntent());
		final ActionBar ab = getActionBar();
		Typeface ironman = Typeface.createFromAsset(getAssets(),
				"actionman.ttf");
		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");
		TextView yourTextView = (TextView) findViewById(titleId);
		Fab fab = (Fab) findViewById(R.id.fabbutton);
		fab.setFabColor(Color.parseColor("#ef6c00"));
		fab.setFabDrawable(getResources().getDrawable(
				R.drawable.ic_action_send_now));
		fab.showFab();
		final Level data[] = new Level[] {
				//new Level("Text", R.drawable.ic_action_chat_orange),
				new Level("Picture", R.drawable.ic_action_picture_orange),
				new Level("Video", R.drawable.ic_action_video_orange), };
		fab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View customTitle = inflater
						.inflate(R.layout.dialog_title, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChatsActivity.this).setTitle(R.string.share_option)
						.setAdapter(
								new LevelAdapter(ChatsActivity.this,
										R.layout.dialog_list_item, data),
								mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.setCustomTitle(customTitle);
				builder.show();

			}
		});
		yourTextView.setTypeface(ironman);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.mpager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			navigateToLogin();
		}
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						ab.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			ab.addTab(ab.newTab().setIcon(mSectionsPagerAdapter.getIcon(i))
					.setTabListener(this));
		}
	}
	
		
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK)
		{
			//add to gallery
			if(requestCode==PICTURE_INTENT_CODE||requestCode==VIDEO_INTENT_CODE){
			Intent galleryAddIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			galleryAddIntent.setData(mMediaUri);
			sendBroadcast(galleryAddIntent);

			Intent sendIntent = new Intent(ChatsActivity.this,RecipientsActivity.class);
			sendIntent.setData(mMediaUri);
			String type = new String();
			if(requestCode == PICTURE_INTENT_CODE)
			{
				type = com.shivamb7.chitchat.workers.Constants.TYPE_PICTURE;
			}
			else if(requestCode == VIDEO_INTENT_CODE)
			{
				type = com.shivamb7.chitchat.workers.Constants.TYPE_VIDEO;
			}
			
			
			sendIntent.putExtra(com.shivamb7.chitchat.workers.Constants.FILE_TYPE, type);
			startActivity(sendIntent);
		}
			else
			{
				
			}
		}
		else if(resultCode != RESULT_CANCELED)
		{
			Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
		}
	}

	public void navigateToLogin() {
		Intent i = new Intent(ChatsActivity.this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// if (mDrawerToggle.onOptionsItemSelected(item)) {
		// return true;
		// }
		if (id == R.id.action_logout) {
			ParseUser.logOut();
			navigateToLogin();
		} else if (id == R.id.action_add_friends) {
			Intent i = new Intent(ChatsActivity.this, AddFriendsActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			if (position == 0) {
				Fragment frag1 = new ChatsFragment();
				return frag1;
			} else if (position == 1) {
				Fragment frag2 = new ContactsFragment();
				return frag2;
			} else if (position == 2) {
				Fragment frag3 = new ProfileFragment();
				return frag3;
			} else {
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
						position + 1);
				fragment.setArguments(args);
				return fragment;
			}

		}

		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.fragment1).toUpperCase(l);
			case 1:
				return getString(R.string.fragment2).toUpperCase(l);
			case 2:
				return getString(R.string.fragment3).toUpperCase(l);
			}
			return null;
		}

		public int getIcon(int position) {
			switch (position) {
			case 0:
				return R.drawable.ic_action_chat;
			case 1:
				return R.drawable.ic_action_group;
			case 2:
				return R.drawable.ic_action_person;
			}
			return position;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.dummy, container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.textView1);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
