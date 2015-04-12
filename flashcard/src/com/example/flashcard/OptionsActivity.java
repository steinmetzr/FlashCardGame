package com.example.flashcard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
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
	private String filename;
	private final int offset = 4;

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
		
		filename = (OptionsActivity.this).getFilesDir().getPath().toString() + "/options";
		BufferedReader reader = null;
		
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
				Bundle bundle = new Bundle();
				BufferedWriter writer = null;
				
				try{
					writer = new BufferedWriter(new FileWriter(filename));
					
					int size = Integer.valueOf(Tools.toString(boardSizeText));

					bundle.putInt("boardSize", size);
					
					writer.write(Tools.toString(boardSizeText) + "\n");
					
					if(timeRadioGroup.getCheckedRadioButtonId() == R.id.timeLimitRadio){
						int hour = Integer.valueOf(Tools.toString(hourText));
						int min = Integer.valueOf(Tools.toString(minText));
						int sec = Integer.valueOf(Tools.toString(secText));

						bundle.putInt("timerHour", hour);
						bundle.putInt("timerMin", min);
						bundle.putInt("timerSec", sec);
						
						writer.write(Tools.toString(hourText) + "\n");
						writer.write(Tools.toString(minText) + "\n");
						writer.write(Tools.toString(secText));
					}
					
					Tools.startIntent(OptionsActivity.this, MainActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
				}
			}

		});
		
		try{
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			
			boardSizeSeek.setProgress(Integer.parseInt(line) - offset);
			boardSizeText.setText(line);
			if((line = reader.readLine()) != null){
				hourText.setText(line);
				minText.setText(reader.readLine());
				secText.setText(reader.readLine());
				timeRadioGroup.check(R.id.timeLimitRadio);
			}
			else{
				timeRadioGroup.check(R.id.timerRadio);
			}
		}
		catch (IOException e) {}
		catch (NumberFormatException e){}
		finally{
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {}
		}
	}
}
