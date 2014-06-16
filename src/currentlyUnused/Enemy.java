
/**
 *	Enemy - This class extends MovingAnimation.
 *
 * @Author Micah Stairs
 *
 **/

import java.awt.*;
import acm.graphics.*;

public class Enemy extends AdvancedMovingAnimation {

	public GRect healthBar, healthBarBackground;
	public double currentHealth, maxHealth;
	private final int VERTICAL_SPACING = 10, BAR_HEIGHT = 3, BAR_WIDTH = 15;

	public Enemy(int[] xPos, int[] yPos, GImage[][] originalImages, boolean reverse, int delay, boolean repeat, double dx, double dy) {
	
		super(xPos, yPos, originalImages, reverse, delay, repeat, dx, dy, Animation.Type.ENEMY);
		
		maxHealth = 1000;
		currentHealth = maxHealth;
		
		healthBarBackground = new GRect(xPos[0] + (width - BAR_WIDTH)/2, yPos[0] - VERTICAL_SPACING, BAR_WIDTH, BAR_HEIGHT);
		healthBarBackground.setFilled(true);
		healthBarBackground.setColor(Color.red.darker());
		
		healthBar = new GRect(xPos[0] + (width + BAR_WIDTH)/2, yPos[0] - VERTICAL_SPACING, BAR_WIDTH, BAR_HEIGHT);
		healthBar.setFilled(true);
		healthBar.setColor(Color.green.darker());
		
	}
	
	// Overrides AdvancedMovingAnimation.animate()
	@Override public void animate() {
		
		// Calls AdvancedMovingAnimation.animate()
		super.animate();
		
		/** Temporary **/
		currentHealth--;
		if(currentHealth <= 0)
			active = false;
		/** **/
		
		/** Re-position and re-calculate health bar **/
		healthBar.setLocation(imageX - FruitFever.viewX + (width - BAR_WIDTH)/2, imageY - FruitFever.viewY - VERTICAL_SPACING);
		healthBar.setSize((int) ((currentHealth/maxHealth)*BAR_WIDTH), BAR_HEIGHT);
		healthBarBackground.setLocation(imageX - FruitFever.viewX + (width - BAR_WIDTH)/2, imageY - FruitFever.viewY- VERTICAL_SPACING);
		
	}

}
