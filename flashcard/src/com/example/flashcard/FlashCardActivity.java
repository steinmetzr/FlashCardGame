package com.example.flashcard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class FlashCardActivity extends Activity {
	Context context = this;
	ListCardAdapter adapter;
	ListView listView;
	String filename;
	File fileDir;
	List<ListCard> list = new ArrayList<ListCard>();
	boolean fileType = false;
	LayoutInflater layoutInflater;
	TextView ms;
	View promptView, messageView;
	AlertDialog alert;
	AlertDialog.Builder editPrompt, addPrompt, message;
	SharedPreferences cardsPrefs;
	Editor editor;
	
	Comparator<ListCard> fSort = new Comparator<ListCard>(){
		@Override
		public int compare(ListCard left, ListCard right) {
			return left.front.compareToIgnoreCase(right.front);
		}
	};
	
	Comparator<ListCard> bSort = new Comparator<ListCard>(){
		
		@Override
		public int compare(ListCard left, ListCard right) {
			return left.back.compareToIgnoreCase(right.back);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_card);
		layoutInflater = LayoutInflater.from(context);
		message = new AlertDialog.Builder(context);
		message.setTitle("Notice")
	 	   .setCancelable(true)
		   .setNeutralButton("OK",
	        new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			dialog.cancel();
	    		}
		   });
		editPrompt = new AlertDialog.Builder(context);
		addPrompt = new AlertDialog.Builder(context);

		Bundle data = this.getIntent().getExtras();
		filename = data.getString("filename");

		cardsPrefs = getSharedPreferences(filename, Context.MODE_PRIVATE);
		Set<String> keys = cardsPrefs.getAll().keySet();
		for(String key : keys){
			Set<String> cards = cardsPrefs.getStringSet(key, null);
			Iterator<String> it = cards.iterator();
			ListCard temp = new ListCard(list.size(), it.next(), it.next());
			list.add(temp);
		}
		
		TextView title = (TextView) findViewById(R.id.fileTitle1);
		title.setText(Tools.underLine(filename));

		Button addCards = (Button) findViewById(R.id.addButton);
		Button removeCards = (Button) findViewById(R.id.removeButton);
		Button play = (Button) findViewById(R.id.playButton);
		Button front = (Button) findViewById(R.id.frontButton);
		Button back = (Button) findViewById(R.id.backButton);
		
		adapter = new ListCardAdapter(this, 0, list, fileType);
		listView = (ListView) findViewById(R.id.cardList);
		listView.setAdapter(adapter);
		editor = cardsPrefs.edit();
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				View promptView = layoutInflater.inflate(R.layout.add_card, null);
				editPrompt.setView(promptView);
				editPrompt.setTitle("Edit Card");
				final EditText FInput = (EditText) promptView.findViewById(R.id.editFront);
				FInput.setText(list.get(pos).front);
				final EditText BInput = (EditText) promptView.findViewById(R.id.editBack);
				BInput.setText(list.get(pos).back);
				editPrompt
					.setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// get user input and set it to result
							
							if((FInput.getText().toString().trim().length() == 0) || 
							   (BInput.getText().toString().trim().length() == 0)) {
								message.setMessage("Cards cannot have a blank side!");
								alert = message.create();
								alert.show();
							}
							else if(FInput.getText().toString().trim().equals(BInput.getText().toString().trim())){
								message.setMessage("Cards sides cannot be the same!");
								alert = message.create();
								alert.show();
							}
							else {
								list.get(pos).front = FInput.getText().toString();
								list.get(pos).back = BInput.getText().toString();
								adapter.notifyDataSetChanged();
								
								Set<String> card = new LinkedHashSet<String>();
								card.add(list.get(pos).front);
								card.add(list.get(pos).back);
								editor.putStringSet(String.valueOf(list.get(pos).id), card);
								
								Log.v("myTest", FInput.getText().toString() + "\t" + 
												BInput.getText().toString() + "\n");
							}
							
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id) { 
							dialog.cancel(); 
						}
					});

				alert = editPrompt.create();
				alert.show();
				return true;
			}
		});
	
		addCards.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "add button is clicked");
				View promptView = layoutInflater.inflate(R.layout.add_card, null);
				addPrompt.setView(promptView);
				addPrompt.setTitle("Add Card");
				final EditText FInput = (EditText) promptView.findViewById(R.id.editFront);
				final EditText BInput = (EditText) promptView.findViewById(R.id.editBack);
				// setup a dialog window
				addPrompt
					.setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// get user input and set it to result
							
							if((FInput.getText().toString().trim().length() == 0) || 
							   (BInput.getText().toString().trim().length() == 0)) {
								message.setMessage("Cards cannot have a blank side!");
								alert = message.create();
								alert.show();
							}
							else if(FInput.getText().toString().trim().equals(BInput.getText().toString().trim())){
								message.setMessage("Cards sides cannot be the same!");
								alert = message.create();
								alert.show();
							}
							else if(checkCard(FInput.getText().toString().trim(), BInput.getText().toString().trim())){
								message.setMessage("Cards cannot be the same in any way!");
								alert = message.create();
								alert.show();
							}
							else {
								ListCard temp = new ListCard();
								temp.id = list.size();
								temp.front = FInput.getText().toString();
								temp.back = BInput.getText().toString();
								list.add(temp);
								adapter.notifyDataSetChanged();
								
								Set<String> card = new LinkedHashSet<String>();
								card.add(temp.back);
								card.add(temp.front);
								editor.putStringSet(String.valueOf(temp.id), card);
								
								Log.v("myTest", temp.front + "\t" + temp.back + "\n");
							}
							
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id) { 
							dialog.cancel(); 
						}
					});

				// create an alert dialog
				AlertDialog alert = addPrompt.create();
				alert.show();
			}
		});
		
		removeCards.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(list.size() != 0) {
					Log.v("click", "remove button is clicked");
					Bundle bundle = new Bundle();
					bundle.putString("filename", filename);
					bundle.putBoolean("fileType", fileType);
					editor.commit();
					Tools.startIntent(FlashCardActivity.this, RemoveItemActivity.class, bundle, Intent.FLAG_ACTIVITY_NO_HISTORY);
				}
				else {
					message.setMessage("No Cards to Remove!");
					alert = message.create();
					alert.show();
				}
			}
		});
		
		play.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "play button is clicked");
				
				if(list.size() > 3) {
					editor.commit();
					Bundle bundle = new Bundle();
					bundle.putString("filename", filename);
					Tools.startIntent(FlashCardActivity.this, GameActivity.class, bundle);
				}
				else {
					message.setMessage("In order to play you need at least 4 cards!");
					alert = message.create();
					alert.show();
				}
			}
		});
		
		front.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "frontSort button is clicked");
				Collections.sort(list, fSort);
				adapter.notifyDataSetChanged();
			}
		});
		
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "backSort button is clicked");
				Collections.sort(list, bSort);
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private boolean checkCard(String front, String back){
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).front.equals(front) || list.get(i).back.equals(back)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		editor.commit();
		Tools.startIntent(this, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		editor.commit();
		super.onDestroy();
	}
}
