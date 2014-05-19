
/**
*
* @Author William Fiset
*
* The Block Class provides functionality for blocks to have interact
* and be part of the FruitFever World
**/

import acm.graphics.*;
import java.util.*;

public class Block extends Thing {

	public int type;

	/**
	* @param type - defines what type of block it is (the reference file contains a list of these values
	*				and associated types)
	**/

	private static HashMap<Integer, ArrayList<Block>> xBlockPositions = new HashMap<Integer, ArrayList<Block>> ();
	private static HashMap<Integer, ArrayList<Block>> yBlockPositions = new HashMap<Integer, ArrayList<Block>> ();

	/** To fix issue #41 the first itme you draw on the screen you must call naturalAnimate() **/
	private static boolean performedNaturalAnimate = false;

	public Block(int x, int y, int width, int height, int type, GImage image){

		/** To improve the rotation couldn't we simply generate the four rotations and then pick one 
		    Randomly instead of running the filter each time? This doesn't matter because we have 
		    few blocks atm but this could save us a lot of time **/
		super(x, y, width, height, ImageTransformer.rotateRandomly(image));

		// Search if row exists within HashMap
		if (xBlockPositions.containsKey(x)) {
			
			// If it does, extract ArrayList, add element and push it back into hashmap
			ArrayList <Block> xPos = xBlockPositions.get(x);
			xPos.add(this);
			xBlockPositions.put(x, xPos);

		} else {
			ArrayList<Block> xPos = new ArrayList<Block> ();
			xPos.add(this);
			xBlockPositions.put(x, xPos);
		}
		

		// Search if column exists within hashmap
		if (yBlockPositions.containsKey(y)) {
			
			// If it does, extract ArrayList, add element and push it back into hashmap
			ArrayList <Block> yPos = yBlockPositions.get(y);
			yPos.add(this);
			yBlockPositions.put(y, yPos);

		} else {
			ArrayList<Block> yPos = new ArrayList<Block> ();
			yPos.add(this);
			yBlockPositions.put(y, yPos);
		}
		

		this.type = type;

	}
	
	public Block(int x, int y, int type, GImage image){
		this(x, y, WebData.TILE_SIZE, WebData.TILE_SIZE, type, image);
	}

	public static void resetPerformedNaturalAnimate(){
		performedNaturalAnimate = false;
	}

	public static void drawBlocks(){

		/** Temporary, make sure to optimize block drawing **/
		performedNaturalAnimate = false;

		if (!performedNaturalAnimate) {
			
			for (Block block : FruitFever.blocks)
				block.naturalAnimate();
			performedNaturalAnimate = true;

		} else {

			outerLoop:
			for (int rowNumber = 0; rowNumber <= FruitFever.LEVEL_WIDTH; rowNumber += WebData.TILE_SIZE) {
				ArrayList <Block> rowBlocks = xBlockPositions.get(rowNumber);


				if (rowBlocks == null) continue;
				
				row : for (Block block : rowBlocks ) {
					
					int x = block.imageX - FruitFever.viewX;
					int y = block.imageY - FruitFever.viewY;
					
					if (x > FruitFever.SCREEN_WIDTH + WebData.TILE_SIZE) {

						// Breaks Out of loop when you hit a block South East of the Screen
						if (y > FruitFever.SCREEN_HEIGHT) 
							break outerLoop;
						// Breaks row when the first block east goes off screen
						else break row;

					// Skips drawing blocks Left and up off the screen
					} else if (x < -FruitFever.LEFT_BOUNDARY || y < -FruitFever.UP_BOUNDARY)
						continue;

					block.animate();
				}
			}
		}
	}


	/** 
	 * When changing levels you must empty the block list or else
	 * you are left with the blocks from the previous level
	 */

	public static void resetBlockList(){
	
		xBlockPositions.clear();
		yBlockPositions.clear();			
		
	}


	/** 
	 * Returns the block bounded in the region 
	 * (xPos, yPos) & (xPos + blockWidth, yPos + blockHeight)
	 * @param xPos - The X position of a region within the block
	 * @param yPos - the Y position of a region within the block
	 *
	 **/

	public static Block getBlock(int xPos, int yPos){

		// Gets both the center row and column containing the block were looking for
		int rowNumber = ( (xPos + FruitFever.viewX) / WebData.TILE_SIZE) * WebData.TILE_SIZE;
		int columnNumber = ( (yPos + FruitFever.viewY) / WebData.TILE_SIZE) * WebData.TILE_SIZE;

		try{

			// Defines center row & Column
			ArrayList <Block> row = xBlockPositions.get(rowNumber);
			ArrayList <Block> column = yBlockPositions.get(columnNumber);

			for (Block xBlock : row) {
				for (Block yBlock : column) {
					// check if both blocks point to each other
					if (xBlock == yBlock)

						// Make sure point is actually within block
						if (xBlock.contains(xPos, yPos))
							return xBlock;
				}
			}

		// I got too lazy to check the boundaries so I used a try block which is error proof			
		} catch(NullPointerException e) {

			// Block coordinates were not found, typically due to air space or out of bounds
			return null;
		}

			
		/* This is the old Block finder method, I'm keeping it just in case we need to go back to it

		for (Block block : FruitFever.blocks)
			if (block.contains(xPos, yPos)) // From java.awt.Rectangle.contains(x,y) 
				return block;
		*/

		// Block coordinates were not found, typically due to air space or out of bounds
		return null;
	}

}