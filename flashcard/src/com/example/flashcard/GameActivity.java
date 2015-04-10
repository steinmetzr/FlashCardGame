package com.example.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.graphics.Color;

public class GameActivity extends Activity {
	Context context = this;
	List<GridCard> list = new ArrayList<GridCard>();
	GridCardAdapter adapter;
	GridView GridView;
	String filename;
	
	int counter = 0, pos_1 = -1, pos_2 = -1, sec, min, hr, listSize;
	Long totalMillis, maxTotalMillis;
	TextView timeText, ms;
	CountDownTimer countdown;
	View promptView;
	AlertDialog alert;
	LayoutInflater layoutInflater;
	AlertDialog.Builder message;
	String[] frontSide = {"a","b","c","d","e","f","g","h","i","j","k","l","m"};
	String[] backSide =  {"a","b","c","d","e","f","g","h","i","j","k","l","m"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		timeText = (TextView) findViewById(R.id.timerText);
		layoutInflater = LayoutInflater.from(context);
		totalMillis = (long) 7200000;
		maxTotalMillis = totalMillis;
		//timeText.setText(filename);
		
		countdown = new CountDownTimer(totalMillis, 1000) {
		     public void onTick(long millis) {
		    	 totalMillis = millis;
		         timeText.setText("Countdown: " + hr + ":" + min + ":" + sec);
		        		 sec = (int) (millis / 1000) % 60 ;
		        		 min = (int) ((millis / (1000*60)) % 60);
		        		 hr = (int) ((millis / (1000*60*60)) % 24);
		     }
		     public void onFinish() {
		    	 timeText.setText("Game Over!");
		    	 GridView.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}});
		     }
		  };
		countdown.start();
		
		GridCardAdapter adapter = new GridCardAdapter(this, 0, list);
		GridView = (GridView) findViewById(R.id.gameGrid);
		GridView.setAdapter(adapter);
		
		listSize = 13;
	
		for(int i=0; i<listSize ; i++){
			GridCard temp = new GridCard();
			temp.id = i;
			temp.word = frontSide[i];
			list.add(temp);
			
			temp = new GridCard();
			temp.id = i;
			temp.word = backSide[i];
			list.add(temp);
		}
		
		Collections.shuffle(list);
		adapter.notifyDataSetChanged();
		GridView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
				Log.v("grid", "gridItem: " + position);
				list.get(position).color = Color.GREEN;
				if(pos_1 == -1) {
					pos_1 = position;
					list.get(position).color = Color.GREEN;
				}
				else if(pos_2 == -1 && pos_2 != pos_1) {
					pos_2 = position;

					countdown.cancel();
					if(matcher(pos_1, pos_2)) {
						counter++;
					}
					else {
						list.get(pos_1).color = Color.BLACK;
						list.get(pos_2).color = Color.BLACK;
					}
					pos_1 = -1;
					pos_2 = -1;
				}
				
				if(counter == listSize){
					
				};
			}
		});

		Button quit = (Button) findViewById(R.id.quitButton);
		Button reset = (Button) findViewById(R.id.resetButton);
		
		quit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layoutInflater = LayoutInflater.from(context);
				promptView = layoutInflater.inflate(R.layout.message, null);
				message = new AlertDialog.Builder(context);
				message.setView(promptView);
				ms = (TextView) promptView.findViewById(R.id.message);
				ms.setText("Are you sure you want to quit! Game will reset!");
				message.setTitle("Warning")
				 	   .setCancelable(false)
				 	   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								quitter();
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) { 
								dialog.cancel(); 
							}
						});
				alert = message.create();
				alert.show();
			}
		});
		
		reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				layoutInflater = LayoutInflater.from(context);
				promptView = layoutInflater.inflate(R.layout.message, null);
				message = new AlertDialog.Builder(context);
				message.setView(promptView);
				ms = (TextView) promptView.findViewById(R.id.message);
				ms.setText("Are you sure you want to reset your progress!");
				message.setTitle("Warning")
				 	   .setCancelable(false)
				 	   .setPositiveButton("YES", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								counter = 0;
								totalMillis = maxTotalMillis;
								for(int i=0; i<list.size();i++) {
									list.get(i).color = Color.BLACK;
								}
							}
						})
						.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) { 
								dialog.cancel(); 
							}
						});
				alert = message.create();
				alert.show();
			}
		});
	}
	
	boolean matcher(int pos_1, int pos_2){
		boolean isMatched = false;
		layoutInflater = LayoutInflater.from(context);
		promptView = layoutInflater.inflate(R.layout.match_message, null);
		message = new AlertDialog.Builder(context);
		TextView ms1 = (TextView) promptView.findViewById(R.id.matchCard1);
		TextView ms2 = (TextView) promptView.findViewById(R.id.matchCard2);
		message.setView(promptView);
		ms1.setText(list.get(pos_1).word);
		ms2.setText(list.get(pos_2).word);
		
		String result = "No Match";
		if(list.get(pos_1).id == list.get(pos_2).id ) { 
			result = "Matched!"; 
			isMatched = true;
		};
		
		message.setCancelable(true)
			   .setNeutralButton(result,
		        new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int id) {
		    			dialog.cancel();
		    			countdown = new CountDownTimer(totalMillis, 1000) {
						     public void onTick(long millis) {
						         timeText.setText("Countdown: " + hr + ":" + min + ":" + sec);
						        		 sec = (int) (millis / 1000) % 60 ;
						        		 min = (int) ((millis / (1000*60)) % 60);
						        		 hr = (int) ((millis / (1000*60*60)) % 24);
						     }
						     public void onFinish() {
						    	 timeText.setText("Game Over!");
						    	 GridView.setOnItemClickListener(new OnItemClickListener(){
									@Override
									public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
									}});
						     }
						  };
						countdown.start();
		    		}
			    });
		
		alert = message.create();
		alert.show();
		return isMatched;
		
	}
	
	public void quitter() {
		Tools.startIntent(this, FlashCardActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}
	
	@Override
	public void onBackPressed() {
		Tools.startIntent(this, FlashCardActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		super.onBackPressed();
	}
}
