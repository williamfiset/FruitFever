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

	public GImage defaultImage, hoverImage, clickImage;
	public Type type;
	public boolean active = true;
	
	public GImage bar; // Only used for SLIDER button
	public int level; // Only used for LEVEL_BOXES button
	
	public enum Type {
		PLAY,
		CONTROLS,
		OPTIONS,
		MULTIPLAYER,
		LEFT_ARROW,
		RIGHT_ARROW,
		LEVEL_BOXES,
		GEAR,
		REFRESH,
		SOUND_EFFECTS,
		MUSIC,
		
		SLIDER;
	};
	
	/** Designated constructor **/
	public Button(int x, int y, Type type, GImage defaultImg, GImage hoverImg, GImage clickImg) {
	
		super(x, y, (int) defaultImg.getWidth(), (int) defaultImg.getHeight(), defaultImg);
		
		// Sets the position of the button images
		super.animate();
		
		this.type = type;
		this.defaultImage = defaultImg;
		this.hoverImage = hoverImg;
		this.clickImage = clickImg;
	
	}
	
	/** Constructor used for LEVEL_BOXES button **/
	public Button(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, int level) {
		this(x, y, Type.LEVEL_BOXES, defaultImg, hoverImg, clickImg);
		this.level = level;
	}
	
	/** Constructor used for SLIDER button **/
	public Button(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar, double defaultValue) {
		this((int) (x + bar.getWidth()*defaultValue - Slider.CIRCLE_RADIUS), y, Type.SLIDER, defaultImg, hoverImg, clickImg);
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
	
}