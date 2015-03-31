package com.example.flashcard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addFile = (Button) findViewById(R.id.addButton);
		removeFile = (Button) findViewById(R.id.removeButton);
		option = (Button) findViewById(R.id.optionButton);
		list = new ArrayList<ListCard>();
		adapter = new ListCardAdapter(this, 0, list);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		
		TextView title = (TextView) findViewById(R.id.files);
		title.setText(Tools.underLine(title.getText().toString()));

		addFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO

			}
		});

		removeFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "remove button is clicked");
				//TODO
			}
		});

		option.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "option button is clicked");
				Tools.startIntent(MainActivity.this, Options.class);
			}
		});
			
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListCard temp = (ListCard)parent.getItemAtPosition(position);
				Log.v("click", "add button is clicked");
				Bundle bundle = new Bundle();
				bundle.putString("filename", temp.front);
				Tools.startIntent(MainActivity.this, FlashCardActivity.class, bundle);
			}

		});

		addFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "add button is clicked");

				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.add_file, null);
				View messageView = layoutInflater.inflate(R.layout.message, null);
				AlertDialog.Builder addPrompt = new AlertDialog.Builder(context);

				final AlertDialog.Builder message = new AlertDialog.Builder(context);
				message.setView(messageView);
				TextView ms = (TextView) messageView.findViewById(R.id.message);
				ms.setText("File names cannont be empty!");
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

						if(FInput.getText().toString().trim().length() == 0) {
							AlertDialog warning = message.create();
							warning.show();
						}
						else {
							ListFile temp = new ListFile();
							temp.id = list.size();
							temp.front = FInput.getText().toString();
							list.add(temp);
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
			}
		});
	}
		/*addFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "add button is clicked");
				Bundle bundle = new Bundle();
				bundle.putString("filename", "flashcard");
				Tools.startIntent(MainActivity.this, FlashCardActivity.class, bundle);
			}
		});
		
		removeFile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "remove button is clicked");
				//TODO
			}
		});
		
		option.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "option button is clicked");
				Tools.startIntent(MainActivity.this, Options.class);
			}
		});
		
		ListFile temp = new ListFile();
		temp.id = 0;
		temp.front = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		temp.back = "";
		list.add(temp);
	    
	    ListFile temp1 = new ListFile();
	    temp1.id = 1;
	    temp1.front = "abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz";
	    temp.back = "abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz";
	    list.add(temp1);
	    
	    ListFile temp11 = new ListFile();
	    temp11.id = 2;
	    temp11.front = "cccccccccccccccccccccccccccccccccccccccc";
	    temp.back = "";
	    list.add(temp11);
	    
	    ListCardAdapter adapter = new ListCardAdapter(this, 0, list);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			Log.v("click", "add button is clicked");
		}
		if (id == R.id.action_remove) {
			//TODO
		}
		if (id == R.id.action_settings) {
			Log.v("click", "option button is clicked");
			Tools.startIntent(MainActivity.this, Options.class);
		}
		return super.onOptionsItemSelected(item);
	}*/
}
