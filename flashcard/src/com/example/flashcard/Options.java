package com.example.flashcard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class Options extends ActionBarActivity {
	private SeekBar boardSizeSeek;
	private TextView boardSizeText, hourText, minText, secText;
	private LinearLayout timeLimitText;
	private RadioGroup timeRadioGroup;
	private String filename;
	private File options;

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
		filename = (Options.this).getFilesDir().getPath().toString() + "/options";
		//options = new File((Options.this).getFilesDir().getPath().toString() + "/" + filename);
		
		/*try {
			byte[] buffer = new byte[10];
			if(options.exists()){
				FileInputStream inputStream = openFileInput(filename);
				inputStream.read(buffer);
				String[] options =  buffer.toString().split("\n");
				for(String o : options){
					Log.v("Debug", o);
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new FileReader(filename));
			String[] file = new String[4];
			
			for(int i = 0; i < 4; i++){
				file[i] = reader.readLine();
			}
			boardSizeText.setText(file[0]);
			boardSizeSeek.setProgress(Integer.parseInt(file[0]));
			if(file.length > 1){
				hourText.setText(file[1]);
				minText.setText(file[2]);
				secText.setText(file[3]);
				timeRadioGroup.check(R.id.timeLimitRadio);
			}
			else{
				timeRadioGroup.check(R.id.timerRadio);
			}
				
			
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
		}

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
					BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
					
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
					
					writer.write(Tools.toString(boardSizeText) + "\n");
					writer.write(Tools.toString(hourText) + "\n");
					writer.write(Tools.toString(minText) + "\n");
					writer.write(Tools.toString(secText));
					
					writer.close();
					/*
					FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
					outputStream.write((Tools.toString(hourText) + "\n").getBytes());
					outputStream.write((Tools.toString(minText) + "\n").getBytes());
					outputStream.write((Tools.toString(secText) + "\n").getBytes());
					outputStream.write(Tools.toString(boardSizeText).getBytes());

					outputStream.close();
					*/
					Tools.startIntent(Options.this, MainActivity.class, bundle);
				}
				catch(NumberFormatException e){
					Toast.makeText(Options.this, "Error: must enter value for timer", Toast.LENGTH_SHORT).show();
				}
				catch (FileNotFoundException e) {
					Toast.makeText(Options.this, "Error: FileNotFound", Toast.LENGTH_SHORT).show();
				}
				catch (IOException e) {
					Toast.makeText(Options.this, "Error: IOException", Toast.LENGTH_SHORT).show();
				}
			}

		});
	}
}
