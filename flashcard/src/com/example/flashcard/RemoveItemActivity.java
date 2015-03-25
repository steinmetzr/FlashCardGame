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
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class RemoveItemActivity extends Activity {
	File file;
	final Context context = this;
	RemoveItemAdapter adapter;
	ListView listView;
	CheckBox checkItem;

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
				adapter.notifyDataSetChanged();
		}});	
		
		for(int i=0; i<10; i++) {
			ListCard temp = new ListCard();
			temp.id = list.size();
			temp.front = "x " + i;
			temp.back = "y " + i;
		    list.add(temp);
		}
		
		TextView title = (TextView) findViewById(R.id.fileTitle2);
		title.setText(Tools.underLine("flashcard"));
		
		adapter = new RemoveItemAdapter(this, 0, list);
		listView = (ListView) findViewById(R.id.removeList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v("item", "item " + position + " is clicked");

				if(list.get(position).checked == true){
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		Button delete =(Button)findViewById(R.id.deleteButton);
		
		delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				// get prompts.xml view
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View promptView = layoutInflater.inflate(R.layout.message, null);
				AlertDialog.Builder message = new AlertDialog.Builder(context);
				TextView ms = (TextView) promptView.findViewById(R.id.message);
				message.setView(promptView);
				
				int count=0;
				for(int i=0; i<list.size(); i++){
					if(list.get(i).checked == true) {
						count++;
					}
				}
				
				ms.setText("Are you sure you want to delete " + count +" card");
				message.setTitle("Warning")
					.setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(selectAll.isChecked()) {
							list.clear();
						}
						else {
							for(int i=0; i<list.size(); i++){
								if(list.get(i).checked == true) { 
									list.remove(i); 
									adapter.notifyDataSetChanged();
									i--;
								}
							}
						}
						adapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int id) { 
						dialog.cancel(); 
					}
				});
				AlertDialog alert = message.create();
				alert.show();
				
				/*
				if(count > 0) {
					AlertDialog alert = message.create();
					alert.show();
				}
				*/
			}
		});
		
		Button confirm = (Button)findViewById(R.id.doneButton);
		confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "done button is clicked");
				Bundle bundle = new Bundle();
				bundle.putString("filename", "flashcard");
				Tools.startIntent(RemoveItemActivity.this, FlashCardActivity.class, bundle);
			}
		});
	}
}