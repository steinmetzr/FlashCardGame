package com.example.flashcard;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListCardAdapter extends ArrayAdapter<ListCard>{

	private LayoutInflater mInflater;
	private int layout;
	
	public ListCardAdapter(Context context, int rid, List<ListCard> list, int layout) {
		super(context, rid, list);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setLayout(layout);
		
	}

	public View getView(int pos, View convertView, ViewGroup partent){
		//Retrieve data
		ListCard item = (ListCard) getItem(pos);
		
		//Use layout file to generate View
		View view = mInflater.inflate(layout, null);
		
		if(layout == R.layout.remove_card) {
			CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
			cb.setChecked(item.checked);
			
			TextView front = (TextView)view.findViewById(R.id.front2);
			front.setText(item.front);
			
			TextView back = (TextView)view.findViewById(R.id.back2);
			back.setText(item.back);
		}
		else {
			TextView index = (TextView)view.findViewById(R.id.id);
			index.setText(pos+1 + ". ");
			
			TextView front = (TextView)view.findViewById(R.id.front);
			front.setText(item.front);
			
			TextView back = (TextView)view.findViewById(R.id.back);
			back.setText(item.back);
		}
		return view;
	}

	public int getLayout()
	{ return layout; }

	public void setLayout(int layout)
	{ this.layout = layout; }
}