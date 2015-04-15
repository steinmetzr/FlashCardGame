package com.example.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class GameActivity extends Activity {
	Context context = this;
	List<ListCard> cardList = new ArrayList<ListCard>();
	List<GridCard> gridList = new ArrayList<GridCard>();
	GridCardAdapter adapter;
	GridView GridView;
	String filename;
	
	int timerSetting, counter = 0, pos1 = -1, pos2 = -1, sec, min, hr;
	Long totalMSec, maxTotalMSec, timerMSec;
	TextView timeText, ms, ts;
	CountDownTimer countdown;
	Timer timer;
	View promptView, timePrompt;
	AlertDialog alert;
	LayoutInflater layoutInflater;
	AlertDialog.Builder message, timeMessage;
	String optionValues;
	SharedPreferences cardsPrefs, options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Bundle data = this.getIntent().getExtras();
		filename = data.getString("filename");
		timeText = (TextView) findViewById(R.id.timerText);
		timeText.setText(Tools.underLine("FlashCard Game"));
		layoutInflater = LayoutInflater.from(context);
		totalMSec = (long) 300000;
		timerMSec = (long) 0;
		counter = 0;
		pos1 = -1;
		pos2 = -1;
		message = new AlertDialog.Builder(context);
		message.setView(promptView);
		adapter = new GridCardAdapter(this, 0, gridList);
		GridView = (GridView) findViewById(R.id.gameGrid);
		GridView.setAdapter(adapter);
		
		options = getSharedPreferences("", Context.MODE_PRIVATE);
		timerSetting = options.getInt("checked", R.id.noneRadio);	
		
		if(timerSetting == R.id.timeLimitRadio) {
			totalMSec = options.getLong("millisecs", 60000);
			maxTotalMSec = totalMSec;
			
		}
		else {
			totalMSec = (long) 0;
			maxTotalMSec = totalMSec;
		}
		
		cardsPrefs = getSharedPreferences(filename, Context.MODE_PRIVATE);
		if(cardsPrefs.getAll() != null){
			ListCard temp;
			while(cardsPrefs.contains(String.valueOf(cardList.size()))){
				Set<String> cards = cardsPrefs.getStringSet(String.valueOf(cardList.size()), null);
				Iterator<String> it = cards.iterator();
				temp = new ListCard(cardList.size(), it.next(), it.next());
				cardList.add(temp);
			}
		}
		
		Collections.shuffle(cardList);
		adapter.notifyDataSetChanged();
		int maxSize = options.getInt("boardSize", cardList.size());
		
		while(cardList.size() > maxSize)
		{ cardList.remove(cardList.size()-1); }
		
		GridCard gridTemp;
		for(int i=0; i<cardList.size(); i++) {
			gridTemp = new GridCard(i, cardList.get(i).front);
			gridList.add(gridTemp);
			gridTemp = new GridCard(i, cardList.get(i).back);
			gridList.add(gridTemp);
		}
		
		Collections.shuffle(gridList);
		adapter.notifyDataSetChanged();
		
		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		
		//if(maxSize/2 <= 4){
			//GridView.setHorizontalSpacing(width/(maxSize/2));
			GridView.setNumColumns((int) Math.ceil(maxSize/2.0));
			int numRows = gridList.size()/(maxSize/2);
			GridView.setVerticalSpacing(height/(numRows*2));
		/*}
		else{	
			//GridView.setHorizontalSpacing(width/4);
			GridView.setNumColumns(4);
			int numRows = gridList.size()/4;
			GridView.setVerticalSpacing(height/(numRows*2));
		}*/
		
		GridView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
				if(pos1 == -1 && gridList.get(position).color != Color.WHITE) {
					gridList.get(position).color = Color.GRAY;
					adapter.notifyDataSetChanged();
					pos1 = position;
				}		
				else if(pos2 != pos1) {
					gridList.get(position).color = Color.GRAY;
					adapter.notifyDataSetChanged();
					pos2 = position;
					matcher(pos1, pos2);
					pos1 = -1;
					pos2 = -1;
				}
			}
		});
	
		Button quit = (Button) findViewById(R.id.quitButton);
		quit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		Button reset = (Button) findViewById(R.id.resetButton);
		reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				stopTimer(timerSetting);
				layoutInflater = LayoutInflater.from(context);
				promptView = layoutInflater.inflate(R.layout.message, null);
				message = new AlertDialog.Builder(context);
				message.setView(promptView);
				ms = (TextView) promptView.findViewById(R.id.message);
				ms.setText("Do you want to start over and try again !?");
				message.setTitle("Warning")
				 	   .setCancelable(false)
				 	   .setPositiveButton("YES", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								counter = 0;
								totalMSec = maxTotalMSec;
								timerMSec = (long) 0;
								for(int i=0; i<gridList.size();i++) {
									gridList.get(i).color = Color.BLACK;
								}
								Collections.shuffle(gridList);
								adapter.notifyDataSetChanged();
								GridView.setOnItemClickListener(new OnItemClickListener(){
									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
										if(pos1 == -1 && gridList.get(position).color != Color.WHITE) {
											gridList.get(position).color = Color.GRAY;
											adapter.notifyDataSetChanged();
											pos1 = position;
										}		
										else if(pos2 != pos1) {
											stopTimer(totalMSec);
											gridList.get(position).color = Color.GRAY;
											adapter.notifyDataSetChanged();
											pos2 = position;
											matcher(pos1, pos2);
											pos1 = -1;
											pos2 = -1;
										}
									}
								});
								startTimer(timerSetting);
							}
						})
						.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) { 
								startTimer(timerSetting);
								dialog.cancel();
							}
						});
				alert = message.create();
				alert.show();
			}
		});
	}
	
	void startTimer(int timerID){	
		if(timerID == R.id.timeLimitRadio) {
			CountDownTimer temp = new CountDownTimer(totalMSec, 1000) {
			     public void onTick(long mmSec) {
			    	 totalMSec = mmSec;
			         timeText.setText("Countdown: " + hr + ":" + min + ":" + sec);
			        		 sec = (int) (mmSec / 1000) % 60 ;
			        		 min = (int) ((mmSec / (1000*60)) % 60);
			        		 hr = (int) ((mmSec / (1000*60*60)) % 24);
			     }
			     public void onFinish() {
			    	timePrompt = layoutInflater.inflate(R.layout.message, null);
			 		timeMessage = new AlertDialog.Builder(context);
			 		timeMessage.setView(timePrompt); 	 
			     	ts = (TextView) timePrompt.findViewById(R.id.message);
			     	ts.setText("Game Over! \n Try Again?");
			     	timeMessage.setCancelable(true)
			 			   	   .setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
			 						public void onClick(DialogInterface dialog, int id) {
			 							dialog.cancel(); 
			 						}
			 			   	   });
					alert = timeMessage.create();
					alert.show();
			    	GridView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {}
					});
			     }
			  };
			temp.start();
			countdown = temp;
		}
		else if (timerID == R.id.timerRadio) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){
				@Override
				public void run() {
					timerMSec = timerMSec + 1000;
	                runOnUiThread(new Runnable() { 
	                	public void run() { 
	                		timeText.setText("Timer: " + hr + ":" + min + ":" + sec);
			        		sec = (int) (timerMSec / 1000) % 60 ;
			        		min = (int) ((timerMSec / (1000*60)) % 60);
			        		hr = (int) ((timerMSec / (1000*60*60)) % 24);
	                	}
	                });
				}}, 1000, 1000);
	    }
	}
	
	void stopTimer(long value){
		if(value > 0) {
			countdown.cancel();
		}
		else if (value == R.id.timerRadio){
			timer.cancel();
		}
	}
	
	void matcher(int pos1, int pos2){
		//stopTimer(totalMSec);
		//layoutInflater = LayoutInflater.from(context);
		//promptView = layoutInflater.inflate(R.layout.match_message, null);
		//message = new AlertDialog.Builder(context);
		//TextView ms1 = (TextView) promptView.findViewById(R.id.matchCard1);
		//TextView ms2 = (TextView) promptView.findViewById(R.id.matchCard2);
		//message.setView(promptView);
		//ms1.setText(gridList.get(pos1).word);
		//ms2.setText(gridList.get(pos2).word);
		
		//String result;
		if(gridList.get(pos1).id == gridList.get(pos2).id ) { 
			//result = "Matched!"; 
			counter++;
			gridList.get(pos1).color = Color.WHITE;
			gridList.get(pos2).color = Color.WHITE;
			adapter.notifyDataSetChanged();
		}
		else {
			//result = "No Match!";
			gridList.get(pos1).color = Color.BLACK;
			gridList.get(pos2).color = Color.BLACK;
			adapter.notifyDataSetChanged();
		}
		
		if(counter == cardList.size()){
			stopTimer(totalMSec);
			layoutInflater = LayoutInflater.from(context);
			promptView = layoutInflater.inflate(R.layout.message, null);
			message = new AlertDialog.Builder(context);
			message.setView(promptView);
			ms = (TextView) promptView.findViewById(R.id.message);
			ms.setText("Congradulation! Good Effort!");
			message.setCancelable(false)
			 	   .setNeutralButton("confirm", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();    
							GridView.setOnItemClickListener(new OnItemClickListener(){
										@Override
										public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
										}});
						}
					});
			alert = message.create();
			alert.show();
		}
		//else {
			/*
			message.setTitle("Card Match?")
			   .setCancelable(false)
			   .setNeutralButton(result,
		        new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int id) {
		    			startTimer(timerSetting);
		    			dialog.cancel();
		    		}
			    });
			alert = message.create();
			alert.show();
			*/
		//}
	}
	
	@Override
	protected void onResume() {
		startTimer(timerSetting);
		super.onResume();
	}

	@Override
	protected void onPause() {
		stopTimer(timerSetting);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		stopTimer(timerSetting);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		/*Bundle data = new Bundle();
		stopTime(timerSetting);
		data.putString("filename", filename);
		Tools.startIntent(GameActivity.this, FlashCardActivity.class, data, Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
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
						Bundle bundle = new Bundle();
						stopTimer(timerSetting);
						bundle.putString("filename", filename);
						Tools.startIntent(GameActivity.this, FlashCardActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int id) { 
						dialog.cancel(); 
					}
				});
		alert = message.create();
		alert.show();
		//super.onBackPressed();
	}
}
