
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
	public int color;

	/**
	* @param type - defines what type of block it is (the reference file contains a list of these values
	*				and associated types)
	*
	* @Param color - defines the color the block is
	**/

	private static HashMap<Integer, ArrayList<Block>> xBlockPositions = new HashMap<Integer, ArrayList<Block>> ();
	private static HashMap<Integer, ArrayList<Block>> yBlockPositions = new HashMap<Integer, ArrayList<Block>> ();

	static ArrayList<Block> fallingBlocks = new ArrayList<Block>();

	/** To fix issue #41 the first time you draw on the screen you must call naturalAnimate() **/
	private static boolean performedNaturalAnimate = false;

	public Block(int x, int y, int width, int height, int color, GImage image){

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
		this.color = color;

	}
	
	public Block(int x, int y, int color, GImage image){
		this(x, y, WebData.TILE_SIZE, WebData.TILE_SIZE, color, image);
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

	/** Selects all the block candidates that are worthy of falling **/
	public static void updateFallingBlocksByNaturalDisaster(){

		// Only loop through the blocks on the screen (with a little buffer)
		// Select a few worthy falling block candidates
		// make sure that the block above doesn't start falling 

	}

	public static void updateFallingBlocksWithPlayerPosition(int playerX, int playerY){


		// from the players position get the column of blocks below the position
		// get the last block and add it to the falling block list

		int column0 = ((playerX / WebData.TILE_SIZE) * WebData.TILE_SIZE);
		int column1 = ((playerX / WebData.TILE_SIZE) * WebData.TILE_SIZE) + WebData.TILE_SIZE;
		int column2 = column1 + WebData.TILE_SIZE;

		boolean random = (Math.random() * 100) % 2 == 0;

		Block fallingBlock0 = getLastBlockInColumn(xBlockPositions.get(column0), playerX, playerY);
		Block fallingBlock1 = getLastBlockInColumn(xBlockPositions.get(column1), playerX, playerY);
		Block fallingBlock2 = getLastBlockInColumn(xBlockPositions.get(column2), playerX, playerY);

		// found the block we were looking for
		if (fallingBlock1 != null) {
			fallingBlocks.add(fallingBlock1);
			fallingBlock1.changeImage(WebData.blockImages[17]);
		}

		if (fallingBlock2 != null) {
			fallingBlocks.add(fallingBlock2);
			fallingBlock2.changeImage(WebData.blockImages[17]);	
		}

		if (fallingBlock0 != null) {
			fallingBlocks.add(fallingBlock0);
			fallingBlock0.changeImage(WebData.blockImages[17]);	
		}

	}

	/** Gets the furthest block down below the player **/
	private static Block getLastBlockInColumn(ArrayList<Block> column, int playerX, int playerY){


		Block furthestBlockDown = null;

		// list is empty
		if (column == null || column.size() == 0)
			return furthestBlockDown;

		outerLoop:
		for (Block fallingBlock : column) {
			
			// ignore the blocks above the player
			if (fallingBlock.y <= playerY)
				continue;	

			// Starting searching position is in the middle of the first block found
			int startY = fallingBlock.y + WebData.TILE_SIZE/2;
			int startX = fallingBlock.x + WebData.TILE_SIZE/2;


			furthestBlockDown = fallingBlock;
			
			Block nextBlock = fallingBlock;

			while (true){

				startY += WebData.TILE_SIZE;

				nextBlock = getBlock(startX, startY);

				// next block was found
				if (nextBlock != null) furthestBlockDown = nextBlock;
				else break outerLoop;
				

			}
		}

		return furthestBlockDown;

	}

}

/*

fallingBlock
- falls when the player lands on it
- falls by natural disaster 

movingBlock 
- A block that moves between points

*/



























// don't delete space!



