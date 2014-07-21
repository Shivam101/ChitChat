package com.shivamb7.chitchat;

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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shivamb7.chitchat.R;
import com.shivamb7.chitchat.workers.RippleView;

public class RegisterActivity2 extends Activity {

	EditText mName,mEmail;
	Button mFinalSignUp;
	TextView mHeader;
	public Dialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_activity2);
		mName = (EditText)findViewById(R.id.name_field);
		mEmail = (EditText)findViewById(R.id.email_field);
		mHeader = (TextView)findViewById(R.id.register_heading);
		Typeface ironman = Typeface.createFromAsset(getAssets(), "actionman.ttf");
		mHeader.setTypeface(ironman);
		mFinalSignUp = (Button)findViewById(R.id.register_final);
		new RippleView(this,mFinalSignUp);
		ActionBar ab = getActionBar();
		ab.hide();
		mFinalSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 String name = mName.getText().toString();
				 String email = mEmail.getText().toString();
				 email = email.trim();
				 ParseUser currentUser = ParseUser.getCurrentUser();
				 if(name.isEmpty()||email.isEmpty())
				 {
					AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity2.this);
					builder.setMessage(R.string.empty_values_1);
					builder.setTitle(R.string.signup_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				 }
				 else
				 {
					 currentUser.setEmail(email);
					 currentUser.put("Name", name);
					 RegisterActivity2.this.progressDialog=ProgressDialog.show(RegisterActivity2.this, "", "Getting Ready to ChitChat", true);
					 currentUser.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e==null)
							{
								RegisterActivity2.this.progressDialog.dismiss();
								Intent i = new Intent(RegisterActivity2.this,ChatsActivity.class);
								i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
							}
							else
							{
								RegisterActivity2.this.progressDialog.dismiss();
								AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity2.this);
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
