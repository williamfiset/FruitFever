/** 
  * GameStarter - Starts the application by putting the Applet into a JFrame.
  * 
  * @Author William Fiset
  * 
  * April 21, 2014
  */

import java.awt.*;
import javax.swing.*;

public class GameStarter {

	// Used PhotoShop to calculate the Border Height
	final static int FRAME_BORDER_HEIGHT = 22;
	static JFrame appletFrame;

	public static void main(String... args) {	
		
		setMode(args);

		FruitFever gameApplet = new FruitFever();
		appletFrame = new JFrame();

		// Center Frame to the middle of screen on start
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int screenStartX = screenDimension.width/2 - appletFrame.getSize().width/2;
		int screenStartY = screenDimension.height/2 - appletFrame.getSize().height/2;
   		appletFrame.setLocation(screenStartX - FruitFever.SCREEN_WIDTH/2, screenStartY - FruitFever.SCREEN_HEIGHT/2 - FRAME_BORDER_HEIGHT/2);

   		// Set Applet Default Settings
		gameApplet.setSize(FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT);
		gameApplet.init();

		// Set Frame default settings
		appletFrame.setResizable(false);
		appletFrame.setSize(FruitFever.SCREEN_WIDTH , FruitFever.SCREEN_HEIGHT + FRAME_BORDER_HEIGHT);
		appletFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		appletFrame.setTitle("Fruit Fever - Micah & Will");	
		appletFrame.setVisible(true); 

		// Add Applet to Frame and start
    	appletFrame.add(gameApplet);
		gameApplet.start();
    	
		tasksBeforeProgramQuits();

	}

	/*
	 * Sets the game mode for convenient testing. 
	 */
	private static void setMode(String [] modes) {

		for (int mode = 0; mode < modes.length; mode++) {
			switch (modes[mode]) {
				case "debug": FruitFever.debugMode = true; break;
				case "!debug": FruitFever.debugMode = false; break;
				case "level=": FruitFever.currentLevel = Integer.valueOf(modes[mode + 1]); mode++; break;
				default: FruitFever.MAIN_LOOP_SPEED = Integer.valueOf(modes[0]);
			}
		}

	}

	/** Make a new thread that monitors when the program quits **/
	private static void tasksBeforeProgramQuits() {

		// The code within this will execute when the program exits for good
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() { public void run() {

    		// Things you want to do before quiting FruitFever (save?)
    		executePythonScript();

		 }}));
	}

	private static void executePythonScript() {

		try {

			// Calls the Python script telling it to delete all class files the cwd
			ProcessBuilder processStarter = new ProcessBuilder("python","dc.py", "./", ".class");
			processStarter.start();
			
		} catch(Exception e) { }
		
	}

}