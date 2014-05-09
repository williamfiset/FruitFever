
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

public class Player extends MovingAnimation {
	
	static Swirl swirl;
	static int lives = 3, maxLives = 3;

// Movement Variables
	static final int HORIZONTAL_VELOCITY = 3;
	int dx = 0;

// Variables concerning Gravity

	final double TERMINAL_VELOCITY = Data.TILE_SIZE - 1;
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
		
		swirl = new Swirl();

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

	//** Moves the view of the screen relative to the character **/
	private void relativisticScreenMovement(){

		// Horizontal screen movement
		if (x + width > FruitFever.RIGHT_BOUNDARY && dx > 0) {
			
			// Makes sure view never passes maximum level width 
			if (FruitFever.viewX >= FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE) 
				FruitFever.vx = 0;
			else
				FruitFever.vx = dx;	
		
		}
		else if (x < FruitFever.LEFT_BOUNDARY && dx < 0) {

			// Makes sure view never shows blank left of screen
			if (FruitFever.viewX <= 0)
				FruitFever.vx = 0;
			else
				FruitFever.vx = dx;	
		}

		// DOWN bound 
		if (y + height > FruitFever.DOWN_BOUNDARY)
			FruitFever.vy = fallingVelocity;

		// UPPER bound
		else if (y < FruitFever.UP_BOUNDARY && FruitFever.viewY >= 0) {
			
			// When jump gets and starts falling jumpingVelocity = STARTING_JUMPING_VELOCITY
			if (jumpingVelocity != STARTING_JUMPING_VELOCITY)
				FruitFever.vy = -jumpingVelocity;		

		// Make sure screen doesn't move when player is not jumping or on platform
		} else if (!isJumping && onPlatform) 
			FruitFever.vy = 0;
		
		// Stop moving the screen up if you passed the 
		if (FruitFever.viewY >= FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE && FruitFever.vy > 0 )
			FruitFever.vy = 0;

		FruitFever.viewX += FruitFever.vx;
		
		// Fixes the glitch with half jumping height
		if (FruitFever.viewY + FruitFever.vy >= 0)
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

			if (fallingVelocity < TERMINAL_VELOCITY) {

				// Acceleration effect
				fallingVelocity += fallingAcceleration;
				fallingAcceleration += changeInAcceleration;
			}
			else
				fallingVelocity = TERMINAL_VELOCITY;
			

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
			if(eastSouth == null && eastNorth == null){
				dx = HORIZONTAL_VELOCITY;
			}else{
				// Stop viewX from moving as well as player
				dx = 0; 
				FruitFever.vx = 0;
			}
			
		// WEST
		} else if(FruitFever.dx == -1) {

			Block westNorth = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block westSouth = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			// No block left of player
			if(westNorth == null && westSouth == null){
				dx = -HORIZONTAL_VELOCITY;
			}
			else{
				
				// Stop viewX from moving as well as player
				dx = 0; 
				FruitFever.vx = 0;
			}
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

			if (southEast != null)
				placePlayerOnBlock(southEast);
			else
				placePlayerOnBlock(southWest);
			
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
		
		// Makes sure you finish a cycle of images before starting a new one
		if(!images.equals(tongueAnim) && !images.equals(tongueAnimH))
			counter = -1;

		// Adjust Animation variables		
		repeat = false;
		doneAnimating = false;

		// Switch animation images
		if(facingRight)
			images = tongueAnim;
		else
			images = tongueAnimH;
		
	}
	
	public void shootSwirl(){
	
		// The swirl is at rest
		if(swirl.xSpeed == 0){
			// Makes sure you finish a cycle of images before starting a new one
			if(!images.equals(shootAnim) && !images.equals(shootAnimH))
				counter = -1;

			// Adjust Animation variables
			doneAnimating = false;
			repeat = false;
			
			// Switch animation images
			if(facingRight)
				images = shootAnim;
			else
				images = shootAnimH;
				

			swirl.imageX = FruitFever.player.facingRight ? FruitFever.player.x + 15 + FruitFever.viewX : FruitFever.player.x - 15 + FruitFever.viewX;
			swirl.imageY = FruitFever.player.facingRight ? FruitFever.player.y + 5 + FruitFever.viewY : FruitFever.player.y + 5 + FruitFever.viewY;
			swirl.xSpeed = FruitFever.player.facingRight ? 10 : -10;
		
		// Teleports Player
		}else{

			// Place player somewhat in the middle of the screen
			FruitFever.viewX = swirl.imageX - FruitFever.SCREEN_WIDTH/2;
			FruitFever.viewY = swirl.imageY - FruitFever.SCREEN_HEIGHT/2;

			// Adjust screen so that player cannot see outside view box
			if (FruitFever.viewY < 0) FruitFever.viewY = 0;
			if (FruitFever.viewX < 0) FruitFever.viewX = 0;
			
			if (FruitFever.viewY > FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE)
				FruitFever.viewY = FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE;
			
			if (FruitFever.viewX > FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE) 
				FruitFever.viewX = FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE;

			imageX = swirl.imageX;
			imageY = swirl.imageY - 5;
			
			swirl.resetState();
		}
		
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
		
		
		// Swirl
		
		swirl.animate();
		
		if(swirl.imageX < 0 || swirl.imageX > FruitFever.LEVEL_WIDTH)
			swirl.resetState();
			
	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}

class Swirl extends MovingAnimation{

	public Swirl(){
		super(-100, -100, Data.swirlAnimation, false, 0, true, 0, 0, 1);
		resetState();
	}
	
	public void resetState(){	
		imageX = -100;
		imageY = -100;
		xSpeed = 0;
		FruitFever.swirlAllowed = true;
	}
	
}



















