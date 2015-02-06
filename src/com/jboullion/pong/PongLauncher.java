package com.jboullion.pong;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.GLGame;

public class PongLauncher extends GLGame {
	boolean firstTimeCreate = true;
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.androidgames.framework.Game#getStartScreen()
	 * Think about 
	 */
	
	@Override
	public Screen getStartScreen() {
		return new PongMenu(this);
		//return new MainMenuScreen(this);
	}

   @Override
   public void onSurfaceCreated(GL10 gl, EGLConfig config){
	   super.onSurfaceCreated(gl, config);
	   
	   
	   if(firstTimeCreate){
		   //Settings.load(getFileIO());

		   Assets.load(this);
		   Assets.loadAudio(this); //load sound
		   
		   firstTimeCreate = false;
	   }else{
		   Assets.load(this);
	   }
	   
   }
   
   
   @Override
   public void onPause(){
	   super.onPause();
	   //if(Assets.music.isPlaying()){
		//	Assets.music.pause();
		//}
   }
   
}
