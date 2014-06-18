/*
*
* @Author William Fiset, Micah Stairs
*
*/

import acm.graphics.*;
// import java.awt.*;

public class Slider extends Button {

	// Designated constructor
	public Slider(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar, double defaultValue) {
		super(x, y, defaultImg, hoverImg, clickImg, bar, defaultValue);
	}
	
	// Convenience constructor
	public Slider(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar) {
		this(x, y, defaultImg, hoverImg, clickImg, bar, 0.0);
	}

	/** Places the position of the button to where the mouse is (if allowed) **/
	public void slideButton(int mouseXPos) {
	
		imageX = (int) Math.max(bar.getX(), Math.min(bar.getX() + bar.getWidth(), mouseXPos + FruitFever.viewX));
		
		// Accounts for the center of the circle
		imageX -= (width/2);

		super.animate();
		
	}

	/** @return: a percentage from 0 - 1 on where the progression of the slider is **/
	public double getPercentage() {

		try {
			return (x - bar.getX() + (width/2))/bar.getWidth();
		} catch (ArithmeticException zeroDivision) {
			return 0.0;
		}
		
	}

}