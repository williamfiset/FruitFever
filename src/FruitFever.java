/**
 * FruitFever - The main class of the game.
 *
 * @Author William Fiset, Micah Stairs
 *
 */

// CurrentScreen: -1 = N/A, 0 = Loading Game, 1 = Main Menu, 2 = Level Selection, 3 = Playing, 4 = Controls, 5 = Options, 6 = Multi-player Playing

import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;



public class FruitFever extends GraphicsProgram implements MouseMotionListener{

/** Constants **/

	static GraphicsProgram screen;
	final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500, MAIN_LOOP_SPEED = 30;

/** Level Information/Objects/Lists **/
	
	static int LEVEL_WIDTH, LEVEL_HEIGHT;
	static String LEVEL_NAME;
	
	static Animation vortex, grabbedItem;
	static Thing greenCheckPoint;
	static boolean levelComplete;

	static ArrayList<Block> blocks;
	static ArrayList<Thing> things, dangerousThings, checkPoints;
	static ArrayList<Animation> edibleItems;
	static ArrayList<TextAnimator> texts;

/** Player **/
	static Player player;
	static int playerStartX, playerStartY, dx;
	static boolean swirlButtonReleased = true, tongueButtonReleased = true;
	
/** Menus/GUI **/
	
	// Defines ScreenMode constants	
	private enum ScreenMode{
		NOT_AVAILABLE,
		LOADING_GAME,
		MAIN_MENU,
		LEVEL_SELECTION,
		PLAYING,
		CONTROLS,
		OPTIONS,
		MULTIPLAYER;
	};

	static ArrayList<Button> mainMenuButtons = new ArrayList<Button>();
	static ArrayList<Button> levelSelectionButtons = new ArrayList<Button>();
	static ArrayList<Button> inGameButtons = new ArrayList<Button>();
	static ArrayList<Button> buttons = new ArrayList<Button>(); // Includes all buttons (even those in other array lists)
	static Button clickedOnButton = null;
	
	static GLabel[] levelNumbers = new GLabel[20];
	static GImage[] livesImages = new GImage[Player.MAX_LIVES];
	
	
	static int currentLevel = 1, levelSelectionPage = 0;
	static ScreenMode currentScreen;

	static Thing levelBackDrop;

	static ArrayList<Alarm>	alarms = new ArrayList<Alarm>();
	
/** Screen View Variables **/

	// This value seems to be optimal for screen view 	
	final static double viewBoxSpacing = 0.29;
	final static int LEFT_BOUNDARY = (int) (SCREEN_WIDTH * viewBoxSpacing);
	final static int RIGHT_BOUNDARY = (int) (SCREEN_WIDTH * (1.0 - viewBoxSpacing));
	final static int UP_BOUNDARY = (int) (SCREEN_HEIGHT * viewBoxSpacing);
	final static int DOWN_BOUNDARY = (int) (SCREEN_HEIGHT * (1.0 - viewBoxSpacing));
		
	static int viewX, viewY;
	static int vx; // Δ in viewX & viewY
	static double vy; 

/** Natural Disaster Variables **/

	static int earthQuakeMagnitude = 6;

/** TEMPORARY COLLISION DETECTION **/
	static GRect point1;
	static GRect point2;



	@Override public void init() {
		screen = this;
	}
	
	/** Contains the main game loop **/
	@Override public void run(){
		
		postInit();
		
		/** TEMPORARY **/

		// GRect leftRect = new GRect(LEFT_BOUNDARY, 0, 3, SCREEN_HEIGHT);
		// GRect rightRect = new GRect(RIGHT_BOUNDARY, 0, 3, SCREEN_HEIGHT);
		// GRect upRect = new GRect(0, UP_BOUNDARY, SCREEN_WIDTH, 3);
		// GRect downRect = new GRect(0, DOWN_BOUNDARY, SCREEN_WIDTH, 3); 
		// GRect centerRect = new GRect(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 3, 3);
		point1 = new GRect(0,0,2,3);
		point2 = new GRect(0,0,2,3);

		// leftRect.setFillColor(Color.RED);
		// rightRect.setFillColor(Color.RED);
		// upRect.setFillColor(Color.RED);
		// downRect.setFillColor(Color.RED);
		// centerRect.setFillColor(Color.RED);
		point1.setFillColor(Color.RED);
		point2.setFillColor(Color.RED);

		// leftRect.setFilled(true);
		// rightRect.setFilled(true);
		// downRect.setFilled(true);
		// upRect.setFilled(true);
		// centerRect.setFilled(true);
		point1.setFilled(true);
		point2.setFilled(true);
		/** TEMPORARY **/
		

		while(true){
		
			Timer_ loopTimer = new Timer_();
		
			// Countdown all of the alarms towards execution
			for (int i = 0; i < alarms.size(); i++) {
					if (!alarms.get(i).active) {
						alarms.remove(i);
						i--;
					} else alarms.get(i).execute();
				}
				
			// Playing
			if(currentScreen == ScreenMode.PLAYING){
			
				// Controls if it is time to return to the level selection menu
				if(levelComplete || player.getLives() <= 0){
					drawLevelSelection();
					levelComplete = false;
					continue;
				}
			
				animateText();
				
				// Activates EarthQuake Effect				
				// earthQuakeEffect();

				// Tests for falling blocks
				// Block.updateFallingBlocksByNaturalDisaster();
				Block.updateFallingBlocksWithPlayerPosition(player.imageX, player.y, player.onSurface());

				/** Animate all objects (Scenery, Animation, MovingAnimation, Swirl, etc..) **/
				for (int i = 0; i < things.size(); i++) {
					if (!things.get(i).active) {
						remove(things.get(i).image);
						things.remove(i);
						i--;
					} else things.get(i).animate();
				}
					
				/** Animate all edible items **/
				for (Thing item : edibleItems)
					item.animate();

				Block.drawBlocks();


				player.jump();
				player.motion();
				player.animate();
				
				add(point1);
				add(point2);
				// add(leftRect);
				// add(rightRect);
				// add(upRect);
				// add(downRect);
				// add(centerRect);

			}

			double loopTime = loopTimer.stop();

			try{
				pause(Math.max(0, MAIN_LOOP_SPEED - loopTime*1000));
			}catch(IllegalArgumentException exception){
				pause(MAIN_LOOP_SPEED);	
				System.out.println("MAIN_LOOP_SPEED  =  " + (MAIN_LOOP_SPEED - loopTime*1000) );
				exception.printStackTrace();
			}			
			
		}
		
	}


	/** Makes the screen shake violently like an earthquake **/
	private void earthQuakeEffect(){

		if (viewY%2 == 0) {
			viewX += (int) (Math.random()*earthQuakeMagnitude);
			viewY -= (int) (Math.random()*earthQuakeMagnitude);
		} else {
			viewX -= (int) (Math.random()*earthQuakeMagnitude);
			viewY += (int) (Math.random()*earthQuakeMagnitude);
		}

	}

/** Loads and displays all initial graphics of a level on the screen  **/

	private void loadLevel() {
	
		/** Reset all lists and variables pertaining to the previous played level **/
		blocks = new ArrayList<Block>();
		things = new ArrayList<Thing>();
		edibleItems = new ArrayList<Animation>();;
		dangerousThings = new ArrayList<Thing>();
		texts = new ArrayList<TextAnimator>();
		checkPoints = new ArrayList<Thing>();

		LEVEL_NAME = "";
		LEVEL_WIDTH = 0;
		LEVEL_HEIGHT = 0;

		viewX = 0;
		viewY = 0;
		greenCheckPoint = null;
		grabbedItem = null;
		vortex = null;
		levelComplete = false;
		dx = 0;
		vx = 0;
		vy = 0;

		// Loads all objects for the current level
		Data.loadObjects("levels/levels.txt", currentLevel);
		
		findScreenDimensions();

		// Clear the screen
		removeAll();
		
		// Fill the screen with new images
		addBackground();
		addImagesToScreen();
		
		texts.add(new TextAnimator(SCREEN_WIDTH/2, 50, LEVEL_NAME, 30, Color.WHITE, 800, 5, "center"));
		add(texts.get(0).label);
		
		Block.resetPerformedNaturalAnimate();
		Block.findNaturalFallingBlockCandidates();
		
		currentScreen = ScreenMode.PLAYING;

		/** TEMPORARY for powerup testing, this automatically gives the player a jump powerup at the beginning of the level. eventually he'll have blocks to get this power-up **/
		// alarms.add(new Alarm (100, 0, 0));
		/** TEMPORARY **/

	}

	private void drawMainMenu(){
		removeAll();
		addToScreen(mainMenuButtons);
		add(Data.fruitFeverTitle);
		levelSelectionPage = 0;

		currentScreen = ScreenMode.MAIN_MENU;
	}
	
	private void drawLevelSelection(){
	
		/** Remove all images from screen and add the level selection images **/
		removeAll();
		add(levelBackDrop.image);
		addToScreen(levelSelectionButtons);
		
		for(int i = 0; i < levelNumbers.length; i++)
			add(levelNumbers[i]);
			
		currentScreen = ScreenMode.LEVEL_SELECTION;
		
	}

	// Loads the Hearts
	private void addHearts(){
		
		for(int i = 0; i < Player.MAX_LIVES; i++){
			livesImages[i] = new GImage(Data.heartImage.getImage());
			livesImages[i].setLocation(i*Data.TILE_SIZE, 0);
			add(livesImages[i]);
		}

	}

	private void addBackground(){

		// Creates a black background on the screen
		GRect background = new GRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		add(background);

	}

	/** Sets the variables LEVEL_WIDTH & LEVEL_HEIGHT to the furthest blocks found horizontally and vertically **/
	private void findScreenDimensions(){
	
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
		
		for (Thing item : edibleItems ) {
			item.image.setLocation(item.getX(), item.getY());
			add(item.image);
		}

		placePlayerOnScreen();

		addHearts();

		addToScreen(inGameButtons);

		/** TESTING PURPOSES ONLY **/
		//addToThings(new MovingAnimation(0, 100, Data.redFruit, true, 2, true, 2, 0, 1));
		// addToThings(new MovingAnimation(0, 100, Data.wormEnemyMoving, true, 2, true, 1, 0, 1));
		// addToThings(new AdvancedMovingAnimation(new int[]{0, 100}, new int[]{50, 50}, new GImage[][]{ Data.wormEnemyMoving, Data.wormEnemyMovingH}, true, 2, true, 2, 0));
		// addToThings(new AdvancedMovingAnimation(new int[]{0, 100}, new int[]{50, 50}, Data.wormEnemyMoving, true, 2, true, 2, 0));
		// addToThings(new Animation(0, 125, Data.fuzzyDiskAnimation, true, 2, true, -1));
		/** **/
		
	}

	// ** Creates and correctly places player on screen **/
	private void placePlayerOnScreen(){
		
		// Creates the Player class
		player = new Player(playerStartX, playerStartY);
		add(player.image);

		player.focusViewOnPlayer(playerStartX, playerStartY, true);
		
		add(player.swirl.image);

	}
	
	/** Adds a list of buttons to the screen **/
	public void addToScreen(ArrayList<Button> arr){
		for(int i = 0; i < arr.size(); i++)
			add(arr.get(i).image);
	}
	
	
	/** Checks all buttons in a list, and changes the subimage if it has been clicked on **/
	public void checkAndSetClick(ArrayList<Button> arr, MouseEvent mouse){
		for(Button obj : arr)
			// Check to see if the mouse is on the button
			if(obj.checkOverlap(mouse.getX(), mouse.getY())){
				// Make the image appear to be clicked on
				obj.setClick();
				clickedOnButton = obj;
			}
	}
	
	/** Shifts the labels of the level selection button by a positive or negative integer value **/
	private void shiftLevelLabels(int shift){
	
		for(int i = 0; i < 20; i++){
			levelNumbers[i].setLabel(String.valueOf(Integer.valueOf(levelNumbers[i].getLabel()) + shift));
			levelNumbers[i].setLocation((int) (SCREEN_WIDTH/2 - levelNumbers[i].getWidth()/2 - 90 + (i%4)*60), 132 + (i/4)*55);
		}
			
	}
	
	// Add a Thing to the "things" arrayList, setting its position and adding it to the screen
	public static void addToThings(Thing obj){
		things.add(obj);
		obj.image.setLocation(obj.x - viewX, obj.y - viewY);
		screen.add(obj.image);
	}
	
	/** This code is not in init() since it won't allow us to display anything on the screen during that method **/
	private void postInit(){
	
		/** Loading screen **/
		Data.loadingScreen();
		add(Data.loadingScreenBackground);
		
		/** Set up keyboard and mouse **/
		addMouseListeners();
		addKeyListeners();
		
		/** Renders Images in the Data class, and fills the object ArrayLists **/
		Data.loadImages();
	
		drawMainMenu();
	
	}
	
	@Override public void keyPressed(KeyEvent key){
		
		// If the game mode is PLAYING
		if (currentScreen == ScreenMode.PLAYING) {
		
			int keyCode = key.getKeyCode();

			// JUMP
			if (keyCode == KeyEvent.VK_W) {
				player.setKeepJumping(true);	

			// TONGUE
			} else if (keyCode == KeyEvent.VK_E) {
				if (tongueButtonReleased && grabbedItem == null){
					player.eat();
					tongueButtonReleased = false;
				}

				// Shoot Swirl
			} else if (keyCode == KeyEvent.VK_SPACE) {
				
				if (swirlButtonReleased) {
				
					if(player.swirl.reset)
						player.shootSwirl();
					else
						player.swirlTeleport();
					
					swirlButtonReleased = false;
				}
		
			// Movement LEFT
			} else if (keyCode == KeyEvent.VK_A) {
				player.facingRight = false; 
				dx = -1;
			
			// Movement RIGHT
			} else if (keyCode == KeyEvent.VK_D) {
				dx = 1;
				player.facingRight = true;
				
			}
		}
	}
	
	@Override public void keyReleased(KeyEvent key){
		
		// If the game mode is PLAYING
		if (currentScreen == ScreenMode.PLAYING) {
		
			int keyCode = key.getKeyCode();

			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_A) {
			
				dx = 0;
				player.dx = 0;
				vx = 0;
				vy = 0;
			
			} else if (keyCode == KeyEvent.VK_SPACE)
				swirlButtonReleased = true;
			else if (keyCode == KeyEvent.VK_E)
				tongueButtonReleased = true;
			else if (keyCode == KeyEvent.VK_W) 
				player.setKeepJumping(false);
			
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
		
		if (currentScreen == ScreenMode.MAIN_MENU)
			checkAndSetClick(mainMenuButtons, mouse);
		else if (currentScreen == ScreenMode.LEVEL_SELECTION)
			checkAndSetClick(levelSelectionButtons, mouse);
		else if (currentScreen == ScreenMode.PLAYING)
			checkAndSetClick(inGameButtons, mouse);
			
	}
	
	@Override public void mouseReleased(MouseEvent mouse) {
		
		if (clickedOnButton != null) {
		
			/** Unclick the button image and preform action (if applicable) **/
			if (clickedOnButton.contains(mouse.getX(), mouse.getY())) {
				clickedOnButton.setHover();
				
				// Play button
				if (clickedOnButton.type == 0)
					drawLevelSelection();
				
				// Level left arrow button
				else if (clickedOnButton.type == 4) {
					if (levelSelectionPage > 0) {
						levelSelectionPage--;
						shiftLevelLabels(-20);
					}		
			
				// Level right arrow button
				} else if (clickedOnButton.type == 5) {
					if (levelSelectionPage < 4) {
						levelSelectionPage++;
						shiftLevelLabels(20);
					}
				
				// Level button
				} else if (clickedOnButton.type == 6) {
					currentLevel = clickedOnButton.level + levelSelectionPage*20;
					loadLevel();
				
				// Main Menu Button (Gear)
				} else if (clickedOnButton.type == 7) {
					drawMainMenu();
				
				// Refresh Button
				} else if (clickedOnButton.type == 8) {
					currentScreen = ScreenMode.NOT_AVAILABLE;
					loadLevel();
				}
				
			}
			else clickedOnButton.setDefault();
		}
		clickedOnButton = null;
	}
	
	/** Animates all text in the "texts" ArrayList, removing the inactive ones **/
	private void animateText(){
			
		for (int i = 0; i < texts.size(); i++)
			if (texts.get(i).active)
				texts.get(i).animate();
			else {
				remove(texts.get(i).label);
				texts.remove(i);
				i -= 1;
				continue;
			}
	}

	/** This method properly sets the position of everything on the screen 
	 * especially after a teleportation, death or a level selection **/

	/** Fixes Issue #41. Since the optimized .animate() method in Things doesn't move
	the blocks off screen when you teleport to a location where there are unmoved blocks off
	screen they appear on the screen. To fix this issue I added a new method in Thing called 
	'naturalAnimate' which is the old .animate() method that moves all the Things 
	in sync together. Thus when you teleport it also moves the blocks off screen as well **/

	public static void naturalAnimateAll(){

		// avoids double drawing blocks on the screen
		Block.resetPerformedNaturalAnimate();

		for (Thing thing : FruitFever.things)
			thing.naturalAnimate();
		
		for (Animation fruit : FruitFever.edibleItems)
			fruit.naturalAnimate();
		
		for (Thing dangerousSprite : FruitFever.dangerousThings)
			dangerousSprite.naturalAnimate();

	}


}




















