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

public class FruitFever extends GraphicsProgram implements MouseMotionListener{

	final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500, MAIN_LOOP_SPEED = 30;
	static int LEVEL_WIDTH = 0, LEVEL_HEIGHT = 0;
	static String LEVEL_NAME = "";

	static Player player;

// Game Object Lists
	static ArrayList<Block> blocks;
	static ArrayList<Thing> things;
	static ArrayList<Animation> fruits;
	
// Menu/Level selection variables	
	static ArrayList<Button> mainMenuButtons = new ArrayList<Button>();
	static ArrayList<Button> levelSelectionButtons = new ArrayList<Button>();
	static ArrayList<Button> inGameButtons = new ArrayList<Button>();
	static ArrayList<Button> buttons = new ArrayList<Button>(); // Includes all buttons (even those in other array lists)
	static Button clickedOnButton = null;

	static GLabel[] levelNumbers = new GLabel[20];
	static GImage[] livesImages = new GImage[player.maxLives];
	
	// 0 = Loading Game, 1 = Main Menu, 2 = Level Selection, 3 = Playing, 4 = Controls, 5 = Options, 6 = Multi-player Playing
	static int currentScreen = 0;
	static int currentLevel = 1;
	static int levelSelectionPage = 0; // 0-based, just like the levels

	static Thing levelBackDrop;

// Player Variables

	static int playerStartX = 100, playerStartY= 100;
	static int dx = 0, dy = 0;

// Screen View Variables

	// This value seems to be optimal for screen view 	
	final static double viewBoxSpacing = 0.29;
	final static int LEFT_BOUNDARY = (int) (SCREEN_WIDTH * viewBoxSpacing);
	final static int RIGHT_BOUNDARY = (int) (SCREEN_WIDTH * (1.0 - viewBoxSpacing));
	final static int UP_BOUNDARY = (int) (SCREEN_HEIGHT * viewBoxSpacing);
	final static int DOWN_BOUNDARY = (int) (SCREEN_HEIGHT * (1.0 - viewBoxSpacing));
		
	static int viewX, viewY;
	static int vx; // Δ in viewX & viewY
	static double vy; 

	// Unnecessary now that the code is moved into the run method? What exactly is this overriding?
	@Override public void init() {}
	
/** Contains the main game loop **/
	@Override public void run(){
		
		postInit();
		
		/** TEMPORARY **/
		GRect leftRect = new GRect(LEFT_BOUNDARY, 0, 3, SCREEN_HEIGHT);
		GRect rightRect = new GRect(RIGHT_BOUNDARY, 0, 3, SCREEN_HEIGHT);
		GRect upRect = new GRect(0, UP_BOUNDARY, SCREEN_WIDTH, 3);
		GRect downRect = new GRect(0, DOWN_BOUNDARY, SCREEN_WIDTH, 3);
		leftRect.setFillColor(Color.RED);
		rightRect.setFillColor(Color.RED);
		upRect.setFillColor(Color.RED);
		downRect.setFillColor(Color.RED);
		leftRect.setFilled(true);
		rightRect.setFilled(true);
		downRect.setFilled(true);
		upRect.setFilled(true);
		/** TEMPORARY **/
		
		while(true){
			
			// Playing
			if(currentScreen == 3){

				/** Animate all objects (Scenery, Animation, MovingAnimation, Swirl, etc..)**/
				for(int i = 0; i < things.size(); i++)
					things.get(i).animate();
					
				/** Animate all fruit **/
				for(int i = 0; i < fruits.size(); i++)
					fruits.get(i).animate();

				/** Blocks **/
				for(Block obj : blocks)
					obj.animate();

				player.animate();
				player.motion();
				

				add(leftRect);
				add(rightRect);
				add(upRect);
				add(downRect);

			}
			
			pause(MAIN_LOOP_SPEED);
		}
		
	}

	private void drawMainMenu(){
		currentScreen = 1;
		removeAll();
		addToScreen(mainMenuButtons);
		levelSelectionPage = 0;
	}
	
	private void drawLevelSelection(){
		removeAll();
		add(levelBackDrop.image);
		addToScreen(levelSelectionButtons);
		
		for(int i = 0; i < levelNumbers.length; i++)
			add(levelNumbers[i]);
			
		blocks = new ArrayList<Block>();
		things = new ArrayList<Thing>();
		fruits = new ArrayList<Animation>();
	}

/** Loads and Displays all initial graphics of a level on the screen  **/
	private void loadLevel(){
		
		// Resets level

		// Empties Block HashMaps used for collision detection (So that they don't have blocks from the previous level in them)
		Block.resetBlockList();
		LEVEL_WIDTH = 0;
		LEVEL_HEIGHT = 0;
		viewX = 0;
		viewY = 0;
	
		
		// Clear the screen
		removeAll();

		addBackground();

		// Loads all Blocks and Things
		Data.loadObjects("../levels/levels.txt", currentLevel);

		findScreenDimensions();

		addImagesToScreen();

		
		// Loads the Hearts
		for(int i = 0; i < player.maxLives; i++){
			livesImages[i] = new GImage(Data.heartImage.getImage());
			livesImages[i].setLocation(i*Data.TILE_SIZE, 0);
			add(livesImages[i]);
		}
		
		addToScreen(inGameButtons);
	}

	private void addBackground(){

		// Creates a black background on the screen
		GRect background = new GRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		add(background);

	}

	/** 
	  * Sets the variables LEVEL_WIDTH & LEVEL_HEIGHT to the furthest blocks
	  * found horizontally and vertically
	  **/
	private void findScreenDimensions(){

		// Get Level Width and Height
		for (Block block : blocks ) {
			
			if (block.x > LEVEL_WIDTH)
				LEVEL_WIDTH = block.x;

			if (block.y > LEVEL_HEIGHT)
				LEVEL_HEIGHT = block.y;
		}
	}

	private void addImagesToScreen(){


		/** Adds all blocks, things and fruits to the screen **/
		
		for(Block obj : blocks){
			obj.image.setLocation(obj.getX(), obj.getY());
			add(obj.image);
		}
		
		for (Thing thing : things ) {
			thing.image.setLocation(thing.getX(), thing.getY());
			add(thing.image);
		}
		
		for (Thing fruit : fruits ) {
			fruit.image.setLocation(fruit.getX(), fruit.getY());
			add(fruit.image);
		}

		placePlayerOnScreen();

		/** TESTING PURPOSES ONLY **/
		// addToThings(new Animation(0, 100, Data.vortexAnimation, false, 2, true, -1));
		// addToThings(new Animation(0, 125, Data.fuzzyDiskAnimation, true, 2, true, -1));
		/** **/
		

	}

	// ** Creates and correctly places player on screen **/
	private void placePlayerOnScreen(){
		
		// Creates the Player class
		player = new Player(playerStartX, playerStartY, Data.playerStill, Data.playerStillH, Data.playerShoot, Data.playerShootH, Data.playerTongue, Data.playerTongueH);

		add(player.image);

		// Place player somewhat in the middle of the screen
		viewX = playerStartX - SCREEN_WIDTH/2;
		viewY = playerStartY - SCREEN_HEIGHT/2;

		// Adjust screen so that player cannot see outside view box
		if (viewY < 0) viewY = 0;
		if (viewX < 0) viewX = 0;
		
		if (viewY > LEVEL_HEIGHT - SCREEN_HEIGHT + Data.TILE_SIZE)
			viewY = LEVEL_HEIGHT - SCREEN_HEIGHT + Data.TILE_SIZE;
		
		if (viewX > LEVEL_WIDTH - SCREEN_WIDTH + Data.TILE_SIZE) 
			viewX = LEVEL_WIDTH - SCREEN_WIDTH + Data.TILE_SIZE;

		// Still bewildered by why this works...
		player.imageX -= Data.TILE_SIZE; 
		player.x -= Data.TILE_SIZE;

	}
	
	public void addToScreen(ArrayList<Button> arr){
		for(int i = 0; i < arr.size(); i++)
			add(arr.get(i).image);
	}
	
	
	public void checkAndSetClick(ArrayList<Button> arr, MouseEvent mouse){
		for(Button obj : arr)
			// Check to see if the mouse is on the button
			if(obj.checkOverlap(mouse.getX(), mouse.getY())){
				// Make the image appear to be clicked on
				obj.setClick();
				clickedOnButton = obj;
			}
	}
	
	public void shiftLevelLabel(int shift){
	
		for(int i = 0; i < 20; i++){
			levelNumbers[i].setLabel(String.valueOf(Integer.valueOf(levelNumbers[i].getLabel()) + shift));
			levelNumbers[i].setLocation((int) (SCREEN_WIDTH/2 - levelNumbers[i].getWidth()/2 - 90 + (i%4)*60), 132 + (i/4)*55);
		}
			
	}
	
	public void addToThings(Thing obj){
	
		things.add(obj);
		obj.image.setLocation(obj.x - viewX, obj.y - viewY);
		add(obj.image);
		
	}
	
	private void postInit(){
	
		// Loading screen
		GLabel loadingText = new GLabel("Loading...");
		loadingText.setLocation(SCREEN_WIDTH/2 - (int)loadingText.getWidth()/2, SCREEN_HEIGHT/2);
		add(loadingText);
		
		// Set up keyboard and mouse
		addMouseListeners();
		addKeyListeners();
		
		// Renders Images in the Data class, and fills the object Arrays^
		Data.loadImages();

		drawMainMenu();
	
	}
	
	@Override public void keyPressed(KeyEvent key){

		int keyCode = key.getKeyCode();

		// JUMP
		if(keyCode == KeyEvent.VK_W)
			player.setIsJumping(true);

		// Tongue Attack
		else if (keyCode == KeyEvent.VK_S)
			player.tongueAttack();

		// Shoot Swirl
		else if (keyCode == KeyEvent.VK_SPACE){

			if(player.facingRight)
				addToThings(new MovingAnimation(player.x + 15 + viewX, player.y + 5 + viewY, Data.swirlAnimation, false, 0, true, 10, 0, 1));
			else
				addToThings(new MovingAnimation(player.x - 15 + viewX, player.y + 5 + viewY, Data.swirlAnimation, false, 0, true, -10, 0, 1));
			player.shootSwirl();
	

		// Movement LEFT
		}else if (keyCode == KeyEvent.VK_A) {

			player.facingRight = false; 
			dx = -1;
		
		// Movement RIGHT
		}else if (keyCode == KeyEvent.VK_D) {
			
			dx = 1;
			player.facingRight = true;
			
		}
	}
	
	@Override public void keyReleased(KeyEvent key){
		
		int keyCode = key.getKeyCode();

		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_A) {
			dx = 0;
			player.dx = 0;
			vx = 0;
			vy = 0;
		}
	}
	
	@Override public void mouseMoved(MouseEvent mouse) {
	
		/** Check to see if the mouse is hovering over any images and sets them accordingly **/
		
		for (int i = 0; i < buttons.size(); i++ ) {
			
			Button obj = buttons.get(i);

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
	
		if(currentScreen == 1)
			checkAndSetClick(mainMenuButtons, mouse);
		else if(currentScreen == 2)
			checkAndSetClick(levelSelectionButtons, mouse);
		else if(currentScreen == 3)
			checkAndSetClick(inGameButtons, mouse);
			
	}
	
	@Override public void mouseReleased(MouseEvent mouse) {
	
		/** Temporary code to jump to the main menu **/
		if(mouse.getX() < 25 && mouse.getY() < 25){
			drawMainMenu();
			return;
		}
			
		if(clickedOnButton != null){
		
			/** Unclick the button image and preform action (if applicable) **/
			if(clickedOnButton.contains(mouse.getX(), mouse.getY())){
				clickedOnButton.setHover();
				
				// Play button
				if(clickedOnButton.type == 0){
					
					currentScreen = 2;
					drawLevelSelection();
					
				}
				
				// Level left arrow button
				else if(clickedOnButton.type == 4){
					
					if(levelSelectionPage > 0){
						levelSelectionPage--;
						shiftLevelLabel(-20);
					}
					
				}
				
				// Level right arrow button
				else if(clickedOnButton.type == 5){
					
					if(levelSelectionPage < 4){
						levelSelectionPage++;
						shiftLevelLabel(20);
					}
					
				}
				
				// Level button
				else if(clickedOnButton.type == 6){

					currentLevel = clickedOnButton.level + levelSelectionPage*20;
					loadLevel();
					currentScreen = 3;
					
				}				

			}
			else clickedOnButton.setDefault();
		}
		clickedOnButton = null;
	}

}