
/**
 *	Enemy - This class extends MovingAnimation.
 *
 * @Author Micah Stairs
 *
 **/

import java.awt.*;
import acm.graphics.*;

public class Enemy extends AdvancedMovingAnimation {

	public Rectangle healthBar, healthBarBackground;
	public double currentHealth, maxHealth;
	private final int BAR_HEIGHT = 10, BAR_WIDTH = 30;

	public Enemy(int[] xPos, int[] yPos, GImage[][] originalImages, boolean reverse, int delay, boolean repeat, int dx, int dy) {
	
		super(xPos, yPos, originalImages, reverse, delay, repeat, dx, dy);
		
		maxHealth = 100;
		currentHealth = maxHealth;
		healthBar = new Rectangle(xPos[0] + 1, yPos[0] + 1, BAR_WIDTH - 2, BAR_HEIGHT - 2);
		healthBarBackground = new Rectangle(xPos[0], yPos[0], BAR_WIDTH, BAR_HEIGHT);
		
	}
	
	// Overrides AdvancedMovingAnimation.animate()
	@Override public void animate() {
		
		// Calls AdvancedMovingAnimation.animate()
		super.animate();
		
		// Re-position and re-calculate health bar
		healthBar.setLocation(imageX - FruitFever.viewX + 1, imageY - FruitFever.viewY + 1);
		healthBar.setSize((int) ((currentHealth/maxHealth)*(BAR_WIDTH - 2)), BAR_HEIGHT);
		healthBarBackground.setLocation(imageX - FruitFever.viewX, imageY - FruitFever.viewY);
		
	}

}