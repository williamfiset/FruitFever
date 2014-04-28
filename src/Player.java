
/**
 * @Author William Fiset 
 * This class controls all the actions and collisions of the main character
 *
 **/

import acm.graphics.*;
import acm.program.*;
import java.util.*;
import java.awt.Color.*;
import java.awt.*;

class Player extends MovingAnimation {
	
	static int lives = 3, maxLives = 3;

	// Movement Variables
	static final int VERTICAL_VELOCITY = 5, HORIZONTAL_VELOCITY = 3;
	int dy = 0, dx = 0;

	// Variables concerning Gravity

	final double STARTING_FALLING_VELOCITY = 2.5;
	final double STARTING_FALLING_ACCELERATION = 0.5;
	final double changeInAcceleration = 0.015;

	double fallingVelocity = STARTING_FALLING_VELOCITY;
	double fallingAcceleration = STARTING_FALLING_ACCELERATION;

	static boolean onPlatform = false;
	static boolean gravity = true;


	// Variables concerning jumping
	boolean isJumping = false;
	boolean reachedBaseLine = true;
	int maxJump;


	// The distance from a corner of the image used in collision detection
	final int VERTICAL_PX_BUFFER = 2;
	final int HORIZONTAL_PX_BUFFER = 3;
	
	
	GImage[] stillAnim, shootAnim, tongueAnim;
	
	public Player(int x, int y, GImage[] stillAnim, GImage[] shootAnim, GImage[] tongueAnim){
		super(x, y, stillAnim, false, 1, true, 0);
		this.stillAnim = stillAnim;
		this.shootAnim = shootAnim;
		this.tongueAnim = tongueAnim;
	}

	// Has not been implemented yet, just the skeleton 
	public void motion(){

		// The intersects overlaps by 1px
		checkCollisionDetection();

		// if (isJumping) {
		// 	// move up
		// 	if (imageY - dy <= baseLine) {
		// 		reachedBaseLine = true;
		// 	}

		// }else{
		// 	if (!reachedBaseLine) {
		// 		// move down
		// 	}
		// }

		imageX += dx;
		imageY += dy;

		// Gravity Effect triggered here
		if(!isJumping && gravity && !onPlatform){

			imageY += fallingVelocity;

			// Acceleration effect
			fallingVelocity += fallingAcceleration;
			fallingAcceleration += changeInAcceleration;				
			

		// Executes when not falling or not allowed to fall
		}
		else{
			// Reset falling speed
			fallingVelocity = STARTING_FALLING_VELOCITY;
			fallingAcceleration = STARTING_FALLING_ACCELERATION;
		}

	}

	/** Responds accordingly to collision detection **/
	private void checkCollisionDetection(){

		/** Sideways Collisions **/

		// EAST
		if(FruitFever.dx == 1){

			Block eastNorth = Block.getBlock(x + width + HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block eastSouth = Block.getBlock(x + width + HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			if(eastSouth == null && eastNorth == null)
				dx = HORIZONTAL_VELOCITY;
			else
				dx = 0;
			
		// WEST
		}
		else if(FruitFever.dx == -1) {

			Block westNorth = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block westSouth = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			// No block in back of player
			if(westNorth == null && westSouth == null)
				dx = -HORIZONTAL_VELOCITY;
			else
				dx = 0; 
		}


		/** Test if player is going to hit a platform while falling **/

		// SOUTH
		Block southWest, southEast;

		// Need to do this because starting starting falling velocity is never 0
		if(gravity) {
			southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER+ (int) fallingVelocity);			
			southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER+ (int) fallingVelocity);
		}
		else{
			southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);			
			southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);
		}


		// Checks if player is in free fall
		if(southEast != null || southWest != null){
			
			onPlatform = true;	

			if (southEast != null)
				placePlayerOnBlock(southEast);
			else
				placePlayerOnBlock(southWest);

		}
		else
			onPlatform = false;

	}

	public void toungueAttack(){}
	
	public void shootSwirl(){
	
		doneAnimating = false;
		counter = -1;
		images = shootAnim;
		repeat = false;
		
	}

	/** Places the player on top of the block he is currently on **/
	private void placePlayerOnBlock(Block block){
		if(onPlatform)
			imageY = block.y - block.width;
	}

	/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly **/	
	public static void adjustLives(int changeInLives){
	
		lives += changeInLives;
		
		for(int i = 0; i < maxLives; i++)
			FruitFever.livesImages[i].setVisible(i < lives);

	}
	
	// Overrides MovingAnimation.animate()
	@Override public void animate(){
		
		if(doneAnimating){
			counter = -1;
			images = stillAnim;
			repeat = true;
		}
			
		super.animate();
	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}