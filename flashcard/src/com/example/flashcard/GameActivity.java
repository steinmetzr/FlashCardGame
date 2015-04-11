package com.example.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
	Long totalMSec, maxTotalMSec;
	TextView timeText, ms, ts;
	CountDownTimer countdown;
	Timer timer;
	View promptView, timePrompt;
	AlertDialog alert;
	LayoutInflater layoutInflater;
	AlertDialog.Builder message, timeMessage;
	String[] frontSide = {"aaaaaaaaaaaaaaaa","b","c","d","e","f","gggggggggggg","h","i","j","k","l","mmmmmmmmmmmmmmmmm"};
	String[] backSide =  {"aaaaaaaaaaaaaaaa","b","c","d","e","f","gggggggggggg","h","i","j","k","l","mmmmmmmmmmmmmmmmm"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Bundle data = this.getIntent().getExtras();
		filename = data.getString("filename");
		//totalMSec = data.getLong("timeValue");
		timeText = (TextView) findViewById(R.id.timerText);
		timeText.setText(filename);
		
		layoutInflater = LayoutInflater.from(context);
		totalMSec = (long) 7200000;
		maxTotalMSec = totalMSec;
		timeText.setText(filename);
		message = new AlertDialog.Builder(context);
		message.setView(promptView);
		adapter = new GridCardAdapter(this, 0, list);
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

				if(pos_1 == -1 && list.get(position).color != Color.WHITE) {
					list.get(position).color = Color.GRAY;
					adapter.notifyDataSetChanged();
					pos_1 = position;
				}
				else if(pos_2 == -1 && list.get(position).color != Color.WHITE && pos_2 != pos_1) {
					stopTime(totalMSec);
					list.get(position).color = Color.GRAY;
					adapter.notifyDataSetChanged();
					pos_2 = position;
					if(matcher(pos_1, pos_2)) {
						counter++;
						list.get(pos_1).color = Color.WHITE;
						list.get(pos_2).color = Color.WHITE;
						adapter.notifyDataSetChanged();
					}
					else {
						list.get(pos_1).color = Color.BLACK;
						list.get(pos_2).color = Color.BLACK;
						adapter.notifyDataSetChanged();
					}
					pos_1 = -1;
					pos_2 = -1;
				}
				
				if(counter == listSize){
					stopTime(totalMSec);
					timeText.setText("Game Over!");
					GridView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
						}});
					layoutInflater = LayoutInflater.from(context);
					promptView = layoutInflater.inflate(R.layout.message, null);
					//message = new AlertDialog.Builder(context);
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

				};
			}
		});
	
		startTimer(totalMSec);
		
		Button quit = (Button) findViewById(R.id.quitButton);
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
								Bundle bundle = new Bundle();
								bundle.putString("filename", filename);
								Tools.startIntent(GameActivity.this, FlashCardActivity.class, bundle);
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
		
		Button reset = (Button) findViewById(R.id.resetButton);
		reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				stopTime(totalMSec);
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
								for(int i=0; i<list.size();i++) {
									list.get(i).color = Color.BLACK;
								}
								GridView.setOnItemClickListener(new OnItemClickListener(){
									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	

										if(pos_1 == -1 && list.get(position).color != Color.WHITE) {
											list.get(position).color = Color.GRAY;
											adapter.notifyDataSetChanged();
											pos_1 = position;
										}
										else if(pos_2 == -1 && list.get(position).color != Color.WHITE && pos_2 != pos_1) {
											stopTime(totalMSec);
											list.get(position).color = Color.GRAY;
											adapter.notifyDataSetChanged();
											pos_2 = position;
											if(matcher(pos_1, pos_2)) {
												counter++;
												list.get(pos_1).color = Color.WHITE;
												list.get(pos_2).color = Color.WHITE;
												adapter.notifyDataSetChanged();
											}
											else {
												list.get(pos_1).color = Color.BLACK;
												list.get(pos_2).color = Color.BLACK;
												adapter.notifyDataSetChanged();
											}
											pos_1 = -1;
											pos_2 = -1;
										}
										
										if(counter == listSize){
											stopTime(totalMSec);
											timeText.setText("Game Over!");
											GridView.setOnItemClickListener(new OnItemClickListener(){
												@Override
												public void onItemClick(AdapterView<?> parent, View view,
														int position, long id) {
												}});
											layoutInflater = LayoutInflater.from(context);
											promptView = layoutInflater.inflate(R.layout.message, null);
											//message = new AlertDialog.Builder(context);
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

										};
									}
								});
								adapter.notifyDataSetChanged();
								startTimer(totalMSec);
								
							}
						})
						.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) { 
								startTimer(totalMSec);
								dialog.cancel();
							}
						});
				alert = message.create();
				alert.show();
			}
		});
	}
	
	void startTimer(long value){
		if(value > 0) {
			countdown = new CountDownTimer(totalMSec, 1000) {
			     public void onTick(long MSec) {
			    	 totalMSec = MSec;
			         timeText.setText("Countdown: " + hr + ":" + min + ":" + sec);
			        		 sec = (int) (MSec / 1000) % 60 ;
			        		 min = (int) ((MSec / (1000*60)) % 60);
			        		 hr = (int) ((MSec / (1000*60*60)) % 24);
			     }
			     public void onFinish() {
			    	timeText.setText("Game Over");
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
			countdown.start();
		}
		else if (value == 0) {
			
		}
	}
	
	void stopTime(long value){
		if(value > 0) {
			countdown.cancel();
		}
		else if (value == 0){
			
		}
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
		
		message.setTitle("Card Match?")
			   .setCancelable(true)
			   .setNeutralButton(result,
		        new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int id) {
		    			startTimer(totalMSec);
		    			dialog.cancel();
		    		}
			    });
		
		alert = message.create();
		alert.show();
		return isMatched;
		
	}
	
	@Override
	protected void onResume() {
		startTimer(totalMSec);
		super.onResume();
	}

	@Override
	protected void onPause() {
		stopTime(totalMSec);
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		Tools.startIntent(this, FlashCardActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		super.onBackPressed();
	}
}
