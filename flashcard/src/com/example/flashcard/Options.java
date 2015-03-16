package com.example.flashcard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Options extends PreferenceActivity {
	SeekBar boardSizeSeek;
	TextView boardSizeText, hourText, minText, secText;
	LinearLayout timeLimitText;
	RadioGroup timeRadioGroup;
	String filename = "options";
	File options;

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
		
		/*BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new FileReader(filename));
			String[] file = null;
			int i = 0;
			while(reader.readLine() != null){
				file[i] = reader.readLine();
				i++;
			}
			hourText.setText(file[0]);
			minText.setText(file[1]);
			secText.setText(file[2]);
			boardSizeText.setText(file[3]);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(reader != null)
			try{
				reader.close();
			}
			catch(IOException e){}
		}*/

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
				String size = String.valueOf(progress+4);
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
		Button done = (Button)findViewById(R.id.done);
		done.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				
				try{
					if(timeLimitText.getVisibility() == View.VISIBLE){
						int hour = Integer.valueOf(Tools.toString(hourText));
						int min = Integer.valueOf(Tools.toString(minText));
						int sec = Integer.valueOf(Tools.toString(secText));

						bundle.putInt("timerHour", hour);
						bundle.putInt("timerMin", min);
						bundle.putInt("timerSec", sec);
					}
					int size = Integer.valueOf(Tools.toString(boardSizeText));

					bundle.putInt("boardSize", size);

					//FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
					/*BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

					writer.write(Tools.toString(hourText));
					writer.write(Tools.toString(minText));
					writer.write(Tools.toString(secText));
					writer.write(Tools.toString(boardSizeText));*/

					/*
					outputStream.write(Tools.toString(hourText).getBytes());
					outputStream.write(Tools.toString(minText).getBytes());
					outputStream.write(Tools.toString(secText).getBytes());
					outputStream.write(Tools.toString(boardSizeText).getBytes());

					outputStream.close();
					 */
					//writer.close();
					Tools.startIntent(Options.this, MainActivity.class, bundle);
				}
				catch(NumberFormatException e){
					Toast.makeText(Options.this, "Error: must enter value for timer", Toast.LENGTH_SHORT).show();
				}
				/*catch (FileNotFoundException e) {
					Toast.makeText(Options.this, "Error: FileNotFound", Toast.LENGTH_SHORT).show();
				}
				catch (IOException e) {
					Toast.makeText(Options.this, "Error: IOException", Toast.LENGTH_SHORT).show();
				}*/
			}

		});
	}
}
