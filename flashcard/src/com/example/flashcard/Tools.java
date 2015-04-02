package com.example.flashcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
import android.widget.Toast;

public class Tools extends Activity{
	/**
	 * Starts an intent to go from here to there.
	 * @param here
	 * @param there
	 */
	public static void startIntent(Context here, Class<?> there){
		Intent intent = new Intent().setClass(here, there);
		here.startActivity(intent);
	}
	
	/**
	 * Starts an intent to go from here to there with a bundle.
	 * @param here
	 * @param there
	 * @param bundle
	 */
	public static void startIntent(Context here, Class<?> there, Bundle bundle){
		Intent intent = new Intent().setClass(here, there);
		
		intent.putExtras(bundle);
		here.startActivity(intent);
	}
	
	public static void startIntent(Context here, Class<?> there, int Flag){
		Intent intent = new Intent().setClass(here, there);
		
		intent.addFlags(Flag);
		here.startActivity(intent);
	}
	
	public static void startIntent(Context here, Class<?> there, Bundle bundle, int Flag){
		Intent intent = new Intent().setClass(here, there);
		
		intent.putExtras(bundle);
		intent.addFlags(Flag);
		
		here.startActivity(intent);
	}
	
	/**
	 * returns the string inside of a TextView.
	 * @param textView
	 * @return a string
	 */
	public static String toString(TextView textView){
		return textView.getText().toString();
	}
	
	/**
	 * displays a toast message for context with string.
	 * @param context
	 * @param string
	 */
	public static void toast(Context context, CharSequence string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	
	public static SpannableString underLine(String name){
		SpannableString underlinedFilename = new SpannableString(name);
		underlinedFilename.setSpan(new UnderlineSpan(), 0, underlinedFilename.length(), Spanned.SPAN_PARAGRAPH);
		return underlinedFilename;
	}
}
