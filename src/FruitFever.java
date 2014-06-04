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

	static ScreenHandler screenHandler;

/** Constants **/

	static GraphicsProgram screen;
	final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500, MAIN_LOOP_SPEED = 30;

/** Level Information/Objects/Lists **/
	
	static int currentFruitRings, totalFruitRings;
	static double currentEnergy, maxEnergy = 1000;
	static LevelInformation[] levelInformation = new LevelInformation[100];
	static int LEVEL_WIDTH, LEVEL_HEIGHT;
	
	static Animation vortex, grabbedItem;
	static Thing greenCheckPoint;
	static boolean levelComplete;

	static ArrayList<Block> blocks;
	static ArrayList<Thing> things, dangerousThings, checkPoints;
	static ArrayList<Enemy> enemies;
	static ArrayList<Animation> edibleItems;
	static ArrayList<TextAnimator> levelTexts;

/** Player **/

	static Player player;
	static int playerStartX, playerStartY, dx;
	static boolean swirlButtonPressed = false, tongueButtonPressed = false, shootButtonPressed = false;
	
/** Menus/GUI **/
	
	// Defines ScreenMode constants	
	public enum ScreenMode {
		LEVEL_REFRESH,
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
	
	static int currentLevel = 1, levelSelectionPage = 0;
	static ScreenMode currentScreen;

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

	private static int earthQuakeMagnitude = 2;
	private static int quakeBoundary_x = 0, quakeBoundary_y = 0;
	final private static int QUAKE_BOUNDARY = 5;

/** TEMPORARY COLLISION DETECTION **/
	// static GRect point1;
	// static GRect point2;

	@Override public void init() {
		screen = this;
	}
	
	/** Contains the main game loop **/
	@Override public void run(){
		
		postInit();
		
		/** TEMPORARY **/
		GRect leftRect = new GRect(LEFT_BOUNDARY, 0, 3, SCREEN_HEIGHT);
		GRect rightRect = new GRect(RIGHT_BOUNDARY, 0, 3, SCREEN_HEIGHT);
		GRect upRect = new GRect(0, UP_BOUNDARY, SCREEN_WIDTH, 3);
		GRect downRect = new GRect(0, DOWN_BOUNDARY, SCREEN_WIDTH, 3); 
		GRect centerRect = new GRect(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 3, 3);
		// point1 = new GRect(0,0,2,3);
		// point2 = new GRect(0,0,2,3);

		leftRect.setFillColor(Color.RED);
		rightRect.setFillColor(Color.RED);
		upRect.setFillColor(Color.RED);
		downRect.setFillColor(Color.RED);
		centerRect.setFillColor(Color.RED);
		// point1.setFillColor(Color.GREEN);
		// point2.setFillColor(Color.BLUE);

		leftRect.setFilled(true);
		rightRect.setFilled(true);
		downRect.setFilled(true);
		upRect.setFilled(true);
		centerRect.setFilled(true);
		// point1.setFilled(true);
		// point2.setFilled(true);
		/** TEMPORARY **/
		

		// It's a byte an not an int because we're paranoid about saving memory! 
		// Three bytes saved through this action! 
		byte loops = Byte.MIN_VALUE;
		

		while(true){

			// Paused the main loop until the screen is refocused
			// This is where the pause screen is the be implemented
			while( !GameStarter.frame.isFocused() ){ }

			Timer_ loopTimer = new Timer_();
		
			// Activates EarthQuake Effect				
			// earthQuakeEffect();

			// Countdown all of the alarms towards execution
			for (int i = 0; i < alarms.size(); i++) {
					if (!alarms.get(i).active) {
						alarms.remove(i);
						i--;
					} else alarms.get(i).execute();
				}
				
			if (currentScreen == ScreenMode.PLAYING) {
			
				/** Controls if it is time to return to the level selection menu **/
				if (levelComplete || player.getLives() <= 0) {
					
					if (levelComplete) {
						
						if (totalFruitRings == 0)
							levelInformation[currentLevel].stars = 3;
						else
							levelInformation[currentLevel].stars = (byte) Math.max(levelInformation[currentLevel].stars, (((double) currentFruitRings / (double) totalFruitRings)*3.0));
							
						Data.updateLevelInformation(currentLevel);
						levelComplete = false;
					}
					
					screenHandler.drawLevelSelection();
					continue;
				}
			
				screenHandler.animateAndRemoveText(levelTexts);

				/** Animate all objects (Scenery, Animation, MovingAnimation, Swirl, etc..) **/
				for (int i = 0; i < things.size(); i++) {
					if (!things.get(i).active) {
						remove(things.get(i).image);
						things.remove(i);
						i--;
					} else things.get(i).animate();
				}
				
				/** Animate all enemies **/
				for (int i = 0; i < enemies.size(); i++) {
					if (!enemies.get(i).active) {
						remove(enemies.get(i).healthBar);
						remove(enemies.get(i).healthBarBackground);
						remove(enemies.get(i).image);
						enemies.remove(i);
						i--;
					} else enemies.get(i).animate();
				}
					
					
				/** Animate all edible items **/
				for (Thing item : edibleItems)
					item.animate();


				// Tests for falling blocks
					
				// Block.updateNaturalFallingBlockCandidates();
				// Block.activateFallingBlocksByNaturalDisaster();	

				// Block.activateFallingBlocksWithPlayerPosition(player.imageX, player.y, player.onSurface());

				Block.motion();	
				Block.drawBlocks();

				player.jump();
				player.motion();
				player.animate();
				ScreenHandler.adjustHearts(player.getLives());
				
				/** Adjust energy bar **/
				currentEnergy -= 0.1;
				screenHandler.adjustEnergyBar(currentEnergy/maxEnergy);
				
				// add(point1);
				// add(point2);
				add(leftRect);
				add(rightRect);
				add(upRect);
				add(downRect);
				add(centerRect);

			}

			pauseLoop(loopTimer);

			if (currentScreen == ScreenMode.LEVEL_REFRESH)
				loadLevel();
							
			loops++;
		}
	}

	/** Calculates how much to pause the main loop by so that it runs as smoothly as possible **/
	private void pauseLoop(Timer_ timer){

		double pauseTime = Math.max(0f, MAIN_LOOP_SPEED - timer.stop()*1000);

		String strPauseTime  = new Double(pauseTime).toString();

		// Cut the double to limit it's decimal places
		if (strPauseTime.length() > 7)
			strPauseTime = strPauseTime.substring(0, 7);

		double milliSeconds = new Double(strPauseTime).doubleValue();

		try { pause(milliSeconds); } 
		catch (IllegalArgumentException exception) 
		{
			pause(MAIN_LOOP_SPEED);	
			System.out.println("IllegalArgumentException error. MAIN_LOOP_SPEED  =  " + milliSeconds );
			exception.printStackTrace();
		}

	}

	/** Makes the screen shake violently like an earthquake **/
	private void earthQuakeEffect(){

		// NOTE: NOT WORKING CORRECTLY

		int xShift = (int) (Math.random()*earthQuakeMagnitude);
		int yShift = (int) (Math.random()*earthQuakeMagnitude);

		if (viewY%2 == 0 && quakeBoundary_x < QUAKE_BOUNDARY && quakeBoundary_y > QUAKE_BOUNDARY) {

			viewX += xShift;
			viewY -= yShift;
			quakeBoundary_x += xShift;
			quakeBoundary_y -= yShift;

		} else {

			viewX -= xShift;
			viewY += yShift;
			quakeBoundary_x -= xShift;
			quakeBoundary_y += yShift;
		}

	}

	/** Loads and displays all initial graphics of a level on the screen  **/
	private void loadLevel() {
	
		/** RESET all lists and variables pertaining to the previous played level **/
		blocks = new ArrayList<Block>();
		things = new ArrayList<Thing>();
		edibleItems = new ArrayList<Animation>();
		enemies = new ArrayList<Enemy>();
		dangerousThings = new ArrayList<Thing>();
		levelTexts = new ArrayList<TextAnimator>();
		checkPoints = new ArrayList<Thing>();

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
		
		shootButtonPressed = false;
		tongueButtonPressed = false;
		swirlButtonPressed = false;
		
		currentFruitRings = 0;
		totalFruitRings = 0;
		
		currentEnergy = maxEnergy;

		/** LOAD NEW LEVEL**/
		
		Data.loadObjects("levels/levels.txt", currentLevel);
		
		screenHandler.updateFruitRingDisplay(0);
		
		if (vortex == null)
			System.out.println("Umm.. so sorry to break it to you, but the person who programmed this level forgot to make a vortex.. so you do not have any way to beat this level!");
			
		findScreenDimensions();
		
		player = new Player(playerStartX, playerStartY);
		player.focusViewOnPlayer(playerStartX, playerStartY, true);

		/** Clear the screen and fill it with new images **/
		removeAll();
		screenHandler.addBackground();
		screenHandler.addImagesToScreen();
	
		/** Add animated level title to the screen **/
		levelTexts.add(new TextAnimator(SCREEN_WIDTH/2, 50, levelInformation[currentLevel].name, 30, Color.WHITE, 800, 5, "center"));
		add(levelTexts.get(0).label);
		
		Block.resetPerformedNaturalAnimate();
		Block.updateNaturalFallingBlockCandidates();

		currentScreen = ScreenMode.PLAYING;

	}

	/** Sets the variables LEVEL_WIDTH & LEVEL_HEIGHT to the furthest blocks found horizontally and vertically **/
	private void findScreenDimensions() {
		for (Block block : blocks ) {
			LEVEL_WIDTH = Math.max(LEVEL_WIDTH, block.x);
			LEVEL_HEIGHT = Math.max(LEVEL_HEIGHT, block.y);
		}
	}
	
	/** Checks all buttons in a list, and changes the subimage if it has been clicked on **/
	private void checkAndSetClick(ArrayList<Button> arr, MouseEvent mouse) {
		for (Button obj : arr)
			// Check to see if the mouse is on the button
			if (obj.active && obj.checkOverlap(mouse.getX(), mouse.getY())) {
				// Make the image appear to be clicked on
				obj.setClick();
				clickedOnButton = obj;
			}
	}
	
	/** Add a Thing to the "things" ArrayList, setting its position and adding it to the screen **/
	public static void addToThings(Thing obj){
		things.add(obj);
		obj.image.setLocation(obj.x - viewX, obj.y - viewY);
		screen.add(obj.image);
	}
	
	/** This code is not in init() since it won't allow us to display anything on the screen during that method **/
	private void postInit(){
	
		screenHandler = new ScreenHandler(this);
	
		/** Loading screen **/
		Data.loadingScreen();
		add(Data.loadingScreenBackground);
		
		/** Load level information **/
		Data.loadLevelInformation();
		
		/** Set up keyboard and mouse **/
		addMouseListeners();
		addKeyListeners();
		
		/** Renders Images in the Data class, and fills the object ArrayLists **/
		Data.loadImages();
		
		screenHandler.init();
	
		screenHandler.drawMainMenu();
	
	}
	
	@Override public void keyPressed(KeyEvent key){
		
		if (currentScreen == ScreenMode.PLAYING) {
		
			int keyCode = key.getKeyCode();

			/** JUMP **/
			if (keyCode == KeyEvent.VK_W) {
				player.setKeepJumping(true);	

			/** SHOOT **/
			} else if (keyCode == KeyEvent.VK_S) {
				if (!shootButtonPressed)
					player.shootProjectile();

			/** TONGUE **/
			} else if (keyCode == KeyEvent.VK_SHIFT) {
				if (!tongueButtonPressed && grabbedItem == null && player.finishedTongueAnimation){
					player.finishedTongueAnimation = false;
					tongueButtonPressed = true;
					player.eat();
				}

			/** Shoot Swirl **/
			} else if (keyCode == KeyEvent.VK_SPACE) {
				
				if (!swirlButtonPressed && player.finishedTongueAnimation) {
				
					if (player.swirl.reset) {
						if (currentEnergy - Swirl.energyRequired >= 0) {
							player.shootSwirl();
							currentEnergy -= Swirl.energyRequired;
							screenHandler.adjustEnergyBar(currentEnergy/maxEnergy);
						}
					}
					else
						player.swirlTeleport();
					
					swirlButtonPressed = true;
				}
		
			/** Movement LEFT **/
			} else if (keyCode == KeyEvent.VK_A) {
				player.facingRight = false; 
				dx = -1;
			
			/** Movement RIGHT **/
			} else if (keyCode == KeyEvent.VK_D) {
				dx = 1;
				player.facingRight = true;
			}
			
		}
	}
	
	@Override public void keyReleased(KeyEvent key){
		
		if (currentScreen == ScreenMode.PLAYING) {
		
			int keyCode = key.getKeyCode();

			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_A) {
			
				dx = 0;
				player.dx = 0;
				vx = 0;
				vy = 0;
			
			} else if (keyCode == KeyEvent.VK_SPACE)
				swirlButtonPressed = false;
			else if (keyCode == KeyEvent.VK_SHIFT)
				tongueButtonPressed = false;
			else if (keyCode == KeyEvent.VK_S)
				shootButtonPressed = false;
			else if (keyCode == KeyEvent.VK_W) 
				player.setKeepJumping(false);
			
		}
		
	}
	
	/** Check to see if the mouse is hovering over any images and sets them accordingly **/
	@Override public void mouseMoved(MouseEvent mouse) {
	
		for (int i = 0; i < buttons.size(); i++ ) {
			
			Button obj = buttons.get(i);
			
			if(obj.equals(clickedOnButton) || !obj.active)
				continue;
				
			if(obj.checkOverlap(mouse.getX(), mouse.getY()))
				obj.setHover();
			else
				obj.setDefault();
		}
	}
	
	/** Check to see if the mouse is on the selected button or not and sets the image accordingly **/
	@Override public void mouseDragged(MouseEvent mouse) {
		if (clickedOnButton != null) {
			if (clickedOnButton.checkOverlap(mouse.getX(), mouse.getY()))
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
				if (clickedOnButton.type == Button.Type.PLAY)
					screenHandler.drawLevelSelection();
				
				// Level left arrow button
				else if (clickedOnButton.type == Button.Type.LEFT_ARROW) {
					if (levelSelectionPage > 0) {
						levelSelectionPage--;
						screenHandler.shiftLevelLabels(-20);
					}		
			
				// Level right arrow button
				} else if (clickedOnButton.type == Button.Type.RIGHT_ARROW) {
					if (levelSelectionPage < 4) {
						levelSelectionPage++;
						screenHandler.shiftLevelLabels(20);
					}
				
				// Level button
				} else if (clickedOnButton.type == Button.Type.LEVEL_BOXES) {
					currentLevel = clickedOnButton.level + levelSelectionPage*20;
					loadLevel();
				
				// Main Menu Button (Gear)
				} else if (clickedOnButton.type == Button.Type.GEAR) {
					screenHandler.drawMainMenu();
				
				// Refresh Button
				} else if (clickedOnButton.type == Button.Type.REFRESH) {
					currentScreen = ScreenMode.LEVEL_REFRESH;
				}
				
			}
			else clickedOnButton.setDefault();
		}
		clickedOnButton = null;
	}

	/** This method properly sets the position of everything on the screen 
	 * especially after a teleportation, death or a level selection **/

	/** Fixes Issue #41. Since the optimized .animate() method in Things doesn't move
	the blocks off screen when you teleport to a location where there are unmoved blocks off
	screen they appear on the screen. To fix this issue I added a new method in Thing called 
	'naturalAnimate' which is the old .animate() method that moves all the Things 
	in sync together. Thus when you teleport it also moves the blocks off screen as well **/

	public static void naturalAnimateAll(){

		// Avoids double drawing blocks on the screen
		Block.resetPerformedNaturalAnimate();

		for (Thing thing : FruitFever.things)
			thing.naturalAnimate();
		
		for (Animation fruit : FruitFever.edibleItems)
			fruit.naturalAnimate();
		
		for (Thing dangerousSprite : FruitFever.dangerousThings)
			dangerousSprite.naturalAnimate();
		
		for (Enemy enemy : FruitFever.enemies)
			enemy.naturalAnimate();
		
		/** Fixes Issue #81 (Teleportation problem) **/
		player.animate();

	}

}




















