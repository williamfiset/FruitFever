
/**
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class BlueEnemy extends MovingAnimation{

	public BlueEnemy(int x, int y, int xSpeed, int ySpeed){
		super(x, y, Data.blueEnemyAnimation, true, 3, true, xSpeed, ySpeed);
	}
	
	// Overrides MovingAnimation.animate()
	@Override public void animate(){
		
		// Right here is where we can do things like checking to see if the enemy needs to switch directions or something
		
		super.animate();
		
	}

}