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
	private int x, y, xSpeed, ySpeed, maxOpacity, opacity, fadeSpeed;
	private Color color;
	private String align;
		
	public TextAnimator(int x, int y, int xSpeed, int ySpeed, String text, int textSize, Color color, int opacity, int fadeSpeed, String align){
	
		label = new GLabel(text);
		label.setFont(new Font("Dialog", Font.BOLD, textSize));
		this.x = x;
		this.y = y;	
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.color = color;
		this.opacity = opacity;
		this.maxOpacity = opacity;
		this.fadeSpeed = fadeSpeed;
		this.align = align;
		
		setColorAndPosition();
		
		active = true;
	}
	
	
	// Constructor for centered text with no movement
	public TextAnimator(int x, int y, String text, int textSize, Color color, int opacity, int fadeSpeed, String align){
	
		this(x, y, 0, 0, text, textSize, color, opacity, fadeSpeed, "center");
	}
	
	public void animate(){
		opacity -= fadeSpeed;
		
		if(opacity <= 0){
			active = false;
			opacity = 0;
		}	
		
		x += xSpeed;
		y += ySpeed;
		
		setColorAndPosition();
	}
	
	private void setColorAndPosition(){
		
		label.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255*((double)opacity/(double)maxOpacity))));
		
		if(align == "left")
			label.setLocation(x, y);
		else if(align == "center")
			label.setLocation(x - (int) (label.getWidth()/2), y);
		else
			label.setLocation(x - (int) label.getWidth(), y);
	
	}
}