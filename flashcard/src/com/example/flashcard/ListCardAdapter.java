package com.example.flashcard;

import java.util.List;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;	
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListCardAdapter extends ArrayAdapter<ListCard>{

	private LayoutInflater mInflater;
	boolean fileType;

	public ListCardAdapter(Context context, int rid, List<ListCard> list, boolean type) {
		super(context, rid, list);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fileType = type;
	}

	public View getView(int pos, View convertView, ViewGroup partent){
		//Retrieve data
		ListCard item = (ListCard) getItem(pos);

		View view;

		if (!fileType) {
			//Use layout file to generate View
			view = mInflater.inflate(R.layout.list_card, null);

			TextView index = (TextView)view.findViewById(R.id.id);
			index.setText(pos+1 + ". ");

			index.setOnDragListener(new OnDragListener(){
				@Override
				public boolean onDrag(View v, DragEvent event) {
					return false;
				}
			});
			TextView front = (TextView)view.findViewById(R.id.front);
			front.setText(item.front);

			TextView back = (TextView)view.findViewById(R.id.back);
			back.setText(item.back);
		}
		else{
			view = mInflater.inflate(R.layout.list_file, null);

			TextView index = (TextView)view.findViewById(R.id.id);
			index.setText(pos+1 + ". ");

			index.setOnDragListener(new OnDragListener(){
				@Override
				public boolean onDrag(View v, DragEvent event) {
					return false;
				}
			});
			TextView file = (TextView)view.findViewById(R.id.file);
			file.setText(item.front);
		}
		return view;
	}
}
