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
	int imageX, imageY;
	
	// These are no yet fully implemented, but they will be used to alter the underlyingRectangle's boundaries,
	// without adjusting the size of the image's position (this will add a lot of flexibility to our game and 
	// will be extremely useful, esepcially for the Player class).
	public int boundaryLeft, boundaryRight, boundaryTop, boundaryBottom;
	
	/** Constructors that define the image **/
	
	public Thing(int x, int y, int width, int height, GImage image){
		super(x, y, width, height);
		this.image = image;
		this.imageX = x;
		this.imageY = y;
	}
	
	public Thing(int x, int y, GImage image){
		this(x, y, (int) image.getWidth(), (int) image.getHeight(), image);
	}
	
	/** Constructors that do not define the image **/
	
	public Thing(int x, int y, int width, int height){
		super(x, y, width, height);
		this.image = image;
		this.imageX = x;
		this.imageY = y;
	}
	
	public Thing(int x, int y){
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE);	
	}

	/** Update position **/
	public void animate(){

		image.setLocation(imageX - FruitFever.viewX, imageY  - FruitFever.viewY);		
		
		setLocation(imageX + boundaryLeft - FruitFever.viewX, imageY + boundaryTop - FruitFever.viewY);
		setSize((int)image.getWidth() - boundaryLeft + boundaryRight, (int)image.getHeight() - boundaryTop + boundaryBottom);
		
	}
	
	/** Change image **/
	public void changeImage(GImage img){
		image.setImage(img.getImage());
	}

}