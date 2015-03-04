package com.example.flashcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
	 * Starts an intent and sends bundle with it
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
	 * returns the string in a TextView
	 * @param textView
	 * @return String
	 */
	public static String toString(TextView textView){
		return textView.getText().toString();
	}
}
