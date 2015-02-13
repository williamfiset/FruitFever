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
import java.lang.reflect.*;

public class Player extends Animation {

	private static final long serialVersionUID = 1L;

	public static int startX, startY;
	
	public static Animation poisonAnimation;
	
	public Animation grabbedItem = null;
	
    // static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
  // Player Stats
  
	public static final int MAX_LIVES = 100;
	private static int STARTING_LIVES = 3;
	private int lives                 = STARTING_LIVES;
	private boolean poisoned          = false;
	private int poisonLeft            = 0;
	
	public double maxEnergy = 1000, currentEnergy = maxEnergy, maxHealth = 1000, currentHealth = maxHealth;
	
  // Swirl related Variables
	
	Swirl swirl;
	private final static byte SWIRL_MOUTH_DISTANCE = 15; 
	
  // Horizontal Movement Variables
	
	public static enum MovementDirection { LEFT, RIGHT, NONE }	
	private MovementDirection movementDirection = MovementDirection.NONE;
	
	static int ORIGINAL_STARTING_HORIZONTAL_VELOCITY = 3;
	static int horizontalVelocity = ORIGINAL_STARTING_HORIZONTAL_VELOCITY; 
	public int dx = 0;
	public boolean facingRight = true;
	
  // Collision Detection Constants 
	
	private static final byte VERTICAL_PX_BUFFER = 2;
	private static final byte CRACK_SPACING      = 3;
	private static final byte JUMP_SPACING       = 3;
	
  // Gravity/Falling Variables & Constants
	
	private static final double STARTING_FALLING_VELOCITY = 2.5,
								TERMINAL_VELOCITY = Data.TILE_SIZE - VERTICAL_PX_BUFFER - 1,STARTING_FALLING_ACCELERATION = 0.5,
								CHANGE_IN_ACCELERATION = 0.015;
	
	private double 	fallingVelocity = STARTING_FALLING_VELOCITY,
					fallingAcceleration = STARTING_FALLING_ACCELERATION;
	
	private boolean gravity = true;
	
  // Jumping Variables 
	
	private boolean setBaseLine = true, // true because we don't know where the player starts
					keepJumping = false,
					onSurface   = false, // true if the player is on any surface 
					isJumping   = false;
	
	private int baseLine, // holds the y value of where the player started when jumping
				maxJumpHeight = (int)(3.5*Data.TILE_SIZE); // Jump a maximum of 3.5 blocks high
	
  // Jumping Motion Variables
	
	private static final double ORIGINAL_STARTING_JUMPING_VELOCITY = 6.25,
								STARTING_JUMPING_DECCELERATION = 0,
								CHANGE_IN_DECLERATION = 0.043; 

	private static double startingJumpingVelocity = ORIGINAL_STARTING_JUMPING_VELOCITY; 
	
	private double 	jumpingDecceleration = STARTING_JUMPING_DECCELERATION,
					jumpingVelocity = ORIGINAL_STARTING_JUMPING_VELOCITY;
	
  // Animation Variables
	
	private GImage[] stillAnim, stillAnimH, shootAnim, shootAnimH, tongueAnim, tongueAnimH;
	
	public boolean finishedTongueAnimation = true;
	
	private boolean sideCollisionFacingLeft = false,
					sideCollisionFacingRight = false,
					sideCollision = false;

	public Player() {

		super(startX, startY, Data.playerStill, false, 1, true, Animation.Type.NOT_AVAILABLE, false, false, false, Layer.ABOVE_BLOCKS);

		stillAnim  = Data.playerStill;   stillAnimH = Data.playerStillH;  
		shootAnim  = Data.playerShoot;   shootAnimH = Data.playerShootH;  
		tongueAnim = Data.playerTongue; tongueAnimH = Data.playerTongueH;

		boundaryLeft = Data.TILE_SIZE; boundaryRight = -Data.TILE_SIZE;
		
		poisonAnimation = new Animation(x, y, Data.playerGasBubbles, false, 4, true, Animation.Type.NOT_AVAILABLE, false, false, false, Layer.ABOVE_BLOCKS);
		poisonAnimation.image.setVisible(false);

		swirl = new Swirl();

	}

	/** Calls all the players actions **/
	public void motion() {

		// No need to attach this to debug mode because I only want one of them to be printed at a time
		// System.out.printf("jumpingVelocity: %f isJumping: %b \n", jumpingVelocity, isJumping);
		// System.out.printf("fallingVelocity: %f  imageY: %d  imageX: %d \n", fallingVelocity, imageY, imageX);
		// System.out.printf("imageX: %d imageY: %d X: %d Y: %d\n", imageX, imageY, x, y);
		// System.out.printf("viewX %d  viewY: %d \n", FruitFever.viewX, FruitFever.viewY);
		// System.out.printf("Falling Velocity: %f\n", fallingVelocity);
		// System.out.println(maxJumpHeight + " " + maxJumpHeight);
		// System.out.printf("horizontalVelocity: %d\n", horizontalVelocity);
		// System.out.println(swirl);

		jump();
		
		// Collisions
		checkCollisionDetection();
		objectCollisions();

		// View Movement
		relativisticScreenMovement();
	
		// These methods control the movement of the player 
		jumpingEffect();
		gravityEffect();
		imageX += dx;
		extraHorizontalMovement();

		// Other needed functions
		enableJumping();
		poison();
		updateHealth();
		grabbingItem();
		checkCollisionWithHintSigns();
	}
	
	public void poison() {

		if (poisoned) {
			
			ScreenHandler.adjustHealthBar(--currentHealth/maxHealth);
			poisonAnimation.imageX = imageX + Data.TILE_SIZE;
			poisonAnimation.imageY = imageY - Data.TILE_SIZE;
			poisonAnimation.animate();
			poisonLeft--;
			
			if (poisonLeft <= 0) {
				poisoned = false;
				poisonAnimation.image.setVisible(false);
			}
		}
	
	}

	public void startJumpPowerup() {
	
		if (FruitFever.debugMode)
			System.out.println("Executed startJumpPower");
			
		startingJumpingVelocity = 9;
		maxJumpHeight = (int)(5.5*Data.TILE_SIZE);

	}
	
	public void stopJumpPowerup() {
	
		if (FruitFever.debugMode)
			System.out.println("stopped startJumpPower");
		
		maxJumpHeight = (int)(3.5*Data.TILE_SIZE);
		startingJumpingVelocity = ORIGINAL_STARTING_JUMPING_VELOCITY;

	}

	public void startSpeedPowerup(){
		horizontalVelocity = 5;
	}

	public void stopSpeedPowerup(){
		horizontalVelocity = 3;
	}
	
	/** This method pushes the player up close to the wall when there's a side collision.
		This method fixes issue #94 concerning different player speeds **/
	private void extraHorizontalMovement() {

		if (sideCollision) {
			
			// Moving Right
			if (movementDirection == MovementDirection.RIGHT) {
				if (!sideCollisionFacingRight) {

					Block rightCollision1 = null, rightCollision2 = null;
					int hv = horizontalVelocity;

					// While horizontalVelocity is greater than zero find the optimal distance 
					while (hv > 0 ) {

						rightCollision1 = Block.getBlock(x + hv + Data.TILE_SIZE, y + VERTICAL_PX_BUFFER);
						rightCollision2 = Block.getBlock(x + hv + Data.TILE_SIZE, y + Data.TILE_SIZE - VERTICAL_PX_BUFFER);

						if (rightCollision1 == null && rightCollision2 == null) {
							imageX += hv;
							break;
						} else
							hv--;
					}
				}

			// Moving Left
			} else if (movementDirection == MovementDirection.LEFT) {

				if (!sideCollisionFacingLeft) {

					Block leftCollision1 = null, leftCollision2 = null;
					int hv = horizontalVelocity;

					// While horizontalVelocity is greater than zero find the optimal distance 
					while (hv > 0 ) {

						leftCollision1 = Block.getBlock(x - hv , y + VERTICAL_PX_BUFFER);
						leftCollision2 = Block.getBlock(x - hv, y + Data.TILE_SIZE - VERTICAL_PX_BUFFER);

						if (leftCollision1 == null && leftCollision2 == null) {
							imageX -= hv;
							break;
						} else
							hv--;
						
					}
				} 

			} else if (movementDirection == MovementDirection.NONE) {

				sideCollisionFacingRight = true;
				sideCollisionFacingLeft = true;

			}
		} 

		if (facingRight)
			sideCollisionFacingLeft = false;
		else
			sideCollisionFacingRight = false;

		sideCollision = false;

	}

	/** Responds accordingly to collision detection **/
	private void checkCollisionDetection() {

		downwardsCollision(); // downwardsCollision can make onSurface false, so it is first
		extraCollisionChecks();
		sidewaysCollision();
		upwardsCollision();

	}

	/** Side Collisions **/
	private void sidewaysCollision() {

		// Player is moving EAST
		if (movementDirection == MovementDirection.RIGHT) {

			Block eastNorth;
			Block eastSouth;

			// When not on platform
			eastNorth = Block.getBlock(x + Data.TILE_SIZE + horizontalVelocity, y + VERTICAL_PX_BUFFER); 
			eastSouth = Block.getBlock(x + Data.TILE_SIZE + horizontalVelocity, y + Data.TILE_SIZE - VERTICAL_PX_BUFFER);

			// No block right of player
			if (eastSouth == null && eastNorth == null) {
				// System.out.println("Side");
				dx = horizontalVelocity;

			// Collision has occurred while the player was moving right
			} else {

				// System.out.println("right");
				sideCollision = true;
				dx = 0; 
				FruitFever.vx = 0;
			}

		// Player is moving WEST
		} else if (movementDirection == MovementDirection.LEFT) {
						
			Block westNorth = Block.getBlock(x - horizontalVelocity, y + VERTICAL_PX_BUFFER); 
			Block westSouth = Block.getBlock(x - horizontalVelocity, y + Data.TILE_SIZE - VERTICAL_PX_BUFFER);

			// No block left of player
			if (westNorth == null && westSouth == null){
				dx = -horizontalVelocity;

			// Collision has occurred while the player was moving left
			} else {

				// System.out.println("left");
				sideCollision = true;
				dx = 0; 
				FruitFever.vx = 0;
			}
		}
	}

	/** Test if player is going to hit a platform while falling **/
	private void downwardsCollision() {

		// SOUTH
		Block southWest = null, southEast = null;

		// Need to do this because starting starting falling velocity is never 0
		if (gravity) {
			southWest = Block.getBlock(x + CRACK_SPACING, y + Data.TILE_SIZE + VERTICAL_PX_BUFFER + (int) fallingVelocity);			
			southEast = Block.getBlock(x + Data.TILE_SIZE - CRACK_SPACING, y + Data.TILE_SIZE + VERTICAL_PX_BUFFER + (int) fallingVelocity);
		
		// Will this ever execute?
		} else {
			System.out.println("downwardsCollision - ?Executes?");
			southWest = Block.getBlock(x + CRACK_SPACING, y + Data.TILE_SIZE + VERTICAL_PX_BUFFER);			
			southEast = Block.getBlock(x + Data.TILE_SIZE - CRACK_SPACING, y + Data.TILE_SIZE + VERTICAL_PX_BUFFER);
		}

		
		if (southEast != null || southWest != null) {
			
			// System.out.println("Down");

			onSurface = true;	

			// This is what actually stops the fall 
			if (southEast != null)
				placePlayerOnBlock(southEast);
			else
				placePlayerOnBlock(southWest);
			
		} else
			onSurface = false;

	}

	/** Does extra checks for special case(s) **/
	private void extraCollisionChecks() {

		/** Plays a role in solving issue# 64 **/
		
		// Executes only when falling downwards 
		if (!isJumping ) { // && fallingVelocity > STARTING_FALLING_VELOCITY

			for (int horizontalPosition = 3; horizontalPosition <= 22 ; horizontalPosition++) {

				// optimization (we don't need to check all the points)
				if (horizontalPosition > 6 && horizontalPosition < 19) continue;

				// Forms a line of points that determine if the player has hit anything
				Block southernBlock = Block.getBlock(x + horizontalPosition , y + Data.TILE_SIZE + (int) fallingVelocity - 4);

				// If collision 
				if (southernBlock != null) {
					onSurface = true;
					placePlayerOnBlock(southernBlock);
					
					return;
				}
			}
		}

	}

	/** Does all the upwards boundary colliion checks **/
	private void upwardsCollision() {

		Block northWest = Block.getBlock(x + JUMP_SPACING, y - VERTICAL_PX_BUFFER );
		Block northEast = Block.getBlock(x + Data.TILE_SIZE - JUMP_SPACING, y - VERTICAL_PX_BUFFER );

		// Collision on block above this one has happened
		if (northWest != null || northEast != null){
			// System.out.println("Up");
			resetJump();
		}

	}

	/** Places the player on top of the block he is currently on **/
	private void placePlayerOnBlock(Block block) {
		if (onSurface) {
			imageY = block.imageY - block.height;
			y = block.y - block.height;
		}
	}

	/** Handles Jumping triggered by the Player **/
	private void jumpingEffect() {

		// Jumping event was Triggered
		if (isJumping) {

			// Sets baseLine (where the player started before jumping)
			if (!setBaseLine) {
				baseLine = imageY;
				setBaseLine = true;	
			}
			

			// Player has not yet hit the maximum jump limit
			if (imageY - jumpingVelocity > baseLine - maxJumpHeight && jumpingVelocity > 0) {

				// Move the player's image up
				imageY -= jumpingVelocity;

				jumpingVelocity -= jumpingDecceleration;
				jumpingDecceleration += CHANGE_IN_DECLERATION;

			// Player has reached maxHeight, gravity now kicks in
			} else resetJump();				
		}
	}

	private void resetJump() {

		jumpingVelocity = startingJumpingVelocity;
		jumpingDecceleration = STARTING_JUMPING_DECCELERATION;

		isJumping = false;
	}

	/** Triggers the variables that make the player jump **/
	public void jump() {

		if (keepJumping)
			setIsJumping(true);	

	}


	/** 
	 * It was a good idea to have a setter for IsJumping.  
	 * For example you don't always want to set isJumping to true if the character is in free fall. **/
	public void setIsJumping(boolean value) {

		// If you are not jumping and are on a platform
		if (!setBaseLine && onSurface)
			isJumping = true;
	}

	/** Resets players ability to jump if applicable **/
	private void enableJumping() {
		
		if (onSurface)
			setBaseLine = false;
		
	}

	/** Takes care of making the player fall when not jumping and not on a platform **/
	private void gravityEffect() {

		// Gravity Effect triggered here
		if (!isJumping && gravity && !onSurface) {

			// Move the player's image down
			imageY += fallingVelocity;

			if (fallingVelocity < TERMINAL_VELOCITY) {

				// Acceleration effect
				fallingVelocity += fallingAcceleration;
				fallingAcceleration += CHANGE_IN_ACCELERATION;
			
			} else
				fallingVelocity = TERMINAL_VELOCITY;
			
		// Executes when not falling or not allowed to fall
		} else {

			// Reset falling speed
			fallingVelocity = STARTING_FALLING_VELOCITY;
			fallingAcceleration = STARTING_FALLING_ACCELERATION;
		}

	}


	/** 
     * Moves the view of the screen relative to the character
     * (This method could use some serious refactoring, but I dare not!)
	 **/
	private void relativisticScreenMovement() {

		// Horizontal screen movement
		if (x + Data.TILE_SIZE > FruitFever.RIGHT_BOUNDARY && dx > 0) {
			
			// Makes sure view never passes maximum level Data.TILE_SIZE 
			if (FruitFever.viewX >= FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE) 
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
		if (y + Data.TILE_SIZE > FruitFever.DOWN_BOUNDARY)
			FruitFever.vy = fallingVelocity;

		// UPPER bound
		else if (y < FruitFever.UP_BOUNDARY && FruitFever.viewY >= 0) {
			
			// When jump gets and starts falling jumpingVelocity = startingJumpingVelocity
			if (jumpingVelocity != startingJumpingVelocity)
				FruitFever.vy = -jumpingVelocity;		

		// Make sure screen doesn't move when player is not jumping or on platform
		} else if (!isJumping && onSurface) 
			FruitFever.vy = 0;
		
		// Stop moving the screen up 
		if (FruitFever.viewY >= FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE && FruitFever.vy > 0 )
			FruitFever.vy = 0;

		FruitFever.viewX += FruitFever.vx;
		
		// Fixes the glitch with half jumping Data.TILE_SIZE
		if (FruitFever.viewY + FruitFever.vy >= 0)
 			FruitFever.viewY += FruitFever.vy;	
 		
	}

	public void eat() {
		
		// Makes sure you finish a cycle of images before starting a new one
		if (!images.equals(tongueAnim) && !images.equals(tongueAnimH))
			counter = -1;

		// Adjust Animation variables		
		repeat = false;

		// Switch animation images
		if (facingRight)
			setNewAnimation(tongueAnim);
		else
			setNewAnimation(tongueAnimH);
		
	}

	/** Updates the players health/hearts  **/
	private void updateHealth() {

		// This statement kinda looks weird but it's clear and more efficient (I think!)
		if (checkForPlayerOutOfBounds()) { }

		// You die if you have no more energy or health
		else if (currentEnergy <= 0 || currentHealth <= 0) {
			ScreenHandler.adjustHearts(--lives);	
			respawn();

		// Check for dangerous collisions	
		} else if (checkForDangerousSpriteCollisions()) { }

	}

	/** Checks if player touched any dangerous sprites **/
	private boolean checkForDangerousSpriteCollisions() {

		boolean collisionOccurred = false;

		/** Loop through all dangerous sprites **/
		for (Animation obj : FruitFever.dangerousThings) {

			if (obj.intersects(this)) {
				
				// GAS BUBBLES 
				if (obj.type == Animation.Type.GAS_BUBBLES) {
					poisoned  = true;
					poisonLeft = 300;
					poisonAnimation.imageX = imageX + Data.TILE_SIZE;
					poisonAnimation.imageY = imageY - Data.TILE_SIZE;
					poisonAnimation.image.setVisible(true);

				// SPIKES
				} else if (obj.type == Animation.Type.SPIKES){

					
					bloodySpikes(obj);
					
					/** Bloody any other spikes **/
						for (Animation obj2 : FruitFever.dangerousThings)
							if (obj2.type == Animation.Type.SPIKES && obj2.intersects(this))
								bloodySpikes(obj2);
					
					collisionOccurred = true;
					ScreenHandler.adjustHearts(--lives);	
					respawn();

					break;

				} else {

					collisionOccurred = true;
					ScreenHandler.adjustHearts(--lives);	
					respawn();

					break;
				}
			}
		}

		return collisionOccurred;

	}
	
	private void bloodySpikes(Animation obj) {
		
		// Spikes Point upwards
		if (obj.images.equals(Data.spikes))
			obj.setNewAnimation(Data.spikesBlood);

		// Spikes Point downawards	
		if (obj.images.equals(Data.spikesV))
			obj.setNewAnimation(Data.spikesBloodV);
						
	}

	private boolean checkForPlayerOutOfBounds() {

		// boolean playerOutOfBounds = (imageX + Data.TILE_SIZE < 0 || imageX > FruitFever.LEVEL_WIDTH || imageY + Data.TILE_SIZE < 0 || imageY - Data.TILE_SIZE > FruitFever.LEVEL_HEIGHT);
		boolean playerOutOfBounds = imageY - Data.TILE_SIZE > FruitFever.LEVEL_HEIGHT;

		if (playerOutOfBounds) {
			ScreenHandler.adjustHearts(--lives);	
			respawn();
		}

		return playerOutOfBounds;
	}

	/** Checks for collisions with checkPoints, currency, vortex and other matter **/
	private void objectCollisions() {

		/** CheckPoint Collision **/
		for (Thing checkPoint : FruitFever.checkPoints) {
			checkPoint.boundaryLeft = 9;
			checkPoint.boundaryRight = -9;
			// Check if the player intersects the rod
			if (checkPoint.intersects(this)) {
				
				// Check to see if this checkpoint hasn't already been attained
				if (FruitFever.greenCheckPoint == null || !FruitFever.greenCheckPoint.equals(checkPoint)) {
				
					checkPoint.changeImage(Data.checkpointFlagGreen);
					FruitFever.greenCheckPoint = checkPoint;
					startX = checkPoint.imageX;
					startY = checkPoint.imageY + Data.TILE_SIZE;

					// Create seven fireworks once you reach a new checkpoint
					for(int i = 0; i < 7; i++)
						FruitFever.addToThings(new Animation(checkPoint.imageX - 17 + (int) (Math.random()*35),
						checkPoint.imageY - Data.TILE_SIZE*2 + (int) (Math.random()*35),
						Data.fireworkAnimation[(int) (Math.random()*3)], false, 2 + (int)(Math.random()*3),
						false, Animation.Type.FIREWORK, false, false, false, Layer.ABOVE_BLOCKS));
					
					break;
					
				}
			}
		}



		/** Changes all the flags to red flags except the current green flag **/
		for (Thing checkPoint : FruitFever.checkPoints) {
			if (checkPoint == FruitFever.greenCheckPoint) continue;
			checkPoint.changeImage(Data.checkpointFlagRed);
		}

		/** Reset Level if player touches vortex **/
		if (FruitFever.vortex != null && this.intersects(FruitFever.vortex))
			FruitFever.levelComplete = true;
		
	}

	/** Adjusts View to place the player in the middle of the screen 
	 *
	 * @Param levelRespawn: Used to indicate that the player was loaded into a level rather 
	 * 		  than already previously being in the level
	 *
	 * The levelRespawn variable is needed because loading the player in the level as opposed
	 * to changing the players position from within the level seems to have different effect
	 * (I assume this may have to do with the player's actual size of 3*TILE_SIZE)
	 *
	 */
	public void focusViewOnPlayer(int newPlayerXPos, int newPlayerYPos, boolean levelRespawn){

		// In case player is falling rapidly this will ensure that the view is not moving
		FruitFever.vy = 0;
		FruitFever.vx = 0;

		// Places the player exactly in the middle of the screen
		FruitFever.viewX = newPlayerXPos - (FruitFever.SCREEN_WIDTH/2) + (Data.TILE_SIZE/2);
		FruitFever.viewY = newPlayerYPos - (FruitFever.SCREEN_HEIGHT/2) + (Data.TILE_SIZE/2);

		// Adjust screen so that player cannot see outside view box
		FruitFever.viewY = Math.max(FruitFever.viewY, 0);
		FruitFever.viewY = Math.min(FruitFever.viewY, FruitFever.LEVEL_HEIGHT - FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE);

		FruitFever.viewX = Math.max(FruitFever.viewX, 0);
		FruitFever.viewX = Math.min(FruitFever.viewX, FruitFever.LEVEL_WIDTH - FruitFever.SCREEN_WIDTH + Data.TILE_SIZE);


 		// Fixes the loading player position bug 
		if (levelRespawn) {
			imageX -= Data.TILE_SIZE; 
			x -= Data.TILE_SIZE;
		}
			
		FruitFever.naturalAnimateAll();

	}

	/** These are all the settings that need to be reset when the player respawns **/
	public void respawn(){
		
		FruitFever.powerupAlarms.clear();

		horizontalVelocity = ORIGINAL_STARTING_HORIZONTAL_VELOCITY;
		startingJumpingVelocity = ORIGINAL_STARTING_JUMPING_VELOCITY;

		// Reset falling speed
		fallingVelocity = STARTING_FALLING_VELOCITY;
		fallingAcceleration = STARTING_FALLING_ACCELERATION;

		// Load player in the correct spot 
		imageX = startX;
		imageY = startY;			

		// Resetswirl
		swirl.resetState();

		// Focus view on the player
		focusViewOnPlayer(startX, startY, true);
		
		/** Reset health and energy bars **/
		ScreenHandler.resetEnergyBar();
		ScreenHandler.resetHealthBar();

	}

	public void shootSwirl(){

		// Makes sure you finish a cycle of images before starting a new one
		if (!images.equals(shootAnim) && !images.equals(shootAnimH))
			counter = -1;

		// Adjust Animation variable
		repeat = false;
		
		int fv =  fallingVelocity <= Player.STARTING_FALLING_VELOCITY ? 0 : (int) fallingVelocity;

		/** Check if there's a Block in front/in back of the player before he shoots **/
		if (facingRight) {

			Block westNorth = Block.getBlock(x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE/5 );
			Block westMiddle = Block.getBlock(x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/5) );
			Block westSouth = Block.getBlock(x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/5) + fv);

			FruitFever.point1.setLocation( x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE , y + Data.TILE_SIZE/5 );
			FruitFever.point2.setLocation( x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE , y + Data.TILE_SIZE - (Data.TILE_SIZE/5) );
			FruitFever.point3.setLocation( x + Data.TILE_SIZE + SWIRL_MOUTH_DISTANCE , y + Data.TILE_SIZE - (Data.TILE_SIZE/5) + fv );
		

			// If there is not Block in front of player
			if (westNorth == null && westSouth == null && westMiddle == null) {

				// Makes the swirl shoot out of the player from the left
				Swirl.reset = false;
				swirl.imageX = x + SWIRL_MOUTH_DISTANCE + FruitFever.viewX;
				swirl.imageY = y + FruitFever.viewY;
				swirl.xSpeed = Swirl.dx;

				// Set Right shooting animation
				images = shootAnim;
			
			// If there is a block in front of the player, don't do swirl animation
			} else
				setNewAnimation(stillAnim);

		// Facing left
		} else {

			Block eastNorth = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE/5);
			Block eastMiddle = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/5) );
			Block eastSouth = Block.getBlock(x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/5) + fv );

			FruitFever.point1.setLocation( x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE/5 );
			FruitFever.point2.setLocation( x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/5) );
			FruitFever.point3.setLocation( x - SWIRL_MOUTH_DISTANCE, y + Data.TILE_SIZE - (Data.TILE_SIZE/5) + fv );

			// If there is not Block in front of player
			if (eastSouth == null && eastNorth == null && eastMiddle == null) {

				// Makes the swirl shoot out of the player from the left
				Swirl.reset = false;
				swirl.imageX = x - SWIRL_MOUTH_DISTANCE + FruitFever.viewX;
				swirl.imageY = y + FruitFever.viewY;
				swirl.xSpeed = -Swirl.dx;

				// Set Left shooting animation
				images = shootAnimH;
				
			// If there is a block in front of the player, don't do swirl animation
			} else
				setNewAnimation(stillAnim);

		}
	}

	public void swirlTeleport() {

		imageX = swirl.imageX - Data.TILE_SIZE;
		imageY = swirl.imageY;

		// Makes sure the player cannot jump directly after teleportation
		resetJump();
		setKeepJumping(false);

		// Focuses the view on the player placing the player in the center of the screen
		focusViewOnPlayer(imageX, imageY, false); 

		swirl.resetState();
	
	}
	
	private void checkCollisionWithHintSigns() {
		for (int i = 0; i < FruitFever.hints.size(); i++) {
			if (this.intersects(FruitFever.hints.get(i)))
				if (!FruitFever.hints.get(i).visiting) {
					FruitFever.hints.get(i).setVisited();
					FruitFever.screenHandler.setHint(new TextAnimator(FruitFever.SCREEN_WIDTH/2, FruitFever.SCREEN_HEIGHT - 50, FruitFever.hints.get(i).hint, 20, Color.WHITE, 0.8, 5, 100, "center"));
				}
			else
				FruitFever.hints.get(i).visiting = false;
		}
	}

	// Overrides MovingAnimation.animate()
	@Override public void animate() {
		
		if (facingRight) {

			if (images.equals(stillAnimH))
				images = stillAnim;
			else if (images.equals(shootAnimH))
				images = shootAnim;
			else if (images.equals(tongueAnimH))
				images = tongueAnim;

		} else {

			if (images.equals(stillAnim))
				images = stillAnimH;
			else if (images.equals(shootAnim))
				images = shootAnimH;
			else if (images.equals(tongueAnim))
				images = tongueAnimH;
		}
		
		if (!active) {
			
			// Fixes Issue #79 which is when the 'e' key is held you eat things automatically, even after the animation is finished
			if (images.equals(tongueAnim) || images.equals(tongueAnimH))
				finishedTongueAnimation = true;
		
			// Adjust Animation variables
			repeat = true;
			
			// Switch animation images
			if (facingRight)
				setNewAnimation(stillAnim);
			else
				setNewAnimation(stillAnimH);
		}

		super.animate();

	}
	
	/** Update currently grabbed item or try to grab a item **/
	private void grabbingItem() {
		
		// If the player already has a grabbbed item
		if (grabbedItem != null) {
			
			// Reset item's position based on tongue's position
			grabbedItem.imageX = getTonguePosition().x - Data.TILE_SIZE/2;
			grabbedItem.imageY = getTonguePosition().y - Data.TILE_SIZE/2;
			grabbedItem.animate();
			
			// Remove item if animation has finished
			if (finishedTongueAnimation) {
				FruitFever.screen.remove(grabbedItem.image);

				for (int i = 0; i < FruitFever.edibleItems.size(); i++)

					if (FruitFever.edibleItems.get(i).equals(grabbedItem)) {
						
						// Grabs Fruit Ring
						if (FruitFever.edibleItems.get(i).type == Animation.Type.FRUIT_RING) {
							FruitFever.screenHandler.updateFruitRingDisplay(1);
							FruitFever.fruitRingCollectionSound.play();

						// Grabs Fruit
						} else if (FruitFever.edibleItems.get(i).type == Animation.Type.FRUIT) {

							currentEnergy = Math.min(currentEnergy + 300, maxEnergy);
							ScreenHandler.adjustEnergyBar(currentEnergy/maxEnergy);

						// Grabs JumpPowerUp
						} else if (FruitFever.edibleItems.get(i).type == Animation.Type.JUMP_POWERUP) {

							startJumpPowerup();
							try {
								FruitFever.powerupAlarms.add(new Alarm(300, Player.class.getMethod("stopJumpPowerup"), this));
							} catch (NoSuchMethodException e) {
								System.out.println("JumpPower alarm could not be created!");
								e.printStackTrace();
							}
						
						// Grabs speed powerup
						} else if (FruitFever.edibleItems.get(i).type == Animation.Type.SPEED_POWERUP) {

							startSpeedPowerup();
							try {
								FruitFever.powerupAlarms.add(new Alarm(300, Player.class.getMethod("stopSpeedPowerup"), this));
							} catch (NoSuchMethodException e) {
								System.out.println("SpeedPower alarm could not be created!");
								e.printStackTrace();
							}
						}

						FruitFever.things.remove(FruitFever.edibleItems.get(i));
						FruitFever.edibleItems.remove(i);

						break;
					}

				grabbedItem = null;
			}
		
		// Try grabbing item (only eats one at a time because of the break statement)
		} else if (!finishedTongueAnimation) {
			for (int i = 0; i < FruitFever.edibleItems.size(); i++)
				// Check tongue's intersection with the fruit and make it the grabbed fruit if it collides
				if (FruitFever.edibleItems.get(i).intersects(getTongueRectangle())) {
					grabbedItem = FruitFever.edibleItems.get(i);
					break;
				}
		
		}
	
	}

	/** Returns the current location of the tip of the tongue **/
	private Point getTonguePosition(){
	
		int currentTongueWidth = 0;
		
		switch (counter){
			case 3: currentTongueWidth = 8; break;
			case 4: currentTongueWidth = 20; break;
		}
		
		if (facingRight)
			return new Point(imageX + Data.TILE_SIZE*2 + currentTongueWidth, imageY + (int) image.getHeight()/2);
		else
			return new Point(imageX + Data.TILE_SIZE - currentTongueWidth, imageY + (int) image.getHeight()/2);
	}
	
	/** Returns the a Rectangle representing the tongue for collision detection **/
	private Rectangle getTongueRectangle(){
	
		int currentTongueWidth = 1;
		
		switch (counter){
			case 3: currentTongueWidth = 8; break;
			case 4: currentTongueWidth = 20; break;
		}
		
		// NOTE: The Data.TILE_SIZE of the tongue is 1 full tile for collision detection, whereas the real tongue is
		// only a few pixels tall.
		
		if (facingRight)
			return new Rectangle(x + Data.TILE_SIZE, y, currentTongueWidth, Data.TILE_SIZE);
		else
			return new Rectangle(x - currentTongueWidth, y, currentTongueWidth, Data.TILE_SIZE);
	}

	public void setKeepJumping(boolean keepJumping){ this.keepJumping = keepJumping; }
	public boolean onSurface(){ return onSurface; }
	public boolean isJumping(){	return isJumping; }
	public int getLives(){ return lives; }
	public void setMovementDirection(MovementDirection direction){ this.movementDirection = direction; }

	@Override public String toString(){
		return "ImageX: " + imageX + "   ImageY: " + imageY + "   X: " + x + "   Y: " + y ;
	}
	
	/** (Deprecated) 
	public void animateProjectiles() {
		
		for (Projectile obj : projectiles)
			obj.animate();
	
	} **/
	
	/** Shoots a fireball (Deprecated) 
	public void shootProjectile() {
		Projectile obj = new Projectile(imageX + Data.TILE_SIZE, imageY, facingRight ? Projectile.dx : -Projectile.dx);
		projectiles.add(obj);
		FruitFever.screen.add(obj.image);
		
	}
	**/
}

/** A swirl is a projectile shot from the player as a teleportation method  **/
class Swirl extends MovingAnimation {

	private static final long serialVersionUID = 1L;

	static final int energyRequired = 50;		
	static boolean reset = true;
	
	// Swirl's velocity
	static final byte dx = 7; // (Must remain an integer for collision detection to work)

	// This is the location of where the swirl is off screen when it is at rest
	private static final short SWIRL_X_REST_POS = -100;
	private static final short SWIRL_Y_REST_POS = -100;

	// These values are the actual image dimensions not the Data.TILE_SIZE Data.TILE_SIZE and Data.TILE_SIZE
	static final byte SWIRL_IMG_WIDTH = 14, SWIRL_IMG_HEIGHT = 14; 

	// Since the swirl is a circle the collision buffer makes collision much more accurate 
	static final byte AIR_SPACING = 6;

	public Swirl() {

		super(SWIRL_X_REST_POS, SWIRL_Y_REST_POS, Data.swirlAnimation, false, 0, true, 0, 0, Animation.Type.NOT_AVAILABLE);
		resetState();

	}
	
	@Override public void animate() {
	
		// If Swirl goes off screen or hits a block, destroy it
		if (imageX + SWIRL_IMG_WIDTH < 0 || imageX > FruitFever.LEVEL_WIDTH || collidesWithBlock())
			resetState();
	
		super.animate();
	
	}

	public void resetState() {	

		imageX = SWIRL_X_REST_POS;
		imageY = SWIRL_Y_REST_POS;

		// added since issue #121
		// x = SWIRL_X_REST_POS;
		// y = SWIRL_Y_REST_POS;

		xSpeed = 0;
		ySpeed = 0;

		reset = true;

	}

	/** Returns true or false depending on if the swirl has collided with a block **/
	public boolean collidesWithBlock(){

		Block westNorth = Block.getBlock(x + AIR_SPACING + (int) xSpeed, y + AIR_SPACING) ;
		if (westNorth != null) return true;

		Block eastNorth = Block.getBlock(x + SWIRL_IMG_WIDTH + AIR_SPACING + (int) xSpeed, y + AIR_SPACING) ;
		if (eastNorth != null) return true;

		Block westSouth = Block.getBlock(x + AIR_SPACING + (int) xSpeed, y + SWIRL_IMG_HEIGHT + AIR_SPACING) ;
		if (westSouth != null) return true;

		Block eastSouth = Block.getBlock(x + SWIRL_IMG_WIDTH + AIR_SPACING + (int) xSpeed, y + SWIRL_IMG_HEIGHT + AIR_SPACING);
		if (eastSouth != null) return true;

		return false;

	}

	@Override public String toString(){
		return "Swirl   X: " + x + "  Y: " + y + " ImageX: " + imageX + " ImageY: " + imageY;
	}
}















































