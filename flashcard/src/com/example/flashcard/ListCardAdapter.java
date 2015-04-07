package com.example.flashcard;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListCardAdapter extends ArrayAdapter<ListCard>{

	private LayoutInflater mInflater;
	
	public ListCardAdapter(Context context, int rid, List<ListCard> list) {
		super(context, rid, list);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int pos, View convertView, ViewGroup partent){
		//Retrieve data
		ListCard item = (ListCard) getItem(pos);
		
		//Use layout file to generate View
		View view = mInflater.inflate(R.layout.list_card, null);
		
		TextView index = (TextView)view.findViewById(R.id.id);
		index.setText(pos+1 + ". ");
		
		TextView front = (TextView)view.findViewById(R.id.front);
		front.setText(item.front);
		
		TextView back = (TextView)view.findViewById(R.id.back);
		back.setText(item.back);

		return view;
	}
}
