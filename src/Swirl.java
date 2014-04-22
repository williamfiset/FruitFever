
/**
 *	Swirl - This class demonstrates how MovingAnimation should be used to create moving objects.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Swirl extends MovingAnimation{

	public Swirl(int x, int y, int xSpeed, int ySpeed){
		super(x, y, Data.swirlAnimation, false, 1, true, xSpeed, ySpeed);
	}
	
	// Overrides MovingAnimation.animate()
	public void animate(){
		
		// Right here is where we can do things like checking to see if the swirl has hit a Block, and acting accordingly
		
		super.animate();
		
	}

}