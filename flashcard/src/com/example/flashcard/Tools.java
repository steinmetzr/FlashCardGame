package com.example.flashcard;

import java.io.BufferedReader;
import java.io.FileReader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Tools{
	/**
	 * Starts an intent
	 * @param context
	 * @param goTo
	 */
	public static void startIntent(Context context, Class<?> goTo){
		Intent intent = new Intent().setClass(context, goTo);
		context.startActivity(intent);
	}
	
	/**
	 * Starts an intent and sends a bundle with it
	 * @param context
	 * @param goTo
	 * @param bundle
	 */
	public static void startIntent(Context context, Class<?> goTo, Bundle bundle){
		Intent intent = new Intent().setClass(context, goTo);
		
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	/**
	 * returns the string inside of a TextView
	 * @param textView
	 * @return String
	 */
	public static String toString(TextView textView){
		return textView.getText().toString();
	}
	
	public static void Toast(Context context, CharSequence string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
}
