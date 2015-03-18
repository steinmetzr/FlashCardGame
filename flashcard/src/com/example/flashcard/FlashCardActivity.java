package com.example.flashcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
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
	final Context context = this;
	ListCardAdapter adapter;
	ListView listView;
	
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

	List<ListCard> list = new ArrayList<ListCard>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_card);

		Bundle data = this.getIntent().getExtras();
		String filename = data.getString("filename");
		
		ListCard temp1 = new ListCard();
		temp1.id = 0;
		temp1.front = "a";
		temp1.back = "e";
	    list.add(temp1);
	    
	    ListCard temp2 = new ListCard();
	    temp2.id = 1;
		temp2.front = "b";
		temp2.back = "d";
	    list.add(temp2);
	     
	    ListCard temp3 = new ListCard();
	    temp3.id = 2;
	    temp3.front = "c";
		temp3.back = "c";
	    list.add(temp3);
		try {
		    BufferedReader bf = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
		    String line = bf.readLine();

		    while(line != null){
		      Log.v("list", line);
		      line = bf.readLine();
		    }
		} catch (IOException e) {}
		
		SpannableString underlinedFilename = new SpannableString(filename);
		underlinedFilename.setSpan(new UnderlineSpan(), 0, underlinedFilename.length(), Spanned.SPAN_PARAGRAPH);
		TextView title = (TextView) findViewById(R.id.fileTitle);
		title.setText(underlinedFilename);

		Button addCards = (Button) findViewById(R.id.addButton);
		Button removeCards = (Button) findViewById(R.id.removeButton);
		Button play = (Button) findViewById(R.id.playButton);
		Button front = (Button) findViewById(R.id.frontButton);
		Button back = (Button) findViewById(R.id.backButton);
		
		adapter = new ListCardAdapter(this, 0, list);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v("item", "item " + position + " is clicked");
			}
		});
		
		addCards.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "add button is clicked");
		        adapter.notifyDataSetChanged();
				
				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.addcard, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				// set prompts.xml to be the layout file of the alertdialog builder
				alertDialogBuilder.setView(promptView);
				alertDialogBuilder.setTitle("Add Card");

				final EditText FInput = (EditText) promptView.findViewById(R.id.editFront);
				final EditText BInput = (EditText) promptView.findViewById(R.id.editBack);
				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// get user input and set it to result
										ListCard temp = new ListCard();
										temp.id = list.size();
										temp.front = FInput.getText().toString();
										temp.back = BInput.getText().toString();
										list.add(temp);
										adapter.notifyDataSetChanged();
										Log.v("myTest", temp.front + "\t" + temp.back + "\n");
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,	int id) {
										dialog.cancel();
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
			}
	
			    //write output add in strings to end of file
		});
		
		removeCards.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "remove button is clicked");
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
	
}
