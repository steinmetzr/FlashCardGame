package com.example.flashcard;

public class ListFile extends ListCard {
	public ListFile(){
		back = null;
	}

	public	ListFile(int id, String front) {
        this.id = id;
        this.front = front;
        this.back = null;
        this.checked = false;
   }
}