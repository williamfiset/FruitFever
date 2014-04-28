
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
	static final int HORIZONTAL_VELOCITY = 3;
	int dx = 0;

// Variables concerning Gravity

	final double STARTING_FALLING_VELOCITY = 2.5;
	final double STARTING_FALLING_ACCELERATION = 0.5;
	final double changeInAcceleration = 0.015;

	double fallingVelocity = STARTING_FALLING_VELOCITY;
	double fallingAcceleration = STARTING_FALLING_ACCELERATION;

	private static boolean onPlatform = false;
	private static boolean gravity = true;


// Variables concerning jumping
	
	// setBaseLine is true because we don't know where the player starts
	private boolean setBaseLine = true;
	private boolean isJumping = false;

	int maxJumpHeight = (3*25 + 25/2); // 3.5 tile jump limit
	private int baseLine;

	// Jumping motion Varibles
	final double STARTING_JUMPING_VELOCITY = 6.25; 
	final double STARTING_JUMPING_DECCELERATION = 0;
	final double changeInDecceleration = 0.043; 

	double jumpingDecceleration = STARTING_JUMPING_DECCELERATION;
	double jumpingVelocity = STARTING_JUMPING_VELOCITY;

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

		checkCollisionDetection();
		jumpingEffect();

		// Resets players ability to jump
		if (onPlatform) setBaseLine = false;

		gravityEffect();
		relativisticScreenMovement();

		imageX += dx;

	}

	private void relativisticScreenMovement(){

		// FruitFever.viewX += 1;

	}

	/** Responds accordingly to collision detection **/
	private void checkCollisionDetection(){

		sidewaysCollision();
		downwardsCollision();
		upwardsCollision();

	}

	/** Handles Jumping triggered by the Player **/
	private void jumpingEffect(){

		// Jumping event was Triggered
		if (isJumping) {

			// Sets baseLine (where the player started before jumping)
			if (!setBaseLine){
				baseLine = y;
				setBaseLine = true;	
			}
			
			// Player has not yet hit the maximum jump limit
			if (imageY - jumpingVelocity > baseLine - maxJumpHeight && jumpingVelocity > 0) {

				imageY -= jumpingVelocity;

				jumpingVelocity -= jumpingDecceleration;
				jumpingDecceleration += changeInDecceleration;

			// Player has reached maxHeight, gravity now kicks in
			}else{
				resetJump();				
			}
		}
	}

	/** Takes care of making the player fall when not jumping and not on a platform **/
	private void gravityEffect(){

		// Gravity Effect triggered here
		if(!isJumping && gravity && !onPlatform){

			imageY += fallingVelocity;

			// Acceleration effect
			fallingVelocity += fallingAcceleration;
			fallingAcceleration += changeInAcceleration;				
			

		// Executes when not falling or not allowed to fall
		}else{

			// Reset falling speed
			fallingVelocity = STARTING_FALLING_VELOCITY;
			fallingAcceleration = STARTING_FALLING_ACCELERATION;
		}

	}

	/** Sideways Collisions **/
	private void sidewaysCollision(){

		// EAST
		if(FruitFever.dx == 1){

			Block eastNorth = Block.getBlock(x + width + HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block eastSouth = Block.getBlock(x + width + HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			// No block right of player
			if(eastSouth == null && eastNorth == null)
				dx = HORIZONTAL_VELOCITY;
			else
				dx = 0;
			
		// WEST
		} else if(FruitFever.dx == -1) {

			Block westNorth = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block westSouth = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			// No block left of player
			if(westNorth == null && westSouth == null)
				dx = -HORIZONTAL_VELOCITY;
			else
				dx = 0; 
		}

	}

	private void upwardsCollision(){

		Block northWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y - VERTICAL_PX_BUFFER);
		Block northEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y - VERTICAL_PX_BUFFER);

		// Collision on block above this one ahs happened
		if (northWest != null || northEast != null) {
			resetJump();
		}

	}

	/** Test if player is going to hit a platform while falling **/
	private void downwardsCollision(){


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

		checkForFreeFall(southEast, southWest);

	}

	private void checkForFreeFall(Block southEast, Block southWest){

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


	/** Places the player on top of the block he is currently on **/
	private void placePlayerOnBlock(Block block){
		if(onPlatform)
			imageY = block.y - block.width;
	}

	/** It was a good idea to have a setter for IsJumping.  
	 * For example you don't always want to set isJumping to true if the
	 * character is in free fall. **/
	public void setIsJumping(boolean value){

		if (!setBaseLine)
			isJumping = true;
	}

	private void resetJump(){

		jumpingVelocity = STARTING_JUMPING_VELOCITY;
		jumpingDecceleration = STARTING_JUMPING_DECCELERATION;

		isJumping = false;
	}

	public void toungueAttack(){}
	
	public void shootSwirl(){
	
		doneAnimating = false;
		counter = -1;
		images = shootAnim;
		repeat = false;
		
	}

	/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly **/	
	public static void adjustLives(int changeInLives){
	
		lives += changeInLives;
		
		for(int i = 0; i < maxLives; i++)
			FruitFever.livesImages[i].setVisible(i < lives);

	}
	
	// Overrides MovingAnimation.animate()
	@Override public void animate(){
		
		super.animate();
		
		if(doneAnimating){
			counter = -1;
			images = stillAnim;
			repeat = true;

			width = (int) stillAnim[0].getWidth();
			height = (int) stillAnim[0].getHeight();

		}
			
	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}




















