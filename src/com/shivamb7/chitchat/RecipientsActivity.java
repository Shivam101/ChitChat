package com.shivamb7.chitchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shivamb7.chitchat.workers.Constants;
import com.shivamb7.chitchat.workers.FileHelper;

public class RecipientsActivity extends Activity {

	ListView mRecipientList;
	TextView mEmptyText;
	ImageView mEmptyImage;
	List<ParseUser> pfriends;
	ParseUser currentUser;
	ParseRelation<ParseUser> mFriendRelation;
	MenuItem mSend;
	Uri mMediaUri;
	String textMessage;
	String mFileType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipients);
		mRecipientList = (ListView)findViewById(R.id.recipient_list);
		mEmptyText = (TextView)findViewById(R.id.empty_state_text);
		mEmptyImage = (ImageView)findViewById(R.id.empty_state_image);
		mRecipientList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString(Constants.FILE_TYPE);
		mRecipientList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mRecipientList.getCheckedItemCount()>0)
				{
					mSend.setVisible(true);
				}
				else
				{
					mSend.setVisible(false);
				}
			}
		});
	}
	
	public ParseObject createMessage()
	{
		ParseObject message = new ParseObject(Constants.CLASS_MESSAGES);
		message.put(Constants.SENDER_NAME, currentUser.getUsername());
		message.put(Constants.SENDER_ID, currentUser.getObjectId());
		message.put(Constants.RECIPIENT_IDS,getRecipientIds());
		message.put(Constants.FILE_TYPE,mFileType);
		message.put(Constants.TEXT_CONTENTS, ComposeTextActivity.msg);
		//message.put(Constants.TEXT_CONTENTS, ComposeTextActivity.msg);
		//if(ChatsActivity.flag==0)
		//{
			byte[] fileData = FileHelper.getByteArrayFromFile(RecipientsActivity.this, mMediaUri);
			if(fileData==null)
			{
				return null;
			}
			else
			{
				if(mFileType.equals(Constants.TYPE_PICTURE))
				{
					fileData = FileHelper.reduceImageForUpload(fileData);
				}
				String fileName = FileHelper.getFileName(RecipientsActivity.this, mMediaUri, mFileType);
				ParseFile mFile = new ParseFile(fileName, fileData);
				message.put(Constants.FILE, mFile);
				return message;
			}

//		}
		/*else if(ChatsActivity.flag==1)
		{
			message.put(Constants.FILE, "N/A");
		}*/
				
	}
	
	/*public ParseObject createTextMessage()
	{
		ParseObject message = new ParseObject(Constants.CLASS_TEXT_MESSAGES);
		message.put(Constants.SENDER_NAME, currentUser.getUsername());
		message.put(Constants.SENDER_ID, currentUser.getObjectId());
		message.put(Constants.RECIPIENT_IDS,getRecipientIds());
		message.put(Constants.FILE_TYPE,Constants.TEXT_TYPE);
		message.put(Constants.TEXT_CONTENTS, ComposeTextActivity.msg);
		return message;
	}*/
	
	public ArrayList<String> getRecipientIds()
	{
		ArrayList<String> recipientIds = new ArrayList<String>();
		for(int i = 0;i<mRecipientList.getCount();i++)
		{
			if(mRecipientList.isItemChecked(i))
			{
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
		if(id==R.id.action_send)
		{
			//do sending here
			//if(ChatsActivity.flag==0)
			//{
			ParseObject message = createMessage();
			if(message!=null)
			{
				send(message);
				finish(); 
			}
			else if(message==null)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RecipientsActivity.this);
				builder.setMessage(R.string.message_error);
				builder.setTitle(R.string.signup_error_title);
				builder.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			
			/*else if(ChatsActivity.flag==1)
			{
				ParseObject message = createTextMessage();
				if(message!=null)
				{
					sendText(message);
					finish(); 
				}
				else if(message==null)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RecipientsActivity.this);
					builder.setMessage(R.string.message_error);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}*/
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	private void send(ParseObject message) {
		// TODO Auto-generated method stub
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e==null)
				{
					Toast.makeText(RecipientsActivity.this, R.string.message_sent_success, Toast.LENGTH_SHORT).show();
				}
				else
				{
					//if(!isFinishing()){
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RecipientsActivity.this);
					builder.setMessage(R.string.message_error);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
					}
				}
			//}
		});
	}

	
	/*private void sendText(ParseObject message) {
		// TODO Auto-generated method stub
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e==null)
				{
					Toast.makeText(RecipientsActivity.this, R.string.message_sent_success, Toast.LENGTH_SHORT).show();
					Intent i = new Intent(RecipientsActivity.this,ChatsActivity.class);
					startActivity(i);
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RecipientsActivity.this);
					builder.setMessage(R.string.message_error);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}*/

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
								mRecipientList.setVisibility(View.GONE);
							} else {
								mEmptyImage.setVisibility(View.GONE);
								mEmptyText.setVisibility(View.GONE);
								mRecipientList.setVisibility(View.VISIBLE);
								ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
										RecipientsActivity.this,
										android.R.layout.simple_list_item_checked,
										usernames);
								mRecipientList.setAdapter(mAdapter);
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
