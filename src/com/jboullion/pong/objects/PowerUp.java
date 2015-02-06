package com.jboullion.pong.objects;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.jboullion.pong.World;

public class PowerUp extends DynamicGameObject{
	public static final float POWER_UP_RADIUS = .5F;
	
	public static final int RANDOM_DIRECTION = 0;
	public static final int SPEED_UP = 1;
	public static final int MULTI_BALL = 2;
	public static final int STICKY_PADDLE = 3;
	public static final int LOOP = 4;
	public static final int ENLARGE_PADDLE = 5;

	public float stateTime = 0;
	public boolean created;
	public boolean isRolling = false;
	public int type;
	public int angle;
	
	public PowerUp(float x, float y, int type) {
		super(x, y, POWER_UP_RADIUS);
		velocity.set(0, 0);
		created=true;
		this.type = type;
		this.angle = 0;
	}
	
	public void update(float deltaTime){
		
		switch(type){
			case 0:
				//SPEED_UP
				break;
			case 1:
				//RANDOM_DIRECTION
				if(angle >= 360){
					angle = 0;
				}else{
					angle += 4;
				}
				break;
		}

		stateTime += deltaTime;
		
	}

}