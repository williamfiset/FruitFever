/**
 *	Animation - This class takes in an array of GImages and animates a GImage through it.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Animation extends Thing {

	/** Instance variables **/
	protected int counter = 0;
	public Type type;

	protected GImage[] images;
	protected boolean counterGoingUp = true; 

	private int delayCounter, delay;
	
	protected boolean reverse, repeat;
	
	public enum Type {
		NOT_AVAILABLE,
		FRUIT_RING,
		ENEMY,
		FRUIT,
		FIREWORK;
	};
	
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
	 
	public Animation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat, Type type){
		this(x, y, originalImages, reverse, delay, repeat, type, false);	
	}

	public Animation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat, Type type, boolean randomStartingFrame){
	
		super(x, y);
		
		// Randomizes which frame the animation starts on
		// (makes things such as fruit or fruit rings look better when they are clustered together)
		if(randomStartingFrame)
			counter = (int) (Math.random()*(originalImages.length - 1));

		// Make copy of images
		this.images = originalImages;
		
		// Set these instance variables, now that we know the image
		super.image = new GImage(images[counter].getImage());
		super.setSize((int) image.getWidth(), (int) image.getHeight());
		
		this.reverse = reverse;
		this.delay = delay;
		this.repeat = repeat;
		
		this.type = type;

	}

	public Animation(int x, int y, GImage[] originalImages){
		this(x, y, originalImages, false, 1, true, Type.NOT_AVAILABLE);

		// Make copy of images
		this.images = originalImages;
		
		// Set these instance variables, now that we know the image
		super.image = new GImage(images[counter].getImage());
		super.setSize((int) image.getWidth(), (int) image.getHeight());
	}

	public void animate(){
	
		if(active){
			
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
				if (counter == images.length - 1)
					counterGoingUp = false;
				else if (counter == 0) {
					if (!repeat)
						active = false;
					else
						counterGoingUp = true;
				}
			} else if (counter == images.length)  {
				if (!repeat)
					active = false;
				counter = 0;
			}
			
			// Switch the GImage to the correct index, and adjust the width and height of the Rectangle
			changeImage(images[counter]);
			setSize((int) image.getWidth(), (int) image.getHeight());
		
		}
		
		// Calls Thing.animate()
		super.animate();
	
	}
}