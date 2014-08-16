/**
 * ScreenHandler - Helps the FruitFever class manage the images displayed on the screen.
 *
 * @Author Micah Stairs, William Fiset
 *
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.DecimalFormat;

public class ScreenHandler {
	
	static FruitFever fruitFever;
	
	/** Debug mode **/
	static GLabel nodes = new GLabel(""), speed = new GLabel("");
	static GRect nodesBackground = new GRect(0, 0, 0, 0), speedBackground = new GRect(0, 0, 0, 0);
	
	/** Level Selection Screen**/
	static GLabel[] levelNumbers = new GLabel[20];
	static GImage[] levelLocks = new GImage[20];	
	static GImage[][] levelStars = new GImage[20][3], levelNoStars = new GImage[20][3];
	static String[] levelSelectionPages = new String[] {"0-19", "20-39", "40-59", "60-79", "80-99"};
	static GLabel levelRange = new GLabel(levelSelectionPages[0]);
	
	/** In-Game **/
	static GLabel numberOfFruitRings = new GLabel(""), numberOfLives = new GLabel("");
	static GImage[] livesImages = new GImage[Player.MAX_LIVES];
	static int HEART_AREA_WIDTH = 3*Data.TILE_SIZE;
	static GImage currentHealthBar, currentEnergyBar;
	
	/** Pause Menu **/
	static GImage musicX, soundEffectsX;
	static GLabel 	pauseMenuTitle = new GLabel("Paused"),
					musicLabel = new GLabel("Music"),
					soundEffectsLabel = new GLabel("Sound Effects"),
					mainMenuButtonText = new GLabel("Main Menu"),
					levelSelectionButtonText = new GLabel("Level Selection"),
					resumeButtonText = new GLabel("Resume");
	
	/** End of Level Screen **/

	


	/** Constructor **/
	public ScreenHandler(FruitFever fruitFever) {
		this.fruitFever = fruitFever;
	}
	
	private void setLocations() {
	
		centerObject(Data.fruitFeverTitle, 50);
		centerObject(Data.windowBorder);
		centerObject(Data.endScreenWindow);
		
		/** Lock and Star images **/
		for (int i = 0; i < 20; i++) {
		
			levelLocks[i].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - 115 + (i%4)*60), 97 + (i/4)*55);
  			
  			for (int j = 0; j < 3; j++) {
  				levelStars[i][j].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - 110 + (i%4)*60 + 12*j), 127 + (i/4)*55);
  				levelNoStars[i][j].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - 110 + (i%4)*60 + 12*j), 127 + (i/4)*55);
  			}
		}
		
		Data.healthBarBackground.setLocation(FruitFever.SCREEN_WIDTH/2 - (int) (Data.healthBar.getWidth()/2), 1);
		currentHealthBar.setLocation(FruitFever.SCREEN_WIDTH/2 - (int) (Data.healthBar.getWidth()/2), 1);
		Data.energyBarBackground.setLocation(FruitFever.SCREEN_WIDTH/2 - (int) (Data.energyBar.getWidth()/2), 13);
		currentEnergyBar.setLocation(FruitFever.SCREEN_WIDTH/2 - (int) (Data.energyBar.getWidth()/2), 13);

		Data.fruitRingAnimation[5].setLocation(Data.TILE_SIZE*4, 0);
		
		numberOfFruitRings.setLocation(Data.TILE_SIZE*5, 16);
		
		numberOfLives.setLocation(Data.TILE_SIZE + 3, 16);

		/** Pause Menu **/
		centerObject(pauseMenuTitle, Data.TILE_SIZE*5);
		centerObject(musicX, (int) (-Data.TILE_SIZE*4), Data.TILE_SIZE*6);
		centerObject(musicLabel, Data.TILE_SIZE/2, Data.TILE_SIZE*6);
		centerObject(soundEffectsX, (int) (-Data.TILE_SIZE*4), Data.TILE_SIZE*8);
		centerObject(soundEffectsLabel, Data.TILE_SIZE/2, Data.TILE_SIZE*8);
		centerObject(mainMenuButtonText, Data.TILE_SIZE*11);
		centerObject(levelSelectionButtonText, Data.TILE_SIZE*13);
		centerObject(resumeButtonText, Data.TILE_SIZE*15);
		
		/** Debugging **/
		nodes.setLocation(2, FruitFever.SCREEN_HEIGHT - 9);
		speed.setLocation(FruitFever.SCREEN_WIDTH - 110, FruitFever.SCREEN_HEIGHT - 9);
		speedBackground.setSize(114, (int)speed.getHeight());
		speedBackground.setLocation(FruitFever.SCREEN_WIDTH - 114, FruitFever.SCREEN_HEIGHT - (int)speed.getHeight() - 7);
		
	}
	
	/** Create the images and set their starting locations **/
	public void init() {
		
		/** Adds lock and star images to array **/
		for (int i = 0; i < 20; i++) {
			levelLocks[i] = Thing.copyImage(Data.locked);

			for (int j = 0; j < 3; j++) {
				levelStars[i][j] = Thing.copyImage(Data.starIcon);
				levelNoStars[i][j] = Thing.copyImage(Data.noStarIcon);
			}
		}
		
		/** Level page range label **/
		levelRange.setFont(new Font("Helvetica", Font.BOLD, 35));
		
		/** Create numbers to display in the level boxes **/
		for (int i = 0; i < 20; i++) {
			levelNumbers[i] = new GLabel(String.valueOf(i));
			levelNumbers[i].setFont(new Font("Helvetica", Font.BOLD, 30));
		}
		
		centerObject(Data.healthBarBackground, 1);
		centerObject(Data.energyBarBackground, 13);
		
		
		Data.fruitRingAnimation[5].setLocation(Data.TILE_SIZE*4, 0);
		numberOfFruitRings.setColor(Color.white);
		numberOfLives.setColor(Color.white);
		for (int i = 0; i < Player.MAX_LIVES; i++)
			livesImages[i] = Thing.copyImage(Data.heartImage);
		
		nodes.setColor(Color.white);
		nodesBackground.setFilled(true);
		speed.setColor(Color.white);
		speedBackground.setFilled(true);
		
		pauseMenuTitle.setFont(new Font("Helvetica", Font.BOLD, 35));
		mainMenuButtonText.setFont(new Font("Helvetica", Font.BOLD, 20));
		levelSelectionButtonText.setFont(new Font("Helvetica", Font.BOLD, 20));
		resumeButtonText.setFont(new Font("Helvetica", Font.BOLD, 20));
		musicX = Thing.copyImage(Data.redX);
		musicX.setVisible(false);
		soundEffectsX = Thing.copyImage(Data.redX);
		soundEffectsX.setVisible(false);
		
		setLocations();
		
	}
	
	/** Draws the main menu screen **/
	public void drawMainMenu() {

		removeAll();
		
		add(Data.menu_background2);
		add(Data.menu_background1);
		
		addButtonsToScreen(FruitFever.mainMenuButtons);
		add(Data.fruitFeverTitle);
		fruitFever.levelSelectionPage = 0;
		
		fruitFever.currentScreen = FruitFever.ScreenMode.MAIN_MENU;
	}
	
	/** Draws the level selection screen **/
	public void drawLevelSelection() {
		removeAll();
		add(Data.windowBorder);
		addButtonsToScreen(fruitFever.levelSelectionButtons);
		
		add(levelNumbers, levelLocks);
		add(levelStars, levelNoStars);
		
		fruitFever.add(levelRange);
		
		shiftLevelLabels(0);
			
		fruitFever.currentScreen = FruitFever.ScreenMode.LEVEL_SELECTION;
	}

	/** Draws the end of level screen **/
	public void drawEndOfLevel() {
		
		// add(Data.windowBorder);
		// for (int i = 0; i < FruitFever.levelInformation[currentLevel].stars; i++)
			
		add(Data.endScreenWindow);
		add(Data.buttonFrame[1]);

		// fruitFever.currentScreen = FruitFever.ScreenMode.END_OF_LEVEL;
	}
	
	public void drawPauseMenu() {

		add(Data.windowBorder);
		addButtonsToScreen(FruitFever.pauseMenuButtons);
		add(pauseMenuTitle, musicX, soundEffectsX, musicLabel, soundEffectsLabel, mainMenuButtonText, levelSelectionButtonText, resumeButtonText);
	}
	
	public void removePauseMenu() {
		remove(Data.windowBorder);
		removeButtonsFromScreen(FruitFever.pauseMenuButtons);
		remove(pauseMenuTitle, musicX, soundEffectsX, musicLabel, soundEffectsLabel, mainMenuButtonText, levelSelectionButtonText, resumeButtonText);
	}
	
	/** Shifts the level selection screen by a positive or negative integer value **/
	public void shiftLevelLabels(int shift) {
	
		levelRange.setLabel(levelSelectionPages[FruitFever.levelSelectionPage]);
		centerObject(levelRange, 402);
		
		for (int i = 0; i < 20; i++) {
			levelNumbers[i].setLabel(String.valueOf(Integer.valueOf(levelNumbers[i].getLabel()) + shift));
			centerObject(levelNumbers[i], -90 + (i%4)*60, 128 + (i/4)*55);
			
			int level = Integer.valueOf(levelNumbers[i].getLabel());
			
			/** Level is locked **/
			if (fruitFever.levelInformation[level].locked) {
				levelLocks[i].setVisible(true);
				levelNumbers[i].setVisible(false);
				fruitFever.levelSelectionButtons.get(i).active = false;
				for (int j = 0; j < 3; j++) {
					levelStars[i][j].setVisible(false);
					levelNoStars[i][j].setVisible(false);
				}
			/** Level is unlocked **/
			} else {
				levelLocks[i].setVisible(false);
				levelNumbers[i].setVisible(true);
				fruitFever.levelSelectionButtons.get(i).active = true;
				for (int j = 0; j < 3; j++) {
					if (fruitFever.levelInformation[level].stars > j) {
						levelStars[i][j].setVisible(true);
						levelNoStars[i][j].setVisible(false);
					} else {
						levelStars[i][j].setVisible(false);
						levelNoStars[i][j].setVisible(true);
					}
				}
			}
		}
	}
	
	/** Adds a black background to screen **/
	public void addBackground() {
		GRect background = new GRect(0,0, FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT);
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		add(background);
	}
	
	/** Adds all images including blocks, things, fruits, and enemies to the screen **/
	public void addImagesToScreen() {

		for (Thing obj : fruitFever.things)
			if (obj.layer == Thing.Layer.BELOW_BLOCKS)
				add(obj.image);

		for (Block obj : fruitFever.blocks)
			add(obj.image);
		
		for (Thing obj : fruitFever.things)
			if (obj.layer == Thing.Layer.ABOVE_BLOCKS)
				add(obj.image);
		
		/** Player Images **/
		add(fruitFever.player.image, fruitFever.player.swirl.image);

		/** GUI **/
		add(Data.iconBackgroundBar, Data.fruitRingAnimation[5], numberOfFruitRings, numberOfLives);
		add(Data.healthBarBackground, currentHealthBar, Data.energyBarBackground, currentEnergyBar);
		add(livesImages);
		
		addButtonsToScreen(fruitFever.inGameButtons);
		
	}
	
	public void updateFruitRingDisplay(int newFruitRings) {
		FruitFever.currentFruitRings += newFruitRings;
		numberOfFruitRings.setLabel("x " + FruitFever.currentFruitRings + "/" + FruitFever.totalFruitRings);
	}
	
	public void updateDebugModeDisplay(double milliSeconds) {
		
		nodes.setLabel("Nodes: " + fruitFever.getElementCount());
		nodesBackground.setSize((int) nodes.getWidth() + 4, (int) nodes.getHeight());
		nodesBackground.setLocation(0, FruitFever.SCREEN_HEIGHT - (int) nodes.getHeight() - 7);
		
		DecimalFormat f = new DecimalFormat("#.00");
		
		double fps = 1000/(Math.max(milliSeconds, 30));
		
		speed.setLabel("FPS: " + f.format(fps) + " (" + f.format(milliSeconds) + ")");
		
		add(nodesBackground, nodes, speedBackground, speed);
	}
	
	/** Magical code that redraws the hearts according to the amount of lives left */
	public static void adjustHearts(int livesLeft) {
	
		numberOfLives.setVisible(livesLeft > 10);
		
		if (livesLeft > 10)
			redrawLivesLabel(livesLeft, Player.MAX_LIVES);
		
		for (int i = 0; i < Player.MAX_LIVES; i++) {
			livesImages[i].setVisible((i == 0 && livesLeft > 10) || (livesLeft <= 10 && i < livesLeft));	
			livesImages[i].setLocation(((double)i/(double)Math.max(livesLeft, 3))*HEART_AREA_WIDTH, 0);
		}
	}
	
	public static void adjustEnergyBar(double percentage) {
		currentEnergyBar.setImage(ImageTransformer.crop(Data.energyBar, percentage*Data.energyBar.getWidth()).getImage());
	}
	
	public static void resetEnergyBar() {
		FruitFever.player.currentEnergy = FruitFever.player.maxEnergy;
		adjustEnergyBar(1.0);
	}
	
	public static void adjustHealthBar(double percentage) {
		currentHealthBar.setImage(ImageTransformer.crop(Data.healthBar, percentage*Data.healthBar.getWidth()).getImage());
	}
	
	public static void resetHealthBar() {
		FruitFever.player.currentHealth = FruitFever.player.maxHealth;
		adjustHealthBar(1.0);
	}
	
	public static void redrawLivesLabel(int current, int max) {
	
		numberOfLives.setLabel("x " + current);
			
		for (int i = 0; i < max; i++) {
			livesImages[i].setVisible(i < 1);
			livesImages[i].setLocation(0, 0);
		}
	}
	
	/** Animates all text in an ArrayList, removing the inactive ones **/
	public void animateAndRemoveText(ArrayList<TextAnimator> arr) {
		for (int i = 0; i < arr.size(); i++)
			if (!animateText(arr.get(i))) {
				remove(arr.get(i).label);
				arr.remove(i);
				i--;
			}
	}
	
	/** Animates a text returning true if active **/
	public boolean animateText(TextAnimator obj) {
		if (obj.active)
			obj.animate();
		
		return obj.active;
	}
	
	public void setHint(TextAnimator obj) {
		
		if (FruitFever.hintText != null)
			remove(FruitFever.hintText.label);
			
		FruitFever.hintText = obj;
		add(obj.label);
	
	}
	
	public void toggleVisibility(GObject obj) {
		obj.setVisible(!obj.isVisible());
	}
	
	/** This will center an object horizontally (with either a positive or negative offset)
	  * and at the desired vertical position **/
	private void centerObject(GObject obj, int xOffset, int yPosition) {
		obj.setLocation( (int) ((FruitFever.SCREEN_WIDTH - obj.getWidth())/2) + xOffset, yPosition);
	
	}
	
	/** This will center an object horizontally and at the desired vertical position **/
	private void centerObject(GObject obj, int yPosition) {
		centerObject(obj, 0, yPosition);
	}
	
	/** This will center an object horizontally and vertically **/
	private void centerObject(GObject obj) {
		centerObject(obj, 0, (int) ((FruitFever.SCREEN_HEIGHT - obj.getHeight())/2));
	}
	
	/** Adds a list of buttons to the screen **/
	public static void addButtonsToScreen(ArrayList<Button> arr) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).type == Button.Type.SLIDER)
				fruitFever.add(arr.get(i).bar);
			fruitFever.add(arr.get(i).image);
		}
	}
	
	/** Removes a list of buttons from the screen **/
	public static void removeButtonsFromScreen(ArrayList<Button> arr) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).type == Button.Type.SLIDER)
				fruitFever.remove(arr.get(i).bar);
			fruitFever.remove(arr.get(i).image);
		}
	}
	
	public void add(GObject... objects) {
		for (GObject obj : objects)
			fruitFever.add(obj);
	}
	
	public void add(GObject[]... arrays) {
		for (GObject[] array : arrays)
			add(array);
	}
	
	public void add(GObject[][]... setOfArrays) {
		for (GObject[][] arraySet : setOfArrays)
			add(arraySet);
	}
	
	public void remove(GObject obj) {
		fruitFever.remove(obj);
	}
	
	public void remove(GObject... objects) {
		for (GObject obj : objects)
			fruitFever.remove(obj);
	}
	
	public void removeAll() {
		fruitFever.removeAll();
	}

}