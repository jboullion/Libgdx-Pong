package com.jboullion.pong;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;
import com.jboullion.pong.objects.Ball;



public class PaddleSelection extends GLScreen {
	Camera2D guiCam;
	SpriteBatcher batcher;
	
	//PongMenu.NUM_PLAYERS
	
	Texture selectscreen;
	TextureRegion selectscreenRegion;
	
	Texture paddles;
	
	float stateTime;
	
	ArrayList<Rectangle> paddleBoundsArray = new ArrayList<Rectangle>();
	ArrayList<Rectangle> paddle2BoundsArray = new ArrayList<Rectangle>();
	
	Vector2 touchPoint;
	static final String TAG = "PaddleSelection";
	
	final float MIDDLEX = 960/2;
	final float MIDDLEY = 640/2;
	
	int fightTracker = 0;
	
	final float PADDLE_WIDTH = 32;
	final float PADDLE_HEIGHT = 160;
	final float BALL_DIAMETER = 64;
	float PADDLEX = 64 + PADDLE_WIDTH/2;
	float PADDLE2X = 544 + PADDLE_WIDTH/2;
	float PADDLEY = 242;
	
	public static int paddleSelected;
	public static int paddle2Selected;
	
	boolean playerOneReady;
	boolean playerTwoReady;
	
	Rectangle readyOneBounds;
	Rectangle readyTwoBounds;
	
	final float READY_WIDTH = 128;
	final float READY_HEIGHT = 128;
	float READYX = 96;
	float READY2X = 864;
	float READYY = 480;
	
	public PaddleSelection(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 960, 640);
		batcher = new SpriteBatcher(glGraphics, 100);

		touchPoint = new Vector2();
		
		readyOneBounds = new Rectangle(READYX-(READY_WIDTH/2), READYY-(READY_HEIGHT/2), READY_WIDTH, READY_HEIGHT);
		readyTwoBounds = new Rectangle(READY2X-(READY_WIDTH/2), READYY-(READY_HEIGHT/2), READY_WIDTH, READY_HEIGHT);
		
		paddleSelected = -1;
		paddle2Selected = -1;
		
		paddleBoundsArray.clear();
		paddle2BoundsArray.clear();
		
		for(int i = 0; i < Assets.NUM_PADDLES; i++){
			/*
        	if(i % 6 == 0){
        		PADDLEX = 64 + PADDLE_WIDTH/2;
        		PADDLE2X = 544 + PADDLE_WIDTH/2;
        		PADDLEY -= PADDLE_HEIGHT + 32;
			}
			*/
        	paddleBoundsArray.add(new Rectangle(PADDLEX-PADDLE_WIDTH, PADDLEY-PADDLE_HEIGHT-BALL_DIAMETER/2, PADDLE_WIDTH*2, PADDLE_HEIGHT + BALL_DIAMETER * 2));
        	PADDLEX += PADDLE_WIDTH * 2;
        	
        	paddle2BoundsArray.add(new Rectangle(PADDLE2X-PADDLE_WIDTH, PADDLEY-PADDLE_HEIGHT-BALL_DIAMETER/2, PADDLE_WIDTH*2, PADDLE_HEIGHT + BALL_DIAMETER * 2));
        	PADDLE2X += PADDLE_WIDTH * 2;
        }
        
        PADDLEX = 64 + PADDLE_WIDTH/2;
    	PADDLE2X = 544 + PADDLE_WIDTH/2;
    	
    	PADDLEY = 242;
		
    	if(PongMenu.NUM_PLAYERS == 1){
    		paddle2Selected = ((int) Math.ceil(Math.random() * Assets.NUM_PADDLES))-1;
    	}
    	
    	playerOneReady = false;
    	
    	if(PongMenu.NUM_PLAYERS == 1){
    		playerTwoReady = true;
		}else{
			playerTwoReady = false;
		}
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		if(playerOneReady && playerTwoReady){
			fightTracker++;
			if(fightTracker >=30){
				game.setScreen(new GameScreen(game));
			}
		}
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			if(event.type == TouchEvent.TOUCH_DOWN){
				touchPoint.set(event.x, event.y);
				guiCam.touchToWorld(touchPoint);
				Log.d(TAG, "X="+touchPoint.x+", Y="+touchPoint.y);
				
				for(int j = 0; j<Assets.NUM_PADDLES;j++){
					
					if(OverlapTester.pointInRectangle(paddleBoundsArray.get(j), touchPoint)){
						//Assets.playSound(hitLetter);
						Log.d(TAG, "X="+touchPoint.x+", Y="+touchPoint.y+" Inside Paddle " + j + " Bounds!");		
						paddleSelected = j;
						
					}
				}
				
				if(paddleSelected != -1){
					if(OverlapTester.pointInRectangle(readyOneBounds, touchPoint)){
						//Assets.playSound(hitLetter);
						Log.d(TAG, "X="+touchPoint.x+", Y="+touchPoint.y+" Inside Ready One");		
						if(playerOneReady){
							playerOneReady = false;
						}else{
							playerOneReady = true;
						}
					}
				}
			
				if(PongMenu.NUM_PLAYERS == 2){
					
					for(int j = 0; j<Assets.NUM_PADDLES;j++){
						if(OverlapTester.pointInRectangle(paddle2BoundsArray.get(j), touchPoint)){
							//Assets.playSound(hitLetter);
							Log.d(TAG, "X="+touchPoint.x+", Y="+touchPoint.y+" Inside Paddle2 " + j + " Bounds!");		
							paddle2Selected = j;
							
						}
					}
					
					if(paddle2Selected != -1){
						if(OverlapTester.pointInRectangle(readyTwoBounds, touchPoint)){
							//Assets.playSound(hitLetter);
							Log.d(TAG, "X="+touchPoint.x+", Y="+touchPoint.y+" Inside Ready Two");			
							if(playerTwoReady){
								playerTwoReady = false;
							}else{
								playerTwoReady = true;
							}
							
						}
					}//END if(paddle2Selected != -1)
		        }//END if(PongMenu.NUM_PLAYERS == 2)
			}//END if(event.type == TouchEvent.TOUCH_DOWN)
		}//END for(int i = 0; i < len; i++)
	}//END public void update(float deltaTime)

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
		
		batcher.beginBatch(selectscreen);
		batcher.drawSprite(MIDDLEX, MIDDLEY, 960, 640, Assets.selectscreenRegion);
    	
    	if(!playerOneReady){
    		batcher.drawSprite(READYX, READYY, READY_WIDTH, READY_HEIGHT, Assets.notReady);
    	}else{
    		batcher.drawSprite(READYX, READYY, READY_WIDTH, READY_HEIGHT, Assets.ready);
    	}
    	
    	if(!playerTwoReady){
    		batcher.drawSprite(READY2X, READYY, READY_WIDTH, READY_HEIGHT, Assets.notReady);
    	}else{
    		batcher.drawSprite(READY2X, READYY, READY_WIDTH, READY_HEIGHT, Assets.ready);
    	}
    	
		batcher.endBatch();
		
		batcher.beginBatch(paddles);
		//DRAW PADDLES
		//int paddleCount = 0;
		for(int i = 0; i < Assets.NUM_PADDLES; i++) {
			
			/*
        	if(paddleCount % 6 == 0){
        		PADDLEX = 64 + PADDLE_WIDTH/2;
        		PADDLE2X = 544 + PADDLE_WIDTH/2;
        		PADDLEY -= PADDLE_HEIGHT + 32;
    		}
    		*/
    		
        	batcher.drawSprite(PADDLEX, PADDLEY, PADDLE_WIDTH, PADDLE_HEIGHT, Assets.paddlesArray.get(i));
        	batcher.drawSprite(PADDLEX, PADDLEY - PADDLE_HEIGHT, BALL_DIAMETER, BALL_DIAMETER, Assets.ballsArray.get(i));
        	PADDLEX += PADDLE_WIDTH * 2;
        	
        	batcher.drawSprite(PADDLE2X, PADDLEY, PADDLE_WIDTH, PADDLE_HEIGHT, Assets.paddlesArray.get(i));
        	batcher.drawSprite(PADDLE2X, PADDLEY - PADDLE_HEIGHT, BALL_DIAMETER, BALL_DIAMETER, Assets.ballsArray.get(i));
        	PADDLE2X += PADDLE_WIDTH * 2;
        	
        	//paddleCount++;
		}
		 
		if(paddleSelected != -1){
			batcher.drawSprite(240, 480, PADDLE_WIDTH, PADDLE_HEIGHT, Assets.paddlesArray.get(paddleSelected));
			
		}	
		
		if(paddle2Selected != -1){
			batcher.drawSprite(720, 480, PADDLE_WIDTH, PADDLE_HEIGHT, Assets.paddlesArray.get(paddle2Selected));
			
		}
		
		PADDLEX = 64 + PADDLE_WIDTH/2;
    	PADDLE2X = 544 + PADDLE_WIDTH/2;
    	PADDLEY = 242;
    	
    	//DISPLAY FIGHT
		if(playerOneReady && playerTwoReady){
			batcher.drawSprite(MIDDLEX, MIDDLEY, 256, 256, Assets.fight);
		}
    			
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);

	}

	@Override
	public void pause() {
		//Settings.save(game.getFileIO());
		selectscreen.dispose();
		paddles.dispose();
		//if(Assets.music.isPlaying()){
			//Assets.music.pause();
		//}
	}

	@Override
	public void resume() {
		selectscreen = new Texture(((GLGame)game), "selectscreen.png");
		//selectscreenRegion = new TextureRegion(selectscreen, 0, 384, 960, 640);
		
		paddles = new Texture(((GLGame)game), "paddles.png");

		//if(!Assets.music.isPlaying()){
		//	Assets.music.play();
		//}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
