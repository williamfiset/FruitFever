/*
*
* @Author William Fiset, Micah Stairs
*
*/

import acm.graphics.*;
// import java.awt.*;

public class Slider extends Button {

	// Designated constructor
	public Slider(int x, int y, GImage defaultImg, GImage hoverImg, GImage clickImg, GImage bar) {
		super(x, y, defaultImg, hoverImg, clickImg, bar);
	}

	/** Places the position of the button to where the mouse is (if allowed) **/
	public void slideButton(int mouseXPos) {

		// New mouseXPos is on the slider
		if (mouseXPos >= bar.getX() && mouseXPos <= bar.getX() + bar.getWidth()) {

			imageX = mouseXPos - (width/2) + FruitFever.viewX;
			super.animate();
		}
		
	}

	/** @return: a percentage from 0 - 1 on where the progression of the slider is **/
	public double getPercentage() {

		try {
			return (x - bar.getX())/bar.getWidth();
		} catch (ArithmeticException zeroDivision) {
			return 0.0;
		}
		
	}

}