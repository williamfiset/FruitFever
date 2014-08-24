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

	private static final long serialVersionUID = 1L;

/** Constants **/
	
	static GraphicsProgram screen;
	static ScreenHandler screenHandler;
	final static int 	SCREEN_WIDTH = 700,
						SCREEN_HEIGHT = 500;
	
	// Cannot be made final since they can be modified through GameStarter
	static boolean debugMode      = false;
	static int MAIN_LOOP_SPEED    = 30;

/** Level Information/Objects/Lists **/
	
	static int 	currentFruitRings,
				totalFruitRings;
	
	static LevelInformation[] levelInformation = new LevelInformation[100];
	
	static int LEVEL_WIDTH, LEVEL_HEIGHT;
	
	static Animation vortex;
	static Thing greenCheckPoint;
	static boolean levelComplete;
	
	static ArrayList<Block> blocks;
	static ArrayList<Thing> things, checkPoints;
	static ArrayList<Hint> hints;
	static ArrayList<Animation> edibleItems, dangerousThings;
	static ArrayList<TextAnimator> levelTexts;
	static TextAnimator hintText;

	public static MusicPlayer 	slurpSound = new MusicPlayer("sound/slurp.mp3"),
								fruitRingCollectionSound = new MusicPlayer("sound/fruitRingCollection.mp3");

/** Player **/

	static Player player;
	static boolean 	swirlButtonPressed = false,
					tongueButtonPressed = false;
	
	// These variables are used to help deal with multi-threading, it allows us to control when the events occur during the main loop (this hasn't been implemented for simple events like a user pressing A, D, or W)
	static boolean 	swirlEventInvoked = false,
					tongueEventInvoked = false;
	
/** Menus/GUI **/
	
	// Defines ScreenMode constants	
	public static enum ScreenMode {

		LEVEL_RESTART,
		LOADING_GAME,
		MAIN_MENU,
		LEVEL_SELECTION,
		PLAYING,
		PAUSED,
		END_OF_LEVEL,
		CONTROLS,
		OPTIONS,
		MULTIPLAYER;

	}

	static boolean placed_menu = false;

	static ArrayList<Button> 	mainMenuButtons = new ArrayList<>(),
								levelSelectionButtons = new ArrayList<>(),
								inGameButtons = new ArrayList<>(),
								pauseMenuButtons = new ArrayList<>(),
								endOfLevelButtons = new ArrayList<>(),
								buttons = new ArrayList<>(); // Includes all buttons (even those in other ArrayLists)

	static Button 	clickedOnButton = null,
					leftArrow, rightArrow; // Level selection screen
	
	public static int currentLevel = -1, levelSelectionPage = 0;
	
	static ScreenMode currentScreen;

	static ArrayList<Alarm>	powerupAlarms = new ArrayList<Alarm>();
	
/** Screen View Variables **/

	final static double viewBoxSpacing = 0.29; // (This value seems to be optimal for screen view)
	final static int 	LEFT_BOUNDARY = (int) (SCREEN_WIDTH * viewBoxSpacing),
						RIGHT_BOUNDARY = (int) (SCREEN_WIDTH * (1.0 - viewBoxSpacing)),
						UP_BOUNDARY = (int) (SCREEN_HEIGHT * viewBoxSpacing),
						DOWN_BOUNDARY = (int) (SCREEN_HEIGHT * (1.0 - viewBoxSpacing));
		
	static int 	viewX,
				viewY,
				vx; // Δ in viewX & viewY
				
	static double vy; 

/** Natural Disaster Variables **/

	private static int earthQuakeMagnitude  = 2, quakeBoundary_x = 0, quakeBoundary_y = 0;
	final private static int QUAKE_BOUNDARY = 5;

/** BUG TESTING COLLISION DETECTION **/
	static GRect point1, point2, point3, point4, point5, point6;

	@Override public void init() {
		screen = this;
	}
	
	/** Contains the main game loop **/
	@Override public void run(){
		
		postInit();
		
		/** Temporary **/
		GRect 	leftRect = new GRect(LEFT_BOUNDARY, 0, 3, SCREEN_HEIGHT),
				rightRect = new GRect(RIGHT_BOUNDARY, 0, 3, SCREEN_HEIGHT),
				upRect = new GRect(0, UP_BOUNDARY, SCREEN_WIDTH, 3),
				downRect = new GRect(0, DOWN_BOUNDARY, SCREEN_WIDTH, 3),
				centerRect = new GRect(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 3, 3);
		
		point1 = new GRect(0,0,2,3); point2 = new GRect(0,0,2,3); point3 = new GRect(0,0,2,3);
		point4 = new GRect(0,0,2,3); point5 = new GRect(0,0,2,3); point6 = new GRect(0,0,2,3);

		if (debugMode) {
			leftRect.setFillColor(Color.RED); rightRect.setFillColor(Color.RED); upRect.setFillColor(Color.RED);
			downRect.setFillColor(Color.RED); centerRect.setFillColor(Color.RED);
			point1.setFillColor(Color.GREEN); point2.setFillColor(Color.BLUE);
			point3.setFillColor(Color.ORANGE); point4.setFillColor(Color.YELLOW);
			point5.setFillColor(Color.RED);	point6.setFillColor(Color.MAGENTA);

			leftRect.setFilled(true); rightRect.setFilled(true); downRect.setFilled(true);
			upRect.setFilled(true);	centerRect.setFilled(true); point1.setFilled(true);	point2.setFilled(true);
			point3.setFilled(true);	point4.setFilled(true); point5.setFilled(true);	point6.setFilled(true);
		}		

		//MusicPlayer backgroundMusic = new MusicPlayer("sound/Fur_Elise.mp3");
		//backgroundMusic.play();

		for (int loops = 0; true; loops++) {

			/** Pauses the main loop until the screen is refocused **/
			while (!GameStarter.appletFrame.isFocused()) { }

			Timer_ loopTimer = new Timer_();
		
			// Activates EarthQuake Effect				
			// earthQuakeEffect();
			
			if (currentScreen == ScreenMode.PLAYING) {
			
				/** Controls if it is time to display end of level screen **/
				if (levelComplete || player.getLives() <= 0) {
					
					if (levelComplete) {
						
						if (totalFruitRings == 0)
							levelInformation[currentLevel].stars = 3;
						else
							levelInformation[currentLevel].stars = (byte) Math.max(levelInformation[currentLevel].stars, (((double) currentFruitRings / (double) totalFruitRings)*3.0));
							

						levelInformation[currentLevel].completed = true;
						levelInformation[currentLevel + 1].locked = false;

						Data.updateLevelInformation(currentLevel);

						screenHandler.drawEndOfLevelMenu(true);
					} else
						screenHandler.drawEndOfLevelMenu(false);
					
				}
				
				/** Countdown all of the alarms towards execution **/
				for (int i = 0; i < powerupAlarms.size(); i++) {	
					if (!powerupAlarms.get(i).active) {
						powerupAlarms.remove(i);
						i--;
					} else powerupAlarms.get(i).execute();
				}
			
				/** Animate all text **/
				screenHandler.animateAndRemoveText(levelTexts);
				if (hintText != null) {
					if (!screenHandler.animateText(hintText)) {
						remove(hintText.label);
						hintText = null;
					}
				}

				/** Animate all objects **/
				for (int i = 0; i < things.size(); i++) {
					if (!things.get(i).active) {
						remove(things.get(i).image);
						things.remove(i);
						i--;
					} else things.get(i).animate();
				}

				/** Actions triggered by user **/
				if (swirlEventInvoked)		
					swirlEvent();
				if (tongueEventInvoked)		
					tongueEvent();


				// Disaster Blocks
				// Block.updateNaturalFallingBlockCandidates();
				// Block.activateFallingBlocksByNaturalDisaster();	

				// Blocks that fall relative to player
				Block.activateFallingBlocksWithPlayerPosition(player.imageX, player.y, player.onSurface());
				Block.motion();

				Block.drawBlocks();
				
				player.swirl.animate();
				player.motion();
				player.animate();
				
				ScreenHandler.adjustHearts(player.getLives());
				
				/** Adjust energy bar **/
				player.currentEnergy = Math.max(player.currentEnergy - 0.1, 0);
				ScreenHandler.adjustEnergyBar(player.currentEnergy/player.maxEnergy);
				
				if (debugMode)
					screenHandler.add(point1, point2, point3, point4, point5, point6, leftRect, rightRect, upRect, downRect, centerRect);		

			} 

			if (currentScreen == ScreenMode.MAIN_MENU) {

				final byte MENU_BACKGROUND_SCROLL_SCREEN = 2;
				final double MENU_WIDTH = Data.menu_background1.getWidth() - MENU_BACKGROUND_SCROLL_SCREEN;

				if (!placed_menu) {
					Data.menu_background2.setLocation( MENU_WIDTH , 0 );
					placed_menu = true;
				}

				if (Data.menu_background1.getX() < -MENU_WIDTH) 
					Data.menu_background1.setLocation( MENU_WIDTH , 0 );
				else if (Data.menu_background2.getX() < -MENU_WIDTH) 
					Data.menu_background2.setLocation( MENU_WIDTH , 0 );				

				Data.menu_background1.setLocation(Data.menu_background1.getX() - MENU_BACKGROUND_SCROLL_SCREEN, Data.menu_background1.getY());
				Data.menu_background2.setLocation(Data.menu_background2.getX() - MENU_BACKGROUND_SCROLL_SCREEN, Data.menu_background2.getY());

			}

			if (currentScreen == ScreenMode.LEVEL_RESTART)
				loadLevel();							
			
			pauseLoop(loopTimer);

		}
	}

	/** Calculates how much to pause the main loop by so that it runs as smoothly as possible **/
	private void pauseLoop(Timer_ timer){
		
		double loopTime = timer.stop()*1000;
		
		if (debugMode)
			screenHandler.updateDebugModeDisplay(loopTime);
		
		double pauseTime = Math.max(0f, MAIN_LOOP_SPEED - loopTime);

		String strPauseTime = new Double(pauseTime).toString();

		// Cut the double to limit it's decimal places
		if (strPauseTime.length() > 7)
			strPauseTime = strPauseTime.substring(0, 7);

		double milliSeconds = new Double(strPauseTime).doubleValue();

		try {
			pause(milliSeconds);
		} 
		catch (IllegalArgumentException exception) {
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

		if (viewY % 2 == 0 && quakeBoundary_x < QUAKE_BOUNDARY && quakeBoundary_y > QUAKE_BOUNDARY) {

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
		blocks = new ArrayList<>();
		things = new ArrayList<>();
		edibleItems = new ArrayList<>();
		dangerousThings = new ArrayList<>();
		levelTexts = new ArrayList<>();
		checkPoints = new ArrayList<>();
		hints = new ArrayList<>();
		
		viewX = 0;
		viewY = 0;
		greenCheckPoint = null;
		vortex = null;
		levelComplete = false;
		vx = 0;
		vy = 0;
		hintText = null;

		levelComplete = false;
		
		tongueButtonPressed = false;
		swirlButtonPressed = false;
		
		currentFruitRings = 0;
		totalFruitRings = 0;

		/** LOAD NEW LEVEL**/
		
		Data.loadObjects();
		
		screenHandler.updateFruitRingDisplay(0);
		
		if (vortex == null)
			System.out.println("Umm.. so sorry to break it to you, but the person who programmed this level forgot to make a vortex.. so you do not have any way to beat this level!");
		
		player = new Player();
		player.focusViewOnPlayer(Player.startX, Player.startY, true);
		player.setMovementDirection(Player.MovementDirection.NONE);
		// player.respawn();

		/** Clear the screen and fill it with new images **/
		removeAll();
		screenHandler.addBackground();
		screenHandler.addImagesToScreen();
		add(Player.poisonAnimation.image); 
	
		/** Add animated level title to the screen **/
		addToTexts(new TextAnimator(SCREEN_WIDTH/2, 50, levelInformation[currentLevel].name, 30, Color.WHITE, 1.0, 5, 50, "center"), levelTexts);
		
		Block.resetPerformedNaturalAnimate();
		Block.updateNaturalFallingBlockCandidates();

		currentScreen = ScreenMode.PLAYING;

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

		/** Set up keyboard and mouse **/
		addMouseListeners();
		addKeyListeners();
	
		Data.loadingScreen();
		add(Data.loadingScreenBackground);
						
		Data.loadLevelInformation();

		/** Renders Images in the Data class, and fills the object ArrayLists **/
		Data.loadImages();
		
		screenHandler.init();
	
		if (currentLevel != -1)
			loadLevel();
		else
			screenHandler.drawMainMenu();
	
	}

	/** Check to see if the mouse is hovering over any images and sets them accordingly **/
	@Override public void mouseMoved(MouseEvent mouse) {
	
		for (int i = 0; i < buttons.size(); i++ ) {
			
			Button obj = buttons.get(i);
			
			if (obj.equals(clickedOnButton) || !obj.active)
				continue;
				
			if (obj.checkOverlap(mouse.getX(), mouse.getY()))
				obj.setHover();
			else
				obj.setDefault();
		}
	}
	
	/** Check to see if the mouse is on the selected button or not and sets the image accordingly **/
	@Override public void mouseDragged(MouseEvent mouse) {
		if (clickedOnButton != null) {
			/** SLIDER buttons work differently **/
			if (clickedOnButton.type == Button.Type.SLIDER) {
				clickedOnButton.slideButton(mouse.getX());
			/** Other button types **/
			} else if (clickedOnButton.checkOverlap(mouse.getX(), mouse.getY()))
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
		else if (currentScreen == ScreenMode.PAUSED) {
			checkAndSetClick(pauseMenuButtons, mouse);
			checkAndSetClick(inGameButtons, mouse);
		}
		else if (currentScreen == ScreenMode.END_OF_LEVEL)
			checkAndSetClick(endOfLevelButtons, mouse);
			
	}
	
	@Override public void mouseReleased(MouseEvent mouse) {
		
		if (clickedOnButton != null) {
		
			/** Unclick the button image and preform action (if applicable) **/
			if (clickedOnButton.contains(mouse.getX(), mouse.getY())) {
				clickedOnButton.setHover();
				
				/** Play button **/
				if (clickedOnButton.type == Button.Type.PLAY || clickedOnButton.type == Button.Type.LEVEL_SELECTION)
					screenHandler.drawLevelSelectionMenu();
				
				/** Level left arrow button **/
				else if (clickedOnButton.type == Button.Type.LEFT_ARROW) {
					// if (levelSelectionPage > 0) {
						levelSelectionPage--;
						screenHandler.shiftLevelLabels(-20);
					// }
			
				/** Level right arrow button **/
				} else if (clickedOnButton.type == Button.Type.RIGHT_ARROW) {
					// if (levelSelectionPage < 4) {
						levelSelectionPage++;
						screenHandler.shiftLevelLabels(20);
					// }
				
				/** Level box button **/
				} else if (clickedOnButton.type == Button.Type.LEVEL_BOXES) {
					currentLevel = clickedOnButton.level + levelSelectionPage*20;
					loadLevel();
				
				/** Pause Menu Button (Gear) **/
				} else if (clickedOnButton.type == Button.Type.GEAR || clickedOnButton.type == Button.Type.RESUME) {
					if (currentScreen != ScreenMode.PAUSED) {
						currentScreen = ScreenMode.PAUSED;
						screenHandler.drawPauseMenu();
					} else {
						currentScreen = ScreenMode.PLAYING;
						screenHandler.removePauseMenu();
					}
				
				/** Slider button **/
				} else if (clickedOnButton.type == Button.Type.SLIDER) {
					clickedOnButton.slideButton(mouse.getX());
					
				/** Music button **/
				} else if (clickedOnButton.type == Button.Type.MUSIC) {
					screenHandler.toggleVisibility(ScreenHandler.musicX);
				
				/** Sound Effects button **/
				} else if (clickedOnButton.type == Button.Type.SOUND_EFFECTS) {
					screenHandler.toggleVisibility(ScreenHandler.soundEffectsX);
					
				/** Main Menu button **/
				} else if (clickedOnButton.type == Button.Type.MAIN_MENU) {
					screenHandler.drawMainMenu();
				
				/** Restart Button **/
				} else if (clickedOnButton.type == Button.Type.RESTART) {
					currentScreen = ScreenMode.LEVEL_RESTART;

				/** Next Level Button **/
				} else if (clickedOnButton.type == Button.Type.NEXT_LEVEL) {
					currentLevel++;
					loadLevel();
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
		
		for (Animation item : FruitFever.edibleItems)
			item.naturalAnimate();
		
		/** Fixes Issue #81 (Teleportation problem) **/
		player.animate();

	}
	
		@Override public void keyPressed(KeyEvent key){
		
		if (currentScreen == ScreenMode.PLAYING || currentScreen == ScreenMode.PAUSED) {
		
			int keyCode = key.getKeyCode();

			/** JUMP **/
			if (keyCode == KeyEvent.VK_W) {
				player.setKeepJumping(true);	
				
			/** TONGUE **/
			} else if (keyCode == KeyEvent.VK_SHIFT) {
				tongueEventInvoked = true;

			/** SWIRL **/
			} else if (keyCode == KeyEvent.VK_SPACE) {
				swirlEventInvoked = true;
		
			/** Movement LEFT **/
			} else if (keyCode == KeyEvent.VK_A) {
				player.facingRight = false; 
				player.setMovementDirection(Player.MovementDirection.LEFT);
			
			/** Movement RIGHT **/
			} else if (keyCode == KeyEvent.VK_D) {
				player.facingRight = true;
				player.setMovementDirection(Player.MovementDirection.RIGHT);

			/** Reload level **/
			} else if (keyCode == KeyEvent.VK_R) {
				currentScreen = ScreenMode.LEVEL_RESTART;								
			}
			
		}
	}
	
	@Override public void keyReleased(KeyEvent key) {
		
		if (currentScreen == ScreenMode.PLAYING || currentScreen == ScreenMode.PAUSED) {
		
			int keyCode = key.getKeyCode();

			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_A) {
			
				player.setMovementDirection(Player.MovementDirection.NONE);
				player.dx = 0;
				vx = 0;
				vy = 0;
			
			} else if (keyCode == KeyEvent.VK_SPACE) {
				swirlButtonPressed = false;
				swirlEventInvoked = false;
			} else if (keyCode == KeyEvent.VK_SHIFT) {
				tongueButtonPressed = false;
				tongueEventInvoked = false;
			} else if (keyCode == KeyEvent.VK_W) 
				player.setKeepJumping(false);
			
		}
		
	}
	
	private void tongueEvent() {
		
		if (!tongueButtonPressed && player.grabbedItem == null && player.finishedTongueAnimation){
			player.finishedTongueAnimation = false;
			tongueButtonPressed = true;
			slurpSound.play();
			player.eat();
		}
		
		tongueEventInvoked = false;
	
	}
	
	private void swirlEvent() {
		
		if (!swirlButtonPressed && player.finishedTongueAnimation) {
					
			if (Swirl.reset) {
				if (player.currentEnergy - Swirl.energyRequired >= 0)
					player.shootSwirl();
			}
			else {
				player.currentEnergy = Math.max(player.currentEnergy - Swirl.energyRequired, 0);
				ScreenHandler.adjustEnergyBar(player.currentEnergy/player.maxEnergy);
				player.swirlTeleport();
			}
			
			swirlButtonPressed = true;
		}
		
		swirlEventInvoked = false;
	
	}
	
	public static void addToTexts(TextAnimator obj, ArrayList<TextAnimator> textList) {
		textList.add(obj);
		screen.add(obj.label);
	}

	@SafeVarargs public static void removeThingFromLists(Block obj, ArrayList<Block>... lists) {

		screen.remove(obj.image);

		for (ArrayList<Block> list : lists)
			if (list.contains(obj))
				list.remove(obj);

	}

}




















