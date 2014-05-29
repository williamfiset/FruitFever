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
	
	public ScreenHandler(FruitFever fruitFever) {
		this.fruitFever = fruitFever;
	}
	
	/** Draws the main menu screen **/
	static public void drawMainMenu(){
		FruitFever.screen.removeAll();
		fruitFever.addButtonsToScreen(FruitFever.mainMenuButtons);
		FruitFever.screen.add(Data.fruitFeverTitle);
		FruitFever.levelSelectionPage = 0;
		
		FruitFever.currentScreen = FruitFever.ScreenMode.MAIN_MENU;
	}


}