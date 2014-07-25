package com.shivamb7.chitchat.fragments;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.shivamb7.chitchat.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
	
	TextView mName,mUsername,mFriends;
	int PICK_PICTURE = 100;
	Uri pictureUri;
	com.shivamb7.chitchat.workers.CircularImageView img;
	ParseUser currentUser = ParseUser.getCurrentUser();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.profile_view, container, false);
		mName = (TextView)rootView.findViewById(R.id.profile_name);
		mUsername = (TextView)rootView.findViewById(R.id.profile_username);
		mFriends = (TextView)rootView.findViewById(R.id.profile_friends);
		mName.setText(currentUser.getString("Name"));
		mUsername.setText(currentUser.getUsername());
		String friendCount = (ContactsFragment.numberofFriends)+" friends";
		mFriends.setText(friendCount);
		img = (com.shivamb7.chitchat.workers.CircularImageView)rootView.findViewById(R.id.profile_picture);
		img.setBorderColor(getResources().getColor(R.color.orange_800));
		img.setBorderWidth(40);
		img.setSelectorColor(getResources().getColor(R.color.orange_800));
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "Stuuf", Toast.LENGTH_SHORT).show();
				Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
				choosePictureIntent.setType("image/*");
				getActivity().startActivityForResult(choosePictureIntent, PICK_PICTURE);
			}
		});
		/*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//getActivity();
		if(resultCode == Activity.RESULT_OK)
		{
			if(requestCode == PICK_PICTURE)
			{
				if(data==null)
				{
					Toast.makeText(getActivity(), "Try picking a file again", Toast.LENGTH_SHORT).show();
				}
				else
				{
					pictureUri = data.getData();
					//Picasso.with(getActivity()).load(pictureUri.toString()).into(img);
				}
			}
			else
			{
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

}
