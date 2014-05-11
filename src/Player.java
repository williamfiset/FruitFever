
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
	
	static byte lives = 3;
	static final int maxLives = 3;

// Swirl related Variables
	static Swirl swirl;
	private final static byte SWIRL_MOUTH_DISTANCE = 15; 

// Movement Variables
	static final int HORIZONTAL_VELOCITY = 3; // For release make horizontal velocity 3
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
	private boolean isJumping = false;

	int maxJumpHeight = (3*25 + 25/2); // 3.5 tile jump limit
	private int baseLine;

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

		updateHealth();

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

	/** Updates the players health  **/
	private void updateHealth(){

		// Checks if player is out of the level bounds
		if (playerWithinLevelBounds()){
			
			lives--;

			focusViewOnPlayer(FruitFever.playerStartX, FruitFever.playerStartY, true);
			// x = FruitFever.playerStartX;
			// y = FruitFever.playerStartY;
			imageX = FruitFever.playerStartX;
			imageY = FruitFever.playerStartY;

			for (Block block : FruitFever.blocks) {
				block.naturalAnimate();
			}

		}

		adjustLives(lives);	

	}

	private boolean playerWithinLevelBounds(){

		return (x + Data.TILE_SIZE < 0 || x > FruitFever.LEVEL_WIDTH || y + height < 0 || y - height > FruitFever.LEVEL_HEIGHT );

	} 
	
	/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly */
	public static void adjustLives(int livesLeft){
	
		for (int i = 0; i < maxLives; i++)
			FruitFever.livesImages[i].setVisible(livesLeft > i);

	}

	public void focusViewOnPlayer(int newPlayerXPos, int newPlayerYPos){

		// Place player somewhat in the middle of the screen
		FruitFever.viewX = newPlayerXPos - FruitFever.SCREEN_WIDTH/2;
		FruitFever.viewY = newPlayerYPos - FruitFever.SCREEN_HEIGHT/2;

		// Adjust screen so that player cannot see outside view box
		if (FruitFever.viewY < 0) FruitFever.viewY = 0;
		if (FruitFever.viewX < 0) FruitFever.viewX = 0;
		 
		if (FruitFever.viewY > FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE)
			FruitFever.viewY = FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE;
		
		if (FruitFever.viewX > FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE) 
			FruitFever.viewX = FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE;

	}

	public void focusViewOnPlayer(int newPlayerXPos, int newPlayerYPos, boolean levelRespawn){

		focusViewOnPlayer(newPlayerXPos, newPlayerYPos);
		if (levelRespawn) {
			imageX -= Data.TILE_SIZE; 
			x -= Data.TILE_SIZE;
		}

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
			

			// Check if there's a Block in front/in back of the player before he shoots
			if (facingRight) {

				Block westNorth = Block.getBlock(x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE / 4 );
				Block westSouth = Block.getBlock(x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/4));

				// If there is not Block in front of player
				if (westNorth == null && westSouth == null) {

					// Makes the swirl shoot out of the player from the left	
					swirl.imageX = x + SWIRL_MOUTH_DISTANCE + FruitFever.viewX;
					swirl.imageY = y + FruitFever.viewY;
					swirl.xSpeed = Swirl.swirlVelocity;

					// Set Right shooting animation
					images = shootAnim;

				}else{

					// If there is a block in front of the player, don't do swirl animation
					images = stillAnim;
				}

			// Facing left
			}else{

				Block eastNorth = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE / 4 );
				Block eastSouth = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/4));

				// If there is not Block in front of player
				if (eastSouth == null && eastNorth == null) {

					// Makes the swirl shoot out of the player from the left
					swirl.imageX = x - SWIRL_MOUTH_DISTANCE + FruitFever.viewX;
					swirl.imageY = y + FruitFever.viewY;
					swirl.xSpeed = -Swirl.swirlVelocity;

					// Set Left shooting animation
					images = shootAnimH;
				}else{

					// If there is a block in front of the player, don't do swirl animation
					images = stillAnim;
				}
			}

		
		// Teleports Player
		}else{

			// Focuses the view on the player placing the player in the center of the screen
			focusViewOnPlayer(swirl.imageX, swirl.imageY);

			/** 
			 * Teleport the Player to the location of the swirl. The -5 is a photoshop determined
			 * value of how much you need to shift the player up to position is directly on top of the swirl
			 **/

			// Remember that the player has a width of TileSize*3 so we must subtract a tile-size!
			imageX = swirl.imageX - Data.TILE_SIZE;
			imageY = swirl.imageY;
			
			/** Fixes Issue #41. Since the optimized .animate() method in Things doesn't move
			the blocks off screen when you teleport to a location where there are unmoved blocks off
			screen they appear on the screen. To fix this issue I added a new method in Thing called 
			'naturalAnimate' which is the old .animate method that moves all the Things (in this case 
			blocks) in sync together. Thus when you teleport it also moves the blocks off screen as well*/

			for (Block block : FruitFever.blocks)
				block.naturalAnimate();

			swirl.resetState();
		}
		
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
		
		
		
		// If Swirl goes off screen or hits a block, destroy it
		if(swirl.imageX < 0 || swirl.imageX > FruitFever.LEVEL_WIDTH || swirl.collidesWithBlock())
			swirl.resetState();

		swirl.animate();
			
		
	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}

/** A swirl is a projectile shot from the player as a teleportation method  **/
class Swirl extends MovingAnimation{

	static final int swirlVelocity = 8;

	// This is the location of where the swirl is off screen when it is at rest
	static final short SWIRL_X_REST_POS = -100;
	static final short SWIRL_Y_REST_POS = -100;

	// These values are the actual image dimensions not the Data.TILE_SIZE width and height
	static final byte SWIRL_IMG_WIDTH = 14; 
	static final byte SWIRL_IMG_HEIGHT = 14; 

	// Since the swirl is a circle the collision buffer makes collision much more accurate 
	static final byte AIR_SPACING = 6;

	public Swirl(){
		super(SWIRL_X_REST_POS, SWIRL_Y_REST_POS, Data.swirlAnimation, false, 0, true, 0, 0, 1);
		resetState();
	}
	
	public void resetState(){	

		imageX = SWIRL_X_REST_POS;
		imageY = SWIRL_Y_REST_POS;
		xSpeed = 0;
		FruitFever.swirlAllowed = true;

	}

	/** Returns true or false depending on if the swirl has collided with a block**/
	public boolean collidesWithBlock(){

		Block westNorth = Block.getBlock(x + AIR_SPACING + xSpeed, y + AIR_SPACING ) ;
		if (westNorth != null) return true;

		Block westSouth = Block.getBlock(x + AIR_SPACING + xSpeed, y + SWIRL_IMG_HEIGHT + AIR_SPACING ) ;
		if (westSouth != null) return true;

		Block eastNorth = Block.getBlock(x + SWIRL_IMG_WIDTH + AIR_SPACING + xSpeed, y + AIR_SPACING) ;
		if (eastNorth != null) return true;

		Block eastSouth = Block.getBlock(x + SWIRL_IMG_WIDTH + AIR_SPACING + xSpeed, y + SWIRL_IMG_HEIGHT + AIR_SPACING );
		if (eastSouth != null) return true;

		return false;

	}
	
}



















