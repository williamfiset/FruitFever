
/**
 *	MovingAnimation - This class extends Animation, allowing basic moving functionality.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class MovingAnimation extends Animation{

	/** Private instance variables **/
	public int xSpeed, ySpeed;

	public MovingAnimation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat, int xSpeed, int ySpeed){
		super(x, y, originalImages, reverse, delay, repeat);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public MovingAnimation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat){
		super(x, y, originalImages, reverse, delay, repeat);
	}
	
	// Overrides Animation.animate()
	@Override public void animate(){
	
		// Move the image
		moveIt(xSpeed, ySpeed);
		super.animate();
		
		// Move the uderlying Rectangle
		setLocation(x, y);
		
	}

	public void moveIt(int xChange, int yChange){
		x += xChange;
		y += yChange;
	}

}