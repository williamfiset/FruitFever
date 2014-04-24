/**
 *	Thing - This is the superclass for Animation, Scenery, and most other classes which draw an image on-screen.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Thing extends Rectangle{

	/** Public instance variables **/
	public GImage image;
	
	/** Constructors that define the image **/
	
	public Thing(int x, int y, int width, int height, GImage image){
		
		super(x, y, width, height);
		
		this.image = image;
	}
	
	public Thing(int x, int y, GImage image){
		this(x, y, (int) image.getWidth(), (int) image.getHeight(), image);
	}
	
	/** Constructors that do not define the image **/
	
	public Thing(int x, int y, int width, int height){
	
		super(x, y, width, height);
		
		this.image = image;
	}
	
	public Thing(int x, int y){
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE);	
	}

	/** Update position **/
	public void animate(){
	
		image.setLocation(x - FruitFever.viewX, y  - FruitFever.viewY);
	
	}

}