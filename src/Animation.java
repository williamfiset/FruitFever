/**
 *	Animation - This class takes in an array of GImages and animates a GImage through it.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Animation extends Thing {

	private static final long serialVersionUID = 1L;

	/** Instance variables **/
	protected int counter = 0;
	public Type type;

	public GImage[] images;
	protected boolean counterGoingUp = true; 

	private int delayCounter, delay;
	
	protected boolean reverse, repeat;
	
	public enum Type {

		JUMP_POWERUP, SPEED_POWERUP,

		NOT_AVAILABLE,
		FRUIT_RING,
		ENEMY,
		FRUIT,
		FIREWORK,

		SPIKES, GAS_BUBBLES, LAVA;
		
	};
	
	public static final Type[] powerupTypes = new Type[] { Type.JUMP_POWERUP, Type.SPEED_POWERUP };
	
	/**
	 * @param x : default x-position
	 * @param y : default y-position
	 * @param originalImages : array of images for the animation
	 * @param reverse (optional - default is false):
	 * 	-If true, the images go in order: 0, 1, ...images.length-2, images.length-1, images.length-2, ...1, 0
	 *	-If false, the images go in order: 0, 1, ...images.length-1 (and then it will begin at 0 again on next cycle)
	 * @param delay (optional - default is 1): the higher this value is, the slower the image will animate
	 * @param repeat (optional - default is true):
	 *	-If true, the animation is played over and over
	 *	-If false, the animation is played once
	 **/
	

	// Designated constructor 
	public Animation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat, Type type, boolean randomStartingFrame, boolean canFall, boolean canBeCrushed, Layer layer) {
	
		super(x, y);

		this.canFall = canFall;
		this.canBeCrushed = canBeCrushed;
		this.layer = layer;
		
		// Randomizes which frame the animation starts on
		// (makes things such as fruit or fruit rings look better when they are clustered together)
		if (randomStartingFrame)
			counter = (int) (Math.random()*(originalImages.length - 1));

		// Make copy of images
		this.images = originalImages;
		
		// Set these instance variables, now that we know the image
		super.image = copyImage(images[counter]);
		super.setSize((int) image.getWidth(), (int) image.getHeight());
		
		this.reverse = reverse;
		this.delay = delay;
		this.repeat = repeat;
		
		this.type = type;

	}

	// Convenience constructor 
	public Animation (int x, int y, GImage originalImages[], Type type, boolean canFall, boolean canBeCrushed, Layer layer) {
		this (x, y, originalImages, false, 1, true, type, false, canFall, canBeCrushed, layer);

		// Make copy of images
		this.images = originalImages;

		// Set these instance variables, now that we know the image
		super.image = copyImage(images[counter]);
		super.setSize((int) image.getWidth(), (int) image.getHeight());
	}

	// Convenience constructor 
	public Animation(int x, int y, GImage[] originalImages) {
		this(x, y, originalImages, Type.NOT_AVAILABLE, false, false, Layer.BELOW_BLOCKS);
	}

	public void animate() {
	
		if (active) {
			
			// Break out of this method if it's not time to change the image yet

			delayCounter++;

			if (delayCounter < delay) {
				super.animate();
				return;
			} else
				delayCounter = 0;
			
			// Adjust the counter in the correct direction
			if (counterGoingUp)
				counter++;
			else
				counter--;
			
			/** Determine if the counter needs to switch directions **/
			if (reverse) {
				if (counter >= images.length - 1)
					counterGoingUp = false;
				else if (counter <= 0) {
					if (!repeat)
						active = false;
					else
						counterGoingUp = true;
				}
			} else if (counter >= images.length) {
				if (!repeat)
					active = false;
				counter = 0;
			}
			
			// Switch the GImage to the correct index, and adjust the width and height of the Rectangle
			// Fixed issue #51
			try {
				changeImage(images[counter]);
			} catch (ArrayIndexOutOfBoundsException e) {
				counter = 0;
				counterGoingUp = true;
				changeImage(images[counter]);
			}
			
			setSize((int) image.getWidth(), (int) image.getHeight());
		
		}
		
		// Calls Thing.animate()
		super.animate();
	
	}
	
	/** NOTE: This may result in a resized image, potentially causing issues **/
	@Override public void makeInvisible() {
		images = new GImage[] { Data.invisibleImage };
	}

	public void setNewAnimation(GImage[] newImages) {
		images = newImages;
		counter = -1;
		counterGoingUp = true;
		active = true;
	}

}