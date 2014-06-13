import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import acm.graphics.*;
import acm.program.*;

public class TextAnimator {

	/** Public Instance Variables **/
	public GLabel label;
	public boolean active;
	
	/** Private Instance Variables **/
	private int x, y, xSpeed, ySpeed, maxOpacity, opacity, fadeSpeed, waitTime;
	private Color color;
	private String align;
		
	public TextAnimator(int x, int y, int xSpeed, int ySpeed, String text, int textSize, Color color, double opacity, int fadeSpeed, int waitTime, String align){
	
		label = new GLabel(text);
		label.setFont(new Font("Dialog", Font.BOLD, textSize));
		this.x = x;
		this.y = y;	
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.color = color;
		this.opacity = Math.min((int) (opacity*255), 255);
		this.maxOpacity = this.opacity;
		this.fadeSpeed = fadeSpeed;
		this.waitTime = waitTime;
		this.align = align;
		
		setColorAndPosition();
		
		active = true;
	}
	
	
	// Constructor for centered text with no movement
	public TextAnimator(int x, int y, String text, int textSize, Color color, double opacity, int fadeSpeed, int waitTime, String align){
		this(x, y, 0, 0, text, textSize, color, opacity, fadeSpeed, waitTime, "center");
	}
	
	public void animate(){
	
		if (waitTime > 0)
			waitTime--;
		else {
			opacity -= fadeSpeed;
			
			if (opacity <= 0) {
				active = false;
				opacity = 0;
			}	
		}
		
		x += xSpeed;
		y += ySpeed;
		
		setColorAndPosition();
	}
	
	private void setColorAndPosition() {
		
		label.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255*((double)opacity/(double)maxOpacity))));
		
		if (align == "left")
			label.setLocation(x, y);
		else if (align == "center")
			label.setLocation(x - (int) (label.getWidth()/2), y);
		else if (align == "right")
			label.setLocation(x - (int) label.getWidth(), y);
		else
			System.out.println("TextAnimator -> Unknown text align");
	
	}
}