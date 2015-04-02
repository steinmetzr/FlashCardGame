package com.example.flashcard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	File file;
	Context context = this;
	ListCardAdapter adapter;
	ListView listView;
	String filename;
	List<ListCard> list = new ArrayList<ListCard>();
	
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

		Bundle data = this.getIntent().getExtras();
		filename = data.getString("filename");
		
		for(int i=0; i<10; i++) {
			ListCard temp = new ListCard(list.size(), "x " + i, "y " + i);
		    list.add(temp);
		}
	    
		/*
		try {
		    BufferedReader bf = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
		    String line = bf.readLine();
		    while(line != null){
		    	Log.v("list", line);
		    	AssetManager am = context.getAssets();
		    	InputStream is = am.open("test.txt");
			}
		} catch (IOException e) {}
		*/

		TextView title = (TextView) findViewById(R.id.fileTitle1);
		title.setText(Tools.underLine(filename));

		Button addCards = (Button) findViewById(R.id.addButton);
		Button removeCards = (Button) findViewById(R.id.removeButton);
		Button play = (Button) findViewById(R.id.playButton);
		Button front = (Button) findViewById(R.id.frontButton);
		Button back = (Button) findViewById(R.id.backButton);
		
		adapter = new ListCardAdapter(this, 0, list);
		listView = (ListView) findViewById(R.id.cardList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v("item", "item " + position + " is clicked");
			}
		});
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.add_card, null);
				View messageView = layoutInflater.inflate(R.layout.message, null);
				AlertDialog.Builder editPrompt = new AlertDialog.Builder(context);
				
				final AlertDialog.Builder message = new AlertDialog.Builder(context);
				message.setView(messageView);
				TextView ms = (TextView) messageView.findViewById(R.id.message);
				ms.setText("Cards cannot have a blank side!");
				message.setTitle("Warning")
				 	   .setCancelable(true)
					   .setNeutralButton("OK",
				        new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int id) {
				    			dialog.cancel();
				    		}
					   });

				// set prompts.xml to be the layout file of the alertdialog builder
				editPrompt.setView(promptView);
				editPrompt.setTitle("Edit Card");
				final EditText FInput = (EditText) promptView.findViewById(R.id.editFront);
				FInput.setText(list.get(pos).front);
				final EditText BInput = (EditText) promptView.findViewById(R.id.editBack);
				BInput.setText(list.get(pos).back);
				// setup a dialog window
				editPrompt
					.setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// get user input and set it to result
							
							if((FInput.getText().toString().trim().length() == 0) || 
							   (BInput.getText().toString().trim().length() == 0)) {
								AlertDialog warning = message.create();
								warning.show();
							}
							else {
								list.get(pos).front = FInput.getText().toString();
								list.get(pos).back = BInput.getText().toString();
								adapter.notifyDataSetChanged();
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

				// create an alert dialog
				AlertDialog alert = editPrompt.create();
				alert.show();
				return true;
			}
		});
	
		addCards.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "add button is clicked");
				
				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.add_card, null);
				View messageView = layoutInflater.inflate(R.layout.message, null);
				AlertDialog.Builder addPrompt = new AlertDialog.Builder(context);
				
				final AlertDialog.Builder message = new AlertDialog.Builder(context);
				message.setView(messageView);
				TextView ms = (TextView) messageView.findViewById(R.id.message);
				ms.setText("Cards cannot have a blank side!");
				message.setTitle("Warning")
				 	   .setCancelable(true)
					   .setNeutralButton("OK",
				        new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int id) {
				    			dialog.cancel();
				    		}
					   });

				// set prompts.xml to be the layout file of the alertdialog builder
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
								AlertDialog warning = message.create();
								warning.show();
							}
							else {
								ListCard temp = new ListCard();
								temp.id = list.size();
								temp.front = FInput.getText().toString();
								temp.back = BInput.getText().toString();
								list.add(temp);
								adapter.notifyDataSetChanged();
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
				Log.v("click", "remove button is clicked");
				Bundle bundle = new Bundle();
				bundle.putString("filename", filename);
				bundle.putBoolean("fileType", false);
				Tools.startIntent(FlashCardActivity.this, RemoveItemActivity.class, bundle, Intent.FLAG_ACTIVITY_NO_HISTORY);
			}
		});
		
		play.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "play button is clicked");
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
	
	@Override
	public void onBackPressed() {
		Tools.startIntent(this, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		super.onBackPressed();
	}
}
