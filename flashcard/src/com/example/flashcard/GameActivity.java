package com.example.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

public class GameActivity extends Activity {
	Context context = this;
	List<GridItem> list = new ArrayList<GridItem>();
	GridItemAdapter adapter;
	GridView GridView;
	String filename;
	int counter = 0;
	int card1;
	int card2;
	String[] frontSide = {"a","b","c","d","e","f","g","h","i","j","k","l","m"
			 ,"n","o","p","r","s","t","u","v","w","x","y","z"};
	String[] backSide = {"a","b","c","d","e","f","g","h","i","j","k","l","m"
	 		,"n","o","p","r","s","t","u","v","w","x","y","z"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		GridItemAdapter adapter = new GridItemAdapter(this, 0, list);
		GridView = (GridView) findViewById(R.id.gameGrid);
		GridView.setAdapter(adapter);
		
		for(int i = 0; i < 25 ; i++){
			GridItem temp = new GridItem();
			temp.id = i;
			temp.word = frontSide[i];
			list.add(temp);
			
			temp = new GridItem();
			temp.id = i;
			temp.word = backSide[i];
			list.add(temp);
		}
		
		Collections.shuffle(list);
		adapter.notifyDataSetChanged();
		
		Button pause = (Button) findViewById(R.id.pauseButton);
		Button quit = (Button) findViewById(R.id.quitButton);
		Button reset = (Button) findViewById(R.id.resetButton);
		
		pause.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			}
		});
		
		quit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			}
		});
		
		reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	void matcher(int pos1, int pos2){
		int card1 = adapter.getItem(pos1).id;
		int card2 = adapter.getItem(pos2).id;
		
		if(card1 == card2) {
		}
	}
}
