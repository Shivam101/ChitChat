package com.shivamb7.chitchat.workers;

import com.parse.Parse;
import com.shivamb7.chitchat.R;
import android.app.Application;

public class ChitChatApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "d9MXBG0WtMmelsAAmliJqEpuX6i2cgoGsTOvapE0",
				"H187AlMPaa5WEZKMI3q5m02qc5lBBUCvgHIrqV3X");
	}

}
