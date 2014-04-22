
/**
 *	Animation - This nifty class takes in an array of GImages and animates a GImage through it.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Animation {

	/** Public instance variables **/
	public int x, y;
	public GImage currentImage;
	public boolean doneAnimating = false;
	
	/** Private instance variables **/
	private GImage[] images;
	private int counter = 0, delayCounter = 0, delay;
	private boolean reverse, counterGoingUp = true, repeat;
	
	
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
	 
	public Animation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat){

		this.x = x;
		this.y = y;
		
		// Make copy of images
		this.images = new GImage[originalImages.length];
		for(int i = 0; i < images.length; i++)
			this.images[i] = new GImage(originalImages[i].getImage());
		
		this.reverse = reverse;
		this.delay = delay;
		this.repeat = repeat;
		
		// Gives it a starting image so that it doesn't raise a NullPointerException
		currentImage = new GImage(images[counter].getImage());
		
	}
	
	public Animation(int x, int y, GImage[] originalImages){
		this(x, y, originalImages, false, 1, true);
	}
	
	public void animate(){
	
		if(doneAnimating)
			return;
		
		// Break out of this method if it's not time to change the image yet
		if(++delayCounter < delay)
			return;
		else
			delayCounter = 0;
		
		// Adjust the counter in the correct direction
		if(counterGoingUp)
			counter++;
		else
			counter--;
		
		/** Determine if the counter needs to switch directions **/
		if(reverse){
			if(counter == images.length - 1){
				if(!repeat)
					doneAnimating = true;
				else
					counterGoingUp = false;
			}
			else if(counter == 0){
				if(!repeat)
					doneAnimating = true;
				else
					counterGoingUp = true;
			}
		}
		else if (counter == images.length){
			if(!repeat)
				doneAnimating = true;
			counter = 0;
		}
		
		// Switch the GImage to the correct index
		currentImage.setImage(images[counter].getImage());
	
	}

}