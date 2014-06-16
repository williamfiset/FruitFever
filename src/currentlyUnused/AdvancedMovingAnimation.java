
/**
 *	Enemy - This class extends MovingAnimation.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class AdvancedMovingAnimation extends MovingAnimation{

	/** Public instance variables **/
	public int[] xPos, yPos;
	public int currentDestination, currentAnimation = 0;
	public double dx, dy;
	public GImage[][] animations;

	public AdvancedMovingAnimation(int[] xPos, int[] yPos, GImage[][] originalImages, boolean reverse, int delay, boolean repeat, double dx, double dy, Type type){

		super(xPos[0], yPos[0], originalImages[0], reverse, delay, repeat, type);
		
		this.xPos = xPos;
		this.yPos = yPos;
		this.dx = dx;
		this.dy = dy;
		this.animations = originalImages;
		
		// Current destination will be 0 only when there's one point in the entire path
		currentDestination = 1 % xPos.length;
		
		setXSpeed();
		setYSpeed();
		
	}
	
	// Overrides MovingAnimation.animate()
	@Override public void animate(){
		
		if (imageX == xPos[currentDestination] && imageY == yPos[currentDestination]){
		
			// Adjust destination
			currentDestination = (++currentDestination % xPos.length);
			
			// Adjust animation
			currentAnimation = (currentAnimation + 1) % animations.length;
			images = animations[currentAnimation];
		}
		
		setXSpeed();
		setYSpeed();
		
		// Calls MovingAnimation.animate()
		super.animate();
		
	}
	
	public void setXSpeed(){
		
		if (imageX == xPos[currentDestination])
			xSpeed = 0;
		else if (imageX < xPos[currentDestination])
			xSpeed = dx;
		else
			xSpeed = -dx;
		
	}
	
	public void setYSpeed(){
		
		if (imageY == yPos[currentDestination])
			ySpeed = 0;
		else if (imageY < yPos[currentDestination])
			ySpeed = dy;
		else
			ySpeed = -dy;
		
	}

}
