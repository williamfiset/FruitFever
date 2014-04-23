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

public abstract class Data{

	public static int TILE_SIZE = 25;

	public static BufferedImage spriteSheet = null, menuSheet = null;
	public static GImage heartImage;
	public static GImage[] blockImages = new GImage[15],
						   blockGrassImages = new GImage[15],
						   sceneryImages = new GImage[15],
						   swirlAnimation = new GImage[6],
						   berryAnimation = new GImage[5],
						   blueEnemyAnimation = new GImage[4],
						   menuImages = new GImage[12];
	    
/** Loads all the images from the sprite sheet **/
	public static void loadImages(){
			
		/** Import sprite-sheet **/
		try { spriteSheet = ImageIO.read(new File("../img/spriteSheet.png"));	}
		catch (IOException e) {	e.printStackTrace(); }
	
		// Blocks
		for(int i = 0; i < 15; i++){
			blockImages[i] = makeImage(spriteSheet, 0, TILE_SIZE*i, TILE_SIZE, TILE_SIZE);
			blockGrassImages[i] = makeImage(spriteSheet, TILE_SIZE, TILE_SIZE*i, TILE_SIZE, TILE_SIZE);
		}

		// Scenery (Top Row in spriteSheet)
		sceneryImages[0] = makeImage(spriteSheet, TILE_SIZE*5, TILE_SIZE*11, TILE_SIZE*2, TILE_SIZE);
		sceneryImages[1] = makeImage(spriteSheet, TILE_SIZE*7, TILE_SIZE*11, TILE_SIZE, TILE_SIZE);
		sceneryImages[2] = makeImage(spriteSheet, TILE_SIZE*8, TILE_SIZE*11, TILE_SIZE, TILE_SIZE);
		sceneryImages[3] = makeImage(spriteSheet, TILE_SIZE*9, TILE_SIZE*11, TILE_SIZE, TILE_SIZE);
		sceneryImages[4] = makeImage(spriteSheet, TILE_SIZE*10, TILE_SIZE*11, TILE_SIZE, TILE_SIZE);
		sceneryImages[5] = makeImage(spriteSheet, TILE_SIZE*11, TILE_SIZE*11, TILE_SIZE, TILE_SIZE);
		sceneryImages[6] = makeImage(spriteSheet, TILE_SIZE*12, TILE_SIZE*11 - 2, TILE_SIZE + 5, TILE_SIZE + 2);

		// Scenery (Bottom Row in spriteSheet)
		sceneryImages[7] = makeImage(spriteSheet, TILE_SIZE*6, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[8] = makeImage(spriteSheet, TILE_SIZE*7, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[9] = makeImage(spriteSheet, TILE_SIZE*8, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[10] = makeImage(spriteSheet, TILE_SIZE*9, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[11] = makeImage(spriteSheet, TILE_SIZE*10, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[12] = makeImage(spriteSheet, TILE_SIZE*11, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[13] = makeImage(spriteSheet, TILE_SIZE*12, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		sceneryImages[14] = makeImage(spriteSheet, TILE_SIZE*13, TILE_SIZE*12, TILE_SIZE, TILE_SIZE);
		
		// Swirl Animation Images
		for(int i = 0; i < 6; i++)
			swirlAnimation[i] = makeImage(spriteSheet, TILE_SIZE*(i + 5), 0, TILE_SIZE, TILE_SIZE);
			
		// Berry Animation Images
		for(int i = 0; i < 5; i++)
			berryAnimation[i] = makeImage(spriteSheet, TILE_SIZE*(i + 7), TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		
		// Heart Image
		heartImage = makeImage(spriteSheet, TILE_SIZE*5, TILE_SIZE*2, TILE_SIZE, TILE_SIZE);
		
		// Blue Enemy Animation Images
		for(int i = 0; i < 4; i++)
			blueEnemyAnimation[i] = makeImage(spriteSheet, TILE_SIZE*5, TILE_SIZE*(i + 6), TILE_SIZE*2, TILE_SIZE);
			
			
		/** Import and set location of menu images **/
		try { menuSheet = ImageIO.read(new File("../img/menu0.png"));	}
		catch (IOException e) {	e.printStackTrace(); }
		
		// Menu Button Images
		for(int i = 0; i < 12; i++)
			menuImages[i] = makeImage(menuSheet, 0, i*69, 266, 69);

	}
		
/** Used to help get the sub-images from the sprite-sheet **/
	private static GImage makeImage(BufferedImage i, int x, int y, int width, int height){
		return new GImage(i.getSubimage(x, y, width, height));
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

			int lineNumber = 0;
			String line = "";

			/** BLOCKS **/

			while(sc.hasNextLine() && !(line = sc.nextLine()).equals("") && !line.equals("+")){

				// Iterate through each character in the line, instantiating the specified block (if it exists)
				for(int i = 0; i < line.length(); i++){

					// Skip if it's a blank
					if(line.charAt(i) == '-')
						continue;

					GImage image;
					int type;

					// Normal Blocks
					if(line.charAt(i) - 'a' >= 0){
						type = line.charAt(i) - 'a';
						image = new GImage(Data.blockImages[type].getImage());
					}
					// Grass Blocks
					else{
						type = line.charAt(i) - 'A';
						image = new GImage(Data.blockGrassImages[type].getImage());
					}

					// Add Block to the ArrayList
					FruitFever.blocks.add(new Block(i*TILE_SIZE, lineNumber*TILE_SIZE, type, image));

				}

				lineNumber++;

			}

			lineNumber = 0;

			/** SCENERY **/

			while(sc.hasNextLine() && !(line = sc.nextLine()).equals("") && !line.equals("+")){

				// Iterate through each character in the line, instantiating the specified block (if it exists)
				for(int i = 0; i < line.length(); i++){

					// Skip if it's a blank
					if(line.charAt(i) == '-')
						continue;

					int type = line.charAt(i) - 'a';
					GImage image = new GImage(Data.sceneryImages[type].getImage());
					int xOffset = 0, yOffset = 0;

					// Hard-Coded Exceptions
					if(type == 0)
						xOffset = -TILE_SIZE/2;
					else if(type == 6)
						xOffset = -3;

					// Add Scenery to the ArrayList
					FruitFever.things.add(new Scenery(i*TILE_SIZE + xOffset, lineNumber*TILE_SIZE + yOffset, type, image));

				}

				lineNumber++;

			}

			sc.close();
		
		}
		catch(IOException e){}
		catch(NoSuchElementException e){
			System.out.println("Level was not found.\n");
		}

	}

}