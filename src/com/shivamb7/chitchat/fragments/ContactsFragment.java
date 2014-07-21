package com.shivamb7.chitchat.fragments;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.shivamb7.chitchat.R;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsFragment extends Fragment {

	List<ParseUser> pfriends;
	ParseUser currentUser;
	ParseRelation<ParseUser> mFriendRelation;
	ListView mFriendList;
	TextView mEmptyText;
	ImageView mEmptyImage;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container,
				false);
		mFriendList = (ListView) rootView.findViewById(R.id.friends_list);
		mEmptyText = (TextView) rootView.findViewById(R.id.empty_state_text);
		mEmptyImage = (ImageView) rootView.findViewById(R.id.empty_state_image);
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().setProgressBarIndeterminateVisibility(true);
		currentUser = ParseUser.getCurrentUser();
		mFriendRelation = currentUser.getRelation("friendRelation");
		mFriendRelation.getQuery().findInBackground(
				new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> friends, ParseException e) {
						getActivity().setProgressBarIndeterminateVisibility(false);
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
								mFriendList.setVisibility(View.GONE);
							} else {
								mEmptyImage.setVisibility(View.GONE);
								mEmptyText.setVisibility(View.GONE);
								mFriendList.setVisibility(View.VISIBLE);
								ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
										getActivity(),
										android.R.layout.simple_list_item_1,
										usernames);
								mFriendList.setAdapter(mAdapter);
							}
						} else {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
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
