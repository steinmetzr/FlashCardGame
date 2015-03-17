package com.example.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FlashCardActivity extends Activity {
	
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
	        
	    ListCard temp4 = new ListCard();
	    temp4.id = 3;
	    temp4.front = "d";
	    temp4.back = "b";
	    list.add(temp4);
	        
	   	ListCard temp5 = new ListCard();
	   	temp5.id = 4;
		temp5.front = "e";
		temp5.back = "a";
	    list.add(temp5);
	    Collections.sort(list, fSort);
	        
		/*
		try{
			int i=0;
			InputStream read = getAssets().open("flashcard");
			BufferedReader reader = new BufferedReader(new InputStreamReader(read));
			String line = reader.readLine();
			while(line != null){
				ListCard temp = new ListCard();
				temp.id = i;
				temp.front = line;
				temp.back = reader.readLine();
		        list.add(temp);
		        i++;
		    }
			read.close();
		} catch(IOException e){ e.printStackTrace(); }
		*/
		SpannableString underlinedFilename = new SpannableString(filename);
		underlinedFilename.setSpan(new UnderlineSpan(), 0, underlinedFilename.length(), Spanned.SPAN_PARAGRAPH);
		TextView title = (TextView) findViewById(R.id.fileTitle);
		title.setText(underlinedFilename);

		Button addCards = (Button) findViewById(R.id.addButton);
		Button removeCards = (Button) findViewById(R.id.removeButton);
		Button play = (Button) findViewById(R.id.playButton);
		Button front = (Button) findViewById(R.id.frontButton);
		Button back = (Button) findViewById(R.id.backButton);
		
		ListCardAdapter adapter = new ListCardAdapter(this, 0, list);
		ListView listView = (ListView) findViewById(R.id.listView1);
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
			}
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
			}
		});
		
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.v("click", "backSort button is clicked");
				Collections.sort(list, bSort);
			}
		});
	}
	
}
