package com.shivamb7.chitchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shivamb7.chitchat.workers.Constants;

public class ComposeTextActivity extends Activity {

	static String msg;
	Uri mMediaUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_text);
		Button b = (Button)findViewById(R.id.button1);
		final EditText et1 = (EditText)findViewById(R.id.editText1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				msg = et1.getText().toString();
				if(msg.isEmpty())
				{
					Toast.makeText(ComposeTextActivity.this, R.string.empty_text_message, Toast.LENGTH_SHORT).show();
				}
				else if(msg.length()>140)
				{
					Toast.makeText(ComposeTextActivity.this, R.string.long_text_error, Toast.LENGTH_SHORT).show();
				}
				else
				{
				Intent i = new Intent(ComposeTextActivity.this,RecipientsActivity.class);
				i.putExtra(com.shivamb7.chitchat.workers.Constants.FILE_TYPE, Constants.TEXT_TYPE);
				mMediaUri = Uri.parse("http://www.google.com");
				i.setData(mMediaUri);
				startActivity(i);
				}
			}
		});
}
}
