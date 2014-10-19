package com.shivamb7.chitchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shivamb7.chitchat.adapters.FriendGridAdapter;

public class AddFriendsActivity extends Activity {

	List<ParseUser> pusers;
	GridView mUserGrid;
	ListView mUserList;
	ParseUser currentUser;
	EditText mSearch;
	public static int count = 0;
	public Dialog progressDialog;
	int flag;
	ParseRelation<ParseUser> mFriendRelation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_add_friends);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		//mUserGrid = (GridView) findViewById(R.id.friends_grid);
		mUserList = (ListView)findViewById(R.id.add_friends_list);
		mUserList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//mUserGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		mSearch = (EditText)findViewById(R.id.etsearch);
		mSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					AddFriendsActivity.this.progressDialog = ProgressDialog
							.show(AddFriendsActivity.this, "",
									"Searching for users...", true);
					String search_string = mSearch.getText().toString();
					String regx = search_string.trim();
					final ParseQuery<ParseUser> query = ParseUser.getQuery();
					query.whereNotEqualTo("objectId", currentUser.getObjectId());
					if (search_string.contains(" ")) {
						flag = 1;
						query.whereMatches("Name", regx, "i");

					} else {
						flag = 2;
						query.whereMatches("username", regx, "i");
					}

					query.findInBackground(new FindCallback<ParseUser>() {

						@Override
						public void done(List<ParseUser> pusers,
								ParseException e) {
							AddFriendsActivity.this.progressDialog.dismiss();
							// TODO Auto-generated method stub
							if (e == null) {
								String[] data = null;
								int i = 0;

								for (ParseUser pu : pusers) {

									if (data == null) {
										data = new String[pusers.size()];
									}

									if (flag == 1) {
										data[i] = pu.getString("Name");
										i++;
									} else if (flag == 2) {
										data[i] = pu.getUsername();
										i++;
									}
								}
								ArrayAdapter<String> mAdapter = new ArrayAdapter<>(AddFriendsActivity.this, android.R.layout.simple_list_item_checked);
								mUserList.setAdapter(mAdapter);
								/*if(mUserGrid.getAdapter() == null)
								{
								FriendGridAdapter mAdapter = new FriendGridAdapter(AddFriendsActivity.this, pusers);
								mUserGrid.setAdapter(mAdapter);
								}
								else
								{
									((FriendGridAdapter)(mUserGrid.getAdapter())).refreshAdapter(pusers);
								}*/
								
							} else {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										AddFriendsActivity.this);
								builder.setMessage(R.string.signup_error_title);
								builder.setTitle(R.string.signup_error_title);
								builder.setPositiveButton(android.R.string.ok,
										null);
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
					});

					// Toast.makeText(getBaseContext(), "Search now",
					// Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
		//count = mUserList.getCheckedItemCount();
/*		mUserGrid.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ImageView mCheckImage = (ImageView)findViewById(R.id.user_image_check);

				if (mUserGrid.isItemChecked(position)) {
					mFriendRelation.add(pusers.get(position));
					mCheckImage.setVisibility(View.VISIBLE);
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
					mCheckImage.setVisibility(View.INVISIBLE);
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
	*/	
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
		//Toast.makeText(getApplicationContext(), mUserList.getCheckedItemCount()+"", Toast.LENGTH_SHORT).show();
		currentUser = ParseUser.getCurrentUser();
		mFriendRelation = currentUser.getRelation("friendRelation");
		ParseQuery<ParseUser> mQuery = ParseUser.getQuery();
		mQuery.orderByAscending("username");
		//mQuery.whereNotEqualTo("username", currentUser.getUsername());
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
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
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
