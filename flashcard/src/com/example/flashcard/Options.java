package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Options extends ActionBarActivity {
	SeekBar boardSizeSeek;
	TextView boardSizeText, hourText, minText, secText;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		boardSizeSeek = (SeekBar) findViewById(R.id.boardSizeSeek);
		boardSizeText = (TextView) findViewById(R.id.boardSizeText);
		hourText = (TextView) findViewById(R.id.hour);
		minText = (TextView) findViewById(R.id.min);
		secText = (TextView) findViewById(R.id.sec);
		
		boardSizeSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				String size = String.valueOf(progress+4);
				boardSizeText.setText(size);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		Button done = (Button)findViewById(R.id.done);
		done.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent main = new Intent().setClass(Options.this, MainActivity.class);
				Bundle bundle = new Bundle();
				
				try{
					int size = Integer.valueOf(tvToString(boardSizeText));
					int hour = Integer.valueOf(tvToString(hourText));
					int min = Integer.valueOf(tvToString(minText));
					int sec = Integer.valueOf(tvToString(secText));
					
					bundle.putInt("boardSize", size);
					bundle.putInt("timerHour", hour);
					bundle.putInt("timerMin", min);
					bundle.putInt("timerSec", sec);
					main.putExtras(bundle);
					startActivity(main);
				}
				catch(NumberFormatException e){
					Toast.makeText(Options.this, "Error: must enter value for timer", 3).show();
				}
			}
			
			public String tvToString(TextView textView){
				return textView.getText().toString();
			}
		});
	}
}
