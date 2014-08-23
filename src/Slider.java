/**
 *	Slider - This class extends Button and provides a functionality to have a slider value.
 *
 *	@Author Will Fiset, Micah Stairs
 *
 **/

import acm.graphics.*;

public class Slider extends Button {

	private static final long serialVersionUID = 1L;
	
	public static final int CIRCLE_DIAMETER = 17, CIRCLE_RADIUS = 8, CIRCLE_OFFSET = 13;

	/** Designated constructor **/
	public Slider(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar, double defaultValue) {
		super(x, y, defaultImg, hoverImg, clickImg, bar, defaultValue);
	}
	
	/** Convenience constructor **/
	public Slider(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar) {
		this(x, y, defaultImg, hoverImg, clickImg, bar, 0.0);
	}

	/** Places the position of the button to where the mouse is (if allowed) **/
	public void slideButton(int mouseXPos) {
	
		imageX = (int) Math.max(bar.getX(), Math.min(bar.getX() + bar.getWidth(), mouseXPos)) + FruitFever.viewX;
		
		// Accounts for the center of the circle
		imageX -= 13;

		super.animate();
		
	}

	/** @return: a percentage from 0 - 1 representing the value of the slider **/
	public double getPercentage() {

		try {
			return (x - bar.getX() + CIRCLE_OFFSET)/bar.getWidth();
		}
		catch (ArithmeticException zeroDivision) {
			return 0.0;
		}
		
	}

}