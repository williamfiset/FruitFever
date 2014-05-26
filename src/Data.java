/**
*	Data - Loads images from webserver and stores them as GImages or arrays of GImages.
* 
* @Author Micah Stairs
*
**/

// To-do : Make Fruit Rings appear on top of scenery 

import acm.graphics.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;

public abstract class Data{

	public static final int TILE_SIZE = 25;

	public static BufferedImage sheet = null;
	public static GImage loadingScreenBackground, loadingScreenBar, levelSelectionBackDrop, fruitFeverTitle,
						heartImage,
						lavaImage,
						purpleBallSmall, purpleBallBig, fireBallSmall, fireBallBig,
						checkpointFlagRed, checkpointFlagGreen,
						moss, thickMoss,
						music0, music1,
						bronzeStar, silverStar, goldStar, locked;
	public static GImage[] blockImages = new GImage[18],
						   
						sceneryImages = new GImage[25],
						   
						blueFruit = new GImage[5],
						yellowFruit = new GImage[6],
						redFruit = new GImage[7],	
						purpleFruit = new GImage[10],	
						
						gearButton = new GImage[3],
						fruitRingAnimation = new GImage[6],
						vortexAnimation = new GImage[5],
						powerups = new GImage[3],
						   
						playerStill = new GImage[1],
						playerStillH = new GImage[1],
						playerTongue = new GImage[5],
						playerTongueH = new GImage[5],
						playerShoot = new GImage[6],
						playerShootH = new GImage[6],
						  
						fuzzyEnemyAttack = new GImage[4],
						fuzzyEnemyAttackH = new GImage[4],
						fuzzyEnemyMoving = new GImage[3],
						fuzzyEnemyMovingH = new GImage[3],
						wormEnemyMoving = new GImage[4],
						wormEnemyMovingH = new GImage[4],
						   
						fuzzyShot = new GImage[8],
						swirlAnimation = new GImage[6],
						
						refreshButton = new GImage[3],
						menuButtons = new GImage[12],
						leftArrowButton = new GImage[3],
						rightArrowButton = new GImage[3],
						levelButton = new GImage[2];

	public static GImage[][] fireworkAnimation = new GImage[3][5];	

	/** Loads the images required for the loading screen **/
	public static void loadingScreen(){
		
		sheet = DataLoader.loadImage("img/loadingScreen/loadingScreen" + String.valueOf((int)(Math.random()*4)) + ".png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/loadingScreen/loadingScreen" + String.valueOf((int)(Math.random()*4)) + ".png");
		
		loadingScreenBackground = makeImage(0, 0, 700, 500 );
		loadingScreenBar = makeImage(0, 617, 2, 33);
	
	}
	    
	/** Loads all the images from the sprite sheet **/
	public static void loadImages(){
	
		updateLoadingBar(0.1);
		
		/** Blocks **/
		sheet = DataLoader.loadImage("img/sprites/blocks.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/blocks.png");
	
		for(int i = 0; i < 18; i++)
			blockImages[i] = makeImage(0, TILE_SIZE*i, TILE_SIZE, TILE_SIZE);
		
		updateLoadingBar(0.2);
		
		/** Scenery **/
		sheet = DataLoader.loadImage("img/sprites/plants.png","https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/plants.png");

		// Scenery (Top Row in sheet)
		sceneryImages[0] = makeImage(0, TILE_SIZE, TILE_SIZE*2, TILE_SIZE);
		for (int i = 2; i < 7; i++)
			sceneryImages[i - 1] = makeImage(TILE_SIZE*i, TILE_SIZE, TILE_SIZE, TILE_SIZE);
		sceneryImages[6] = makeImage(TILE_SIZE*7, TILE_SIZE - 2, TILE_SIZE + 5, TILE_SIZE + 2);

		// Scenery (Middle Row in sheet)
		for (int i = 0; i < 9; i++)
			sceneryImages[i + 7] = makeImage(TILE_SIZE*i, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);

		// Scenery (Bottom Row in sheet)
		for (int i = 0; i < 9; i++)
			sceneryImages[i + 16] = makeImage(TILE_SIZE*i, TILE_SIZE*3, TILE_SIZE, TILE_SIZE);
		sceneryImages[24] = makeImage(TILE_SIZE*8, TILE_SIZE*3, TILE_SIZE*2, TILE_SIZE);
		
		// Moss for blocks (Off to the right)
		moss = makeImage(TILE_SIZE*9, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		thickMoss = makeImage(TILE_SIZE*10, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		
		updateLoadingBar(0.3);
		
		/** Fruits **/
		sheet = DataLoader.loadImage("img/sprites/fruits.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/fruits.png");
		
		for(int i = 0; i < 5; i++)
			blueFruit[i] = makeImage(TILE_SIZE*i, 0, TILE_SIZE, TILE_SIZE);

		for(int i = 0; i < 6; i++)
			yellowFruit[i] = makeImage(TILE_SIZE*i, TILE_SIZE, TILE_SIZE, TILE_SIZE);	
			
		for(int i = 0; i < 7; i++)
			redFruit[i] = makeImage(TILE_SIZE*i, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
			
		for(int i = 0; i < 10; i++)
			purpleFruit[i] = makeImage(TILE_SIZE*i, TILE_SIZE*3, TILE_SIZE, TILE_SIZE);
		
		updateLoadingBar(0.4);
		
		/** Miscellaneous **/
		sheet = DataLoader.loadImage("img/sprites/miscellaneous.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/miscellaneous.png");
		
		// Heart Image
		heartImage = makeImage(0, 0, TILE_SIZE, TILE_SIZE);
		
		// Gear Button Images
		for(int i = 0; i < 3; i++)
			gearButton[i] = makeImage(TILE_SIZE*(i + 1), 0, TILE_SIZE, TILE_SIZE);
			
		// Back Button Images
		int randomButtonColor = (int) (Math.random()*4);
		for(int i = 0; i < 3; i++)
			refreshButton[i] = makeImage(TILE_SIZE*randomButtonColor, TILE_SIZE*(i + 5), TILE_SIZE, TILE_SIZE);	
			
		// Lava Image
		lavaImage = makeImage(TILE_SIZE*7, 0, TILE_SIZE, TILE_SIZE);
			
		// Checkpoint Flags
		checkpointFlagRed = makeImage(0, TILE_SIZE, TILE_SIZE, TILE_SIZE*2);
		checkpointFlagGreen = makeImage(TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE*2);
			
		// Fruit Rings Animation Images
		for (int i = 0; i < 6; i++)
			fruitRingAnimation[i] = makeImage(TILE_SIZE*(i + 2), TILE_SIZE*4, TILE_SIZE, TILE_SIZE);
		
		// Vortex Animation Images
		for (int i = 0; i < 5; i++)
			vortexAnimation[i] = makeImage(TILE_SIZE*14, TILE_SIZE*i, TILE_SIZE*2, TILE_SIZE);
		
		// Powerup Blocks
		for (int i = 0; i < 3; i++)
			powerups[i] = makeImage(TILE_SIZE*(i + 11), 0, TILE_SIZE, TILE_SIZE);
	
		// Fireworks
		for (int n = 0; n < 3; n++) 
			for (int i = 0; i < 5; i++) 
				fireworkAnimation[n][i] = makeImage(TILE_SIZE*(i + 2), TILE_SIZE*(n + 1), TILE_SIZE, TILE_SIZE);
				
		// Stars
		bronzeStar = makeImage(TILE_SIZE*8, 0, TILE_SIZE, TILE_SIZE);
		silverStar = makeImage(TILE_SIZE*8, TILE_SIZE*2, TILE_SIZE*2, TILE_SIZE*2);
		goldStar = makeImage(TILE_SIZE*8, TILE_SIZE*4, TILE_SIZE*2, TILE_SIZE*2);
		
		// Locked
		locked = makeImage(TILE_SIZE*10, TILE_SIZE, TILE_SIZE*2, TILE_SIZE*2);
		
		// Notes
		music0 = makeImage(TILE_SIZE*7, TILE_SIZE*1, TILE_SIZE, TILE_SIZE);
		music1 = makeImage(TILE_SIZE*7, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		
		updateLoadingBar(0.5);
	
		/** Projectiles **/
		sheet = DataLoader.loadImage("img/sprites/projectiles.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/projectiles.png");
	
		// Fuzzy Projectile Animation Images
		for(int i = 0; i < 8; i++)
			fuzzyShot[i] = makeImage(TILE_SIZE*i, 0, TILE_SIZE, TILE_SIZE);
			
		// Swirl Animation Images
		for(int i = 0; i < 6; i++)
			swirlAnimation[i] = makeImage(TILE_SIZE*i, TILE_SIZE, TILE_SIZE, TILE_SIZE);
		
		// Fireball and Purple Ball Images
		purpleBallSmall = makeImage(0, TILE_SIZE*2, TILE_SIZE*2, TILE_SIZE);
		purpleBallBig = makeImage(TILE_SIZE*2, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		fireBallSmall = makeImage(TILE_SIZE*3, TILE_SIZE*2, TILE_SIZE*2, TILE_SIZE);
		fireBallBig = makeImage(TILE_SIZE*5, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);

		updateLoadingBar(0.6);
		
		/** Player **/
		sheet = DataLoader.loadImage("img/sprites/player.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/player.png");
		
		playerStill[0] = makeImage(0, 0, TILE_SIZE*3, TILE_SIZE);
		playerStillH[0] = ImageTransformer.horizontalFlip(playerStill[0]);
		
		for(int i = 0; i < 9; i+=2){
			playerTongue[i/2] = makeImage(0, TILE_SIZE*i, TILE_SIZE*3, TILE_SIZE);
			playerTongueH[i/2] = ImageTransformer.horizontalFlip(playerTongue[i/2]);
		}
		for(int i = 0; i < 6; i++){
			playerShoot[i] = makeImage(TILE_SIZE*3, TILE_SIZE*i, TILE_SIZE*3, TILE_SIZE); 
			playerShootH[i] = ImageTransformer.horizontalFlip(playerShoot[i]);	
		}
		
		updateLoadingBar(0.7);
		
		/** Enemies **/
		sheet = DataLoader.loadImage("img/sprites/enemies.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/sprites/enemies.png");
		
		for(int i = 0; i < 4; i++){
			fuzzyEnemyAttack[i] = makeImage(TILE_SIZE*(2*i), 0, TILE_SIZE*2, TILE_SIZE); 
			fuzzyEnemyAttackH[i] = ImageTransformer.horizontalFlip(fuzzyEnemyAttack[i]);	
		}
		
		for(int i = 0; i < 3; i++){
			fuzzyEnemyMoving[i] = makeImage(TILE_SIZE*(2*i), TILE_SIZE*2, TILE_SIZE*2, TILE_SIZE); 
			fuzzyEnemyMovingH[i] = ImageTransformer.horizontalFlip(fuzzyEnemyMoving[i]);	
		}
		
		for(int i = 0; i < 4; i++){
			wormEnemyMoving[i] = makeImage(TILE_SIZE*(2*i), TILE_SIZE*5, TILE_SIZE*2, TILE_SIZE); 
			wormEnemyMovingH[i] = ImageTransformer.horizontalFlip(wormEnemyMoving[i]);
		}
		updateLoadingBar(0.8);
		
		// PICK RANDOM COLOR SCHEME
		
		String color = "";
		int randomColor = (int) (Math.random()*3);
		if(randomColor == 0)
			color = "Orange";
		else if(randomColor == 1)
			color = "Pink";
		else if(randomColor == 2)
			color = "Red";
		
		/** Import menu images **/
		sheet = DataLoader.loadImage("img/Menu/Menu_" + color + ".png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/Menu/Menu_"+color+".png");
		
		// Menu Button Images
		for(int i = 0; i < 12; i++)
			menuButtons[i] = makeImage(0, i*69, 266, 69);
			
		// Fruit Fever Title
		if (color == "Orange")
			fruitFeverTitle = makeImage(267, 212, 351, 36);
		else
			fruitFeverTitle = makeImage(267, 338, 351, 36);
			
		fruitFeverTitle.setLocation(FruitFever.SCREEN_WIDTH/2 - (int)fruitFeverTitle.getWidth()/2, 50);
			
		// PICK RANDOM COLOR SCHEME
		
		color = "";
		randomColor = (int) (Math.random()*3);
		if(randomColor == 0)
			color = "blue";
		else if(randomColor == 1)
			color = "green";
		else if(randomColor == 2)
			color = "purple";
		
		/** Import level selection arrow images **/
		sheet = DataLoader.loadImage("img/LevelSelection/arrows/" + color + "Arrows.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/LevelSelection/arrows/"+color+"Arrows.png");
		
		for(int i = 0; i < 3; i++){
			leftArrowButton[i] = makeImage(0, i*33, 36, 31);
			rightArrowButton[i] = makeImage(36, i*33, 36, 31);
		}
		
		updateLoadingBar(0.9);
		
		/** Import level selection background/level button images **/
		sheet = DataLoader.loadImage("img/LevelSelection/backDrop/" + color + "Level.png", "https://raw.githubusercontent.com/MicahAndWill/FruitFever/master/src/img/LevelSelection/backDrop/"+color+"Level.png");
		
		levelButton[0] = makeImage(0, 139, 51, 45);
		levelButton[1] = makeImage(0, 93, 50, 44);
		levelSelectionBackDrop = makeImage(70, 0, 260, 333);
		FruitFever.levelBackDrop = new Thing((int) (FruitFever.SCREEN_WIDTH/2 - levelSelectionBackDrop.getWidth()/2), (int) (FruitFever.SCREEN_HEIGHT/2 - levelSelectionBackDrop.getHeight()/2), levelSelectionBackDrop);

		// Create numbers to display in the level boxes
		for(int i = 0; i < 20; i++){
			FruitFever.levelNumbers[i] = new GLabel(String.valueOf(i));
			FruitFever.levelNumbers[i].setFont(new Font("Helvetica", Font.BOLD, 30));
			FruitFever.levelNumbers[i].setLocation((int) (FruitFever.SCREEN_WIDTH/2 - FruitFever.levelNumbers[i].getWidth()/2 - 90 + (i%4)*60), 132 + (i/4)*55);
		}
		
		updateLoadingBar(0.95);

		/** Add buttons and set locations **/
		addButtonsToArrayList();
		
		updateLoadingBar(1.0);

	}
		
/** Used to help get the sub-images from the sprite-sheet **/
	private static GImage makeImage(int x, int y, int width, int height){
		return new GImage(sheet.getSubimage(x, y, width, height).getScaledInstance(-50, -50, 0));
	}

/** Loads objects from the file **/
	public static void loadObjects(String fileName, int level){
		
		try{

			Scanner sc = new Scanner(new File(fileName));
			
			// Clear ArrayLists
			FruitFever.blocks.clear();
			FruitFever.things.clear();
			Block.resetBlockLists();

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
						Thing lava = new Thing(i*TILE_SIZE, lineNumber*TILE_SIZE, lavaImage);
						FruitFever.things.add(lava);
						FruitFever.dangerousThings.add(lava);
						continue;
					}
					
					// Set the player's start position
					if(character == '@'){
						FruitFever.playerStartX = i*TILE_SIZE;
						FruitFever.playerStartY = lineNumber*TILE_SIZE;
						continue;
					}

					// Vortex (% sorta looks like vortex), readability counts (line 7 of our coding philosophy)
					if (character == '%') {
						Animation vortex = new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, vortexAnimation, false, 2, true, -1, true);
						FruitFever.vortex = vortex;
						FruitFever.vortex.adjustBoundaries(7, -7, 7, -7);
						FruitFever.things.add(vortex);
						continue;
					}
					
					// Fruit Ring
					if (character == '*') {
						Animation fruitRing = new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, fruitRingAnimation, true, 3, true, -1, true);
						FruitFever.edibleItems.add(fruitRing);
						FruitFever.things.add(fruitRing);
						continue;
					}

					// CheckPoint
					if (character == '|') {
						Thing checkPoint = new Thing(i*TILE_SIZE, lineNumber*TILE_SIZE - TILE_SIZE, checkpointFlagRed);
						FruitFever.checkPoints.add(checkPoint);
						FruitFever.things.add(checkPoint);
						continue;
					}

					// Reads in a fruit
					if(Character.isDigit(character)){
						if(character == '0')
							FruitFever.edibleItems.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, blueFruit, true, 3, true, 2, true));
						else if(character == '1')
							FruitFever.edibleItems.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, yellowFruit, true, 3, true, 2, true));
						else if(character == '2')
							FruitFever.edibleItems.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, redFruit, true, 3, true, 2, true));
						else if(character == '3')
							FruitFever.edibleItems.add(new Animation(i*TILE_SIZE, lineNumber*TILE_SIZE, purpleFruit, false, 3, true, 2, true));
						continue;
					}		

					try{

						GImage image;
						int color;

						// Normal Blocks
						if(character - 'a' >= 0){
							color = character - 'a';
							image = blockImages[color];
						}
						// Capital letters
						else{
							// We will use capitals for something else later (falling blocks?)
							continue;
						}


						// Add Block to the ArrayList
						FruitFever.blocks.add(new Block(i*TILE_SIZE, lineNumber*TILE_SIZE, color, image));

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
					if (character == '-' || character == '#' || character == ' ' || character == '?')
						continue;
					
					try {
						// Read in a powerup
						if(Character.isDigit(character)) {
							FruitFever.addToThings(new Thing(i*TILE_SIZE, lineNumber*TILE_SIZE, powerups[Integer.valueOf(String.valueOf(character))]));
							continue;
						}
					} catch(ArrayIndexOutOfBoundsException e) { 
						System.out.printf("SCENERY LAYER contains invalid character: '%c' \n", character);
						System.exit(0);
					}
					
					
					try {

						int type = character - 'a';
						int xOffset = 0, yOffset = 0;

						// Hard-Coded Exceptions
						if (type == 0)
							xOffset = -TILE_SIZE/2;
						else if (type == 6)
							xOffset = -3;

						// Add Scenery to the ArrayList
						FruitFever.things.add(new Scenery(i*TILE_SIZE + xOffset, lineNumber*TILE_SIZE + yOffset, type, sceneryImages[type]));

					} catch(ArrayIndexOutOfBoundsException e) { 
						System.out.printf("\nSCENERY LAYER contains invalid character: '%c' \n", character);
						System.exit(0);
					}	
				}
				lineNumber++;
			}
			
			lineNumber = 0;
			
			/** ENEMIES **/

			while (sc.hasNextLine() && !(line = sc.nextLine()).equals("") && !line.equals("+")) {
				
				int characterOffset = 0;
				
				// Iterate through each character in the line, instantiating the specified enemy (if it exists)
				for (int i = 0; i < line.length(); i++) {
					
					char character = line.charAt(i);

					// Skip if it's a blank
					if (character == '-' || character == ' ' || character == '#' || character == '?')
						continue;
						
					// Read in enemy
					if (character == '<') {
						int currentCharacterOffset = 1;
						char enemyType = line.charAt(i - characterOffset + currentCharacterOffset);
						currentCharacterOffset++;
						char enemyColor = line.charAt(i - characterOffset + currentCharacterOffset);
						currentCharacterOffset++;
						
						String left = findFirstNumber(line, i - characterOffset + currentCharacterOffset);
						currentCharacterOffset += left.length() + 1;
						
						String right = findFirstNumber(line, i - characterOffset + currentCharacterOffset);
						currentCharacterOffset += right.length() + 1;
						
						String up = findFirstNumber(line, i - characterOffset + currentCharacterOffset);
						currentCharacterOffset += up.length() + 1;
						
						String down = findFirstNumber(line, i - characterOffset + currentCharacterOffset);
						currentCharacterOffset += down.length();
						
						int x = (i - characterOffset);
						characterOffset += currentCharacterOffset;
						int y = lineNumber;
						
						// if(enemyType == 'a')
							FruitFever.enemies.add(new Enemy(new int[]{(x - Integer.valueOf(left))*TILE_SIZE, (x + Integer.valueOf(right))*TILE_SIZE}, new int[]{(y - Integer.valueOf(up))*TILE_SIZE, (y + Integer.valueOf(down))*TILE_SIZE}, new GImage[][]{ wormEnemyMoving, wormEnemyMovingH}, true, 2, true, 1, 1));

					}
		
				}

				lineNumber++;

			}

			sc.close();
		
		}
		catch (NoSuchElementException e) {
			System.out.println("Level " + FruitFever.currentLevel + " was not found.\n");
			e.printStackTrace();
			System.exit(0);
		}
		catch (IOException e) {
			System.out.println("\n IOException \n");
			e.printStackTrace();
			System.exit(0);
		}

	}
	
	public static void addButtonsToArrayList(){
	
		Button tempButton;
		
		// Adds main menu buttons to the ArrayLists
		for(int i = 0; i < 4; i++)
			addToButtons(new Button((int) (FruitFever.SCREEN_WIDTH/2 - menuButtons[i/3].getWidth()/2), 125 + 75*i, i, menuButtons[3*i], menuButtons[3*i + 1], menuButtons[3*i + 2]), FruitFever.mainMenuButtons);
		
		/** Adds arrow buttons to the ArrayLists for Level Selection Screen **/
		addToButtons(new Button((int) (FruitFever.SCREEN_WIDTH/2 - leftArrowButton[0].getWidth()/2 - 70), 375, 4, leftArrowButton[0], leftArrowButton[1], leftArrowButton[2]), FruitFever.levelSelectionButtons);
		
		addToButtons(new Button((int) (FruitFever.SCREEN_WIDTH/2 - rightArrowButton[0].getWidth()/2 + 70), 375, 5, rightArrowButton[0], rightArrowButton[1], rightArrowButton[2]), FruitFever.levelSelectionButtons);
		
		/** Adds back button to the ArrayLists for Level Selection Screen and the In-Game Screen **/
		addToButtons(new Button((int) FruitFever.SCREEN_WIDTH - 31, 0, 8, refreshButton[0], refreshButton[1], refreshButton[2]), FruitFever.inGameButtons);
		
		/** Adds level buttons to the ArrayLists for Level Selection Screen **/
		for(int i = 0; i < 20; i++)
			addToButtons(new Button((int) (FruitFever.SCREEN_WIDTH/2 - 115 + (i%4)*60), 100 + (i/4)*55, 6, levelButton[0], levelButton[1], levelButton[1], i), FruitFever.levelSelectionButtons);
		
		/** Adds gear button to the ArrayLists for In-Game Screen **/
		addToButtons(new Button((int) FruitFever.SCREEN_WIDTH - 31 - TILE_SIZE, 0, 7, gearButton[0], gearButton[1], gearButton[2]), FruitFever.inGameButtons);
		
	}
	
	private static void addToButtons(Button button, ArrayList<Button> buttonList) {
		
		FruitFever.buttons.add(button);
		buttonList.add(button);
	
	}
	
	private static void updateLoadingBar(double percentage){
	
		loadingScreenBar = ImageTransformer.resize(loadingScreenBar, (int) (700*(percentage)), 20);
		loadingScreenBar.setLocation(0, FruitFever.SCREEN_HEIGHT - (int) loadingScreenBar.getHeight());
		FruitFever.screen.add(loadingScreenBar);
	
	}
	
	private static String findFirstNumber(String line, int index){
	
		String str = "";
		
		while(line.charAt(index) != ',' && line.charAt(index) != '>'){
			str += line.charAt(index++);
		}
		
		return str;
	}
	
}