 /** 
  * GameStarter - Starts the application by putting the Applet into a JFrame.
  * 
  * @Author William Fiset
  * @Version 0
  * 
  * April 21, 2014
  */

import java.awt.*;
import javax.swing.*;

public class GameStarter{

	// Used PhotoShop to calculate the Border Height
	final static int FRAME_BORDER_HEIGHT = 22;

	public static void main(String[] args) {
		
		FruitFever gameApplet = new FruitFever();
		JFrame appletFrame = new JFrame();

		// Center Frame to the middle of screen on start
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int screenStartX = screenDimension.width/2 - appletFrame.getSize().width/2;
		int screenStartY = screenDimension.height/2 - appletFrame.getSize().height/2;
		appletFrame.setLocation(screenStartX - FruitFever.SCREEN_WIDTH/2, screenStartY - FruitFever.SCREEN_HEIGHT/2);

		// appletFrame.setLayout(new GridLayout(1,0));

		// Set default program values
		appletFrame.setResizable(false);
		appletFrame.setSize(FruitFever.SCREEN_WIDTH, FruitFever.SCREEN_HEIGHT+FRAME_BORDER_HEIGHT);
	  	appletFrame.setVisible(true); 
		appletFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
   		appletFrame.setTitle("Fruit Fever - Micah & Will");		


		gameApplet.init();

		// Add Applet to Frame and start
    	appletFrame.add(gameApplet);

		gameApplet.start();
    	

	}
}