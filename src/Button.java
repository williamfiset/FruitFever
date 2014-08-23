/**
 *	Button - This class provides the functionality to have a clickable button, with different images for each mode.
 *
 *	@Author Micah Stairs
 *
 *	NOTE: It is assumed that all 3 button images have identical dimensions.
 **/

import acm.graphics.*;
import java.awt.*;

public class Button extends Thing {

	private static final long serialVersionUID = 1L;

	public GImage defaultImage, hoverImage, clickImage, inactiveImage;
	public Type type;
	public boolean active = true;
	
	public GImage bar; // Only used for SLIDER button
	public int level; // Only used for LEVEL_BOXES button
	
	public enum Type {
		
		/** Main Menu **/
		PLAY,
		CONTROLS,
		OPTIONS,
		MULTIPLAYER,
		
		/** Level Selection **/
		LEFT_ARROW,
		RIGHT_ARROW,
		LEVEL_BOXES,
		
		/* In-Game **/
		GEAR,
		RESTART,
		
		/** Pause Menu **/
		SOUND_EFFECTS,
		MUSIC,
		MAIN_MENU,
		LEVEL_SELECTION,
		RESUME,
		NEXT_LEVEL,
		SLIDER;
	};
	
	/** Designated constructor **/
	public Button(int x, int y, Type type, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage inactiveImg) {
	
		super(x, y, (int) defaultImg.getWidth(), (int) defaultImg.getHeight(), defaultImg);
		
		// Sets the position of the button images
		super.animate();
		
		this.type = type;
		this.defaultImage = defaultImg;
		this.hoverImage = hoverImg;
		this.clickImage = clickImg;
		this.inactiveImage = inactiveImg;
		 
	}
	
	/** Convenience constructor for buttons using an array for an array of 2-4 images **/
	public Button(int x, int y, Type type, GImage[] imgArray) {
		this(x, y, type, imgArray[0], imgArray[1], imgArray[imgArray.length > 2 ? 2 : 1], imgArray[imgArray.length > 3 ? 3 : 0]);
	}

	/** Convenience constructor to center buttons horizontally **/
	public Button(int y, Type type, GImage[] imgArray) {
		this((int) (FruitFever.SCREEN_WIDTH/2 - imgArray[0].getWidth()/2), y, type, imgArray);
	}
	
	/** Constructor used for LEVEL_BOXES button **/
	public Button(int x, int y, GImage[] imgArray, int level) {
		this(x, y, Type.LEVEL_BOXES, imgArray);
		this.level = level;
	}
	
	/** Constructor used for SLIDER button **/
	public Button(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar, double defaultValue) {
		this((int) (x + bar.getWidth()*defaultValue - Slider.CIRCLE_RADIUS) + FruitFever.viewX, y + FruitFever.viewY, Type.SLIDER, defaultImg, hoverImg, clickImg, defaultImg);
		this.bar = copyImage(bar);
		this.bar.setLocation(x, y);
	}
	
	/** Empty method to be overidden by Slider.slideButton() **/
	public void slideButton(int mouseXPos) { }

	/** Checks to see if the given coordinates are on the button **/
	public boolean checkOverlap(int xPos, int yPos) {
		return contains(xPos, yPos);
	}
	
	public void setDefault() {
		changeImage(defaultImage);
	}
	
	public void setHover() {
		changeImage(hoverImage);
	}
	
	public void setClick() {
		changeImage(clickImage);
	}

	public void activate() {
		active = true;
		changeImage(defaultImage);
	}

	public void deactivate() {
		active = false;
		changeImage(inactiveImage);
	}
	
}