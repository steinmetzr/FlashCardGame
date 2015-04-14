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
		
		/*filename = getApplication().getFilesDir().getPath().toString() + "/options";
		BufferedReader reader = null;*/
		
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
				
				/*
				//Bundle bundle = new Bundle();
				BufferedWriter writer = null;
				
				try{
					writer = new BufferedWriter(new FileWriter(filename));
					FileWriter file = new FileWriter(filename);
					
					//int size = Integer.valueOf(Tools.toString(boardSizeText));

					//bundle.putInt("boardSize", size);
					
					writer.write(Tools.toString(boardSizeText) + "\n");
					writer.write(timeRadioGroup.getCheckedRadioButtonId() + "\n");
					
					if(timeRadioGroup.getCheckedRadioButtonId() == R.id.timeLimitRadio){
						//int hour = Integer.valueOf(Tools.toString(hourText));
						int min = Integer.valueOf(Tools.toString(minText));
						int sec = Integer.valueOf(Tools.toString(secText));

						bundle.putInt("timerHour", hour);
						bundle.putInt("timerMin", min);
						bundle.putInt("timerSec", sec);//
						int hour = Integer.valueOf(Tools.toString(hourText)) * 3600000;
						int min = Integer.valueOf(Tools.toString(minText)) * 6000;
						int sec = Integer.valueOf(Tools.toString(secText)) * 1000;
						int millisecs = hour + min + sec;
						
						writer.write(Tools.toString(hourText) + "\n");
						writer.write(Tools.toString(minText) + "\n");
						writer.write(Tools.toString(secText));
						writer.write(String.valueOf(millisecs));
					}
					
					Tools.startIntent(OptionsActivity.this, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				catch(NumberFormatException e){
					Tools.toast(OptionsActivity.this, "Must enter value for time limit");
				}
				catch (FileNotFoundException e) {
					Tools.toast(OptionsActivity.this, e.getMessage());
				}
				catch (IOException e) {
					Tools.toast(OptionsActivity.this, e.getMessage());
				}
				finally{
					try {
						if(writer != null)
							writer.close();
					} catch (IOException e) {}
				}*/
			}
		});
		
		int size = options.getInt("boardSize", 18);
		boardSizeSeek.setProgress(size - offset);
		boardSizeText.setText(String.valueOf(size));
		
		int checked = options.getInt("checked", R.id.timerRadio);
		timeRadioGroup.check(checked);
		
		if(checked == R.id.timeLimitRadio){
			hourText.setText(options.getString("timerHour", ""));
			minText.setText(options.getString("timerMin", ""));
			secText.setText(options.getString("timerSec", ""));
		}
		
		/*try{
			reader = new BufferedReader(new FileReader(filename));
			String size = reader.readLine();
			
			boardSizeSeek.setProgress(Integer.parseInt(size) - offset);
			boardSizeText.setText(size);
			int checked = Integer.parseInt(reader.readLine());
			timeRadioGroup.check(checked);
			if(checked == R.id.timeLimitRadio){
				hourText.setText(reader.readLine());
				minText.setText(reader.readLine());
				secText.setText(reader.readLine());
			}
		}
		catch (IOException e) {}
		catch (NumberFormatException e){}
		finally{
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {}
		}*/
	}
}
