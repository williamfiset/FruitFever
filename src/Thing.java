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
	public int x, y;
	public GImage image;
	
	/** Constructors that define the image **/
	
	public Thing(int x, int y, int width, int height, GImage image){
		
		super(x, y, width, height);
		
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	public Thing(int x, int y, GImage image){
		this(x, y, (int) image.getWidth(), (int) image.getHeight(), image);
	}
	
	/** Constructors that do not define the image **/
	
	public Thing(int x, int y, int width, int height){
	
		super(x, y, width, height);
		
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	public Thing(int x, int y){
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE);	
	}
	
	/** Set bounding box (does not adjust the image, only the underlying Rectangle **/
	
	public void offsetAllBounds(int left, int right, int top, int down){
		offsetLeftBounds(left);
		offsetRightBounds(right);
		offsetTopBounds(top);
		offsetBottomBounds(down);
	}
	
	public void offsetLeftBounds(int offset){
		setBounds(x + offset, y, width - offset, y);
	}
	
	public void offsetRightBounds(int offset){
		setBounds(x, y, width + offset, y);
	}
	
	public void offsetTopBounds(int offset){
		setBounds(x, y + offset, width, y - offset);
	}
	
	public void offsetBottomBounds(int offset){
		setBounds(x, y, width, y + offset);
	}
	
	/** Update position **/
	public void animate(){
	
		image.setLocation(x - FruitFever.viewX, y  - FruitFever.viewY);
	
	}

}