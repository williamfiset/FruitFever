
/**
 *	Enemy - This class extends MovingAnimation.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Enemy extends AdvancedMovingAnimation{

	public Enemy(int[] xPos, int[] yPos, GImage[][] originalImages, boolean reverse, int delay, boolean repeat, int dx, int dy){
	
		super(xPos, yPos, originalImages, reverse, delay, repeat, dx, dy);
		
	}
	
	// Overrides AdvancedMovingAnimation.animate()
	@Override public void animate(){
		
		// Calls AdvancedMovingAnimation.animate()
		super.animate();
		
	}

}