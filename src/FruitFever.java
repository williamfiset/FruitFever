/**
 * FruitFever - The main class of the game.
 *
 * @Author William Fiset, Micah Stairs
 *
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class FruitFever extends GraphicsProgram {

	protected final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500, MAIN_LOOP_SPEED = 30;

	public static Block[] blocks;
	public static ArrayList<Thing> things = new ArrayList<Thing>();
	
	public static int currentLevel = 1, lives = 3, maxLives = 3;
	public static GImage[] livesImages = new GImage[maxLives]; 
	
	public static int viewX = 0, viewY = 0;
	
	@Override public void init() {
		
		// Set size
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// Renders Images in the Data class
		Data.loadImages();
		
		// Allow user to select level
		
		// Load level
		loadLevel();

		// Set up keyboard and mouse
		addMouseListeners();
		addKeyListeners();

	}
	
/** Contains the main game loop **/
	@Override public void run(){
		
		while(true){
			
			/** Animate all objects (Scenery, Animation, MovingAnimation, Swirl, etc..)**/
			for(Thing obj : things)
				obj.animate();
			
			/** Blocks **/
			for(int i = 0; i < blocks.length; i++)
				blocks[i].image.setLocation(blocks[i].x - viewX, blocks[i].y - viewY);
			
			pause(MAIN_LOOP_SPEED);
		}
		
	}
	
	@Override public void keyPressed(KeyEvent key){}
	
	@Override public void keyReleased(KeyEvent key){}
	
	@Override public void mousePressed(MouseEvent mouse) { }
		

/** Loads and Displays all initial graphics of a level on the screen  **/
	private void loadLevel(){

		// Creates a black background on the screen
		GRect background = new GRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		add(background);

		// Loads all Blocks and Things
		Data.loadObjects("../levels/levels.txt", currentLevel);
		
		// Displays all blocks on-screen
		for(int i = 0; i < blocks.length; i++){
			blocks[i].image.setLocation(blocks[i].x, blocks[i].y);
			add(blocks[i].image);
		}
		
		
		/** TESTING PURPOSES ONLY **/
		things.add(new Animation(50, 50, Data.berryAnimation, true, 3, true));
		things.add(new Animation(150, 75, Data.berryAnimation, true, 2, false));
		things.add(new Animation(250, 50, Data.swirlAnimation, false, 1, true));
		
		things.add(new MovingAnimation(350, 50, Data.swirlAnimation, false, 1, false, 10, 5));
		things.add(new Swirl(250, 50, 10, 5));
		things.add(new BlueEnemy(175, 50, 0, 0));
		
		/** **/
		
		for(Thing obj : things){
			obj.image.setLocation(obj.x, obj.y);
			add(obj.image);
		}
		
		for(int i = 0; i < maxLives; i++){
			livesImages[i] = new GImage(Data.heartImage.getImage());
			livesImages[i].setLocation(i*Data.TILE_SIZE, 0);
			add(livesImages[i]);
		}
			
	}
	
/** Adjusts the amount of lives that the player has, and redraws the hearts accordingly **/	
	public static void adjustLives(int changeInLives){
	
		lives += changeInLives;
		
		for(int i = 0; i < maxLives; i++){
			if(i < lives)
				livesImages[i].setVisible(true);
			else
				livesImages[i].setVisible(false);
		}
	
	}

}
