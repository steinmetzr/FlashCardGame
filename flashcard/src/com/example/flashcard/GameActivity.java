package com.example.flashcard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		List<GridItem> list = new ArrayList<GridItem>();
		String[] front = {"a","b","c","d","e","f","g","h","i","j","k","l","m"
						 ,"n","o","p","r","s","t","u","v","w","x","y","z"};
		String[] back = {"a","b","c","d","e","f","g","h","i","j","k","l","m"
				 ,"n","o","p","r","s","t","u","v","w","x","y","z"};
		
		for(int i = 0; i < 50 ; i++){
			GridItem temp = new GridItem();
			temp.id = i;
			temp.word = front[i];
			list.add(temp);
		}
		
		GridItemAdapter adapter;
		adapter = new GridItemAdapter(this, 0, list);
		
		Button pause = (Button) findViewById(R.id.pauseButton);
		Button quit = (Button) findViewById(R.id.quitButton);
		Button reset = (Button) findViewById(R.id.resetButton);
		
		pause.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		quit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
