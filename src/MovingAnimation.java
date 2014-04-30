
/**
 *	MovingAnimation - This class extends Animation, allowing basic moving functionality.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class MovingAnimation extends Animation{

	/** Public instance variables **/
	public int xSpeed, ySpeed;

	public MovingAnimation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat, int xSpeed, int ySpeed, int type){
		super(x, y, originalImages, reverse, delay, repeat, type);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public MovingAnimation(int x, int y, GImage[] originalImages, boolean reverse, int delay, boolean repeat, int type){
		super(x, y, originalImages, reverse, delay, repeat, type);
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
		imageX += xChange;
		imageY += yChange;
	}

}