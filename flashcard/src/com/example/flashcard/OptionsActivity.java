package com.example.flashcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class OptionsActivity extends Activity {
	private SeekBar boardSizeSeek;
	private TextView boardSizeText, hourText, minText, secText;
	private LinearLayout timeLimitText;
	private RadioGroup timeRadioGroup;
	private Button done;
	private String filename = "";
	private final int offset = 4;
	private SharedPreferences options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);

		boardSizeSeek = (SeekBar) findViewById(R.id.boardSizeSeek);
		boardSizeText = (TextView) findViewById(R.id.boardSizeText);
		hourText = (TextView) findViewById(R.id.hour);
		minText = (TextView) findViewById(R.id.min);
		secText = (TextView) findViewById(R.id.sec);
		timeLimitText = (LinearLayout) findViewById(R.id.timeLimitText);
		timeRadioGroup = (RadioGroup) findViewById(R.id.timeRadioGroup);
		done = (Button)findViewById(R.id.done);
		
		options = getSharedPreferences(filename, Context.MODE_PRIVATE);
		
		/**
		 * Listener for Radio Buttons
		 */
		timeRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.timeLimitRadio)
					timeLimitText.setVisibility(View.VISIBLE);
				else
					timeLimitText.setVisibility(View.GONE);
			}
		});


		/**
		 * Listener for Seek Bar
		 */
		boardSizeSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				String size = String.valueOf(progress + offset);
				boardSizeText.setText(size);
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});


		/**
		 * Listener for done button
		 */
		done.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Editor edit = options.edit();
				
				edit.putInt("boardSize", (boardSizeSeek.getProgress() + offset));
				edit.putInt("checked", timeRadioGroup.getCheckedRadioButtonId());
				try{
					if(timeRadioGroup.getCheckedRadioButtonId() == R.id.timeLimitRadio){
						long hour = Long.valueOf(Tools.toString(hourText)) * 3600000;
						long min = Long.valueOf(Tools.toString(minText)) * 60000;
						long sec = Long.valueOf(Tools.toString(secText)) * 1000;
						long millisecs = hour + min + sec;
						
						edit.putString("timerHour", Tools.toString(hourText));
						edit.putString("timerMin", Tools.toString(minText));
						edit.putString("timerSec", Tools.toString(secText));
						edit.putLong("millisecs", millisecs);
					}
					edit.commit();
					Tools.startIntent(OptionsActivity.this, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				catch(NumberFormatException e){
					Tools.toast(OptionsActivity.this, "Must enter value for time limit");
				}
			}
		});
		
		int size = options.getInt("boardSize", 12);
		boardSizeSeek.setProgress(size - offset);
		boardSizeText.setText(String.valueOf(size));
		
		int checked = options.getInt("checked", R.id.timerRadio);
		timeRadioGroup.check(checked);
		
		if(checked == R.id.timeLimitRadio){
			hourText.setText(options.getString("timerHour", ""));
			minText.setText(options.getString("timerMin", ""));
			secText.setText(options.getString("timerSec", ""));
		}
	}
}
