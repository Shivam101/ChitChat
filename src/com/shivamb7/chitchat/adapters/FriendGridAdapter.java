package com.shivamb7.chitchat.adapters;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.workers.Constants;

public class FriendGridAdapter extends ArrayAdapter<ParseUser> {
	
	Context mContext;
	List<ParseUser> mFriends;
	
	public FriendGridAdapter(Context c,List<ParseUser> messages)
	{
		super(c,R.layout.message_list_item,messages);
		mContext = c;
		mFriends = messages;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null)
		{
		convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_grid_item, null);
		holder = new ViewHolder();
		holder.iconCheck = (ImageView)convertView.findViewById(R.id.user_image_check);
		holder.nameLabel = (TextView)convertView.findViewById(R.id.friend_name);
		//holder.timeLabel = (TextView)convertView.findViewById(R.id.message_time);
		convertView.setTag(holder); //VERY IMPORTANT LINE !!!
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		ParseUser friend = mFriends.get(position);
		/*Date createdDate = message.getCreatedAt();
		long currentTime = new Date().getTime();
		String convertedTime = DateUtils.getRelativeTimeSpanString(createdDate.getTime(), currentTime, DateUtils.SECOND_IN_MILLIS).toString();
		holder.timeLabel.setText(convertedTime);
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
		}*/
		holder.nameLabel.setText(friend.getUsername());
		GridView gv = (GridView)parent;
		if(gv.isItemChecked(position))
		{
			holder.iconCheck.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.iconCheck.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	public static class ViewHolder
	{
		ImageView iconCheck;
		TextView nameLabel;
		//TextView timeLabel;
	}
	
	public void refreshAdapter(List<ParseUser> users)
	{
		mFriends.clear();
		mFriends.addAll(users);
		notifyDataSetChanged();
	}

}
