
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
	public boolean isJumping = false;

	int maxJumpHeight = (3*25 + 25/2); // 3.5 tile jump limit
	public int baseLine;

// Jumping motion Variables
	final double STARTING_JUMPING_VELOCITY = 6.25; 
	final double STARTING_JUMPING_DECCELERATION = 0;
	final double changeInDecceleration = 0.043; 

	double jumpingDecceleration = STARTING_JUMPING_DECCELERATION;
	double jumpingVelocity = STARTING_JUMPING_VELOCITY;

// The distance from a corner of the image used in collision detection
	final int VERTICAL_PX_BUFFER = 2;
	final int HORIZONTAL_PX_BUFFER = 3;
	

// Animation things
	GImage[] stillAnim, stillAnimH, shootAnim, shootAnimH, tongueAnim, tongueAnimH;
	public static boolean facingRight = true;


	public Player(int x, int y, GImage[] stillAnim, GImage[] stillAnimH, GImage[] shootAnim, GImage[] shootAnimH, GImage[] tongueAnim, GImage[] tongueAnimH){
		super(x, y, stillAnim, false, 1, true, 0);
		this.stillAnim = stillAnim;
		this.stillAnimH = stillAnimH;
		this.shootAnim = shootAnim;
		this.shootAnimH = shootAnimH;
		this.tongueAnim = tongueAnim;
		this.tongueAnimH = tongueAnimH;
		boundaryLeft = Data.TILE_SIZE;
		boundaryRight = -Data.TILE_SIZE;
	}

	/** Calls all the players actions **/
	public void motion(){

		checkCollisionDetection();

		jumpingEffect();

		enableJumping();

		gravityEffect();

		relativisticScreenMovement();

		imageX += dx;	
		
	}

	/** Resets players ability to jump if applicable **/
	private void enableJumping(){
		if(onPlatform)
			setBaseLine = false;
	}

	//** Moves the view with respect to the character **/
	private void relativisticScreenMovement(){

		// Horizontal screen movement
		if (x > FruitFever.RIGHT_BOUNDARY && dx > 0) {
			
			// Makes sure view never passes maximum level width 
			if (FruitFever.viewX >= FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE) 
				FruitFever.vx = 0;
			else
				FruitFever.vx = dx;	
		
		}else if (x < FruitFever.LEFT_BOUNDARY && dx < 0) {

			// Makes sure view never shows blank left of screen
			if (FruitFever.viewX <= 0)
				FruitFever.vx = 0;
			else
				FruitFever.vx = dx;	
		}

		// // Vertical Movement
		if (y > FruitFever.DOWN_BOUNDARY)
			FruitFever.vy = fallingVelocity - STARTING_FALLING_VELOCITY;
		
		// if (y < FruitFever.UP_BOUNDARY && isJumping) {
		// 	System.out.println("-jumpingVelocity");
		// 	FruitFever.vy = - jumpingVelocity;	
		// }else{
		// 	System.out.println("Zero!");
		// 	FruitFever.vy = 0;
		// 	isJumping = false;
		// }

		FruitFever.viewX += FruitFever.vx;
		FruitFever.viewY += FruitFever.vy;

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
		if(isJumping) {

			// Sets baseLine (where the player started before jumping)
			if(!setBaseLine){
				baseLine = y;
				setBaseLine = true;	
			}
			
			// Player has not yet hit the maximum jump limit
			if(imageY - jumpingVelocity > baseLine - maxJumpHeight && jumpingVelocity > 0) {

				imageY -= jumpingVelocity;

				jumpingVelocity -= jumpingDecceleration;
				jumpingDecceleration += changeInDecceleration;

			// Player has reached maxHeight, gravity now kicks in
			}
			else
				resetJump();				
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

		// Collision on block above this one has happened
		if (northWest != null || northEast != null)
			resetJump();

	}

	/** Test if player is going to hit a platform while falling **/
	private void downwardsCollision(){


		// SOUTH
		Block southWest, southEast;

		// Need to do this because starting starting falling velocity is never 0
		if (gravity) {
			southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER+ (int) fallingVelocity);			
			southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER+ (int) fallingVelocity);
		}
		else {
			southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);			
			southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);
		}

		checkForFreeFall(southEast, southWest);

	}

	private void checkForFreeFall(Block southEast, Block southWest){

		// Checks if player is in free fall
		if (southEast != null || southWest != null) {
			
			onPlatform = true;	

			if (southEast != null){
				System.out.printf("imageY: %d y: %d playerY: %d\n", southEast.imageY, southEast.y +FruitFever.viewY, y );
				placePlayerOnBlock(southEast);
			}else{
				System.out.printf("imageY: %d y: %d playerY: %d\n", southWest.imageY, southWest.y+FruitFever.viewY, y );
				placePlayerOnBlock(southWest);
			}
		}
		else
			onPlatform = false;

	}


	/** Places the player on top of the block he is currently on **/
	private void placePlayerOnBlock(Block block) {
		if (onPlatform)
			imageY = block.imageY - block.width;
	}

	/** 
	 * It was a good idea to have a setter for IsJumping.  
	 * For example you don't always want to set isJumping to true if the
	 * character is in free fall. 
	 **/
	public void setIsJumping(boolean value){

		// If you are not jumping and are on a platform
		if (!setBaseLine && onPlatform)
			isJumping = true;
	}

	private void resetJump(){

		jumpingVelocity = STARTING_JUMPING_VELOCITY;
		jumpingDecceleration = STARTING_JUMPING_DECCELERATION;

		isJumping = false;
	}

	public void tongueAttack(){
	
		// Adjust Animation variables
		doneAnimating = false;
		counter = -1;
		repeat = false;
		
		// Switch animation images
		if(facingRight)
			images = tongueAnim;
		else
			images = tongueAnimH;
		
	}
	
	public void shootSwirl(){
	
		// Adjust Animation variables
		doneAnimating = false;
		counter = -1;
		repeat = false;
		
		// Switch animation images
		if(facingRight)
			images = shootAnim;
		else
			images = shootAnimH;
		
	}

	/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly **/	
	public static void adjustLives(int changeInLives){
	
		lives += changeInLives;
		
		for(int i = 0; i < maxLives; i++)
			FruitFever.livesImages[i].setVisible(i < lives);

	}
	
	// Overrides MovingAnimation.animate()
	@Override public void animate(){
		
		if(facingRight){
			if(images.equals(stillAnimH))
				images = stillAnim;
			else if(images.equals(shootAnimH))
				images = shootAnim;
			else if(images.equals(tongueAnimH))
				images = tongueAnim;
		}
		else{
			if(images.equals(stillAnim))
				images = stillAnimH;
			else if(images.equals(shootAnim))
				images = shootAnimH;
			else if(images.equals(tongueAnim))
				images = tongueAnimH;
		}
		
		if(doneAnimating){
		
			// Adjust Animation variables
			repeat = true;
			counter = -1;
			doneAnimating = false;
			
			// Switch animation images
			if(facingRight)
				images = stillAnim;
			else
				images = stillAnimH;

		}
		
		super.animate();
			
	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}




















