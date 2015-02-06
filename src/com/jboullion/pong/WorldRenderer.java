package com.jboullion.pong;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGraphics;
import com.jboullion.pong.objects.Ball;
import com.jboullion.pong.objects.Paddle;
import com.jboullion.pong.objects.PowerUp;
import com.jboullion.pong.objects.Shot;

public class WorldRenderer {
	static final float FRUSTRUM_WIDTH = 15;
	static final float FRUSTRUM_HEIGHT = 10;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	SpriteBatcher batcher;
	
	int ballLooping = Animation.ANIMATION_NONLOOPING;
	static final String TAG = "WorldRenderer";
	
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world){
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new Camera2D(glGraphics, FRUSTRUM_WIDTH, FRUSTRUM_HEIGHT);
		this.batcher = batcher;
	}
	
	public void render(){
		cam.setViewportAndMatrices();
		renderGameScreen();
	}
	
	
	/****************************************************
	 * RENDER GAME SCREEN AND ALL ITS OBJECTS
	 ****************************************************/
	
	public void renderGameScreen(){
		TextureRegion keyFrame = null;

		batcher.beginBatch(Assets.gamescreen);
		//DRAW GAMESCREEN
		batcher.drawSprite(cam.position.x, cam.position.y, FRUSTRUM_WIDTH, FRUSTRUM_HEIGHT, Assets.gamescreenRegion);
		/*
		int len = 0;
		len = world.powerUps.size();
		if(len > 0){
			for(int i = len-1; i >= 0; i--){
				PowerUp powerUp = world.powerUps.get(i);
				keyFrame = Assets.powerUpArray.get(powerUp.type).getKeyFrame(powerUp.stateTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(powerUp.position.x, powerUp.position.y, PowerUp.POWER_UP_RADIUS * 2, PowerUp.POWER_UP_RADIUS*2, keyFrame);
			}
		}
		*/
		
		for(PowerUp powerUp : world.powerUps){
			keyFrame = Assets.powerUpArray.get(powerUp.type).getKeyFrame(powerUp.stateTime, Animation.ANIMATION_LOOPING);
			batcher.drawSprite(powerUp.position.x, powerUp.position.y, PowerUp.POWER_UP_RADIUS * 2, PowerUp.POWER_UP_RADIUS*2, keyFrame);
		}

		batcher.endBatch();
		
		
		batcher.beginBatch(Assets.paddles);
		//DRAW BALLS
		for(Ball ball : world.balls){
			if(ball.direction == Ball.RIGHT){
				batcher.drawSprite(ball.position.x, ball.position.y, Ball.BALL_RADIUS, Ball.BALL_HEIGHT, ball.angle, Assets.ballsArray.get(world.paddles.get(0).type));
			}else if(ball.direction == Ball.LEFT){
				batcher.drawSprite(ball.position.x, ball.position.y, Ball.BALL_RADIUS, Ball.BALL_HEIGHT, ball.angle, Assets.ballsArray.get(world.paddles.get(1).type));
			}
		}
		
		
		//DRAW PADDLES
		batcher.drawSprite(world.paddles.get(0).position.x, world.paddles.get(0).position.y, world.paddles.get(0).width, world.paddles.get(0).height, Assets.paddlesArray.get(world.paddles.get(0).type));
		batcher.drawSprite(world.paddles.get(1).position.x, world.paddles.get(1).position.y, world.paddles.get(1).width, world.paddles.get(1).height, Assets.paddlesArray.get(world.paddles.get(1).type));
		
		//DRAW SHOTS
		int len = world.pOneShots.size();
		for(int i = len-1; i >= 0; i--){
			Shot shot = world.pOneShots.get(i);
			batcher.drawSprite(shot.position.x, shot.position.y, Shot.SHOT_RADIUS, Shot.SHOT_HEIGHT, Assets.shotsArray.get(world.paddles.get(0).type));
		}
		
		//DRAW SHOTS
		len = world.pTwoShots.size();
		for(int i = len-1; i >= 0; i--){
			Shot shot = world.pTwoShots.get(i);
			batcher.drawSprite(shot.position.x, shot.position.y, Shot.SHOT_RADIUS, Shot.SHOT_HEIGHT, 180, Assets.shotsArray.get(world.paddles.get(1).type));
		}
				
		batcher.endBatch();


	}
}
