/**
 *	Thing - This is the superclass for Animation, Scenery, and most other classes which draw an image on-screen.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Thing {

	/** Public instance variables **/
	public int x, y;
	public GImage image;
	
	public Thing(int x, int y, GImage image){
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	public Thing(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	// Update position
	public void animate(){
	
		image.setLocation(x - FruitFever.viewX, y  - FruitFever.viewY);
	
	}

}