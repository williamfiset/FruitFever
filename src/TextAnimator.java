import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import acm.graphics.*;
import acm.program.*;

public class TextAnimator {

	public GLabel label;
	public int x, y, xSpeed, ySpeed, opacity, fadeSpeed;
	public boolean active;
	public static Font font = new Font("Dialog", Font.BOLD, 30);
		
	public TextAnimator(int x, int y, int xSpeed, int ySpeed, String text, Color color, int opacity, int fadeSpeed){
		label = new GLabel(text);
		label.setColor(color);
		label.setFont(font);
		label.setLocation(x - (int)(label.getWidth()/2), y);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.opacity = opacity;
		this.fadeSpeed = fadeSpeed;
		active = true;
	}
	
	public void animate(){
		opacity -= fadeSpeed;
		
		x += xSpeed;
		y += ySpeed;
		
		if(opacity <= 0)
			active = false;
	}
}