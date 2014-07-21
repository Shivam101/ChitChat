package com.shivamb7.chitchat.fragments;



import com.shivamb7.chitchat.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatsFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getActivity().setProgressBarIndeterminateVisibility(false);
		View rootView = inflater.inflate(R.layout.under_construction, container, false);
		return rootView;
	}

}
