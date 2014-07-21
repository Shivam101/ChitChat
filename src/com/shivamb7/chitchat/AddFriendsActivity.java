package com.shivamb7.chitchat;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shivamb7.chitchat.R;

public class AddFriendsActivity extends Activity {

	List<ParseUser> pusers;
	ListView mUserList;
	ParseUser currentUser;
	ParseRelation<ParseUser> mFriendRelation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_add_friends);
		mUserList = (ListView) findViewById(R.id.add_friends_list);
		mUserList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mUserList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mUserList.isItemChecked(position)) {
					mFriendRelation.add(pusers.get(position));
					currentUser.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {

							}
						}
					});
				} else {
					// delete friend
					mFriendRelation.remove(pusers.get(position));
					currentUser.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {

							}
						}
					});
				}

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setProgressBarIndeterminateVisibility(true);
		super.onResume();
		currentUser = ParseUser.getCurrentUser();
		mFriendRelation = currentUser.getRelation("friendRelation");
		ParseQuery<ParseUser> mQuery = ParseUser.getQuery();
		mQuery.orderByAscending("username");
		mQuery.whereNotEqualTo("username", currentUser.getUsername());
		mQuery.setLimit(1000);
		mQuery.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> users, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				// TODO Auto-generated method stub
				if (e == null) {
					pusers = users;
					String[] usernames = new String[pusers.size()];
					int i = 0;
					for (ParseUser user : pusers) {
						usernames[i] = user.getUsername();
						i++;
					}
					ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
							AddFriendsActivity.this,
							android.R.layout.simple_list_item_checked,
							usernames);
					mUserList.setAdapter(mAdapter);
					checkFriends();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							AddFriendsActivity.this);
					builder.setMessage(R.string.friend_error);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();

				}
			}
		});
	}

	protected void checkFriends() {
		mFriendRelation.getQuery().findInBackground(
				new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> friends, ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							for (int i = 0; i < pusers.size(); i++) {
								ParseUser user = pusers.get(i);
								for (ParseUser mFriend : friends) {
									if (mFriend.getObjectId().equals(
											user.getObjectId())) {
										mUserList.setItemChecked(i, true);
									}
								}
							}
						} else {

						}

					}
				});
	}
}
