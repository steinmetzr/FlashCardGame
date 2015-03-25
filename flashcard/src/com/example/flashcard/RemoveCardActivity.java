package com.example.flashcard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class RemoveCardActivity extends Activity {
	File file;
	final Context context = this;
	ListCardAdapter adapter;
	ListView listView;

	List<ListCard> list = new ArrayList<ListCard>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_card);

		//Bundle data = this.getIntent().getExtras();
		//String filename = data.getString("filename");
		
		final CheckBox selectAll = (CheckBox) findViewById(R.id.checkAll);
		selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					for(int i=0; i < list.size(); i++){
						list.get(i).checked = true;
					}
				}
				else{
					for(int i=0; i < list.size(); i++){
						list.get(i).checked = false;
					}
				}
		}});	
		
		
		ListCard temp1 = new ListCard();
		temp1.id = list.size();
		temp1.front = "ant";
		temp1.back = "cat";
		temp1.checked = false;
	    list.add(temp1);
	    
	    ListCard temp2 = new ListCard();
	    temp2.id = list.size();
		temp2.front = "bee";
		temp2.back = "big";
	    list.add(temp2);
	     
	    ListCard temp3 = new ListCard();
	    temp3.id = list.size();
	    temp3.front = "cat";
		temp3.back = "ant";
	    list.add(temp3);
		
		SpannableString underlinedFilename = new SpannableString("flashcard");
		underlinedFilename.setSpan(new UnderlineSpan(), 0, underlinedFilename.length(), Spanned.SPAN_PARAGRAPH);
		TextView title = (TextView) findViewById(R.id.fileTitle2);
		title.setText(underlinedFilename);
		
		adapter = new ListCardAdapter(this, 0, list);
		adapter.setLayout(R.layout.remove_card);
		listView = (ListView) findViewById(R.id.removeList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v("item", "item " + position + " is clicked");
			}
		});
		
	
		
		Button confirm = (Button) findViewById(R.id.doneButton);
		
		confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "done button is clicked");
				
				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.message, null);
				AlertDialog.Builder message = new AlertDialog.Builder(context);
				message.setView(promptView);
				
				TextView ms = (TextView) promptView.findViewById(R.id.message);
				ms.setText("Are you sure you want to delete the card(s)");
				message.setTitle("Warning")
					   .setCancelable(true)
					   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if(selectAll.isChecked()) {
								list.clear();
							}
							else {
								for(int i=0; i<list.size(); i++){
									if(list.get(i).checked == true) 
									{ list.remove(i); }
								}
							}
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id) { 
							dialog.cancel(); 
						}
					});

				// create an alert dialog
				AlertDialog alert = message.create();
				alert.show();
			}
		});
	}
}