package com.example.flashcard;

import android.graphics.Color;

public class GridCard {
	public int id;
	public int color = Color.BLACK;
	public String word;
	
	
	GridCard(int id, String word) {
		this.id = id;
		this.color = Color.BLACK;
		this.word = word;
	}
}