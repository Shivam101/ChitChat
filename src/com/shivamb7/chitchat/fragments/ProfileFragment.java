package com.shivamb7.chitchat.fragments;



import com.pkmmte.circularimageview.CircularImageView;
import com.shivamb7.chitchat.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.profile_view, container, false);
		//Fab ab = (Fab)rootView.findViewById(R.id.fabbutton);
		//ab.setFabColor(Color.parseColor("#ef6c00"));
		//ab.setFabDrawable(getResources().getDrawable(R.drawable.ic_action_send_now));
		//ab.showFab();
		com.shivamb7.chitchat.workers.CircularImageView img = (com.shivamb7.chitchat.workers.CircularImageView)rootView.findViewById(R.id.profile_picture);
		img.setBorderColor(getResources().getColor(R.color.red));
		
		/*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
		return rootView;
	}

}
