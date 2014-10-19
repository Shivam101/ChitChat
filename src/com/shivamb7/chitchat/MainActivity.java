package com.shivamb7.chitchat;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.workers.ChitChatApplication;
import com.shivamb7.chitchat.workers.RippleView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button mRegisterButton,mSignInButton;
	EditText mUserName,mPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar ab = getActionBar();
		ab.hide();
		TextView mHeader = (TextView)findViewById(R.id.app_name_heading);
		//Typeface ironman = Typeface.createFromAsset(getAssets(), "actionman.ttf");
		//mHeader.setTypeface(ironman);
		mRegisterButton = (Button)findViewById(R.id.register_button);
		mSignInButton = (Button)findViewById(R.id.signin_button);
		mUserName = (EditText)findViewById(R.id.username_field);
		mPassword = (EditText)findViewById(R.id.password_field);
		//new RippleView(this,findViewById(R.id.signin_button));
		new RippleView(this,findViewById(R.id.register_button));
		mRegisterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		mSignInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = mUserName.getText().toString();
				String password = mPassword.getText().toString();
				username=username.trim();
				password=password.trim();
				if(password.isEmpty()||username.isEmpty())
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage(R.string.empty_values_1);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else
				{
					//login the user
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						
						@Override
						public void done(ParseUser user, ParseException e) {
							if(e==null)
							{
								//success
								ChitChatApplication.updateInstallation(user);
								Intent i = new Intent(MainActivity.this,ChatsActivity.class);
								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
							}
							else
							{
								AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
								builder.setMessage("Couldn't Sign Up! Try again");
								builder.setTitle(R.string.signup_error_title);
								builder.setPositiveButton(android.R.string.ok, null);
								AlertDialog dialog = builder.create();
								dialog.show();
							
							}
						}
					});
				}
			}
		});

	}

}