
/**
 *	Player - This class controls all the actions and collisions of the main character.
 *
 * @Author William Fiset, Micah Stairs
 * 
 **/

import acm.graphics.*;
import acm.program.*;
import java.util.*;
import java.awt.Color.*;
import java.awt.*;

public class Player extends MovingAnimation {
	
	static byte lives = 3;
	static final int maxLives = 3;

// Swirl related Variables
	static Swirl swirl;
	private final static byte SWIRL_MOUTH_DISTANCE = 15; 

// Movement Variables
	static final int HORIZONTAL_VELOCITY = 3; // For release make horizontal velocity 3
	int dx = 0;

// Variables concerning Gravity

	final double TERMINAL_VELOCITY = WebData.TILE_SIZE - 1;
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

	int maxJumpHeight = (int)(3.5*WebData.TILE_SIZE); // 3.5 tile jump limit
	private int baseLine;

// Jumping motion Variables
	final double STARTING_JUMPING_VELOCITY = 6.25; 
	final double STARTING_JUMPING_DECCELERATION = 0;
	final double CHANGE_INDECLERATION = 0.043; 

	double jumpingDecceleration = STARTING_JUMPING_DECCELERATION;
	double jumpingVelocity = STARTING_JUMPING_VELOCITY;

// The distance from a corner of the image used in collision detection
	final int VERTICAL_PX_BUFFER = 2;
	final int HORIZONTAL_PX_BUFFER = 3;
	
// CheckPoint related Variables



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

		boundaryLeft = WebData.TILE_SIZE;
		boundaryRight = -WebData.TILE_SIZE;
		
		swirl = new Swirl();

	}

	/** Calls all the players actions **/
	public void motion(){

		checkCollisionDetection();

		objectCollisions();

		jumpingEffect();

		enableJumping();

		gravityEffect();

		relativisticScreenMovement();

		updateHealth();

		imageX += dx;	
		
	}
	
	/** Resets players ability to jump if applicable **/
	private void enableJumping(){
		
		if(onPlatform)
			setBaseLine = false;
		
	}

	/** 
     * Moves the view of the screen relative to the character
     * (This method could use serious refactoring, but I dare not!)
	 **/
	private void relativisticScreenMovement(){

		// Horizontal screen movement
		if (x + width > FruitFever.RIGHT_BOUNDARY && dx > 0) {
			
			// Makes sure view never passes maximum level width 
			if (FruitFever.viewX >= FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + WebData.TILE_SIZE) 
				FruitFever.vx = 0;
			else
				FruitFever.vx = dx;	
		
		} else if (x < FruitFever.LEFT_BOUNDARY && dx < 0) {

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
		
		// Stop moving the screen up if you passed the (WILL: YOU DIDN'T FINISH YOUR SENTENCE HERE?)
		if (FruitFever.viewY >= FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + WebData.TILE_SIZE && FruitFever.vy > 0 )
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
		if (isJumping) {

			// Sets baseLine (where the player started before jumping)
			if (!setBaseLine) {
				baseLine = y;
				setBaseLine = true;	
			}
			
			// Player has not yet hit the maximum jump limit
			if (imageY - jumpingVelocity > baseLine - maxJumpHeight && jumpingVelocity > 0) {

				imageY -= jumpingVelocity;

				jumpingVelocity -= jumpingDecceleration;
				jumpingDecceleration += CHANGE_INDECLERATION;

			// Player has reached maxHeight, gravity now kicks in
			}
			else resetJump();				
		}
	}

	/** Takes care of making the player fall when not jumping and not on a platform **/
	private void gravityEffect(){

		// Gravity Effect triggered here
		if (!isJumping && gravity && !onPlatform) {

			imageY += fallingVelocity;

			if (fallingVelocity < TERMINAL_VELOCITY) {

				// Acceleration effect
				fallingVelocity += fallingAcceleration;
				fallingAcceleration += changeInAcceleration;
			}
			else
				fallingVelocity = TERMINAL_VELOCITY;
			

		// Executes when not falling or not allowed to fall
		} else {

			// Reset falling speed
			fallingVelocity = STARTING_FALLING_VELOCITY;
			fallingAcceleration = STARTING_FALLING_ACCELERATION;
		}

	}

	/** Sideways Collisions **/
	private void sidewaysCollision(){

		// EAST
		if (FruitFever.dx == 1) {

			// +1 is hardcoded to precision, HORIZONTAL_PX_BUFFER did not suffice 
			Block eastNorth = Block.getBlock(x + width + 1, y + VERTICAL_PX_BUFFER);
			Block eastSouth = Block.getBlock(x + width + 1, y + height - VERTICAL_PX_BUFFER);

			// No block right of player
			if (eastSouth == null && eastNorth == null)
				dx = HORIZONTAL_VELOCITY;
			else {
				// Stop viewX from moving as well as player
				dx = 0; 
				FruitFever.vx = 0;
			}
			
		// WEST
		} else if (FruitFever.dx == -1) {
			
			// -1 is hardcoded to precision, HORIZONTAL_PX_BUFFER did not suffice 
			Block westNorth = Block.getBlock(x - 1, y + VERTICAL_PX_BUFFER);
			Block westSouth = Block.getBlock(x - 1, y + height - VERTICAL_PX_BUFFER);

			// No block left of player
			if (westNorth == null && westSouth == null)
				dx = -HORIZONTAL_VELOCITY;
			else {
				
				// Stop viewX from moving as well as player
				dx = 0; 
				FruitFever.vx = 0;
			}
		}

	}

	private void upwardsCollision(){

		Block northWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y - VERTICAL_PX_BUFFER );
		Block northEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y - VERTICAL_PX_BUFFER );

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

	public void eat(){
		
		// Makes sure you finish a cycle of images before starting a new one
		if(!images.equals(tongueAnim) && !images.equals(tongueAnimH))
			counter = -1;

		// Adjust Animation variables		
		repeat = false;
		active = true;

		// Switch animation images
		if(facingRight)
			images = tongueAnim;
		else
			images = tongueAnimH;
		
	}

	/** Updates the players health  **/
	private void updateHealth(){

		// This statement kinda looks weird but it's clear and more efficient (I think!)
		if(checkForPlayerOutOfBounds()){}
		else if (checkForDangerousSpriteCollisions()) {}

	}

	/** checks if player touched any dangerous sprites (aka lava)**/
	private boolean checkForDangerousSpriteCollisions(){

		boolean collisionOccurred = false;

		// loop through all dangerous sprites
		for (Thing dangerousThing : FruitFever.dangerousThings) {
			
			/** As we start getting more and more dangerous sprites we will have to
			  * distinguish between types either by using instanceof or giving each 
			  * sprite a property **/
			
			dangerousThing.boundaryTop = (WebData.TILE_SIZE/3);

			if (dangerousThing.intersects(this)) {

				collisionOccurred = true;

				lives--;		
				adjustLives(lives);	
				respawn();

				break;
			}
		}

		return collisionOccurred;

	}

	private boolean checkForPlayerOutOfBounds(){

		boolean playerOutOfBounds = (x + WebData.TILE_SIZE < 0 || x > FruitFever.LEVEL_WIDTH || y + height < 0 || y - height > FruitFever.LEVEL_HEIGHT );

		if (playerOutOfBounds) {
			
			lives--;		
			adjustLives(lives);	
			respawn();
		}

		return playerOutOfBounds;
	}

	/** Checks for collisions with checkPoints, currency, vortex and other matter **/
	private void objectCollisions(){


		/** CheckPoint Collision **/
		for (Thing checkPoint : FruitFever.checkPoints) {
			checkPoint.boundaryLeft = 11;
			checkPoint.boundaryRight = -11;
			// Check if the player intersects the rod
			if (checkPoint.intersects(this)){
				
				// Check to see if this checkpoint hasn't already been attained
				if(FruitFever.greenCheckPoint == null || !FruitFever.greenCheckPoint.equals(checkPoint)){
				
					checkPoint.changeImage(WebData.checkpointFlagGreen);
					FruitFever.greenCheckPoint = checkPoint;
					FruitFever.playerStartX = checkPoint.imageX;
					FruitFever.playerStartY = checkPoint.imageY + WebData.TILE_SIZE;

					for(int i = 0; i < 7; i++)
						FruitFever.addToThings(new Animation(checkPoint.imageX + WebData.TILE_SIZE/2 - 17 + (int) (Math.random()*35), checkPoint.imageY - WebData.TILE_SIZE*2 + (int) (Math.random()*35), WebData.fireworkAnimation[(int) (Math.random()*3)], false, 2 + (int)(Math.random()*3), false, 3 ));
					
					break;
					
				}
			}
		}

		// Changes all the flags to red flags except the current green flag
		for (Thing checkPoint : FruitFever.checkPoints) {
			if (checkPoint == FruitFever.greenCheckPoint) continue;
			checkPoint.changeImage(WebData.checkpointFlagRed);
		}

		FruitFever.vortex.boundaryTop = WebData.TILE_SIZE/2 ;

		// Reset Level if player touches vortex
		if (this.intersects(FruitFever.vortex))
			FruitFever.levelComplete = true;
		
	}


	/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly */
	private void adjustLives(int livesLeft){
	
		for (int i = 0; i < maxLives; i++)
			FruitFever.livesImages[i].setVisible(livesLeft > i);

	}

	/** Adjusts View to place the player in the middle of the screen **/
	public void focusViewOnPlayer(int newPlayerXPos, int newPlayerYPos){

		// Places the player exactly in the middle of the screen
		FruitFever.viewX = newPlayerXPos - (FruitFever.SCREEN_WIDTH/2) + (WebData.TILE_SIZE/2);
		FruitFever.viewY = newPlayerYPos - (FruitFever.SCREEN_HEIGHT/2) + (WebData.TILE_SIZE/2);

		// Adjust screen so that player cannot see outside view box
		FruitFever.viewY = Math.max(FruitFever.viewY, 0);
		FruitFever.viewX = Math.max(FruitFever.viewX, 0);
		
		FruitFever.viewY = Math.min(FruitFever.viewY, FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + WebData.TILE_SIZE);
		FruitFever.viewX = Math.min(FruitFever.viewX, FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + WebData.TILE_SIZE);

		FruitFever.naturalAnimateAll();

	}

	/** 
	 * @Param levelRespawn: Used to indicate that the player was loaded into a level rather 
	 * 		  than already previously being in the level
	 **/
	public void focusViewOnPlayer(int newPlayerXPos, int newPlayerYPos, boolean levelRespawn){

		focusViewOnPlayer(newPlayerXPos, newPlayerYPos);

		// Fixes the loading player position bug 
		if (levelRespawn) {
			imageX -= WebData.TILE_SIZE; 
			x -= WebData.TILE_SIZE;
		}

		FruitFever.naturalAnimateAll();

	}

	/** These are all the settings that need to be reset when the player respawns **/
	private void respawn(){

		// Reset falling speed
		fallingVelocity = STARTING_FALLING_VELOCITY;
		fallingAcceleration = STARTING_FALLING_ACCELERATION;

		// Load player in the correct spot 
		imageX = FruitFever.playerStartX;
		imageY = FruitFever.playerStartY;			

		// Resetswirl
		swirl.resetState();

		// Focus view on the player
		focusViewOnPlayer(FruitFever.playerStartX, FruitFever.playerStartY, true);

	}

	public void shootSwirl(){

		// Makes sure you finish a cycle of images before starting a new one
		if(!images.equals(shootAnim) && !images.equals(shootAnimH))
			counter = -1;

		// Adjust Animation variables
		active = false;
		repeat = false;
		
		/** Check if there's a Block in front/in back of the player before he shoots **/
		if (facingRight) {

			Block westNorth = Block.getBlock(x + WebData.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + WebData.TILE_SIZE/4 );
			Block westSouth = Block.getBlock(x + WebData.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + WebData.TILE_SIZE - (WebData.TILE_SIZE/4));

			// If there is not Block in front of player
			if (westNorth == null && westSouth == null) {

				// Makes the swirl shoot out of the player from the left
				swirl.reset = false;
				swirl.imageX = x + SWIRL_MOUTH_DISTANCE + FruitFever.viewX;
				swirl.imageY = y + FruitFever.viewY;
				swirl.xSpeed = Swirl.dx;

				// Set Right shooting animation
				images = shootAnim;
			
			// If there is a block in front of the player, don't do swirl animation
			} else
				images = stillAnim;

		// Facing left
		} else {

			Block eastNorth = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + WebData.TILE_SIZE/4);
			Block eastSouth = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + WebData.TILE_SIZE - (WebData.TILE_SIZE/4));

			// If there is not Block in front of player
			if (eastSouth == null && eastNorth == null) {

				// Makes the swirl shoot out of the player from the left
				swirl.reset = false;
				swirl.imageX = x - SWIRL_MOUTH_DISTANCE + FruitFever.viewX;
				swirl.imageY = y + FruitFever.viewY;
				swirl.xSpeed = -Swirl.dx;

				// Set Left shooting animation
				images = shootAnimH;
				
			// If there is a block in front of the player, don't do swirl animation
			} else images = stillAnim;

		}
	
	}
	
	public void swirlTeleport(){
	
		// Remember that the player has a width of TileSize*3 so we must subtract a tile-size!
		imageX = swirl.imageX - WebData.TILE_SIZE;
		imageY = swirl.imageY;

		// Hardcoded Values are to make precision more accurate
		Block upperRight = Block.getBlock(x, y + 3);
		Block upperLeft = Block.getBlock(x + WebData.TILE_SIZE, y + 3);
		Block lowerLeft = Block.getBlock(x, y + WebData.TILE_SIZE - 4);
		Block lowerRight = Block.getBlock(x + WebData.TILE_SIZE, y + WebData.TILE_SIZE - 4);

		/** Fixes Issue #42 where player semi teleports into blocks **/

		if (upperRight != null || upperLeft != null || lowerLeft != null || lowerRight != null){
			imageX = (imageX/WebData.TILE_SIZE) * WebData.TILE_SIZE;	
			
			// Takes into account that the player's center is top left
			if (!facingRight)
				imageX += WebData.TILE_SIZE;
			
		}


		// Focuses the view on the player placing the player in the center of the screen
		focusViewOnPlayer(swirl.imageX, swirl.imageY);

		swirl.resetState();
		
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

		} else {

			if(images.equals(stillAnim))
				images = stillAnimH;
			else if(images.equals(shootAnim))
				images = shootAnimH;
			else if(images.equals(tongueAnim))
				images = tongueAnimH;
		}
		
		if(!active){
		
			// Adjust Animation variables
			repeat = true;
			counter = -1;
			active = true;
			
			// Switch animation images
			if(facingRight)
				images = stillAnim;
			else
				images = stillAnimH;
		}

		// Fixes Issue #45 when Animating the swirl before you check the collision
		super.animate();
		swirl.animate();

		// If Swirl goes off screen or hits a block, destroy it
		if(swirl.imageX + Swirl.SWIRL_IMG_WIDTH < 0 || swirl.imageX > FruitFever.LEVEL_WIDTH || swirl.collidesWithBlock())
			swirl.resetState();

	}
	
	/** Returns the location of the tip of the tongue (when fully extended)
		@param collisionDetection: When true, we are dealing with x and y. When false,
		we are dealing with imageX and imageY **/
	public Point getTonguePosition(boolean collisionDetection){
	
		int currentTongueWidth = 0;
		
		switch (counter){
			case 3: currentTongueWidth = 8; break;
			case 4: currentTongueWidth = 20; break;
		}
		
		int tempX, tempY;
		
		if (collisionDetection) {
			tempX = x;
			tempY = y;
		}
		else {
			tempX = imageX + WebData.TILE_SIZE;
			tempY = imageY;
		}
		
		if (facingRight)
			return new Point(tempX + (int)(WebData.TILE_SIZE*1.5) + currentTongueWidth, tempY + (int) image.getHeight()/2);
		else
			return new Point(tempX - WebData.TILE_SIZE/2 - currentTongueWidth, tempY + (int) image.getHeight()/2);
	}

	public void posInfo(){
		System.out.println("ImageX: " + imageX + "   ImageY: " + imageY + "   X: " + x + "   Y: " + y);
	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}

	/** A swirl is a projectile shot from the player as a teleportation method  **/
	class Swirl extends MovingAnimation{
		
		static boolean reset = true;

		// Swirls velocity
		static final byte dx = 8;

		// This is the location of where the swirl is off screen when it is at rest
		static final short SWIRL_X_REST_POS = -100;
		static final short SWIRL_Y_REST_POS = -100;

		// These values are the actual image dimensions not the WebData.TILE_SIZE width and height
		static final byte SWIRL_IMG_WIDTH = 14; 
		static final byte SWIRL_IMG_HEIGHT = 14; 

		// Since the swirl is a circle the collision buffer makes collision much more accurate 
		static final byte AIR_SPACING = 6;

		public Swirl(){

			super(SWIRL_X_REST_POS, SWIRL_Y_REST_POS, WebData.swirlAnimation, false, 0, true, 0, 0, -1);
			resetState();

		}

		public void resetState(){	

			imageX = SWIRL_X_REST_POS;
			imageY = SWIRL_Y_REST_POS;

			x = SWIRL_X_REST_POS;
			y = SWIRL_Y_REST_POS;

			xSpeed = 0;
			ySpeed = 0;

			reset = true;

		}

		/** Returns true or false depending on if the swirl has collided with a block**/
		public boolean collidesWithBlock(){

			Block westNorth = Block.getBlock(x + AIR_SPACING + xSpeed, y + AIR_SPACING ) ;
			if (westNorth != null) return true;

			Block eastNorth = Block.getBlock(x + SWIRL_IMG_WIDTH + AIR_SPACING + xSpeed, y + AIR_SPACING) ;
			if (eastNorth != null) return true;

			Block westSouth = Block.getBlock(x + AIR_SPACING + xSpeed, y + SWIRL_IMG_HEIGHT + AIR_SPACING ) ;
			if (westSouth != null) return true;

			Block eastSouth = Block.getBlock(x + SWIRL_IMG_WIDTH + AIR_SPACING + xSpeed, y + SWIRL_IMG_HEIGHT + AIR_SPACING );
			if (eastSouth != null) return true;

			return false;

		}

		@Override public String toString(){
			return "Swirl   X: " + x + "  Y: " + y;
		}
	}