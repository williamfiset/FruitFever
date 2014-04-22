/**
 * @version: 0.0.0.2
 *
 * @Author William Fiset, Micah Stairs
 *
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import java.awt.*;

public class FruitFever extends GraphicsProgram {

		protected final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500;

		public static Block[] blocks;
		public static Scenery[] scenery;
		public static Animation[] animations = new Animation[3];
		
		public static int currentLevel = 1;
		
		@Override public void init() {
			
			setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			displayInitialScreenGraphics();

			// Set up keyboard and mouse
			addMouseListeners();
			addKeyListeners();
			
			/** Example of Animation class **/
			
			animations[0] = new Animation(50, 50, Data.berryAnimation, true, 3);
			animations[1] = new Animation(150, 75, Data.berryAnimation, true, 2);
			animations[2] = new Animation(250, 50, Data.swirlAnimation, false, 1);
			
			for(int i = 0; i < animations.length; i++){
				animations[i].currentImage.setLocation(animations[i].x, animations[i].y);
				add(animations[i].currentImage);
			}
			
			/** End of Example of Animation class **/
		}
		
		/** Contains the main game loop **/
		@Override public void run(){
			
			
			while(true){
			
				/** Example of Animation class **/
				
				for(int i = 0; i < animations.length; i++)
					animations[i].animate();
					
				/** End of Example of Animation class **/
				pause(30);
			}
			
		}
		
		@Override public void keyPressed(KeyEvent key){}
		
		@Override public void keyReleased(KeyEvent key) {}
		
		@Override public void mousePressed(MouseEvent mouse) { }
		

		/** Loads and Displays all initial graphics on the screen  **/
		private void displayInitialScreenGraphics(){

			// Creates a black background on the screen
			GRect background = new GRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
			background.setFillColor(Color.BLACK);
			background.setFilled(true);
			add(background);

			// Renders Images in the Data class
			Data.loadImages();
			Data.loadObjects("../levels/levels.txt", currentLevel);
			
			// Displays all blocks on-screen
			for(int i = 0; i < blocks.length; i++){
				blocks[i].image.setLocation(blocks[i].x, blocks[i].y);
				add(blocks[i].image);
			}

			// Displays all Scenery on-screen (plants, trees, mushrooms... )
			for(int i = 0; i < scenery.length; i++){
				scenery[i].image.setLocation(scenery[i].x, scenery[i].y);
				add(scenery[i].image);
			}
		}

	}
