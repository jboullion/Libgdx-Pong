package com.jboullion.pong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.util.Log;

import com.badlogic.androidgames.framework.FileIO;

public class Settings {
	public static boolean soundEnabled = true;
	public static int[] highscores = new int[] {1000, 800, 600};
	public static String[] highscoreInitials = {"AG", "MK", "JDB"};
	public static ArrayList<String> scoreString = new ArrayList<String>(); 
	public static String initials = null;
	public static String file = ".byterFyter2";  //NAME  OF FILE SAVED ON THE SD CARD
	
	static final String TAG = "Settings";
	
	public static void load(FileIO files){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(files.readFile(file)));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			
			for (int i=0;i<3; i++){
				scoreString.add(in.readLine());
				String[] score = scoreString.get(i).split(" ");
				highscoreInitials[i] = score[0];
				highscores[i] = Integer.parseInt(score[1]);
				
				//ORIGINAL: highscores[i] = Integer.parseInt(in.readLine());
				Log.d(TAG, "Load: highscores["+i+"] = "+highscores[i]+" highscoreInitials["+i+"]="+highscoreInitials[i]);
			}

		}catch (IOException ex){
			
		}catch (NumberFormatException ex){
			
		}finally {
			try{
				if(in!= null)
					in.close();
			}catch (IOException ex){
				
			}
		}
	}
	
	public static void save(FileIO files){
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
			out.write(Boolean.toString(soundEnabled));
			out.write("\n");
			for(int i=0; i< 3; i++){
				out.write(highscoreInitials[i] + " " + Integer.toString(highscores[i]));
				out.write("\n");
				Log.d(TAG, "Save: highscoreInitials[i]  Integer.toString(highscores[i])" + highscoreInitials[i] + " " + Integer.toString(highscores[i]));
			}
		}catch (IOException ex){
		}finally {
			try {
				if (out != null)
					out.close();
			}catch (IOException ex){
				
			}
		}
	}
	
	public static void addScore (int score, String name){
		for(int i=0; i<3; i++){
			
			if(highscores[i]<score){
				Log.d(TAG, "if(highscores["+i+"]<score) = True");
				for (int j=2; j> i; j--){
					highscores[j] = highscores[j-1];
					highscoreInitials[j] = highscoreInitials[j-1];
					Log.d(TAG, "highscores["+j+"] = "+highscores[j]+" highscoreInitials["+j+"]="+highscoreInitials[j]);
				}
				highscores[i] = score;
				highscoreInitials[i] = name;
				break;
			}
			
			//highscores[i] = score;
			
		}
		initials = name;
		Log.d(TAG, "Settings addScore: initials = "+name+" score = " +score);
	}
}
