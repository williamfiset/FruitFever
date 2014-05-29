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
	
	static GImage[] livesImages = new GImage[Player.MAX_LIVES];
	static GLabel[] levelNumbers = new GLabel[20];
	static GImage[] levelLocks = new GImage[20];	
	
	public ScreenHandler(FruitFever fruitFever) {
		this.fruitFever = fruitFever;
	}
	
	/** Draws the main menu screen **/
	public void drawMainMenu(){
		fruitFever.removeAll();
		addButtonsToScreen(FruitFever.mainMenuButtons);
		fruitFever.add(Data.fruitFeverTitle);
		fruitFever.levelSelectionPage = 0;
		
		fruitFever.currentScreen = FruitFever.ScreenMode.MAIN_MENU;
	}
	
	/** Draws the level selection screen **/
	public void drawLevelSelection(){
		fruitFever.removeAll();
		fruitFever.add(Data.levelSelectionBackDrop);
		addButtonsToScreen(fruitFever.levelSelectionButtons);
		
		for (int i = 0; i < levelNumbers.length; i++) {
			fruitFever.add(levelNumbers[i]);			
			fruitFever.add(levelLocks[i]);
		}
		shiftLevelLabels(0);
			
		fruitFever.currentScreen = FruitFever.ScreenMode.LEVEL_SELECTION;
	}
	
	/** Shifts the level selection screen by a positive or negative integer value **/
	public void shiftLevelLabels(int shift) {
		for (int i = 0; i < 20; i++) {
			levelNumbers[i].setLabel(String.valueOf(Integer.valueOf(levelNumbers[i].getLabel()) + shift));
			levelNumbers[i].setLocation((int) (fruitFever.SCREEN_WIDTH/2 - levelNumbers[i].getWidth()/2 - 90 + (i%4)*60), 132 + (i/4)*55);
			
			int level = Integer.valueOf(levelNumbers[i].getLabel());
			
			// Sets visibility of lock and label
			levelLocks[i].setVisible(fruitFever.levelInformation[level].locked);
			levelNumbers[i].setVisible(!fruitFever.levelInformation[level].locked);
			
			// Set active state of buttons
			fruitFever.levelSelectionButtons.get(i).active = !fruitFever.levelInformation[level].locked;
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

		addButtonsToScreen(fruitFever.inGameButtons);
		
	}
	
	/** Adds heart images to screen **/
	public void addHearts() {
		for (int i = 0; i < Player.MAX_LIVES; i++) {
			livesImages[i] = new GImage(Data.heartImage.getImage());
			livesImages[i].setLocation(i*Data.TILE_SIZE, 0);
			fruitFever.add(livesImages[i]);
		}
	}
	
	/** Redraws the hearts according to the amount of lives left */
	public void adjustHearts(int livesLeft) {
		for (int i = 0; i < Player.MAX_LIVES; i++)
			livesImages[i].setVisible(livesLeft > i);
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