package com.jboullion.pong;

import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.FlingDetector;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;
import com.jboullion.pong.World.WorldListener;
import com.jboullion.pong.objects.Ball;
import com.jboullion.pong.objects.Paddle;
import com.jboullion.pong.objects.PowerUp;
import com.jboullion.pong.objects.Shot;


public class GameScreen extends GLScreen {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	
	static final int WIN_SCORE = 10;
	
	int state;
	Camera2D guiCam;
	SpriteBatcher batcher;
	Vector2 touchPoint;
	Vector2 flingDirection;
	FlingDetector flings = new FlingDetector();
	public static World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	
	public long newTouch = 0;
	public long oldTouch = 0;
	public long newTouch2 = 0;
	public long oldTouch2 = 0;
	
	public static int pOneScore = 0;
	public static int pTwoScore = 0;
	public static String pOneScoreString = "0";
	public static String pTwoScoreString = "0";
	
	public static boolean swipeLeft = false;
	public static boolean swipeRight = false;
	
	public static int swipeLcounter = 0;
	public static int swipeRcounter = 0;
	
	Vector2 startPosition = new Vector2();
	Vector2 endPosition = new Vector2();
	Rectangle backArrowBounds;

	static final String TAG = "GameScreen";
	
	final float MIDDLEX = 960/2;
	final float MIDDLEY = 640/2;
	
	Rectangle MENU_RECT;
	final float MENU_RECT_X = 288;
	final float MENU_RECT_Y = 32;
	final float MENU_RECT_WIDTH = 128;
	final float MENU_RECT_HEIGHT = 64;
	
	Rectangle PAUSE_RECT;
	final float PAUSE_RECT_X = 520;
	final float PAUSE_RECT_Y = 32;
	final float PAUSE_RECT_WIDTH = 128;
	final float PAUSE_RECT_HEIGHT = 64;
	
	Rectangle PLAY;
	final float PLAY_X = MIDDLEX - Assets.play_again.width/2 - 10;
	final float PLAY_Y = MIDDLEY - Assets.play_again.height - 10;
	final float PLAY_WIDTH = Assets.play_again.width;
	final float PLAY_HEIGHT = Assets.play_again.height;
	
	Rectangle OVER_MENU;
	final float OVER_MENU_X = MIDDLEX + Assets.main_menu.width/2 + 10;
	final float OVER_MENU_Y = MIDDLEY - Assets.main_menu.height - 10;
	final float OVER_MENU_WIDTH = Assets.main_menu.width;
	final float OVER_MENU_HEIGHT = Assets.main_menu.height;
	
	public static final Random rand = new Random();
	
	public GameScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 960, 640);
		batcher = new SpriteBatcher(glGraphics, 200);
		touchPoint = new Vector2();
		worldListener = new WorldListener(){
			
			@Override
			public void get(){
				//Assets.playSound(byter1up);
			}
			
		};
		world = new World(worldListener);
		renderer = new WorldRenderer(glGraphics, batcher, world);

		MENU_RECT = new Rectangle(MENU_RECT_X, MENU_RECT_Y, MENU_RECT_WIDTH, MENU_RECT_HEIGHT);
		PAUSE_RECT = new Rectangle(PAUSE_RECT_X, PAUSE_RECT_Y, PAUSE_RECT_WIDTH, PAUSE_RECT_HEIGHT);
		
		PLAY = new Rectangle(PLAY_X-(PLAY_WIDTH/2), PLAY_Y-(PLAY_HEIGHT/2), PLAY_WIDTH, PLAY_HEIGHT);
		OVER_MENU = new Rectangle(OVER_MENU_X-(OVER_MENU_WIDTH/2), OVER_MENU_Y-(OVER_MENU_HEIGHT/2), OVER_MENU_WIDTH, OVER_MENU_HEIGHT);

	}

	@Override
	public void update(float deltaTime) {
		if(deltaTime > 0.1f){
			deltaTime = 0.1f;
		}
		
		switch(state){
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		//case GAME_LEVEL_END:
		//	updateLevelEnd();
		//	break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateReady(){
		//if(game.getInput().getTouchEvents().size() > 0){
			state = GAME_RUNNING;
		//}
	}
	
	/***********************************************************************
	 * UPDATE THIS METHOD WHEN CHECKING FOR USER INPUT ON THE GAME SCREEN
	 ***********************************************************************/
	private void updateRunning(float deltaTime){
		world.update(deltaTime);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		flings.processEvents(touchEvents);
		newTouch++;
		newTouch2++;
		for (TouchEvent event : touchEvents){
			
			if(event.type == TouchEvent.TOUCH_UP){
				touchPoint.set((event.x / (float) glGraphics.getWidth()) * 15, (1 - event.y / (float) glGraphics.getHeight()) * 10);
				endPosition.set(touchPoint.x, touchPoint.y);
				
				if(world.balls.get(0).isRolling==false){
					//MAKE SURE  THE DRAG IS LONG ENOUGH
					if(Math.abs(startPosition.x - endPosition.x) > 2){
						
						//RIGHT PADDLE BALL FACES LEFT
						if(world.balls.get(0).direction==Ball.LEFT){
							if(startPosition.x - endPosition.x < 1f){
								return;
							}else{
								world.balls.get(0).isRolling = true;
							}
						}
						
						//LEFT PADDLE BALL FACES RIGHT
						if(world.balls.get(0).direction==Ball.RIGHT){
							if(startPosition.x - endPosition.x > 1f){
								return;
							}else{
								world.balls.get(0).isRolling = true;
							}
						}
						world.balls.get(0).velocity.x = ((startPosition.x - endPosition.x)*-1)*2;
						world.balls.get(0).velocity.y = ((startPosition.y - endPosition.y)*-1)*2;
						
						
						//Log.d(TAG, "world.ball.velocity.x = "+world.ball.velocity.x+ " world.ball.velocity.y=" + world.ball.velocity.y);
					}
				}else{
					for(Ball ball: world.balls){
						if(ball.isStuck){
							world.balls.get(0).velocity.x = ((startPosition.x - endPosition.x)*-1)*2;
							world.balls.get(0).velocity.y = ((startPosition.y - endPosition.y)*-1)*2;
							ball.isStuck = false;
							ball.isSticky = false;
						}
					}
				}
			}
			
			if(event.type == TouchEvent.TOUCH_DOWN){
				touchPoint.set(event.x,event.y);
				guiCam.touchToWorld(touchPoint);
				if(OverlapTester.pointInRectangle(MENU_RECT, touchPoint)){
					endGame();
					game.setScreen(new PongMenu(game));
					
				}

				if(OverlapTester.pointInRectangle(PAUSE_RECT, touchPoint)){
					pause();
				}
				touchPoint.set((event.x / (float) glGraphics.getWidth()) * 15, (1 - event.y / (float) glGraphics.getHeight()) * 10);
				if(world.balls.get(0).isRolling==false){
					//Start point for throwing ball
					startPosition.set(touchPoint.x, touchPoint.y);
				}else{
					//CHECK FOR DOUBLE TOUCH/SHOOT POWER ONLY WHEN BALL IS ROLLING
					checkDoubleTouch(deltaTime, touchPoint);
				}
				
				for(Ball ball: world.balls){
					if(ball.isStuck){
						//Throw ball if stuck
						startPosition.set(touchPoint.x, touchPoint.y);
					}
				}
			}
			
			if(event.type == TouchEvent.TOUCH_DRAGGED){
				//SCALE POINT TO Float system instead of pixel system.
				touchPoint.set((event.x / (float) glGraphics.getWidth()) * 15, (1 - event.y / (float) glGraphics.getHeight()) * 10);
				/*
				 * IF TOUCHPOINT IS AT 1F OR 14F....OR IF TOUCHPOINT IS WITHIN 2F OF A PADDLE MOVE PADDLE TO THAT TOUCHPOINT'S Y 
				 */
					
				if(touchPoint.x < world.paddles.get(0).position.x + 1f && touchPoint.x > world.paddles.get(0).position.x - 1f){
					world.paddles.get(0).move(touchPoint);
				}
				
				//CHECK IF PLAYING 1 OR 2 PLAYER AND HANDLE CORRECTLY
				if(PongMenu.NUM_PLAYERS == 2){
					if(touchPoint.x < world.paddles.get(1).position.x + 1f && touchPoint.x > world.paddles.get(1).position.x - 1f){
						world.paddles.get(1).move(touchPoint);
					}
				}
				
			}
				
		}
		
			
		/********************************************************
		 * Check if the game is over by checking scores
		 ********************************************************/
		
		if(world.state == World.WORLD_STATE_GAME_OVER){
			state = GAME_OVER;
			//Assets.playSound(Assets.lasthit);
			//game.setScreen(new YouDieScreen(game));
		}else if(PongMenu.NUM_PLAYERS == 1 && pOneScore < WIN_SCORE && pTwoScore < WIN_SCORE){
			//CONTROL AI-----------------------------------------------------------------------
			moveAI();
			
			//THROW BALL IF IT IS AI'S TURN
			if(world.balls.get(0).direction == Ball.LEFT && world.balls.get(0).velocity.x == 0 && world.balls.get(0).velocity.y == 0){
				world.balls.get(0).velocity.x = (rand.nextFloat()+1)*-(rand.nextInt(8)+1);
				world.balls.get(0).velocity.y= (rand.nextFloat()+1)*-(rand.nextInt(8)+1);
				world.balls.get(0).isRolling = true;
			}
			
		}
		//game.getInput().getTouchEvents().clear();
	}
	
	
	private void updatePaused(){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();	

		for (TouchEvent event : touchEvents){

			if(event.type == TouchEvent.TOUCH_UP){
				continue;
			}
			
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			//HERE IS CODE TO WAKE FROM PAUSE
		}
	}
	
	private void updateGameOver(){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		for (TouchEvent event : touchEvents){
			
			if(event.type == TouchEvent.TOUCH_DOWN){
				touchPoint.set(event.x,event.y);
				guiCam.touchToWorld(touchPoint);
				//Log.d(TAG, "touchPoint.x = "+touchPoint.x+ " touchPoint.y=" + touchPoint.y);
				
				if(OverlapTester.pointInRectangle(PLAY, touchPoint)){
					//Log.d(TAG, "Inside Play!");
					endGame();
					state = GAME_RUNNING;
					world.state = World.WORLD_STATE_RUNNING;
					world.stop = false;
					
				}
				
				if(OverlapTester.pointInRectangle(OVER_MENU, touchPoint)){
					//Log.d(TAG, "Inside Over Menu!");
					endGame();
					game.setScreen(new PongMenu(game));
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		renderer.render();
		
		guiCam.setViewportAndMatrices();
		
		switch(state){
		case GAME_READY:
			presentReady();
			break;
		case GAME_RUNNING:
			presentRunning(deltaTime);
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		//case GAME_LEVEL_END:
		//	presentLevelEnd();
		//	break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	public void presentReady(){
		drawScore();
	}
	
	/*
	 * USED TO DISPLAY THE TOP OVERLAY. THINGS LIKE SCORE AND THE HEARTS AND COLLECTED ICONS.
	 */
	public void presentRunning(float deltaTime){
		drawScore();
		
		/*
		 * PUT TOP OVERLAY HERE
		 */
	}
	
	public void presentPaused(){
		drawScore();
	}
	
	public void presentGameOver(){
		drawScore();
		drawEndOverlay();
	}

	private void drawEndOverlay() {
		//DRAW GAME OVER STATE
		batcher.beginBatch(Assets.gamescreen);

		batcher.drawSprite(MIDDLEX, MIDDLEY, MIDDLEX*2, MIDDLEY*2, Assets.game_overlay);
		if(pOneScore > pTwoScore){
			//PLAYER ONE WIN
			batcher.drawSprite(MIDDLEX, MIDDLEY, Assets.you_win.width, Assets.you_win.height, Assets.you_win);
		}else{
			//PLAYER TWO WIN
			batcher.drawSprite(MIDDLEX, MIDDLEY, Assets.you_lose.width, Assets.you_lose.height, Assets.you_lose);
		}
		
		batcher.drawSprite(PLAY_X, PLAY_Y, PLAY_WIDTH, PLAY_HEIGHT, Assets.play_again);
		batcher.drawSprite(OVER_MENU_X, OVER_MENU_Y, OVER_MENU_WIDTH, OVER_MENU_HEIGHT, Assets.main_menu);
		
		batcher.endBatch();
		
	}

	@Override
	public void pause() {
		if(world.stop){
			world.stop = false;
		}else{
			world.stop = true;
		}
		
		/*
		if(state == GAME_RUNNING){
			state = GAME_PAUSED;
		}
		*/
	}

	@Override
	public void resume() {
		
		Assets.load(glGame);
		//if(Assets.music.isPlaying()){
		//	Assets.music.pause();
		//}
		if(state == GAME_PAUSED){
			state = GAME_RUNNING;
		}
	}

	@Override
	public void dispose() {
		
	}
	
	public void checkDoubleTouch(float deltaTime, Vector2 touchPoint){

		if(touchPoint.x < world.paddles.get(0).position.x + 1f && touchPoint.x > world.paddles.get(0).position.x - 1f){
			if(touchPoint.y <= world.paddles.get(0).position.y + Paddle.PADDLE_HEIGHT/2 && touchPoint.y >= world.paddles.get(0).position.y - Paddle.PADDLE_HEIGHT/2){
				
				//Log.d(TAG, "Inside Paddle Area: newTouch="+newTouch+" oldTouch="+oldTouch + 0.2f);
				if(newTouch < oldTouch + 20){
					world.pOneShots.add(shoot(world.paddles.get(0).position, 1));
				}
				oldTouch = newTouch;
			}
		}
		
		if(touchPoint.x < world.paddles.get(1).position.x + 1f && touchPoint.x > world.paddles.get(1).position.x - 1f){
			if(touchPoint.y <= world.paddles.get(1).position.y + Paddle.PADDLE_HEIGHT/2 && touchPoint.y >= world.paddles.get(1).position.y - Paddle.PADDLE_HEIGHT/2){
				
				//Log.d(TAG, "Inside Paddle Area: newTouch2="+newTouch2+" oldTouch2="+oldTouch2 + 0.2f);
				if(newTouch2 < oldTouch2 + 20){
					world.pTwoShots.add(shoot(world.paddles.get(1).position, 2));
				}
				oldTouch2 = newTouch2;
			}
		}		
	}
	
	public static PowerUp generatePowerUp(){
		float randomX = (rand.nextFloat() * 10) + 2.5f;
		float randomY = (rand.nextFloat() * 8)+ 1f;
		Vector2 point = new Vector2(randomX,randomY);
		while(OverlapTester.pointInCircle(world.balls.get(0).bounds, point)){
			randomX = (rand.nextFloat() * 10) + 2.5f;
			randomY = (rand.nextFloat() * 8)+ 1f;
			point = new Vector2(randomX,randomY);
		}
		int powerType = rand.nextInt(6);
		//STICKY PADDLE IS STILL FUCKED UP
		//int powerType = PowerUp.LOOP;
		return (new PowerUp(randomX, randomY, powerType));
	}
	
	public static Shot shoot(Vector2 position, int player){
		return (new Shot(position.x, position.y, player));
	}
	
	public void drawScore(){
		batcher.beginBatch(Assets.fontMap);
		Assets.fontWhite.drawBigText(batcher, pOneScoreString, 400, 550);
		Assets.fontWhite.drawBigText(batcher, pTwoScoreString, 560, 550);
		batcher.endBatch();
	}
	
	public void moveAI(){
		
		//RANDOMLY SHOOT
		if(GameScreen.rand.nextInt(100) < 2 && world.balls.get(0).isRolling){
			pTwoPower();
		}
		
		Paddle aIpaddle = world.paddles.get(1);
		
		int closestBall = 0;
		//EASE PADDLE TOWARDS THE CLOSEST BALL (MULTI BALL)
		int len = world.balls.size();
		if(len > 1){
			for(int i = len-1; i >= 1; i--){
				Ball previousBall = world.balls.get(i-1);
				Ball ball = world.balls.get(i);
				
				//CHECK WHICH BALL IS CLOSER TO AI PADDLE
				if(ball.position.x > previousBall.position.x){
					closestBall = i;
				}
			}
		}
		
		switch(aIpaddle.affliction){
			case Paddle.SLOW:
				//easeAIPaddle(aIpaddle, closestBall);
				
				if(world.balls.get(closestBall).position.y < aIpaddle.position.y - Paddle.PADDLE_HEIGHT){
					aIpaddle.position.y -= 0.08f;
				}else if(world.balls.get(closestBall).position.y < aIpaddle.position.y - Paddle.PADDLE_HEIGHT/2){
					aIpaddle.position.y -= 0.5f;
				}else if(world.balls.get(closestBall).position.y > aIpaddle.position.y + Paddle.PADDLE_HEIGHT){
					aIpaddle.position.y += 0.08f;
				}else if(world.balls.get(closestBall).position.y > aIpaddle.position.y + Paddle.PADDLE_HEIGHT/2){
					aIpaddle.position.y += 0.5f;
				}
				break;
			case Paddle.FREEZE:
				//DO NOTHING					
				break;
			case Paddle.SHRINK:
				easeAIPaddle(aIpaddle, closestBall);
				break;
			case Paddle.FIRE:
				easeAIPaddle(aIpaddle, closestBall);
				
				if(aIpaddle.afflictionCounter % 10 == 0){
					if(GameScreen.rand.nextInt(2) == 1){
						aIpaddle.position.y += GameScreen.rand.nextFloat();
					}else{
						aIpaddle.position.y -= GameScreen.rand.nextFloat();
					}
				}
				break;
			case Paddle.REVERSE:
				easeAIPaddle(aIpaddle, closestBall);
				
				if(GameScreen.rand.nextInt(2) == 1){
					aIpaddle.position.y += 0.5f;
				}else{
					aIpaddle.position.y -= 0.5f;
				}
				//this.position.y = -touchpoint.y + 10f;					
				break;
			default:
				easeAIPaddle(aIpaddle, closestBall);
				break;
		}
		
		//KEEP PADDLE INSIDE BOX
		if(aIpaddle.position.y < aIpaddle.height /2){
			aIpaddle.position.y = aIpaddle.height /2;
		}
		if(aIpaddle.position.y > World.WORLD_HEIGHT - aIpaddle.height /2){
			aIpaddle.position.y = World.WORLD_HEIGHT - aIpaddle.height /2;
		}

		aIpaddle.bounds.center.set(aIpaddle.position);
		aIpaddle.rect.y = aIpaddle.position.y - Paddle.PADDLE_HEIGHT /2;
		
	}
	
	private void easeAIPaddle(Paddle aIpaddle, int closestBall){
		//CHANGE MOVE SPEED BASED ON DISTANCE FROM BALL TO ATTEMPT TO APPEAR LESS "AI" LIKE
		if(world.balls.get(closestBall).position.y < aIpaddle.position.y - Paddle.PADDLE_HEIGHT){
			aIpaddle.position.y -= 0.15f;
		}else if(world.balls.get(closestBall).position.y < aIpaddle.position.y - Paddle.PADDLE_HEIGHT/2){
			aIpaddle.position.y -= 0.1f;
		}else if(world.balls.get(closestBall).position.y > aIpaddle.position.y + Paddle.PADDLE_HEIGHT){
			aIpaddle.position.y += 0.15f;
		}else if(world.balls.get(closestBall).position.y > aIpaddle.position.y + Paddle.PADDLE_HEIGHT/2){
			aIpaddle.position.y += 0.1f;
		}
	}
	
	//USE PLAYER ONE PADDLES POWER
	public void pOnePower(){
		world.pOneShots.add(shoot(world.paddles.get(0).position, 1));
	}
	
	//USE PLAYER TWO PADDLES POWER
	public void pTwoPower(){
		world.pTwoShots.add(shoot(world.paddles.get(1).position, 2));
	}
	
	//RESET GAME VARIABLES
	
	public static void endGame(){
		pOneScore = 0;
		pTwoScore = 0;
		pOneScoreString = "0";
		pTwoScoreString = "0";
	}

}
