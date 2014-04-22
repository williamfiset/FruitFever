/**
 * @version: 0.0.0.1
 *
 * @Author William Fiset
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
		
		public static void main(String[] args) {
			new FruitFever().start();
		}

		@Override public void init() {
			
			// Set background (Shove elsewhere)			
			GRect background = new GRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
			background.setFillColor(Color.BLACK);
			background.setFilled(true);
			add(background);

			Data.loadImages();
			Data.loadBlocks("../levels/levels.txt", 1);

			for(int i = 0; i < blocks.length; i++){

				blocks[i].image.setLocation(blocks[i].x, blocks[i].y);
				add(blocks[i].image);

			}

			for(int i = 0; i < scenery.length; i++){

				scenery[i].image.setLocation(scenery[i].x, scenery[i].y);
				add(scenery[i].image);

			}

			// Set up keyboard and mouse
			addMouseListeners();
			addKeyListeners();

		}
		
		/** Contains the main game loop **/
		@Override public void run(){

			while(true){
				pause(30);
			}
			
		}
		
		@Override public void keyPressed(KeyEvent key){}
		
		@Override public void keyReleased(KeyEvent key) {}
		
		@Override public void mousePressed(MouseEvent mouse) {
			
			
		}
	}
