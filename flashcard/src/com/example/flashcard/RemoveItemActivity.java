package com.example.flashcard;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class RemoveItemActivity extends Activity {
	File file;
	final Context context = this;
	ListView listView;
	CheckBox checkItem;
	Boolean fileType;
	String filename;
	File fileDir;
	SharedPreferences cardsPrefs;
	Editor editor;

	List<ListCard> list = new ArrayList<ListCard>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_card);

		Bundle data = this.getIntent().getExtras();
		fileType = data.getBoolean("fileType");
		filename = data.getString("filename");

		final RemoveItemAdapter adapter = new RemoveItemAdapter(this, 0, list, fileType);
		
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
		
		if(!fileType) {
			cardsPrefs = getSharedPreferences(filename, Context.MODE_PRIVATE);
			if(cardsPrefs.getAll() != null){
				while(cardsPrefs.contains(String.valueOf(list.size()))){
					Set<String> cards = cardsPrefs.getStringSet(String.valueOf(list.size()), null);
					Iterator<String> it = cards.iterator();
					ListCard temp = new ListCard(list.size(), it.next(), it.next());
					list.add(temp);
				}
				editor = cardsPrefs.edit();
			}	
		}
		else {
			fileDir = new File(getApplicationInfo().dataDir, "shared_prefs");
			if(fileDir.exists() && fileDir.isDirectory()){
		        String[] fileList = fileDir.list();
		        for(int i=0; i<fileList.length; i++) {
					ListFile temp = new ListFile(list.size(), fileList[i].substring(0, fileList[i].length()-4));
					list.add(temp);
				}
			}
		}
		
		TextView title = (TextView) findViewById(R.id.fileTitle2);
		title.setText(Tools.underLine(filename));

		listView = (ListView) findViewById(R.id.removeList);
		listView.setAdapter(adapter);
		
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
				
				int count = 0;
				for(int i=0; i<list.size(); i++){
					if(list.get(i).checked == true) 
					{ count++; }
				}
				
				String phrase = "Are you sure you want to delete " + count;
				
				if(!fileType) {
					phrase +=" card(s)";
				}
				else{
					phrase +=" file(s)"; 
				}
				
				ms.setText(phrase);
				
				message.setTitle("Warning")
					.setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if(selectAll.isChecked()) {
								list.clear();
								if(!fileType){
									editor.clear();
								}
								else{
									File[] files = fileDir.listFiles();
									String fileName;
									for(File file : files){
										fileName = file.getName();
										fileName = fileName.substring(0, (int)fileName.length()-4);
										cardsPrefs = getSharedPreferences(fileName, Context.MODE_PRIVATE);
										editor = cardsPrefs.edit();
										editor.clear();
										file.delete();
									}
								}
							}
							else {
								if(!fileType){
									for(int i=0; i<list.size(); i++) {
										if(list.get(i).checked == true) {
											editor.remove(Integer.toString(i));
											list.remove(i);
										}
									}
								}
								else{
									for(int i=0; i<list.size(); i++) {
										if(list.get(i).checked == true) {
											File[] files = fileDir.listFiles();
											String fileName;
											for(File file : files){
												fileName = file.getName();
												fileName = fileName.substring(0, (int)fileName.length()-4);
												if(list.get(i).front.equals(fileName)){
													file.delete();
												}
											}
										}
										list.remove(i);
									}
								}
							}
							adapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) { 
								dialog.cancel();
							}
						});
					
				if(count > 0) {
					AlertDialog alert = message.create();
					alert.show();
				}
			}
		});
		
		Button confirm = (Button)findViewById(R.id.doneButton);
		confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "done button is clicked");
				Bundle bundle = new Bundle();
				bundle.putString("filename", filename);
				if(!fileType){
					editor.commit();
					Tools.startIntent(RemoveItemActivity.this, FlashCardActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				else
					Tools.startIntent(RemoveItemActivity.this, MainActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			}
		});
	}
}