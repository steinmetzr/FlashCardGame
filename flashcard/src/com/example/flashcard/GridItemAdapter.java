package com.example.flashcard;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GridItemAdapter extends ArrayAdapter<GridItem> {
	
	private LayoutInflater mInflater;
	
	public GridItemAdapter(Context context, int rid, List<GridItem> list){
		super(context, rid, list);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup partent){
		
		// Retrieve data
		GridItem item = (GridItem) getItem (position);
		
		// Use layout file to generate View
		View view = mInflater.inflate(R.layout.grid_item, null);
		
		// Set the index word (front or back)
		TextView word = (TextView) view.findViewById(R.id.word);
		word.setText("a");
		
		return view;
		
	}
}
