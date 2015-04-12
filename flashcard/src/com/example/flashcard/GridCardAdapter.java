package com.example.flashcard;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GridCardAdapter extends ArrayAdapter<GridCard> {
	private LayoutInflater mInflater;
	
	public GridCardAdapter(Context context, int rid, List<GridCard> list){
		super(context, rid, list);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int pos, View convertView, ViewGroup partent){	
		// Retrieve data
		GridCard item = (GridCard)getItem(pos);
		
		// Use layout file to generate View
		View view = mInflater.inflate(R.layout.grid_index, null);
		
		// Set the index word (front or back)
		TextView card = (TextView) view.findViewById(R.id.gridPos);
		card.setText(item.word);
		card.setBackgroundColor(item.color);
		return view;
	}
}
