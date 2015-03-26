package com.example.flashcard;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class RemoveItemAdapter extends ArrayAdapter<ListCard>{

	private LayoutInflater mInflater;

	public RemoveItemAdapter(Context context, int rid, List<ListCard> list) {
		super(context, rid, list);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int pos, View convertView, ViewGroup partent){
		//Retrieve data
		final ListCard item = (ListCard) getItem(pos);
		
		//Use layout file to generate View
		View view = mInflater.inflate(R.layout.remove_card, null);
		
		CheckBox cb = (CheckBox) view.findViewById(R.id.check);
		cb.setChecked(item.checked);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            if(isChecked) {
	                item.checked = true;
	            }
	            else {
	                item.checked = false;
	            }
	        }
	    });
			
		TextView front = (TextView)view.findViewById(R.id.front2);
		front.setText(item.front);
			
		TextView back = (TextView)view.findViewById(R.id.back2);
		back.setText(item.back);
		
		return view;
	}
	
}
