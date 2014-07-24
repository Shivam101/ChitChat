package com.shivamb7.chitchat.adapters;

import java.util.ArrayList;
import java.util.List;

import com.shivamb7.chitchat.ChatsActivity;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.workers.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseObject;

public class ChatsAdapter extends ArrayAdapter<ParseObject> {
	
	Context mContext;
	List<ParseObject> mMessages;
	
	public ChatsAdapter(Context c,List<ParseObject> messages)
	{
		super(c,R.layout.message_list_item,messages);
		mContext = c;
		mMessages = messages;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null)
		{
		convertView = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, null);
		holder = new ViewHolder();
		holder.iconImage = (ImageView)convertView.findViewById(R.id.message_type_icon);
		holder.nameLabel = (TextView)convertView.findViewById(R.id.sender_name);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		ParseObject message = mMessages.get(position);
		if(message.getString(Constants.FILE_TYPE).equals(Constants.TYPE_PICTURE))
		{
			holder.iconImage.setImageResource(R.drawable.ic_action_picture_orange);
		}
		else if(message.getString(Constants.FILE_TYPE).equals(Constants.TYPE_VIDEO))
		{
			holder.iconImage.setImageResource(R.drawable.ic_action_video_orange);
		}
		else
		{
			holder.iconImage.setImageResource(R.drawable.ic_action_chat_orange);
		}
		holder.nameLabel.setText(message.getString(Constants.SENDER_NAME));
		return convertView;
	}
	
	public static class ViewHolder
	{
		ImageView iconImage;
		TextView nameLabel;
	}

}
