/**
*	Data - Loads images from file and stores them as GImages or arrays of GImages.
* 
* @Author Micah Stairs
*
**/

import acm.graphics.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;

public abstract class Data{

	public static int TILE_SIZE = 25;

	public static BufferedImage sheet = null;
	public static GImage heartImage, levelBackDropImage, lavaImage,
						 purpleBallSmall, purpleBallBig, fireBallSmall, fireBallBig;
	public static GImage[] blockImages = new GImage[15],
						   blockGrassImages = new GImage[15],
						   sceneryImages = new GImage[16],
						   swirlAnimation = new GImage[6],
						   vortexAnimation = new GImage[5],
						   blueFruitAnimation = new GImage[5],
						   yellowFruitAnimation = new GImage[6],
						   redFruitAnimation = new GImage[7],
						   gearButton = new GImage[3],
						   playerStill = new GImage[1],
						   playerStillH = new GImage[1],
						   playerTongue = new GImage[9],
						   playerTongueH = new GImage[9],
						   playerShoot = new GImage[6],
						   playerShootH = new GImage[6],
						   fuzzyEnemyAttack = new GImage[4],
						   fuzzyEnemyAttackH = new GImage[4],
						   fuzzyEnemyMoving = new GImage[3],
						   fuzzyEnemyMovingH = new GImage[3],
						   fuzzyShot = new GImage[8],
						   wormEnemyMoving = new GImage[4],
						   wormEnemyMovingH = new GImage[4],
						   menuButtons = new GImage[12],
						   leftArrowButton = new GImage[3],
						   rightArrowButton = new GImage[3],
						   levelButton = new GImage[2];
	    
/** Loads all the images from the sprite sheet **/
	public static void loadImages(){
			
		/** Import sprite-sheet **/
		try { sheet = ImageIO.read(new File("../img/spriteSheet.png"));	}
		catch (IOException e) {	e.printStackTrace(); }
	
		// Blocks
		for(int i = 0; i < 15; i++){
			blockImages[i] = makeImage(sheet, 0, TILE_SIZE*i, TILE_SIZE, TILE_SIZE);
			blockGrassImages[i] = makeImage(sheet, TILE_SIZE, TILE_SIZE*i, TILE_SIZE, TILE_SIZE);
		}

		// Scenery (Top Row in sheet)
		sceneryImages[0] = makeImage(sheet, TILE_SIZE*2, TILE_SIZE*7, TILE_SIZE*2, TILE_SIZE);
		sceneryImages[1] = makeImage(sheet, TILE_SIZE*4, TILE_SIZE*7, TILE_SIZE, TILE_SIZE);
		sceneryImages[2] = makeImage(sheet, TILE_SIZE*5, TILE_SIZE*7, TILE_SIZE, TILE_SIZE);
		sceneryImages[3] = makeImage(sheet, TILE_SIZE*6, TILE_SIZE*7, TILE_SIZE, TILE_SIZE);
		sceneryImages[4] = makeImage(sheet, TILE_SIZE*7, TILE_SIZE*7, TILE_SIZE, TILE_SIZE);
		sceneryImages[5] = makeImage(sheet, TILE_SIZE*8, TILE_SIZE*7, TILE_SIZE, TILE_SIZE);
		sceneryImages[6] = makeImage(sheet, TILE_SIZE*9, TILE_SIZE*7 - 2, TILE_SIZE + 5, TILE_SIZE + 2);

		// Scenery (Bottom Row in sheet)
		sceneryImages[7] = makeImage(sheet, TILE_SIZE*2, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[8] = makeImage(sheet, TILE_SIZE*3, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[9] = makeImage(sheet, TILE_SIZE*4, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[10] = makeImage(sheet, TILE_SIZE*5, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[11] = makeImage(sheet, TILE_SIZE*6, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[12] = makeImage(sheet, TILE_SIZE*7, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[13] = makeImage(sheet, TILE_SIZE*8, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[14] = makeImage(sheet, TILE_SIZE*9, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		sceneryImages[15] = makeImage(sheet, TILE_SIZE*10, TILE_SIZE*8, TILE_SIZE, TILE_SIZE);
		
		// Swirl Animation Images
		for(int i = 0; i < 6; i++)
			swirlAnimation[i] = makeImage(sheet, TILE_SIZE*(i + 2), 0, TILE_SIZE, TILE_SIZE);
			
		// Vortex Animation Images
		for(int i = 0; i < 5; i++)
			vortexAnimation[i] = makeImage(sheet, TILE_SIZE*11, TILE_SIZE*(i + 3), TILE_SIZE*2, TILE_SIZE);
		
		// Gear Button Images
		for(int i = 0; i < 3; i++)
			gearButton[i] = makeImage(sheet, TILE_SIZE*(i + 2), TILE_SIZE*3, TILE_SIZE, TILE_SIZE);
		
		// Berry Animation Images
		for(int i = 0; i < 5; i++)
			blueFruitAnimation[i] = makeImage(sheet, TILE_SIZE*(i + 2), TILE_SIZE*13, TILE_SIZE, TILE_SIZE);

		for(int i = 0; i < 6; i++)
			yellowFruitAnimation[i] = makeImage(sheet, TILE_SIZE*(i + 2), TILE_SIZE*14, TILE_SIZE, TILE_SIZE);	
			
		for(int i = 0; i < 7; i++)
			redFruitAnimation[i] = makeImage(sheet, TILE_SIZE*(i + 2), TILE_SIZE*15, TILE_SIZE, TILE_SIZE);

		// Fuzzy Projectile Images
		for(int i = 0; i < 8; i++)
			fuzzyShot[i] = makeImage(sheet, TILE_SIZE*(i + 20), TILE_SIZE*4, TILE_SIZE, TILE_SIZE);

		// Heart Image
		heartImage = makeImage(sheet, TILE_SIZE*2, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		
		// Fireball and Purple Ball Images
		purpleBallSmall = makeImage(sheet, TILE_SIZE*2, TILE_SIZE, TILE_SIZE*2, TILE_SIZE);
		purpleBallBig = makeImage(sheet, TILE_SIZE*4, TILE_SIZE, TILE_SIZE, TILE_SIZE);
		fireBallSmall = makeImage(sheet, TILE_SIZE*5, TILE_SIZE, TILE_SIZE*2, TILE_SIZE);
		fireBallBig = makeImage(sheet, TILE_SIZE*7, TILE_SIZE, TILE_SIZE, TILE_SIZE);

		// Lava Image
		lavaImage = makeImage(sheet, TILE_SIZE*6, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);

		/** Player Images **/
		
		playerStill[0] = makeImage(sheet, TILE_SIZE*13, 0, TILE_SIZE*3, TILE_SIZE);
		playerStillH[0] = ImageTransformer.horizontalFlip(playerStill[0]);
		
		for(int i = 0; i < 9; i++){
			playerTongue[i] = makeImage(sheet, TILE_SIZE*13, TILE_SIZE*i, TILE_SIZE*3, TILE_SIZE);
			playerTongueH[i] = ImageTransformer.horizontalFlip(playerTongue[i]);
		}
		for(int i = 0; i < 6; i++){
			playerShoot[i] = makeImage(sheet, TILE_SIZE*16, TILE_SIZE*i, TILE_SIZE*3, TILE_SIZE); 
			playerShootH[i] = ImageTransformer.horizontalFlip(playerShoot[i]);	
		}
		
		/** Enemy Images **/
		
		for(int i = 0; i < 4; i++){
			fuzzyEnemyAttack[i] = makeImage(sheet, TILE_SIZE*(2*i + 20), TILE_SIZE, TILE_SIZE*2, TILE_SIZE); 
			fuzzyEnemyAttackH[i] = ImageTransformer.horizontalFlip(fuzzyEnemyAttack[i]);	
		}
		
		for(int i = 0; i < 3; i++){
			fuzzyEnemyMoving[i] = makeImage(sheet, TILE_SIZE*(2*i + 20), TILE_SIZE*3, TILE_SIZE*2, TILE_SIZE); 
			fuzzyEnemyMovingH[i] = ImageTransformer.horizontalFlip(fuzzyEnemyMoving[i]);	
		}
		
		for(int i = 0; i < 4; i++){
			wormEnemyMoving[i] = makeImage(sheet, TILE_SIZE*(2*i + 20), TILE_SIZE*5, TILE_SIZE*2, TILE_SIZE); 
			wormEnemyMovingH[i] = ImageTransformer.horizontalFlip(wormEnemyMoving[i]);	
		}
		
		
		/** Import menu images **/
		try { sheet = ImageIO.read(new File("../img/Menu/Menu_Red.png"));	}
		catch (IOException e) {	e.printStackTrace(); }
		
		// Menu Button Images
		for(int i = 0; i < 12; i++)
			menuButtons[i] = makeImage(sheet, 0, i*69, 266, 69);
		
		/** Import level selection arrow images **/
		try { sheet = ImageIO.read(new File("../img/LevelSelection/arrows/blueArrows.png"));	}
		catch (IOException e) {	e.printStackTrace(); }
		
		for(int i = 0; i < 3; i++){
			leftArrowButton[i] = makeImage(sheet, 0, i*33, 36, 31);
			rightArrowButton[i] = makeImage(sheet, 36, i*33, 36, 31);
		}
		
		/** Import level selection background/level button images **/
		try { sheet = ImageIO.read(new File("../img/LevelSelection/backDrop/blueLevel.png"));	}
		catch (IOException e) {	e.printStackTrace(); }

		levelButton[0] = makeImage(sheet, 0, 139, 51, 45);
		levelButton[1] = makeImage(sheet, 0, 93, 50, 44);
		levelBackDropImage = makeImage(sheet, 70, 0, 260, 333);
		FruitFever.levelBackDrop = new Thing((int) (FruitFever.SCREEN_WIDTH/2 - levelBackDropImage.getWidth()/2), (int) (FruitFever.SCREEN_HEIGHT/2 - levelBackDropImage.getHeight()/2), levelBackDropImage);

		// Create numbers to display in the level boxes
		for(int i = 0; i < 20; i++){
			FruitFever.levelNumbers[i] = new GLabel(String.valueOf(i));
			FruitFever.levelNumbers[i].setFont(new Font("Helvetica", Font.BOLD, 30));
			FruitFever.levelNumbers[i].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - FruitFever.levelNumbers[i].getWidth()/2 - 90 + (i%4)*60), 132 + (i/4)*55);
		}

		/** Add buttons and set locations **/
		addButtonsToArrayList();

	}
		
/** Used to help get the sub-images from the sprite-sheet **/
	private static GImage makeImage(BufferedImage i, int x, int y, int width, int height){
		return new GImage(i.getSubimage(x, y, width, height).getScaledInstance(-50, -50, 0));
	}

/** Loads objects from the file **/
	public static void loadObjects(String fileName, int level){
		
		try{

			Scanner sc = new Scanner(new File(fileName));
			
			// Clear ArrayLists
			FruitFever.blocks.clear();
			FruitFever.things.clear();

			/** Find the correct level **/
			while(!sc.nextLine().equals(String.valueOf(level))){}
			
			// Get the name of the level
			FruitFever.LEVEL_NAME = sc.nextLine();

			int lineNumber = 0;
			String line = "";

			/** BLOCKS (as well as Player, Lava, and Fruits) **/

			while(sc.hasNextLine() && !(line = sc.nextLine()).equals("") && !line.equals("+")){

				// Iterate through each character in the line, instantiating the specified block (if it exists)
				for(int i = 0; i < line.length(); i++){

					char character = line.charAt(i);

					// Skip if it's a blank
					if(character == '-' || character == ' ')
						continue;
						
					// Lava
					if(character == '~'){
						FruitFever.things.add(new Thing(i*TILE_SIZE, lineNumber*TILE_SIZE, lavaImage));
						continue;
					}
					
					// Set the player's start position
					if(character == '@'){
						FruitFever.playerStartX = i*TILE_SIZE;
						FruitFever.playerStartY = lineNumber*TILE_SIZE;
						continue;
					}
					
					// Reads in a fruit
					if(Character.isDigit(character)){
						if(character == '0')
							FruitFever.fruits.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, Data.blueFruitAnimation, true, 3, true, 2));
						else if(character == '1')
							FruitFever.fruits.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, Data.yellowFruitAnimation, true, 3, true, 2));
						else if(character == '2')
							FruitFever.fruits.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, Data.redFruitAnimation, true, 3, true, 2));
						continue;
					}
					

					try{

						GImage image;
						int type;

						// Normal Blocks
						if(character - 'a' >= 0){
							type = character - 'a';
							image = Data.blockImages[type];
						}
						// Grass Blocks
						else{
							type = character - 'A';
							image = Data.blockGrassImages[type];
						}


						// Add Block to the ArrayList
						FruitFever.blocks.add(new Block(i*TILE_SIZE, lineNumber*TILE_SIZE, type, image));

					} catch(ArrayIndexOutOfBoundsException e){ 
						System.out.printf("\nBLOCK LAYER contains invalid character: '%c' \n", character);
						System.exit(0);
					}
				}

				lineNumber++;

			}

			lineNumber = 0;

			/** SCENERY **/

			while(sc.hasNextLine() && !(line = sc.nextLine()).equals("") && !line.equals("+")){

				// Iterate through each character in the line, instantiating the specified block (if it exists)
				for(int i = 0; i < line.length(); i++){

					char character = line.charAt(i);

					// Skip if it's a blank
					if(character == '-' || character == '#' || character == ' ')
						continue;

					try{

						int type = character - 'a';
						GImage image = new GImage(Data.sceneryImages[type].getImage());
						int xOffset = 0, yOffset = 0;

						// Hard-Coded Exceptions
						if(type == 0)
							xOffset = -TILE_SIZE/2;
						else if(type == 6)
							xOffset = -3;

						// Add Scenery to the ArrayList
						FruitFever.things.add(new Scenery(i*TILE_SIZE + xOffset, lineNumber*TILE_SIZE + yOffset, type, image));

					} catch(ArrayIndexOutOfBoundsException e){ 
						System.out.printf("\nSCENERY LAYER contains invalid character: '%c' \n", character);
						System.exit(0);
					}	
				}
				lineNumber++;
			}

			sc.close();
		
		}
		catch(NoSuchElementException e){
			System.out.println("Level " + FruitFever.currentLevel + " was not found.\n");
			e.printStackTrace();
			System.exit(0);
		}
		catch(IOException e){
			System.out.println("\n IOException \n");
			e.printStackTrace();
			System.exit(0);
		}
		

	}
	
	public static void addButtonsToArrayList(){
	
		Button tempButton;
		
		// Adds main menu buttons to the ArrayLists
		for(int i = 0; i < 4; i++){
			tempButton = new Button((int) (FruitFever.SCREEN_WIDTH/2 - menuButtons[i/3].getWidth()/2), 100 + 75*i, i, menuButtons[3*i], menuButtons[3*i + 1], menuButtons[3*i + 2]);
			FruitFever.buttons.add(tempButton);
			FruitFever.mainMenuButtons.add(tempButton);
			
		}
		
		// Adds arrow buttons to the ArrayLists
		tempButton = new Button((int) (FruitFever.SCREEN_WIDTH/2 - leftArrowButton[0].getWidth()/2 - 70), 375, 4, leftArrowButton[0], leftArrowButton[1], leftArrowButton[2]);
		FruitFever.buttons.add(tempButton);
		FruitFever.levelSelectionButtons.add(tempButton);
		tempButton = new Button((int) (FruitFever.SCREEN_WIDTH/2 - rightArrowButton[0].getWidth()/2 + 70), 375, 5, rightArrowButton[0], rightArrowButton[1], rightArrowButton[2]);
		FruitFever.buttons.add(tempButton);
		FruitFever.levelSelectionButtons.add(tempButton);
		
		// Level buttons
		for(int i = 0; i < 20; i++){
			tempButton = new Button((int) (FruitFever.SCREEN_WIDTH/2 - 115 + (i%4)*60), 100 + (i/4)*55, 6, levelButton[0], levelButton[1], levelButton[1], i);
			FruitFever.buttons.add(tempButton);
			FruitFever.levelSelectionButtons.add(tempButton);
		}
		
		// Adds gear button to the ArrayLists
		tempButton = new Button((int) FruitFever.SCREEN_WIDTH - 31, 0, 4, gearButton[0], gearButton[1], gearButton[2]);
		FruitFever.buttons.add(tempButton);
		FruitFever.inGameButtons.add(tempButton);
		
	}
	
}