
/**
 * @Author William Fiset 
 * This class controls all the actions and collisions of the main character
 *
 **/

import acm.graphics.*;
import acm.program.*;
import java.util.*;

class Player extends MovingAnimation {
	
	static int lives = 3, maxLives = 3;	

	static int verticalVelocity = 1, horizontalVelocity = 1;
	int dy = 0, dx = 0;

	// Variables concerning jumping
	boolean isJumping = false;
	boolean reachedBaseLine = true;
	int baseLine, maxJump;

	// The constructor will eventually look something like:
	// public Player(int x, int y, GImage[] stillAnimation, GImage[] swirlAnimation, GImage[] tongueAnimation)
	public Player(int x, int y, GImage[] temporaryImage){
		super(x, y, temporaryImage, false, 1, true);
		
		/** Will: This is how you would set the boundaries of the image (the image would remain in the same location)
		boundaryLeft = -5; boundaryRight = 12; boundaryTop = 18; boundaryBottom = -3;
		**/
		
		baseLine = y;
	}

	// Has not been implemented yet, just the skeleton 
	public void motion(){

		checkCollisionDetection();

		if (isJumping) {
			// move up

			if (imageY - dy <= baseLine) {
				reachedBaseLine = true;
			}

		}else{
			if (!reachedBaseLine) {
				// move down
			}
		}

		imageX += dx;
		imageY += dy;

	}

	/** Responds accordingly to collision detection (INCOMPLETE) **/
	private void checkCollisionDetection(){

		// IMPORTANT: MAKE COLLISION DETECTIONS EASY WITH: Rectangle methods, contains() and , intersects()

		// Contains all the surrounding blocks in a list, without doubly accounted blocks
		HashSet<Block> surroundingBlocks = new HashSet<Block>();

		// Gets all the blocks that are surrounding the Player
		surroundingBlocks.add( Block.getBlock(imageX  - width/3, imageY - width/3 ) ); // NorthWest 
		surroundingBlocks.add( Block.getBlock(imageX  + width/3, imageY - width/3 ) ); // North 
		surroundingBlocks.add( Block.getBlock(imageX  + width + width/3, imageY - width/3 ) ); // NorthEast 

		surroundingBlocks.add(Block.getBlock(imageX  + width + width/3, imageY + height/2)); // East
		surroundingBlocks.add(Block.getBlock(imageX  - width/3, imageY + height/3)); // West

		surroundingBlocks.add(Block.getBlock( imageX - width/3, imageY + height + height/3 )); // SouthWest
		surroundingBlocks.add(Block.getBlock( imageX + width/3 , imageY + height + height/3 )); // South
		surroundingBlocks.add(Block.getBlock( imageX + width + width/3, imageY + height + height/3)); // SouthEast


		// remove the singular null reference (due to air blocks)
		surroundingBlocks.remove(null); 

		// System.out.println(surroundingBlocks.size());

		// Verify the surrounding blocks for a collision
		for (Block block : surroundingBlocks) {
			if (block.intersects(this)) {
				
				if (block.imageX < imageY || block.imageY > imageY + height)
					dy = 0;
				else if (block.imageX < imageY || block.imageX > imageX + width)
					dx = 0;

			}
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
