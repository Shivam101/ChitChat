package com.shivamb7.chitchat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shivamb7.chitchat.workers.Constants;
import com.shivamb7.chitchat.workers.FileHelper;

public class RecipientsActivity extends Activity {

	GridView mRecipientsGrid;
	ListView mRecipientsList;
	TextView mEmptyText;
	EditText mSearch;
	ImageView mEmptyImage;
	List<ParseUser> pfriends;
	ParseUser currentUser;
	ParseRelation<ParseUser> mFriendRelation;
	MenuItem mSend;
	Uri mMediaUri;
	String textMessage;
	String mFileType;
	int mNotificationId = 001;
	NotificationManager mNotifyMgr;
	Uri soundUri;
	NotificationCompat.Builder mNotifBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipients);
		getActionBar().setHomeButtonEnabled(true);
		//mSearch = (EditText)findViewById(R.id.etsearch);
		//mSearch.setVisibility(View.GONE);
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);;
		Intent resultIntent = new Intent(this, ChatsActivity.class);
		PendingIntent resultPendingIntent =
			    PendingIntent.getActivity(
			    this,
			    0,
			    resultIntent,
			    PendingIntent.FLAG_UPDATE_CURRENT
			);
		Calendar cal = Calendar.getInstance();              
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis()+60*60*1000);
		intent.putExtra("allDay", false);
		intent.putExtra("rrule", "FREQ=DAILY");
		intent.putExtra("endTime", cal.getTimeInMillis()+2*60*60*1000);
		intent.putExtra("title", "Remember to check your inbox !");
		//startActivity(intent);
		PendingIntent actionPendingIntent =
		        PendingIntent.getActivity(this, 0, intent,
		                PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Action action =
		        new NotificationCompat.Action.Builder(R.drawable.notifications,
		                getString(R.string.remind_me), actionPendingIntent)
		                .build();
		BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
		bigStyle.bigText("New chit !");
		mNotifBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_stat_ic_action_send_now)
				.setContentTitle("ChitChat").setContentText("Your message has been sent !").extend(new WearableExtender().addAction(action)).setSound(soundUri).setStyle(bigStyle).setAutoCancel(true);;
		mNotifBuilder.setContentIntent(resultPendingIntent);
		mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mRecipientsList = (ListView)findViewById(R.id.recipient_list);
		mRecipientsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//mRecipientsGrid = (GridView) findViewById(R.id.friends_grid);
		mEmptyText = (TextView) findViewById(R.id.empty_state_text);
		mEmptyImage = (ImageView) findViewById(R.id.empty_state_image);
		//mRecipientsGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString(Constants.FILE_TYPE);
		/*mRecipientsGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ImageView mCheckImage = (ImageView)findViewById(R.id.user_image_check); 
				if (mRecipientsGrid.getCheckedItemCount() > 0) {
					mSend.setVisible(true);
					mCheckImage.setVisibility(View.VISIBLE);
					
				} else {
					mSend.setVisible(false);
					mCheckImage.setVisibility(View.INVISIBLE);
				}
			}
		});*/
		mRecipientsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mRecipientsList.getCheckedItemCount() > 0) {
					mSend.setVisible(true);
				} else {
					mSend.setVisible(false);
				}
			}
		});

	}
	
	

	public ParseObject createMessage() {
		ParseObject message = new ParseObject(Constants.CLASS_MESSAGES);
		message.put(Constants.SENDER_NAME, currentUser.getString("Name"));
		message.put(Constants.SENDER_ID, currentUser.getObjectId());
		message.put(Constants.RECIPIENT_IDS, getRecipientIds());
		message.put(Constants.FILE_TYPE, mFileType);
		// message.put(Constants.TEXT_CONTENTS, ComposeTextActivity.msg);
		// message.put(Constants.TEXT_CONTENTS, ComposeTextActivity.msg);
		// if(ChatsActivity.flag==0)
		// {
		byte[] fileData = FileHelper.getByteArrayFromFile(
				RecipientsActivity.this, mMediaUri);
		if (fileData == null) {
			return null;
		} else {
			if (mFileType.equals(Constants.TYPE_PICTURE)) {
				fileData = FileHelper.reduceImageForUpload(fileData);
			}
			String fileName = FileHelper.getFileName(RecipientsActivity.this,
					mMediaUri, mFileType);
			ParseFile mFile = new ParseFile(fileName, fileData);
			message.put(Constants.FILE, mFile);
			return message;
		}

		// }
		/*
		 * else if(ChatsActivity.flag==1) { message.put(Constants.FILE, "N/A");
		 * }
		 */

	}
	
	public void setReminder()
	{
		Calendar cal = Calendar.getInstance();              
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("allDay", false);
		intent.putExtra("rrule", "FREQ=DAILY");
		intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", "Look up Chit");
		startActivity(intent);
	}
	
	public void sendPushNotifications()
	{
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn(Constants.USER_ID, getRecipientIds());
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(getString(R.string.message_notification,ParseUser.getCurrentUser().getUsername()));
		push.sendInBackground();
	}

	
	public ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		for (int i = 0; i < mRecipientsList.getCount(); i++) {
			if (mRecipientsList.isItemChecked(i)) {
				recipientIds.add(pfriends.get(i).getObjectId());
			}
		}
		return recipientIds;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.recipients_menu, menu);
		mSend = menu.getItem(0);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_send) {
			// do sending here
			// if(ChatsActivity.flag==0)
			// {
			ParseObject message = createMessage();
			if (message != null) {
				send(message);
				Toast.makeText(RecipientsActivity.this,R.string.message_sent_success,Toast.LENGTH_SHORT).show();
				finish();
			} else if (message == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RecipientsActivity.this);
				builder.setMessage(R.string.message_error);
				builder.setTitle(R.string.signup_error_title);
				builder.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}

			/*
			 * else if(ChatsActivity.flag==1) { ParseObject message =
			 * createTextMessage(); if(message!=null) { sendText(message);
			 * finish(); } else if(message==null) { AlertDialog.Builder builder
			 * = new AlertDialog.Builder( RecipientsActivity.this);
			 * builder.setMessage(R.string.message_error);
			 * builder.setTitle(R.string.signup_error_title);
			 * builder.setPositiveButton(android.R.string.ok, null); AlertDialog
			 * dialog = builder.create(); dialog.show(); } }
			 */
		}
		
		else if(id==android.R.id.home)
		{
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void send(ParseObject message) {
		// TODO Auto-generated method stub
		message.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					mNotifyMgr.notify(mNotificationId, mNotifBuilder.build());
					sendPushNotifications();
				} else {
					// if(!isFinishing()){
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RecipientsActivity.this);
					builder.setMessage(R.string.message_error);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
			// }
		});
	}

	/*
	 * private void sendText(ParseObject message) { // TODO Auto-generated
	 * method stub message.saveInBackground(new SaveCallback() {
	 * 
	 * @Override public void done(ParseException e) { // TODO Auto-generated
	 * method stub if(e==null) { Toast.makeText(RecipientsActivity.this,
	 * R.string.message_sent_success, Toast.LENGTH_SHORT).show(); Intent i = new
	 * Intent(RecipientsActivity.this,ChatsActivity.class); startActivity(i); }
	 * else { AlertDialog.Builder builder = new AlertDialog.Builder(
	 * RecipientsActivity.this); builder.setMessage(R.string.message_error);
	 * builder.setTitle(R.string.signup_error_title);
	 * builder.setPositiveButton(android.R.string.ok, null); AlertDialog dialog
	 * = builder.create(); dialog.show(); } } }); }
	 */

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setProgressBarIndeterminateVisibility(true);
		currentUser = ParseUser.getCurrentUser();
		mFriendRelation = currentUser.getRelation("friendRelation");
		mFriendRelation.getQuery().findInBackground(
				new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> friends, ParseException e) {
						setProgressBarIndeterminateVisibility(false);
						// TODO Auto-generated method stub
						if (e == null) {
							pfriends = friends;
							String[] usernames = new String[pfriends.size()];
							int i = 0;
							for (ParseUser user : pfriends) {
								usernames[i] = user.getUsername();
								i++;
							}
							if (usernames.length == 0) {
								mEmptyImage.setVisibility(View.VISIBLE);
								mEmptyText.setVisibility(View.VISIBLE);
								mRecipientsList.setVisibility(View.GONE);
							} else {
								mEmptyImage.setVisibility(View.GONE);
								mEmptyText.setVisibility(View.GONE);
								mRecipientsList.setVisibility(View.VISIBLE);
								/*if(mRecipientsGrid.getAdapter() == null)
								{
								FriendGridAdapter mAdapter = new FriendGridAdapter(RecipientsActivity.this, pfriends);
								mRecipientsGrid.setAdapter(mAdapter);
								}
								else
								{
									((FriendGridAdapter)(mRecipientsGrid.getAdapter())).refreshAdapter(pfriends);
								}*/
								ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
										RecipientsActivity.this,
										android.R.layout.simple_list_item_checked,
										usernames);
								mRecipientsList.setAdapter(mAdapter);

								}
						} else {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									RecipientsActivity.this);
							builder.setMessage(R.string.friend_error);
							builder.setTitle(R.string.signup_error_title);
							builder.setPositiveButton(android.R.string.ok, null);
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					}
				});
	}

}
