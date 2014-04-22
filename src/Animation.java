
/**
 *	Animation - This nifty class takes in an array of GImages and animates a GImage through it.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Animation {

	public int x, y;
	private GImage[] images;
	public GImage currentImage;
	private int counter = 0, delayCounter = 0, delay;
	private boolean reverse, counterGoingUp = true;

	public Animation(int x, int y, GImage[] originalImages, boolean reverse, int delay){

		this.x = x;
		this.y = y;
		
		// Make copy of images
		this.images = new GImage[originalImages.length];
		for(int i = 0; i < images.length; i++)
			this.images[i] = new GImage(originalImages[i].getImage());
		
		this.reverse = reverse;
		this.delay = delay;
		
		// Gives it a starting image so that it doesn't raise a NullPointerException
		currentImage = new GImage(images[counter].getImage());
		
	}
	
	public void animate(){
		
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
		
		// Determine if the counter needs to switch directions
		if(reverse){
			if(counter == images.length - 1)
				counterGoingUp = false;
			else if(counter == 0)
				counterGoingUp = true;
		}
		else if (counter == images.length)
			counter = 0;
		
		// Switch the GImage to the correct index
		currentImage.setImage(images[counter].getImage());
	
	}

}