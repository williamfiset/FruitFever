
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

	static final int VERTICAL_VELOCITY = 1, HORIZONTAL_VELOCITY = 1;
	static int verticalVelocity = VERTICAL_VELOCITY, horizontalVelocity = HORIZONTAL_VELOCITY;
	int dy = 0, dx = 0;
	int yLine, xLine;

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

		// The intersects overlaps by 1px
		// checkCollisionDetection();


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

		// if dx >= 1 check if block in front

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
