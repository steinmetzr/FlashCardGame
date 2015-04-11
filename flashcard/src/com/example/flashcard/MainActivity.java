package com.example.flashcard;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button addFile, removeFile, option;
	ListCardAdapter adapter;
	ListView listView;
	List<ListCard> list;
	Context context = this;
	File fileDir;
	boolean fileType = true;
	LayoutInflater layoutInflater;
	TextView ms;
	View promptView, messageView;
	AlertDialog alert;
	AlertDialog.Builder editPrompt, addPrompt, message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addFile = (Button) findViewById(R.id.addButton);
		removeFile = (Button) findViewById(R.id.removeButton);
		option = (Button) findViewById(R.id.optionButton);
		list = new ArrayList<ListCard>();
		adapter = new ListCardAdapter(this, 0, list, fileType);
		listView = (ListView) findViewById(R.id.listView1);
		
		listView.setAdapter(adapter);
		
		fileDir = new File(getApplicationInfo().dataDir, "shared_prefs");
		
		if(fileDir.exists() && fileDir.isDirectory()){
	        String[] fileList = fileDir.list();
	        for(int i=fileList.length-1; i>=0; i--) {
				ListFile temp = new ListFile(list.size(), fileList[i].substring(0, fileList[i].length()-4));
			    list.add(temp);
			}
		}

		option.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "option button is clicked");
				Tools.startIntent(MainActivity.this, Options.class, Intent.FLAG_ACTIVITY_NO_HISTORY);
			}
		});
			
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListCard temp = (ListCard)parent.getItemAtPosition(position);
				Log.v("click", "item is clicked");
				Bundle bundle = new Bundle();
				bundle.putString("filename", temp.front);
				Tools.startIntent(MainActivity.this, FlashCardActivity.class, bundle);
			}
		});
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				messageView = layoutInflater.inflate(R.layout.message, null);
				message = new AlertDialog.Builder(context);
				message.setView(messageView);
				ms = (TextView) messageView.findViewById(R.id.message);
				ms.setText("Cards cannot have a blank side!");
				message.setTitle("Warning")
				 	   .setCancelable(true)
					   .setNeutralButton("OK",
				        new DialogInterface.OnClickListener() {
				    		public void onClick(DialogInterface dialog, int id) {
				    			dialog.cancel();
				    		}
					   });
				editPrompt = new AlertDialog.Builder(context);
				promptView = layoutInflater.inflate(R.layout.add_file, null);
				editPrompt.setView(promptView);
				editPrompt.setTitle("Edit File");
				final EditText FInput = (EditText) promptView.findViewById(R.id.editFileName);
				FInput.setText(list.get(pos).front);
				editPrompt
					.setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// get user input and set it to result
							
							if(FInput.getText().toString().trim().length() == 0){
								alert = message.create();
								alert.show();
							}
							else {
								list.get(pos).front = FInput.getText().toString();
								// to stein edit text on shared preference?
								//
								adapter.notifyDataSetChanged();
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

		addFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "add button is clicked");
				layoutInflater = LayoutInflater.from(context);
				promptView = layoutInflater.inflate(R.layout.add_file, null);
				messageView = layoutInflater.inflate(R.layout.message, null);
				addPrompt = new AlertDialog.Builder(context);

				message = new AlertDialog.Builder(context);
				message.setView(messageView);
				TextView ms = (TextView) messageView.findViewById(R.id.message);
				ms.setText("File names can't be empty!");
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
				addPrompt.setTitle("Add File");
				final EditText FInput = (EditText) promptView.findViewById(R.id.editFileName);

				// setup a dialog window
				addPrompt
				.setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						if(FInput.getText().toString().trim().length() == 0 ) {
							AlertDialog warning = message.create();
							warning.show();
						}
						else {
							ListFile temp = new ListFile();
							temp.id = list.size();
							temp.front = FInput.getText().toString();
							list.add(temp);
							getSharedPreferences(temp.front, Context.MODE_PRIVATE).edit().commit();
							adapter.notifyDataSetChanged();
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

		removeFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "remove button is clicked");
				Bundle bundle = new Bundle();
				bundle.putBoolean("fileType", fileType);
				bundle.putString("filename", ((TextView)findViewById(R.id.files)).getText().toString());
				Tools.startIntent(MainActivity.this, RemoveItemActivity.class, bundle, Intent.FLAG_ACTIVITY_NO_HISTORY);
			}
		});
	}
}
