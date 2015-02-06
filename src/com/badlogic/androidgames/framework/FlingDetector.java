package com.badlogic.androidgames.framework;

import java.util.List;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.math.Vector2;
import com.jboullion.pong.World;


public class FlingDetector {
   public static final int LEFT = 0;
   public static final int RIGHT = 1;
   public static final int UP = 2;
   public static final int DOWN = 3;
   Vector2 startPosition = new Vector2();
   Vector2 endPosition = new Vector2();
   Vector2 direction = new Vector2();
   boolean inFling = false;
   
   /*
    * You basically pass in the list of TouchEvents you get from the Input module and it will detect touch up and down events, 
    * recording the start and endposition of the fling. If a touch up was seen the processEvents() method returns true, 
    * indicating that a fling has happened. To figure out which direction the fling went in you can use the other two methods, 
    * one returning a Vector2, the returning the general dominant direction in terms of a constant (LEFT, RIGHT, UP, DOWN). 
    * Just instantiate this class and pass the touch events to it on every main loop iteration, evaluating the return type and 
    * reacting accordingly.
    */

    public boolean processEvents(List<TouchEvent> touchEvents) {
       int len = touchEvents.size();
       for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            if(event.type == TouchEvent.TOUCH_DOWN) {
            	startPosition.set(event.x, event.y);
            	return false;
         	}
            
            if(event.type == TouchEvent.TOUCH_UP) {
                endPosition.set(event.x, event.y);
                //direction.set(endPosition).sub(startPosition);
                direction.set(startPosition.x - endPosition.x, startPosition.y - endPosition.y ); 
                
                
                direction.set(0,0);
                if(startPosition.x > endPosition.x + 50){
                	direction.set(-1,0);
                	//RIGHT TO LEFT
                	//startPosition.x = endPosition.x;
                }
                if(startPosition.x < endPosition.x - 50){
                	direction.set(1,0);
                	//LEFT TO RIGHT
                	//startPosition.x = endPosition.x;
                }  
                if(startPosition.y < endPosition.y - 100){
                	//UP
                	direction.set(0,1);
                	//startPosition.y = endPosition.y;
                }
                if(startPosition.y > endPosition.y + 100){
                	//DOWN
                	direction.set(0,-1);
                	//startPosition.y = endPosition.y;
                }
                return true;
                
            }         
        }
       return inFling;
    }
    
   
   public Vector2 getDirection() {
      return direction;
   }
   
   public int getDominantDirection() {
	   if(direction.x != 0 || direction.y != 0){
		   if(Math.abs(direction.x) > Math.abs(direction.y)) {
			   if(direction.x < 0){
				   direction.set(0,0);
				   return LEFT;
			   }else if(direction.x > 0){
				   direction.set(0,0);
				   return RIGHT;
			   }
			   return -1;
		   } else {
			   if(direction.y < 0){
				   direction.set(0,0);
				   return UP;
			   }else if(direction.y > 0){
				   direction.set(0,0);
				   return DOWN;
			   }
			   return -1;
		   }  
	   }else{
		   return -1; //nothing
	   }
   }
}
