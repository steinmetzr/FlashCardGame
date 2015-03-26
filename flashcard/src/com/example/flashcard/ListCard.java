package com.example.flashcard;

public class ListCard {
	public int id;
	public String front;
	public String back;
	public Boolean checked = false;
	
	public	ListCard() {
        this.id = 0;
        this.front = "";
        this.back = "";
        this.checked = false;
   }
	
	public	ListCard(int id, String front, String back) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.checked = false;
   }
}