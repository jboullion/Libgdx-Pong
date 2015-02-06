package com.jboullion.pong;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Sound;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.Circle;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class PongMenu extends GLScreen {
	Camera2D guiCam;
	SpriteBatcher batcher;

	Circle highscoresBounds;
	Rectangle PLAYER_1_RECT;
	Rectangle PLAYER_2_RECT;
	Rectangle HOT_TO_RECT;
	
	final float PLAYER_1_RECT_X = 320;
	final float PLAYER_1_RECT_Y = 256;
	final float PLAYER_1_RECT_WIDTH = 320;
	final float PLAYER_1_RECT_HEIGHT = 64;
	
	final float PLAYER_2_RECT_X = 320;
	final float PLAYER_2_RECT_Y = 160;
	final float PLAYER_2_RECT_WIDTH = 320;
	final float PLAYER_2_RECT_HEIGHT = 64;
	
	public static int NUM_PLAYERS;
	Sound fyte;
	
	Texture menuscreen;
	TextureRegion menuscreenRegion;
	
	float stateTime;
	
	Vector2 touchPoint;
	static final String TAG = "PongMenu";
	
	final float MIDDLEX = 960/2;
	final float MIDDLEY = 640/2;
	
	public PongMenu(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 960, 640);
		batcher = new SpriteBatcher(glGraphics, 100);

		PLAYER_1_RECT = new Rectangle(PLAYER_1_RECT_X, PLAYER_1_RECT_Y, PLAYER_1_RECT_WIDTH, PLAYER_1_RECT_HEIGHT);
		PLAYER_2_RECT = new Rectangle(PLAYER_2_RECT_X, PLAYER_2_RECT_Y, PLAYER_2_RECT_WIDTH, PLAYER_2_RECT_HEIGHT);
		highscoresBounds = new Circle(MIDDLEX, MIDDLEY + 256, 80); //I set the skull image on top to use as the highScore screen button for now
		touchPoint = new Vector2();
		//byter1up = game.getAudio().newSound("sounds/byter1up.mp3");
		
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		/*
		if(fistHit){
			if(fistTracker >=15){
				game.setScreen(new GameScreen(game));
			}
		}
		
		if(openBook){
			if(bookTracker >=15){
				game.setScreen(new TutorialScreen(game));
			}
		}
		*/
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			if(event.type == TouchEvent.TOUCH_DOWN){
				touchPoint.set(event.x, event.y);
				guiCam.touchToWorld(touchPoint);
				//Log.d(TAG, "X="+touchPoint.x+", Y="+touchPoint.y);
				if(OverlapTester.pointInRectangle(PLAYER_1_RECT, touchPoint)){
					//Assets.playSound(fyte);
					NUM_PLAYERS = 1;
					game.setScreen(new PaddleSelection(game));
					//game.setScreen(new GameScreen(game));
					return;
				}else if(OverlapTester.pointInRectangle(PLAYER_2_RECT, touchPoint)){
					//Assets.playSound(fyte);
					NUM_PLAYERS = 2;
					game.setScreen(new PaddleSelection(game));
					//game.setScreen(new GameScreen(game));
					return;
				}
			}
		}

	}

	@Override
	public void present(float deltaTime) {
		if(deltaTime > 0.1f){
			deltaTime = 0.1f;
		}
		stateTime += deltaTime;
		//TextureRegion keyFrame = null;
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(menuscreen);
		batcher.drawSprite(MIDDLEX, MIDDLEY, 960, 640, menuscreenRegion);
		
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);

	}

	@Override
	public void pause() {
		//Settings.save(game.getFileIO());
		//menuscreen.dispose();
		//if(Assets.music.isPlaying()){
			//Assets.music.pause();
		//}
	}

	@Override
	public void resume() {
		menuscreen = new Texture(((GLGame)game), "menuscreen.png");
		menuscreenRegion = new TextureRegion(menuscreen, 0, 384, 960, 640);

		//if(!Assets.music.isPlaying()){
		//	Assets.music.play();
		//}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}