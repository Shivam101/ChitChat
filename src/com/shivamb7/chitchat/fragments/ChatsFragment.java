package com.shivamb7.chitchat.fragments;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shivamb7.chitchat.ImageMessageActivity;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.adapters.ChatsAdapter;
import com.shivamb7.chitchat.workers.Constants;

public class ChatsFragment extends Fragment {

	ParseUser currentUser = ParseUser.getCurrentUser();
	List<ParseObject> messages;
	ListView mMessageList;
	TextView mEmptyText;
	ImageView mEmptyImage;
	SwipeRefreshLayout mSwipeRefreshLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setProgressBarIndeterminateVisibility(false);
		View rootView = inflater.inflate(R.layout.fragment_chats, container,
				false);
		mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
		mSwipeRefreshLayout.setColorScheme(R.color.orange_300, R.color.orange_500, R.color.orange_700, R.color.orange_900);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				retrieveMessages();
			}
		});
		mMessageList = (ListView) rootView.findViewById(R.id.messages_list);
		mEmptyText = (TextView) rootView.findViewById(R.id.empty_state_text);
		mEmptyImage = (ImageView) rootView.findViewById(R.id.empty_state_image);
		mMessageList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ParseObject msg = messages.get(position);
				String msgType = msg.getString(Constants.FILE_TYPE);
				ParseFile file = msg.getParseFile(Constants.FILE);
				Uri fileUri = Uri.parse(file.getUrl());
				if(msgType.equals(Constants.TYPE_PICTURE))
				{
					Intent mPictureMessageIntent = new Intent(getActivity(),ImageMessageActivity.class);
					mPictureMessageIntent.setData(fileUri);
					startActivity(mPictureMessageIntent);
				}
				else if(msgType.equals(Constants.TYPE_VIDEO))
				{
					Intent mVideoMessageIntent = new Intent(Intent.ACTION_VIEW,fileUri);
					mVideoMessageIntent.setDataAndType(fileUri, "video/*");
					startActivity(mVideoMessageIntent);
				}
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().setProgressBarIndeterminateVisibility(true);
		//List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		retrieveMessages();
	}

	public void retrieveMessages() {
		ParseQuery<ParseObject> messageQuery = new ParseQuery<ParseObject>("Messages");
		messageQuery.whereEqualTo(
				com.shivamb7.chitchat.workers.Constants.RECIPIENT_IDS,
				currentUser.getObjectId());
		messageQuery.addAscendingOrder("createdAt");
		messageQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> pobjects, ParseException e) {
				// TODO Auto-generated method stub
				getActivity().setProgressBarIndeterminateVisibility(false);
				if(mSwipeRefreshLayout.isRefreshing())
				{
					mSwipeRefreshLayout.setRefreshing(false);
				}
				if (e == null) {
					messages = pobjects;
					String[] usernames = new String[messages.size()];
					int i = 0;
					for (ParseObject pobject : messages) {
						usernames[i] = pobject
								.getString(com.shivamb7.chitchat.workers.Constants.SENDER_NAME);
						i++;
					}
					if (usernames.length == 0) {
						mEmptyImage.setVisibility(View.VISIBLE);
						mEmptyText.setVisibility(View.VISIBLE);
						mMessageList.setVisibility(View.GONE);
					} else {
						mEmptyImage.setVisibility(View.GONE);
						mEmptyText.setVisibility(View.GONE);
						mMessageList.setVisibility(View.VISIBLE);
						ChatsAdapter mAdapter = new ChatsAdapter(getActivity(), messages);
						mMessageList.setAdapter(mAdapter);
					}
				}
			}
		});
	}

}
