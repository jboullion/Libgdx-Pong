package com.jboullion.pong.objects;

import android.util.Log;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.badlogic.androidgames.framework.math.Vector2;
import com.jboullion.pong.GameScreen;
import com.jboullion.pong.World;

public class Paddle extends DynamicGameObject{
	public static final float PADDLE_RADIUS = 2F;
	public static final float PADDLE_WIDTH = .5F;
	public static final float PADDLE_HEIGHT = 2.5F;
	//EDIT PADDLE VELOCITY TO INCREASE DIFFICULTY AND DURING POWERUPS. ALSO USED TO CONTROL PADDLE PHYSICS.
	public static float PADDLE_VELOCITY = 2F;
	
	//CHANGED WITH POWERUPS AND ABILITIES
	public float width;
	public float height;
	
	//PADDLE SHOT TYPE
	public static final int SLOW = 0; //SLOW PADDLE EASING
	public static final int FREEZE = 1; //
	public static final int SHRINK = 2; //REDUCE OPPONENT PADDLE SIZE
	public static final int FIRE = 3;  //RANDOM MOTIONS WITH FIRE ANIMATIONS
	public static final int REVERSE = 4; //REVERSE PADDLE POTION 
	public static final int STRAIGHT = 5; //ONLY HAS SLIGHT REFLECTION ANGLE

	public int affliction;
	public int afflictionCounter;
	public static final int AFFLICTION_COUNT = 1000;
	
	//public Rectangle PADDLE_RECT;
	public float stateTime = 0;
	public boolean created;
	public boolean moving = false;
	public int type;
	static final String TAG = "Paddle";
	
	public Paddle(float x, float y, int type) {
		super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		//PADDLE_RECT = new Rectangle(x-PADDLE_WIDTH/2,y-PADDLE_HEIGHT/2,PADDLE_WIDTH,PADDLE_HEIGHT);
		velocity.set(0, 0);
		created=true;
		this.type = type;
		affliction = -1;
		afflictionCounter = 0;
		
		width = PADDLE_WIDTH;
		height = PADDLE_HEIGHT;
	}
	
	public void update(float deltaTime){
		if(affliction != -1){
			switch(affliction){
				case Paddle.SLOW:
					
					break;
				case Paddle.FREEZE:
					afflictionCounter += 10;
					break;
				case Paddle.SHRINK:
					
					break;
				case Paddle.FIRE:
					if(afflictionCounter % 10 == 0){
						if(GameScreen.rand.nextInt(2) == 1){
							this.position.y += GameScreen.rand.nextFloat() * GameScreen.rand.nextInt(3);
						}else{
							this.position.y -= GameScreen.rand.nextFloat() * GameScreen.rand.nextInt(3);
						}
					}
					break;
				case Paddle.REVERSE:
										
					break;
			}
			afflictionCounter++;
			if(afflictionCounter >= AFFLICTION_COUNT){
				afflictionCounter = 0;
				affliction = -1;
				width = PADDLE_WIDTH;
				height = PADDLE_HEIGHT;
			}
		}
		
		//KEEP PADDLE INSIDE BOX
		if(position.y < PADDLE_HEIGHT /2){
			position.y = PADDLE_HEIGHT /2;
		}
		if(position.y > World.WORLD_HEIGHT - PADDLE_HEIGHT /2){
			position.y = World.WORLD_HEIGHT - PADDLE_HEIGHT /2;
		}
		
		//position.add(0, velocity.y * deltaTime);
		bounds.center.set(position);
		stateTime += deltaTime;
	}
	
	public void render(){
		
	}
	
	public void move(Vector2 touchpoint){
		//SETUP TOUCHPOINT EASING IN THE FUTURE.
		
		/*
		if(position.y > touchpoint.y){
			position.y -= 0.2f;
		}else if(position.y < touchpoint.y){
			position.y += 0.2f;
		}*/
		
		switch(affliction){
			case Paddle.SLOW:
				if(position.y > touchpoint.y){
					position.y -= 0.2f;
				}else if(position.y < touchpoint.y){
					position.y += 0.2f;
				}
				break;
			case Paddle.FREEZE:
									
				break;
			case Paddle.SHRINK:
				this.position.y = touchpoint.y;
				break;
			case Paddle.FIRE:
				this.position.y = touchpoint.y;
				break;
			case Paddle.REVERSE:
				this.position.y = -touchpoint.y + 10f;					
				break;
			default:
				this.position.y = touchpoint.y;
				break;
		}
		
		//KEEP PADDLE INSIDE BOX
		if(position.y < PADDLE_HEIGHT /2){
			position.y = PADDLE_HEIGHT /2;
		}
		if(position.y > World.WORLD_HEIGHT - PADDLE_HEIGHT /2){
			position.y = World.WORLD_HEIGHT - PADDLE_HEIGHT /2;
		}
				
		this.bounds.center.set(position);
		this.rect.y = touchpoint.y - PADDLE_HEIGHT /2;
		//Log.d(TAG, "rect.y="+rect.y+" position.y="+position.y);
	}
	
}