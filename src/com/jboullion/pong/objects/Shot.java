package com.jboullion.pong.objects;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.badlogic.androidgames.framework.math.Vector2;
import com.jboullion.pong.GameScreen;
import com.jboullion.pong.World;

public class Shot extends DynamicGameObject{
	public static final float SHOT_RADIUS = .5F;
	public static final float SHOT_HEIGHT = .5F;
	
	//EDIT BALL VELOCITY TO INCREASE DIFFICULTY AND DURING POWERUPS. ALSO USED TO CONTROL BALL PHYSICS.
	public static float SHOT_VELOCITY = 5F;
	public static final int RIGHT = 1;
	public static final int LEFT = -1;
	public int direction;
	public float stateTime = 0;
	public boolean isRolling = false;
	public boolean remove;
	
	public Shot(float x, float y, int player) {
		super(x, y, SHOT_RADIUS);
		
		remove = false;
		
		if(player ==1){
			direction = RIGHT;
			velocity.set(SHOT_VELOCITY, 0);
		}else{
			direction = LEFT;
			velocity.set(-SHOT_VELOCITY, 0);
		}
		
	}
	
	public void update(float deltaTime, Paddle thispaddle, Paddle otherpaddle){
		
		//UP PADDLE SHOT
		if(bounds.center.x <= otherpaddle.position.x + otherpaddle.width/2 + Ball.BALL_RADIUS/2 && bounds.center.x >= otherpaddle.position.x - otherpaddle.width/2 - Ball.BALL_RADIUS/2){
			//IF YES< IS BALL IN Y LOCATION
			if(bounds.center.y <= otherpaddle.position.y + otherpaddle.height/2 + Ball.BALL_RADIUS/2 && bounds.center.y >= otherpaddle.position.y - otherpaddle.height/2 - Ball.BALL_RADIUS/2){
				//bounds.center.x = bounds.center.x + Paddle.PADDLE_WIDTH/2 + Ball.BALL_RADIUS/2;
				otherpaddle.affliction = thispaddle.type;
				if(thispaddle.type == Paddle.SHRINK){
					otherpaddle.width = Paddle.PADDLE_WIDTH / 2;
					otherpaddle.height = Paddle.PADDLE_HEIGHT / 2;
				}
				remove = true;
			}	
		}
		
		position.add(velocity.x * deltaTime, 0);
		bounds.center.set(position);
		stateTime += deltaTime;
		
		//MAKE SURE TO REMOVE SHOTS TO REMOVE FROM MEMORY
		if(position.x < 0f || position.x > 15f){
			remove = true;
		}
	}
	
	
	public void hit(Vector2 paddle, float deltaTime){
		position.add(velocity.x * deltaTime, 0);
		bounds.center.set(position);
	}
	
	public void effect(int whoShot){
		
		if(whoShot == 1){
			position.x = World.WORLD_WIDTH /2 + 2f;
			velocity.x = 0;
			direction = LEFT;
		}else{

			position.x = World.WORLD_WIDTH /2 - 2f;
			velocity.x = 0;
			direction = RIGHT;
		}
		
		bounds.center.set(position);
	}
}
