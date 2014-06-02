/**
 * ScreenHandler - Helps the main class of the game manage the images displayed on the screen.
 *
 * @Author Micah Stairs, William Fiset
 *
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class ScreenHandler {
	
	static FruitFever fruitFever;
	
	/** Level selection screen**/
	static GLabel[] levelNumbers = new GLabel[20];
	static GImage[] levelLocks = new GImage[20];	
	static GImage[][] levelStars = new GImage[20][3];	
	static GImage[][] levelNoStars = new GImage[20][3];
	static String[] levelSelectionPages = new String[]{"0-19", "20-39", "40-59", "60-79", "80-99"};
	static GLabel levelRange = new GLabel(levelSelectionPages[0]);
	
	static GLabel numberOfFruitRings = new GLabel(" x 0");
	static GImage[] livesImages = new GImage[Player.MAX_LIVES];
	static int HEART_AREA_WIDTH = 3*Data.TILE_SIZE;
	
	public ScreenHandler(FruitFever fruitFever) {
		this.fruitFever = fruitFever;
	}
	
	/** Set starting positions **/
	public void setStartingLocations() {
		
		/** Adds lock and star images to array, setting positions **/
		for (int i = 0; i < 20; i++) {
			levelLocks[i] = new GImage(Data.locked.getImage());
			levelLocks[i].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - 115 + (i%4)*60), 97 + (i/4)*55);
			
			for (int j = 0; j < 3; j++) {
				levelStars[i][j] = new GImage(Data.starIcon.getImage());
				levelStars[i][j].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - 110 + (i%4)*60 + 12*j), 127 + (i/4)*55);
				levelNoStars[i][j] = new GImage(Data.noStarIcon.getImage());
				levelNoStars[i][j].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - 110 + (i%4)*60 + 12*j), 127 + (i/4)*55);
			}
		}
		
		/** Level page range label **/
		levelRange.setFont(new Font("Helvetica", Font.BOLD, 35));
		
		/** Create numbers to display in the level boxes **/
		for (int i = 0; i < 20; i++) {
			levelNumbers[i] = new GLabel(String.valueOf(i));
			levelNumbers[i].setFont(new Font("Helvetica", Font.BOLD, 30));
		}
		
	}
	
	/** Draws the main menu screen **/
	public void drawMainMenu() {
		fruitFever.removeAll();
		addButtonsToScreen(FruitFever.mainMenuButtons);
		fruitFever.add(Data.fruitFeverTitle);
		fruitFever.levelSelectionPage = 0;
		
		fruitFever.currentScreen = FruitFever.ScreenMode.MAIN_MENU;
	}
	
	/** Draws the level selection screen **/
	public void drawLevelSelection() {
		fruitFever.removeAll();
		fruitFever.add(Data.levelSelectionBackDrop);
		addButtonsToScreen(fruitFever.levelSelectionButtons);
		
		for (int i = 0; i < levelNumbers.length; i++) {
			fruitFever.add(levelNumbers[i]);			
			fruitFever.add(levelLocks[i]);
			for (int j = 0; j < 3; j++) {
				fruitFever.add(levelStars[i][j]);
				fruitFever.add(levelNoStars[i][j]);
			}
			
		}
		
		fruitFever.add(levelRange);
		
		shiftLevelLabels(0);
			
		fruitFever.currentScreen = FruitFever.ScreenMode.LEVEL_SELECTION;
	}
	
	/** Shifts the level selection screen by a positive or negative integer value **/
	public void shiftLevelLabels(int shift) {
	
		levelRange.setLabel(levelSelectionPages[FruitFever.levelSelectionPage]);
		levelRange.setLocation((int) (fruitFever.SCREEN_WIDTH/2 - levelRange.getWidth()/2), 402);
		
		for (int i = 0; i < 20; i++) {
			levelNumbers[i].setLabel(String.valueOf(Integer.valueOf(levelNumbers[i].getLabel()) + shift));
			levelNumbers[i].setLocation((int) (fruitFever.SCREEN_WIDTH/2 - levelNumbers[i].getWidth()/2 - 90 + (i%4)*60), 128 + (i/4)*55);
			
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
					} else{
						levelStars[i][j].setVisible(false);
						levelNoStars[i][j].setVisible(true);
					}
				}
			}
		}
	}
	
	/** Adds a black background to screen **/
	public void addBackground(){
		GRect background = new GRect(0,0, FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT);
		background.setFillColor(Color.BLACK);
		background.setFilled(true);
		fruitFever.add(background);
	}
	
	/** Adds all blocks, things, fruits, and enemies to the screen **/
	public void addImagesToScreen(){

		for (Block obj : fruitFever.blocks){
			obj.image.setLocation(obj.getX(), obj.getY());
			fruitFever.add(obj.image);
		}
		
		for (Thing thing : fruitFever.things) {
			thing.image.setLocation(thing.getX(), thing.getY());
			fruitFever.add(thing.image);
		}
		
		for (Thing item : fruitFever.edibleItems) {
			item.image.setLocation(item.getX(), item.getY());
			fruitFever.add(item.image);
		}
		
		for (Enemy enemy : fruitFever.enemies) {
			enemy.image.setLocation(enemy.getX(), enemy.getY());
			fruitFever.add(enemy.image);
			fruitFever.add(enemy.healthBarBackground);
			fruitFever.add(enemy.healthBar);
		}

		fruitFever.add(fruitFever.player.image);
		fruitFever.add(fruitFever.player.swirl.image);

		addHearts();
		
		/** Add fruit ring icon at the top of the screen **/
		Data.fruitRingAnimation[5].setLocation(Data.TILE_SIZE*13, 0);
		fruitFever.add(Data.fruitRingAnimation[5]);
		numberOfFruitRings.setColor(Color.white);
		numberOfFruitRings.setLocation(Data.TILE_SIZE*14, 16);
		fruitFever.add(numberOfFruitRings);
		
		addButtonsToScreen(fruitFever.inGameButtons);
		
	}
	
	public void addFruitRing() {
		numberOfFruitRings.setLabel("x " + ++FruitFever.currentFruitRings);
	}
	
	/** Adds heart images to screen **/
	public void addHearts() {
		for (int i = 0; i < Player.MAX_LIVES; i++) {
			livesImages[i] = new GImage(Data.heartImage.getImage());
			livesImages[i].setLocation(((double)i/(double)Player.MAX_LIVES)*HEART_AREA_WIDTH, 0);
			// livesImages[i].setLocation(i*Data.TILE_SIZE, 0);
			fruitFever.add(livesImages[i]);
		}
	}
	
	/** Redraws the hearts according to the amount of lives left */
	public static void adjustHearts(int livesLeft) {
		for (int i = 0; i < Player.MAX_LIVES; i++) {
			livesImages[i].setVisible(livesLeft > i);
			livesImages[i].setLocation(((double)i/(double)livesLeft)*HEART_AREA_WIDTH, 0);
		}
	}
	
	/** Animates all text in an ArrayList, removing the inactive ones **/
	public void animateAndRemoveText(ArrayList<TextAnimator> arr) {
			
		for (int i = 0; i < arr.size(); i++)
			if (arr.get(i).active)
				arr.get(i).animate();
			else {
				fruitFever.remove(arr.get(i).label);
				arr.remove(i);
				i -= 1;
				continue;
			}
	}
	
	/** Adds a list of buttons to the screen **/
	public void addButtonsToScreen(ArrayList<Button> arr) {
		for (int i = 0; i < arr.size(); i++)
			fruitFever.add(arr.get(i).image);
	}
	
	public void add(GObject obj) {
		fruitFever.add(obj);
	}
	
	public void remove(GObject obj) {
		fruitFever.remove(obj);
	}
	
	public void removeAll() {
		fruitFever.removeAll();
	}


}