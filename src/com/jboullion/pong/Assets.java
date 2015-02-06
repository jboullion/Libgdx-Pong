package com.jboullion.pong;

import java.util.ArrayList;

import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Sound;
import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.Font;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	/***********************************************************************************
	 * May need to move images around to get the correct z-order of images so that they 
	 * can be sent to the batcher in the correct order.
	 * 
	 * MAY HAVE TO MOVE THE FOLLOWING: 
	 * 		
	 ***********************************************************************************/
	//ALL TEXTURES USED IN GAME - KEEP IN ALPHABETICAL ORDER!!!!!!!!!!!!!!!!!!!!!!!!!
	
	public static Texture menuscreen;
	public static TextureRegion menuscreenRegion;
	
	public static Texture paddles;
	public static int NUM_PADDLES = 6;
	public static ArrayList<TextureRegion> paddlesArray = new ArrayList<TextureRegion>();
	public static ArrayList<TextureRegion> ballsArray = new ArrayList<TextureRegion>();
	public static ArrayList<TextureRegion> shotsArray = new ArrayList<TextureRegion>();
	
	public static Texture selectscreen;
	public static TextureRegion selectscreenRegion;
	public static TextureRegion notReady;
	public static TextureRegion ready;
	public static TextureRegion fight;
	
	public static int NUM_POWER_UPS = 6;
	public static Texture gamescreen;
	public static TextureRegion gamescreenRegion;
	public static TextureRegion you_win;
	public static TextureRegion you_lose;
	public static TextureRegion play_again;
	public static TextureRegion main_menu;
	public static TextureRegion game_overlay;
	public static TextureRegion shot;
	
	public static ArrayList<Animation> powerUpArray = new ArrayList<Animation>();
	//public static Animation speedUp;
	//public static Animation randomDirection;
	//public static TextureRegion randomDirection;
	
	public static Texture fontMap;
	public static Font fontRed;
	public static Font fontWhite;
	
	public static Music music;
	
	public static Sound book_sound;
	public static ArrayList<Sound> oofArray = new ArrayList<Sound>();
	
	
	public static void load(GLGame game){
		
		menuscreen = new Texture(((GLGame)game), "menuscreen.png");
		menuscreenRegion = new TextureRegion(menuscreen, 0, 384, 960, 640);
		
		paddles = new Texture(((GLGame)game), "paddles.png");
		fight = new TextureRegion(paddles, 0, 256, 256, 256);
		//BUILD ARRAY OF PADDLES AND BALLS FOR SELECTION
		float PADDLEX = 0;
		float BALLX = 0;
		float SHOTY = 224;
		
		for(int i = 0; i < NUM_PADDLES; i++){
			paddlesArray.add(new TextureRegion(paddles, PADDLEX, 0, 32, 160));
			PADDLEX += 32;
			
			ballsArray.add(new TextureRegion(paddles, BALLX, 160, 64, 64));
			BALLX += 64;
			
			shotsArray.add(new TextureRegion(paddles, 448, SHOTY, 64, 32));
			SHOTY += 32;
		}
				
		selectscreen = new Texture(((GLGame)game), "selectscreen.png");
		selectscreenRegion = new TextureRegion(selectscreen, 0, 384, 960, 640);
		notReady = new TextureRegion(selectscreen, 0, 224, 128, 128);
		ready = new TextureRegion(selectscreen, 128, 224, 128, 128);
		//fight = new TextureRegion(selectscreen, 768, 0, 256, 256);
		
		gamescreen = new Texture(((GLGame)game), "gamescreen.png");
		gamescreenRegion = new TextureRegion(gamescreen, 0, 384, 960, 640);

		you_win = new TextureRegion(gamescreen, 1, 1, 383, 159);
		you_lose = new TextureRegion(gamescreen, 1, 160, 383, 160);
		play_again = new TextureRegion(gamescreen, 384, 1, 224, 127);
		main_menu = new TextureRegion(gamescreen, 384, 128, 224, 128);
		game_overlay = new TextureRegion(gamescreen, 608, 1, 32, 31);
		shot = new TextureRegion(gamescreen, 768, 1, 32, 65);
		
		/*
		randomDirection = new Animation(0.15f,
				new TextureRegion(gamescreen, 768, 64, 64, 64),
				new TextureRegion(gamescreen, 832, 64, 64, 64),
				new TextureRegion(gamescreen, 896, 64, 64, 64));
		*/
		float POWER_UP_X = 768;
		float POWER_UP_Y = 0;
		for(int i = 0; i < NUM_POWER_UPS; i++){
			powerUpArray.add(new Animation(0.15f,
					new TextureRegion(gamescreen, POWER_UP_X, POWER_UP_Y, 64, 64),
					new TextureRegion(gamescreen, POWER_UP_X + 64, POWER_UP_Y, 64, 64),
					new TextureRegion(gamescreen, POWER_UP_X + 128, POWER_UP_Y, 64, 64)));
			POWER_UP_Y += 64;
		}
		
		/*
		randomDirection = new TextureRegion(gamescreen, 768, 64, 64, 64);
		
		speedUp = new Animation(0.15f,
				new TextureRegion(gamescreen, 768, 160, 64, 64),
				new TextureRegion(gamescreen, 832, 160, 64, 64),
				new TextureRegion(gamescreen, 896, 160, 64, 64));
		*/


		/*****************************************
		 * FONT
		 ****************************************/
		
		fontMap = new Texture(((GLGame)game), "fontmap.png");
		fontRed = new Font(fontMap,0,0,16,38,38);
		fontWhite = new Font(fontMap,0,228,16,38,38);
	}

	public static void loadAudio(GLGame game){
		
		/*
		book_sound = game.getAudio().newSound("sounds/book.wav");
		
		oofArray.add(game.getAudio().newSound("sounds/oof1.mp3"));
		oofArray.add(game.getAudio().newSound("sounds/oof2.mp3"));
		oofArray.add(game.getAudio().newSound("sounds/oof3.mp3"));

		music = game.getAudio().newMusic("sounds/jamesoftheapes.mp3");
		music.setLooping(true);
		music.setVolume(0.3f);
		//if(Settings.soundEnabled)
			//music.play();
	*/
	}
	
	public static void playSound(Sound sound){
		//if(Settings.soundEnabled){  //DO NOT CURRENTLY USE SETTINGS IN BYTERFYTER SO THIS WILL NOT BE SET YET
			sound.play(1);
		//}
	}
}
