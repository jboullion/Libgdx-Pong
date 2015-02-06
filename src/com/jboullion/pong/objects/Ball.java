package com.jboullion.pong.objects;

import android.util.FloatMath;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.jboullion.pong.GameScreen;
import com.jboullion.pong.World;

public class Ball extends DynamicGameObject{
	public static final float BALL_RADIUS = .75F;
	public static final float BALL_HEIGHT = .75F;
	
	//EDIT BALL VELOCITY TO INCREASE DIFFICULTY AND DURING POWERUPS. ALSO USED TO CONTROL BALL PHYSICS.
	public static float BALL_VELOCITY = 5F;
	public static final int RIGHT = 1;
	public static final int LEFT = -1;
	public int direction = RIGHT;
	public float stateTime = 0;
	public boolean created;
	public boolean isRolling = false;
	public int angle;
	int loopDir;
	
	//AFFLICTIONS
	public boolean isLooping;
	public boolean isSticky;
	public boolean isStuck;
	
	public Ball(float x, float y) {
		super(x, y, BALL_RADIUS);
		velocity.set(0, 0);
		created=true;
		isLooping = false;
		isSticky = false;
		angle = 0;
	}
	
	public void update(float deltaTime){
		
		//ROLL THE BALL BASED ON THE BALL'S VELOCITY
		if(direction == RIGHT && isRolling){
			if(angle <= 0){
				angle = 360;
			}else{
				angle -= Math.abs((int)velocity.x);
			}
		}else if(direction == LEFT && isRolling){
			if(angle >= 360){
				angle = 0;
			}else{
				angle += Math.abs((int)velocity.x);
			}
		}
		
		//MAKE SURE BALL IS NOT GOING TOO SLOW
		if(velocity.x > -4f && velocity.x < 0f){
			velocity.x = -4f;
		}
		if(velocity.x < 4f && velocity.x > 0f){
			velocity.x = 4f;
		}
		
		//NOT TOO FAST LEFT RIGHT
		if(velocity.x > 12f){
			velocity.x = 12f;
		}else if(velocity.x < -12f){
			velocity.x = -12f;
		}
		
		//NOT TOO FAST UP AND DOWN
		if(velocity.y > 10f){
			velocity.y = 10f;
		}else if(velocity.y < -10f){
			velocity.y = -10f;
		}		 

		//SCORE
		/*
		if(position.x < 0){
			score(2);
			isLooping = false;
		}
		if(position.x > World.WORLD_WIDTH + BALL_RADIUS){
			score(1);
			isLooping = false;
		}
		*/
		
		if(position.y < BALL_HEIGHT /2){
			position.y = BALL_HEIGHT /2;
			velocity.y = -velocity.y;
		}
		if(position.y > World.WORLD_HEIGHT - BALL_HEIGHT /2){
			position.y = World.WORLD_HEIGHT - BALL_HEIGHT /2;
			velocity.y = -velocity.y;
			
		}
		
		/*
		//IF AFFECTED BY THE LOOPING BALL
		float circleSize = 360 / 20;
		float circleSpeed = stateTime % 100;
		float circleRadiusX = position.x + BALL_RADIUS/3;
		float circleRadiusY = position.y + BALL_RADIUS/3;
		float circleTime = (circleSize) * (circleSpeed);
		if(isLooping){
			//MOVE BALL IN A LOOP!
			circleSpeed = stateTime % 100;
			circleRadiusX = position.x + BALL_RADIUS/3;
			circleRadiusY = position.y + BALL_RADIUS/3;
			circleTime = (circleSize) * (circleSpeed);
			position.x = circleRadiusX * FloatMath.cos(circleTime);
			position.y = circleRadiusY * FloatMath.sin(circleTime);
			position.add((velocity.x/2) * deltaTime, (velocity.y/2) * deltaTime);
		}else{
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		}
		*/		
		
		//IF AFFECTED BY THE LOOPING BALL
		
		if(isLooping){
			//MOVE BALL IN A LOOP!
			if(direction == Ball.RIGHT){
				loopDir = -1;
			}else{
				loopDir = 1;
			}
			float circleTime = (360 / (loopDir * 20)) * (stateTime % 100);
			position.x = position.x + BALL_RADIUS/3 * FloatMath.cos(circleTime);
			position.y = position.y + BALL_RADIUS/3 * FloatMath.sin(circleTime);
			position.add((velocity.x/2) * deltaTime, (velocity.y/2) * deltaTime);
		}else{
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		}
		
		
		bounds.center.set(position);
		stateTime += deltaTime;
	}
	
	public void bounce(Paddle paddle, float deltaTime){
		direction *= -1;
		
		/*
		//TOP OF PADDLE
		if(position.y > paddle.y + Paddle.PADDLE_HEIGHT/3){
			if(Math.abs(velocity.y) < 3f){
				velocity.y = Math.abs(velocity.y) + 2f;
			}
				
			velocity.y = Math.abs(velocity.y)*1.3f;
			
		}else if(position.y > paddle.y + Paddle.PADDLE_HEIGHT/6){
			if(Math.abs(velocity.y) < 2f){
				velocity.y = Math.abs(velocity.y) + 1f;
			}
			
			velocity.y = Math.abs(velocity.y);
		}
		
		//BOTTOM OF PADDLE
		if( position.y < paddle.y - Paddle.PADDLE_HEIGHT/3){
			if(Math.abs(velocity.y) < 3f){
				velocity.y = Math.abs(velocity.y) + 2f;
			}
			
			velocity.y = -(Math.abs(velocity.y)*1.3f);
		}else if(position.y < paddle.y - Paddle.PADDLE_HEIGHT/6){
			if(Math.abs(velocity.y) < 2f){
				velocity.y = Math.abs(velocity.y) + 1f;
			}
			
			velocity.y = -(Math.abs(velocity.y));
		}
		*/
		if(isSticky){
			velocity.x = 0f;
			velocity.y = 0f;
			isStuck = true;
		}else{
			if(paddle.affliction == Paddle.STRAIGHT){
				velocity.y = GameScreen.rand.nextFloat() * GameScreen.rand.nextInt(9);
			}else{
				velocity.y = (position.y - paddle.position.y) * 9f;
			}
			
			//HAVE TO CHECK IN CASE BALL IS LOOPING WHICH MAKES THE BOUNCE ACT WIERD
			if(velocity.x > 0){
				velocity.x = -(velocity.x * 1.1f);
			}else{
				velocity.x = Math.abs(velocity.x * 1.1f);
			}
			//velocity.x = -(velocity.x * 1.1f);
		
			//CHECK X VELOCITY AND KEEP BALL OUT OF PADDLE
			if(position.x > paddle.position.x - Paddle.PADDLE_WIDTH/2 && position.x < paddle.position.x + Paddle.PADDLE_WIDTH/2){
				if(position.x < World.WORLD_WIDTH /2){
					position.x = paddle.position.x + Paddle.PADDLE_WIDTH/2;
				}else{
					position.x = paddle.position.x - Paddle.PADDLE_WIDTH/2;
				}
			}
					
			//CHECK IF GOING UP OR DOWN AND KEEP OUTSIDE OF PADDLE
			/*
			if(position.y > paddle.y){
				if(position.y < paddle.y + Paddle.PADDLE_HEIGHT/2){
					position.y = paddle.y + Paddle.PADDLE_HEIGHT/2;
				}
			}else{
				if(position.y > paddle.y - Paddle.PADDLE_HEIGHT/2){
					position.y = paddle.y - Paddle.PADDLE_HEIGHT/2;
				}
			}
			*/
		}
		
		//KEEP BALL OUT OF TOP AND BOTTOM
		if(position.y > 10f - Ball.BALL_HEIGHT/2){
			position.y = 10f - Ball.BALL_HEIGHT/2;
		}
		if(position.y < Ball.BALL_HEIGHT/2){
			position.y = Ball.BALL_HEIGHT/2;
		}
		
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.center.set(position);
	}
}