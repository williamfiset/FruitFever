
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
	static final int VERTICAL_VELOCITY = 4, HORIZONTAL_VELOCITY = 3;
	int dy = 0, dx = 0;

// Variables concerning Gravity

	final double STARTING_FALLING_VELOCITY = 0;
	final double STARTING_FALLING_ACCELERATION = 0;

	double fallingVelocity = STARTING_FALLING_VELOCITY;
	static boolean onPlatform = false;
	static boolean gravity = true;
	double fallingAcceleration = STARTING_FALLING_ACCELERATION;
	final double changeInAcceleration = 0.01;


// Variables concerning jumping
	boolean isJumping = false;
	boolean reachedBaseLine = true;
	int maxJump;


// The distance from a corner of the image used in collision detection
	final int VERTICAL_PX_BUFFER = 2;
	final int HORIZONTAL_PX_BUFFER = 3;


	// The constructor will eventually look something like:
	// public Player(int x, int y, GImage[] stillAnimation, GImage[] swirlAnimation, GImage[] tongueAnimation)
	public Player(int x, int y, GImage[] temporaryImage){
		super(x, y, temporaryImage, false, 1, true);
		
		/** Will: This is how you would set the boundaries of the image (the image would remain in the same location)
		boundaryLeft = -5; boundaryRight = 12; boundaryTop = 18; boundaryBottom = -3;
		**/

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

		// System.out.println(onPlatform);

		// Gravity Effect triggered here
		if (!isJumping && gravity && !onPlatform) {

			imageY += fallingVelocity;

			// Acceleration effect
			fallingVelocity += fallingAcceleration;
			fallingAcceleration += changeInAcceleration;

		// Not falling/not allowed to fall
		}else{
			// reset falling speed
			fallingVelocity = STARTING_FALLING_VELOCITY;
			fallingAcceleration = STARTING_FALLING_ACCELERATION;
		}

	}

	/** Responds accordingly to collision detection **/
	private void checkCollisionDetection(){

		// IMPORTANT: MAKE COLLISION DETECTIONS EASY WITH: Rectangle methods, contains() and , intersects()

		/** Grabs the block(s) in the direction of motion of the character and checks
		 * if there is a collision between the player and the selected block(s) (if any) **/

		// EAST
		if (FruitFever.dx == 1) {

			Block northEast = Block.getBlock(x + width + HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block southEast = Block.getBlock(x + width + HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			// No block in front of player
			if (southEast == null && northEast == null){
				dx = HORIZONTAL_VELOCITY;
			}else{
				dx = 0;
			}
			
		// WEST
		} else if (FruitFever.dx == -1) {
			Block northWest = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + VERTICAL_PX_BUFFER);
			Block southWest = Block.getBlock(x - HORIZONTAL_PX_BUFFER, y + height - VERTICAL_PX_BUFFER);

			// No block in back of player
			if (northWest == null && southWest == null)	dx = -HORIZONTAL_VELOCITY;
			else dx = 0;

		}


		// SOUTH
		if (FruitFever.dy == 1) {

			Block southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);
			Block southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);

			if (southWest == null && southEast == null){
				dy = VERTICAL_VELOCITY;
				onPlatform = false;
			}else{
				onPlatform = true;
				dy = 0;
			} 

		// NORTH
		}else if (FruitFever.dy == -1) {
			
			Block northWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y - VERTICAL_PX_BUFFER);
			Block northEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y - VERTICAL_PX_BUFFER);

			if (northWest == null && northEast == null){
				dy = -VERTICAL_VELOCITY;
				onPlatform = false;
			}
			// Just hit the bottom of block
			else dy = 0;
		
		// No vertical movement
		}else if (FruitFever.dy == 0) {
			
			// checks if player is still on platform
			Block southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);
			Block southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);

			if (southEast == null && southWest == null){
				onPlatform = false;	
			}
		}

		// checks if player is in free fall
		Block southWest = Block.getBlock(x + HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);
		Block southEast = Block.getBlock(x + width - HORIZONTAL_PX_BUFFER, y + height + VERTICAL_PX_BUFFER);

		if (southEast != null || southWest != null){
			onPlatform = true;	
		}

	}

	public void toungueAttack(){}

	public void shootSwirl(){}


	/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly **/	
	public static void adjustLives(int changeInLives){
	
		lives += changeInLives;
		
		for(int i = 0; i < maxLives; i++){
			if(i < lives)
				FruitFever.livesImages[i].setVisible(true);
			else
				FruitFever.livesImages[i].setVisible(false);
		}
	

	}

	@Override public String toString(){
		return "Player: (Image: " + imageX + ", " + imageY + "   W: " + image.getWidth() + ", H: " + image.getHeight() + ") (Bounding Box: " + x + ", " + y + "   W: " + width + ", H: " + height + ")"; 
	}

}





















