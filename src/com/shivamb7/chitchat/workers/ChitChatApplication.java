package com.shivamb7.chitchat.workers;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.shivamb7.chitchat.ChatsActivity;
import com.shivamb7.chitchat.R;

public class ChitChatApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "d9MXBG0WtMmelsAAmliJqEpuX6i2cgoGsTOvapE0",
				"H187AlMPaa5WEZKMI3q5m02qc5lBBUCvgHIrqV3X");
		PushService.setDefaultPushCallback(this, ChatsActivity.class,R.drawable.ic_stat_ic_launcher_web);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
	public static void updateInstallation(ParseUser user)
	{
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put(Constants.USER_ID, user.getObjectId());
		installation.saveInBackground();
	}

}
