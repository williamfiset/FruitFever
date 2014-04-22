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
import java.util.*;

public class FruitFever extends GraphicsProgram {

		protected final static int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 500;

		public static Block[] blocks;
		public static Scenery[] scenery;
		public static ArrayList<Animation> animations = new ArrayList<Animation>();
		
		public static int currentLevel = 1;
		
		@Override public void init() {
			
			setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			displayInitialScreenGraphics();

			// Set up keyboard and mouse
			addMouseListeners();
			addKeyListeners();
			
			/** Example of Animation and MovingAnimation and Swirl classes **/
			
			animations.add(new Animation(50, 50, Data.berryAnimation, true, 3, true));
			animations.add(new Animation(150, 75, Data.berryAnimation, true, 2, false));
			animations.add(new Animation(250, 50, Data.swirlAnimation, false, 1, true));
			
			animations.add(new MovingAnimation(350, 50, Data.swirlAnimation, false, 1, false, 10, 5));
			animations.add(new Swirl(250, 50, 10, 5));
			
			for(Animation a : animations){
				a.currentImage.setLocation(a.x, a.y);
				add(a.currentImage);
			}
			
			for(MovingAnimation a : movingAnimations){
				a.currentImage.setLocation(a.x, a.y);
				add(a.currentImage);
			}
			/** End of Example of Animation and MovingAnimation and Swirl classes **/
		}
		
		/** Contains the main game loop **/
		@Override public void run(){
			
			
			while(true){
			
				/** Animate all objects (Animation, MovingAnimation, Swirl)**/
				for(Animation a : animations)
					a.animate();
				
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
