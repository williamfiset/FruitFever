
/**
 *
 *	Button - This class provides the functionality to have a clickable button, with different images for each mode.
 *
 *	@Author Micah Stairs
 *
 *	NOTE: It is assumed that all 3 button images have identical dimensions.
 *
 **/

import acm.graphics.*;
import java.awt.*;

public class Button extends Thing {

	public GImage defaultImage, hoverImage, clickImage;
	public Type type;
	public int level; // level is only used for button type 6
	
	public enum Type {
		PLAY,
		CONTROLS,
		OPTIONS,
		MULTIPLAYER,
		LEFT_ARROW,
		RIGHT_ARROW,
		LEVEL_BOXES,
		GEAR,
		REFRESH;
	};

	public Button(int x, int y, Type type, GImage defaultImg, GImage hoverImg, GImage clickImg){
	
		super(x, y, (int) defaultImg.getWidth(), (int) defaultImg.getHeight(), new GImage(defaultImg.getImage()));
		
		// Sets the position of the button images
		animate();
		
		this.type = type;
		this.defaultImage = new GImage(defaultImg.getImage());
		this.hoverImage = new GImage(hoverImg.getImage());
		this.clickImage = new GImage(clickImg.getImage());
	
	}
	
	/** Used for button type 5 **/
	public Button(int x, int y, Type type, GImage defaultImg, GImage hoverImg, GImage clickImg, int level){
		this(x, y, type, defaultImg, hoverImg, clickImg);
		this.level = level;
	}

/** Checks to see if the given coordinates are on the button **/
	public boolean checkOverlap(int xPos, int yPos){
		return contains(xPos, yPos);
	}
	
	public void setDefault(){
		image.setImage(defaultImage.getImage());
	}
	
	public void setHover(){
		image.setImage(hoverImage.getImage());
	}
	
	public void setClick(){
		image.setImage(clickImage.getImage());
	}
	
}