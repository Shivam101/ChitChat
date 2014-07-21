package com.shivamb7.chitchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.workers.Constants;

public class RecipientsActivity extends Activity {

	ListView mRecipientList;
	TextView mEmptyText;
	ImageView mEmptyImage;
	List<ParseUser> pfriends;
	ParseUser currentUser;
	ParseRelation<ParseUser> mFriendRelation;
	MenuItem mSend;
	Uri mMediaUri;
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
		return message;
	}
	
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
			ParseObject message = createMessage();
		}
		
		return super.onOptionsItemSelected(item);
	}

	
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
