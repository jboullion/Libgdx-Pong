package com.jboullion.pong;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.androidgames.framework.math.OverlapTester;
import com.jboullion.pong.objects.Ball;
import com.jboullion.pong.objects.Paddle;
import com.jboullion.pong.objects.PowerUp;
import com.jboullion.pong.objects.Shot;



public class World {
	public interface WorldListener{
		public void get();
	}
	
	public static final float WORLD_WIDTH = 15F;
	public static final float WORLD_HEIGHT = 10F;

	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_PAUSE = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	//public static final Vector2 gravity = new Vector2(0, -10);
	
	public final List<Paddle> paddles;
	public int numPowerUps = 0;
	public List<Ball> balls;
	public List<PowerUp> powerUps;
	public List<Shot> pOneShots;
	public List<Shot> pTwoShots;
	
	public final WorldListener listener;
	public final Random rand;

	public int score;
	public int state;
	public long count = 0;
	public boolean stop = false;
	public boolean giveScore = false;
	
	final float MIDDLEX = 15f/2f;
	final float MIDDLEY = 10f/2f;
	
	float randomX = 0; 
	float randomY = 0;
	int angle = 0;
	int pocket = 0;
	
	static final String TAG = "World";

	public World(WorldListener listener){
		//this.heart = new Heart(580, 50, 0f);
		
		this.listener = listener;
		this.paddles = new ArrayList<Paddle>();
		this.balls = new ArrayList<Ball>();
		this.powerUps = new ArrayList<PowerUp>();
		this.pOneShots = new ArrayList<Shot>();
		this.pTwoShots = new ArrayList<Shot>();
		rand = new Random();
		generateLevel();
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
		
		
	}
	
	//used to generate the game world
	public void generateLevel(){
		
		createBall();
		createPaddles();
	}
	
	private void createBall() {

		balls.add(new Ball(MIDDLEX - 2f, MIDDLEY));
	}
	
	private void createPaddles() {

		paddles.add(new Paddle(1f, MIDDLEY, PaddleSelection.paddleSelected));
		paddles.add(new Paddle(14f, MIDDLEY, PaddleSelection.paddle2Selected));
	}
	/***************************************************************************************************************************************************
	 * UPDATE THE WORLD HERE!!! THIS IS THE MOST IMPORTANT METHOD!!!-------------------------------------------------------------------------------------
	 * --------------------------------------------------------------------------------------------------------------------------------------------------
	 **************************************************************************************************************************************************/
	public void update(float deltaTime){
		checkGameOver();
		
		if(!stop){
			
			updateBall(deltaTime);
			updatePaddle(deltaTime);
			updatePowerUps(deltaTime);
			updateShots(deltaTime);
		}
		
		//GENERATE A POWERUP
		if(giveScore){
			numPowerUps++;
			powerUps.add(GameScreen.generatePowerUp());
			giveScore = false;
		}
		
	}

	private void updateBall(float deltaTime) {
		/*
		for(Ball ball : balls){
			ball.update(deltaTime);
			
		}
		*/
		
		//CHECK FOR MULTIBALL AND SCORE
		int len = balls.size();
		
		if(len == 1){	
			balls.get(0).update(deltaTime);
			if(balls.get(0).position.x < 0){
				score(2);
			}
			if(balls.get(0).position.x > World.WORLD_WIDTH + Ball.BALL_RADIUS){
				score(1);
			}
		}else{
			//MULTI BALL!!!
			for(int i = len-1; i >= 0; i--){
				Ball ball = balls.get(i);
				ball.update(deltaTime);
				//CHECK IF A MULTI BALL HAS PASSED THE EDGE AND REMOVE IT FROM LIST
				if(balls.get(i).position.x < 0){
					balls.remove(ball);
					len = balls.size();
				}else if(balls.get(i).position.x > World.WORLD_WIDTH + Ball.BALL_RADIUS){
					//CHECK IF LEAVES AT 15 AND THAT IT WASN'T JUST REMOVED BY A 0 CHECK
					balls.remove(ball);
					len = balls.size();
				}
			}
		}
	}
	
	public void score(int whoScored){
		balls.clear();
		createBall();
		
		if(whoScored == 1){
			GameScreen.pOneScoreString= ""+ ++GameScreen.pOneScore;
			balls.get(0).position.x = World.WORLD_WIDTH /2 + 2f;
			balls.get(0).velocity.x = 0;
			balls.get(0).direction = Ball.LEFT;
		}else{
			GameScreen.pTwoScoreString= ""+ ++GameScreen.pTwoScore;
			balls.get(0).position.x = World.WORLD_WIDTH /2 - 2f;
			balls.get(0).velocity.x = 0;
			balls.get(0).direction = Ball.RIGHT;
		}

		angle = 0;
		
		GameScreen.world.giveScore = true;
		
		balls.get(0).position.y = World.WORLD_HEIGHT /2;
		balls.get(0).velocity.y = 0;
		balls.get(0).bounds.center.set(balls.get(0).position);
		
		balls.get(0).isRolling = false;
		balls.get(0).isLooping = false;
	}
	
	private void updatePaddle(float deltaTime) {
		//CHECK IF BALL HITS A PADDLE
		for (Paddle paddle : paddles){
			paddle.update(deltaTime);
			//IS BALL IN X LOCATION
			for(Ball ball : balls){
				if(ball.isStuck){
					
				}else{
					if(ball.bounds.center.x <= paddle.position.x + paddle.width/2 + Ball.BALL_RADIUS/2 && ball.bounds.center.x >= paddle.position.x - paddle.width/2 - Ball.BALL_RADIUS/2){
						//IF YES IS BALL IN Y LOCATION
						if(ball.bounds.center.y <= paddle.position.y + paddle.height/2 + Ball.BALL_RADIUS/2 && ball.bounds.center.y >= paddle.position.y - paddle.height/2 - Ball.BALL_RADIUS/2){
							//ball.bounds.center.x = ball.bounds.center.x + Paddle.PADDLE_WIDTH/2 + Ball.BALL_RADIUS/2;
							ball.bounce(paddle, deltaTime);
						}	
					}
				}
			}
		}
	}
	
	private void updateShots(float deltaTime) {
		//CHECK IF BALL HITS A PADDLE
		int len = pOneShots.size();
		if(len > 0){
			for(int i = len-1; i >= 0; i--){
				Shot shot = pOneShots.get(i);
				shot.update(deltaTime, paddles.get(0), paddles.get(1));
				if(shot.remove){
					pOneShots.remove(shot);
					len = pOneShots.size();
				}
			}
		}
		
		len = pTwoShots.size();
		if(len > 0){
			for(int i = len-1; i >= 0; i--){
				Shot shot = pTwoShots.get(i);
				shot.update(deltaTime, paddles.get(1), paddles.get(0));
				if(shot.remove){
					pTwoShots.remove(shot);
					len = pTwoShots.size();
				}
			}
		}
		
		/*
		for (Shot shot : pOneShots){
			shot.update(deltaTime, paddles.get(1));
			if(shot.remove){
				pOneShots.remove(shot);
			}
		}
		
		for (Shot shot : pTwoShots){
			shot.update(deltaTime, paddles.get(0));
			if(shot.remove){
				pTwoShots.remove(shot);
			}
		}*/
	}
	
	private void updatePowerUps(float deltaTime) {
	int len = powerUps.size();
	int ballLen = balls.size();
	if(len > 0){
		for(int i = len-1; i >= 0; i--){
			PowerUp powerUp = powerUps.get(i);
				powerUp.update(deltaTime);
				//IF BALL OVERLAPS A POWERUP
				if(ballLen > 0){
					for(int j = ballLen-1; j >= 0; j--){
						Ball ball = balls.get(j);
						if(OverlapTester.overlapCircles(ball.bounds, powerUp.bounds)){
							switch(powerUp.type){
								case PowerUp.RANDOM_DIRECTION:
									
									if(rand.nextInt(4) % 2 == 0){
										ball.velocity.x = (rand.nextFloat()+1)*rand.nextInt(7)+3;
										ball.velocity.y = (rand.nextFloat()+1)*rand.nextInt(7)+3;
										ball.direction = Ball.RIGHT;
									}else{
										ball.velocity.x = (rand.nextFloat()+1)*-rand.nextInt(7)-3;
										ball.velocity.y = (rand.nextFloat()+1)*-rand.nextInt(7)-3;
										ball.direction = Ball.LEFT;
									}
									break;
								case PowerUp.SPEED_UP:
									ball.velocity.set(ball.velocity.x * 1.5f, ball.velocity.y * 1.5f);
									break;
								case PowerUp.MULTI_BALL:
									
									balls.add(new Ball(ball.position.x, ball.position.y));
									balls.get(balls.size()-1).velocity.x = -(ball.velocity.x);
									balls.get(balls.size()-1).velocity.y = -(ball.velocity.y);
									break;
								case PowerUp.STICKY_PADDLE:
									ball.isSticky = true;
									break;
								case PowerUp.LOOP:
									ball.isLooping = true;
									break;
								case PowerUp.ENLARGE_PADDLE:
									if(ball.direction == Ball.RIGHT){
										paddles.get(0).affliction= Paddle.SHRINK;
										paddles.get(0).height *= 2;
										paddles.get(0).width *= 2;
									}else{
										paddles.get(1).affliction= Paddle.SHRINK;
										paddles.get(1).height *= 2;
										paddles.get(1).width *= 2;
									}
									break;
									
								default:
								
									break;
							}
							//Assets.playSound(Assets.squish);
							powerUps.remove(powerUp);
							len = powerUps.size();
							numPowerUps--;
						
						}//END if(OverlapTester.overlapCircles(ball.bounds, powerUp.bounds))
					}//END for(int j = ballLen-1; j >= 0; j--){
				}//END if(ballLen > 0)
			}//END for(int i = len-1; i >= 0; i--)		
		}//END if(len > 0)
	}//END updatePowerUps(float deltaTime)
	
	private void checkGameOver() {
		
		if(GameScreen.pOneScore == GameScreen.WIN_SCORE || GameScreen.pTwoScore == GameScreen.WIN_SCORE){
			state = WORLD_STATE_GAME_OVER;
			stop = true;
			
			powerUps.clear();
			pOneShots.clear();
			pTwoShots.clear();
		}
		
	}
}
