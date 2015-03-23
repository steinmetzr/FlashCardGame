package com.example.flashcard;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button addFile = (Button) findViewById(R.id.addButton);
		Button removeFile = (Button) findViewById(R.id.removeButton);
		Button option = (Button) findViewById(R.id.optionButton);
		List<ListCard> list = new ArrayList<ListCard>();
		
		addFile.setOnClickListener(new OnClickListener(){
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
		
		ListCard temp = new ListCard();
		temp.id = 0;
		temp.front = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		temp.back = "";
	    list.add(temp);
	    
	    ListCard temp1 = new ListCard();
	    temp1.id = 1;
	    temp1.front = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
	    temp.back = "";
	    list.add(temp1);
	    
	    ListCard temp11 = new ListCard();
	    temp11.id = 2;
	    temp11.front = "ccccccccccccccccccccccccccccc";
	    temp.back = "";
	    list.add(temp11);

	    ListCardAdapter adapter = new ListCardAdapter(this, 0, list);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
	}

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
