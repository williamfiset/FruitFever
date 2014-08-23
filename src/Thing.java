/**
 *	Thing - This is our superclass for nearly every object in the game, allows an Image to be drawn in our world.
 *
 * @Author Micah Stairs
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Thing extends Rectangle {

	private static final long serialVersionUID = 1L;

	/** Public instance variables **/
	public GImage image;
	public int imageX, imageY;
	public boolean active = true;
	
	public static enum Layer {
		BELOW_BLOCKS,
		BLOCKS,
		ABOVE_BLOCKS;
	}

	public Layer layer;

	public boolean canFall = false;

	public boolean canBeCrushed = false;

	// Used to alter the underlying Rectangle's boundaries, without adjusting the size of the images or its position
	public int boundaryLeft, boundaryRight, boundaryTop, boundaryBottom;
	
	/** Constructors that define the image **/
	
	public Thing(int x, int y, int width, int height, GImage image) {
		
		super(x, y, width, height);
		this.image = copyImage(image);
		this.imageX = x;
		this.imageY = y;
		
		// This call is important for images that do not move (ex: images that are part of a GUI)
		this.animate();
	}
	
	public Thing(int x, int y, GImage image) {
		this(x, y, (int) image.getWidth(), (int) image.getHeight(), image);
	}
	
	public Thing(int x, int y, GImage image, boolean mirrorRandomly) {
		this(x, y, (int) image.getWidth(), (int) image.getHeight(), mirrorRandomly ? ImageTransformer.mirrorRandomly(image) : image);
	}

	public Thing(int x, int y, int width, int height, GImage image, boolean canFall, boolean canBeCrushed, Layer layer) {
		this(x, y, width, height, image);
		this.canFall = canFall;
		this.canBeCrushed = canBeCrushed;
		this.layer = layer;
	}

	public Thing(int x, int y, GImage image, boolean canFall, boolean canBeCrushed, Layer layer) {
		this(x, y, (int) image.getWidth(), (int) image.getHeight(), image, canFall, canBeCrushed, layer);
	}
	
	/** Constructors that do not define the image **/ 
	
	public Thing(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.imageX = x;
		this.imageY = y;
	}
	
	public Thing(int x, int y) {
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE);	
	}

	/** Update image **/
	public void animate() {
		
		/** Moves image to be on screen only if it needs to be on the screen
		  * The '+ Data.TILE_SIZE' prevents the bug of the tiles accumulating at the bottom of the screen **/
		if (x < FruitFever.SCREEN_WIDTH && y < FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE) 
			image.setLocation(imageX - FruitFever.viewX, imageY  - FruitFever.viewY);		
		
		updateBoundingBox();
		
	}

	/** This is the old .animate() method, we still need it to correctly position blocks and things
	  * in some bug situations **/
	public final void naturalAnimate() {

		// Places image at the correct position on the screen
		image.setLocation(imageX - FruitFever.viewX, imageY  - FruitFever.viewY);		

		updateBoundingBox();

	}
	
	/** Updates the size and the position of the bounding box **/
	private final void updateBoundingBox() {
		
		// Changes the size of bounding box
		setSize((int)image.getWidth() - boundaryLeft + boundaryRight, (int)image.getHeight() - boundaryTop + boundaryBottom);
		
		// Changes the position of the bounding box responsible for collision detection 
		setLocation(imageX + boundaryLeft - FruitFever.viewX, imageY + boundaryTop - FruitFever.viewY);
		
	}
	
	/** Change image **/
	public void changeImage(GImage img) {
		image.setImage(img.getImage());
	}
	
	/** Adjusts all boundary variables **/
	public void adjustBoundaries(int left, int right, int top, int bottom) {
		boundaryLeft = left;
		boundaryRight = right;
		boundaryTop = top;
		boundaryBottom = bottom;
	}
	
	public void makeInvisible() {
		changeImage(Data.invisibleImage);
	}

	public static GImage copyImage(GImage img) {
		return new GImage(img.getImage());
	}

}