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

public class FruitFever extends GraphicsProgram implements MouseMotionListener {

	final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500, MAIN_LOOP_SPEED = 30;


	static ArrayList<Block> blocks = new ArrayList<Block>();
	static ArrayList<Thing> things = new ArrayList<Thing>();
	static ArrayList<Button> buttons = new ArrayList<Button>();
	static Player player;
	static Button clickedOnButton = null;
	
	// 0 = Loading Game, 1 = Main Menu, 2 = Level Selection, 3 = Playing, 4 = Controls, 5 = Options, 6 = Multiplayer Playing
	static int currentScreen = 1;
	
	static int viewX = 0, viewY = 0;
	static int currentLevel = 0;

	static int playerStartX = 100, playerStartY= 100;

	static GImage[] livesImages = new GImage[player.maxLives];
	
	@Override public void init() {
		
		// Set up keyboard and mouse
		addMouseListeners();
		addKeyListeners();
		
		// Set size
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// Renders Images in the Data class, and fills the object Arrays^
		Data.loadImages();

		// Adds main menu buttons to the ArrayList
		for(int i = 0; i < 4; i++)
			buttons.add(new Button((int) (FruitFever.SCREEN_WIDTH/2 - Data.menuImages[i/3].getWidth()/2), 100 + 75*i, i, Data.menuImages[3*i], Data.menuImages[3*i + 1], Data.menuImages[3*i + 2]));
		
		drawMainMenu();

	}
	
/** Contains the main game loop **/
	@Override public void run(){
		
		while(true){
			
			// Playing
			if(currentScreen == 3){
			
				/** Animate all objects (Scenery, Animation, MovingAnimation, Swirl, etc..)**/
				for(Thing obj : things)
					obj.animate();
				
				/** Blocks **/
				for(Block obj : blocks)
					obj.image.setLocation(obj.getX() - viewX, obj.getY() - viewY);
					
				player.motion();

			}
			
			pause(MAIN_LOOP_SPEED);
		}
		
	}

	/** TESTING PURPOSES ONLY **/
	public static void draw(Thing thing){add(thing.image);}
	public static void draw(GImage img){add(img);}
	public static void draw(GRect rect){add(rect);}
	
	@Override public void keyPressed(KeyEvent key){

		int keyCode = key.getKeyCode();

		switch (keyCode) {
			case KeyEvent.VK_A: case KeyEvent.VK_LEFT: player.dx = -Player.horizontalVelocity; break;
			case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: player.dx =  Player.horizontalVelocity; break;
			case KeyEvent.VK_W: case KeyEvent.VK_UP: player.dy = -Player.verticalVelocity; break;
			case KeyEvent.VK_S: case KeyEvent.VK_DOWN: player.dy =  Player.verticalVelocity; break;
		}
			

	}
	
	@Override public void keyReleased(KeyEvent key){}
	
	@Override public void mouseMoved(MouseEvent mouse) {
	
		/** Check to see if the mouse is hovering over any images and sets them accordingly **/
		for(Button obj : buttons){
		
			if(obj.equals(clickedOnButton))
				continue;
				
			if(obj.checkOverlap(mouse.getX(), mouse.getY()))
				obj.setHover();
			else
				obj.setDefault();
				
		}		
	
	}
	@Override public void mouseDragged(MouseEvent mouse) {
	
		/** Check to see if the mouse is on the selected button or not and sets the image accordingly **/			
		if(clickedOnButton != null){
			if(clickedOnButton.checkOverlap(mouse.getX(), mouse.getY()))
				clickedOnButton.setClick();
			else
				clickedOnButton.setDefault();
		}
	}
	
	@Override public void mousePressed(MouseEvent mouse) {
	
		clickedOnButton = null;
	
		for(Button obj : buttons){
			
			/** Main Menu buttons **/
			if(currentScreen == 1){
				
				// Check to see if the mouse is on the button
				if(obj.checkOverlap(mouse.getX(), mouse.getY())){
					
					// Make the image appear to be clicked on
					obj.setClick();
					clickedOnButton = obj;
					
				}
			}
		
		}		
		
	}
	
	@Override public void mouseReleased(MouseEvent mouse) {
	
		if(clickedOnButton != null){
		
			/** Unclick the button image and preform action (if applicable) **/
			if(clickedOnButton.contains(mouse.getX(), mouse.getY())){
				clickedOnButton.setHover();
				
				// Play button
				if(clickedOnButton.type == 0){
				
					// Load level
					loadLevel();
					currentScreen = 3;
				}
			}
			else clickedOnButton.setDefault();
		}
		
		clickedOnButton = null;
		
	}
		
	private void drawMainMenu(){
		
		// Clear the screen
		removeAll();
		
		for(Button obj : buttons)
			add(obj.image);
		
	}

/** Loads and Displays all initial graphics of a level on the screen  **/
	private void loadLevel(){
		
		// Clear the screen
		removeAll();

		// Creates a black background on the screen
		GRect background = new GRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		add(background);

		// Loads all Blocks and Things
		Data.loadObjects("../levels/levels.txt", currentLevel);


		// Displays all blocks on-screen
		for(Block obj : blocks){
			obj.image.setLocation(obj.getX(), obj.getY());
			add(obj.image);
		}
		
		// Creates the Player class
		player = new Player(playerStartX, playerStartY, Data.playerTemp) ;
		
		/** TESTING PURPOSES ONLY **/
		things.add(new Animation(50, 50, Data.berryAnimation, true, 3, true));
		things.add(new Animation(150, 75, Data.berryAnimation, true, 2, false));
		things.add(new Animation(250, 50, Data.swirlAnimation, false, 1, true));
		things.add(new MovingAnimation(350, 50, Data.swirlAnimation, false, 1, false, 10, 5));
		things.add(new Swirl(250, 50, 10, 5));
		things.add(new BlueEnemy(175, 50, 0, 0));
		things.add(player);
		/** **/


		// Draws initial objects on the screen
		for(Thing obj : things){
			obj.image.setLocation(obj.getX(), obj.getY());
			add(obj.image);
		}
		
		// Loads the Hearts
		for(int i = 0; i < player.maxLives; i++){
			livesImages[i] = new GImage(Data.heartImage.getImage());
			livesImages[i].setLocation(i*Data.TILE_SIZE, 0);
			add(livesImages[i]);
		}

			
	}
}
