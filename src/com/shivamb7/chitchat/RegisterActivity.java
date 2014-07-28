package com.shivamb7.chitchat;

import java.util.Currency;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.workers.ChitChatApplication;
import com.shivamb7.chitchat.workers.RippleView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	EditText mUserName,mPassword;
	TextView mHeading;
	Button mNextRegister;
	public Dialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar ab = getActionBar();
		ab.hide();
		
		Typeface ironman = Typeface.createFromAsset(getAssets(), "actionman.ttf");
		mHeading = (TextView)findViewById(R.id.register_heading);
		mHeading.setTypeface(ironman);
		mUserName = (EditText)findViewById(R.id.username_field);
		mPassword = (EditText)findViewById(R.id.password_field);
		mNextRegister = (Button)findViewById(R.id.register_next);
		new RippleView(this,mNextRegister);
		mNextRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = mUserName.getText().toString();
				String password = mPassword.getText().toString();
				username = username.trim();
				password = password.trim();
				if(password.isEmpty()||username.isEmpty())
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
					builder.setMessage(R.string.empty_values_2);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else
				{
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					RegisterActivity.this.progressDialog=ProgressDialog.show(RegisterActivity.this, "", "Signing Up...", true);
					newUser.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e==null)
							{
								//success !
								ChitChatApplication.updateInstallation(ParseUser.getCurrentUser());
								RegisterActivity.this.progressDialog.dismiss();
								Intent i = new Intent(RegisterActivity.this,RegisterActivity2.class);
								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
							}
							else
							{
								RegisterActivity.this.progressDialog.dismiss();
								AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
								builder.setMessage(e.getMessage());
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
