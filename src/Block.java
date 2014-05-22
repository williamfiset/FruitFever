

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

	public int color;

	private static HashMap<Integer, ArrayList<Block>> xBlockPositions = new HashMap<Integer, ArrayList<Block>> ();
	private static HashMap<Integer, ArrayList<Block>> yBlockPositions = new HashMap<Integer, ArrayList<Block>> ();

	static ArrayList<Block> naturalFallingBlockCondidates = new ArrayList<Block>();
	static ArrayList<Block> fallingBlocks = new ArrayList<Block>();

	/** To fix issue #41 the first time you draw on the screen you must call naturalAnimate() **/
	private static boolean performedNaturalAnimate = false;

	private double vx, vy;

	/**
 	 * @Param color - defines the color the block is
	 **/

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
		
		this.color = color;

	}
	
	public Block(int x, int y, int color, GImage image){
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE, color, image);
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
			for (int rowNumber = 0; rowNumber <= FruitFever.LEVEL_WIDTH; rowNumber += Data.TILE_SIZE) {
				ArrayList <Block> rowBlocks = xBlockPositions.get(rowNumber);


				if (rowBlocks == null) continue;
				
				row : for (Block block : rowBlocks ) {
					
					int x = block.imageX - FruitFever.viewX;
					int y = block.imageY - FruitFever.viewY;
					
					if (x > FruitFever.SCREEN_WIDTH + Data.TILE_SIZE) {

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

	public static void resetBlockLists(){
	
		xBlockPositions.clear();
		yBlockPositions.clear();
		naturalFallingBlockCondidates.clear();
		fallingBlocks.clear();			
		
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
		int rowNumber = ( (xPos + FruitFever.viewX) / Data.TILE_SIZE) * Data.TILE_SIZE;
		int columnNumber = ( (yPos + FruitFever.viewY) / Data.TILE_SIZE) * Data.TILE_SIZE;

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

	/** Populates the fallingBlockCondidates list to be more effective than looping through all the
	  * blocks very time you want to make a block fall **/
	public static void findNaturalFallingBlockCandidates(){

		Block topBlock = null;
		Block firstBlockDown = null;
		Block secondBlockDown = null;


		/*
 		 * Loops through all the blocks and tests each to see if they meet the criteria to be a natural falling block
 		 *
 		 * Criteria:
 		 * - block must not be alone (stationary platform)
 		 * - block must have two empty blocks directly below itself (so that when they fall they do not clog a tunnel)
		 */

		for (Block block : FruitFever.blocks) {
			
			topBlock = getBlock(block.x + Data.TILE_SIZE/2, block.y - Data.TILE_SIZE/2 );
			if (topBlock == null) continue;

			firstBlockDown = getBlock(block.x + Data.TILE_SIZE/2, block.y + Data.TILE_SIZE + Data.TILE_SIZE/2 );
			if (firstBlockDown != null) continue;
			
			secondBlockDown = getBlock(block.x + Data.TILE_SIZE/2, block.y + Data.TILE_SIZE*2 + Data.TILE_SIZE/2 );
			if (secondBlockDown != null) continue;

			naturalFallingBlockCondidates.add(block);

		}

	}

	/** Selects all the block candidates that are worthy of falling **/
	public static void updateFallingBlocksByNaturalDisaster(){

		// Only loop through the blocks on the screen (with a little buffer)
		// Select a few worthy falling block candidates
		// make sure that the block above doesn't start falling 

		int listLength = naturalFallingBlockCondidates.size();

		// very unlikely to select index 0? 
		if (listLength == 0)
			return;

		int randomIndex = (int) (Math.random() * listLength);
		Block randomlySelectedBlock = naturalFallingBlockCondidates.get(randomIndex);
		randomlySelectedBlock.changeImage(Data.blockImages[17]);

		// Removing form list should be fine as long as this method is called at an interval
		// otherwise blocks will be falling very fast as the list gets sorter
		naturalFallingBlockCondidates.remove(randomlySelectedBlock);
		
		// FruitFever.blocks.remove(randomlySelectedBlock);


	}

	public static void updateFallingBlocksWithPlayerPosition(int playerX, int playerY){


		// from the players position get the column of blocks below the position
		// get the last block and add it to the falling block list

		int column0 = ((playerX / Data.TILE_SIZE) * Data.TILE_SIZE);
		int column1 = ((playerX / Data.TILE_SIZE) * Data.TILE_SIZE) + Data.TILE_SIZE;
		int column2 = column1 + Data.TILE_SIZE;

		Block fallingBlock0 = getLastBlockInColumn(xBlockPositions.get(column0), playerX, playerY);
		Block fallingBlock1 = getLastBlockInColumn(xBlockPositions.get(column1), playerX, playerY);
		Block fallingBlock2 = getLastBlockInColumn(xBlockPositions.get(column2), playerX, playerY);

		// found the block we were looking for
		if (fallingBlock1 != null) {
			fallingBlocks.add(fallingBlock1);
			fallingBlock1.changeImage(Data.blockImages[16]);
		}

		if (fallingBlock2 != null) {
			fallingBlocks.add(fallingBlock2);
			fallingBlock2.changeImage(Data.blockImages[16]);	
		}

		if (fallingBlock0 != null) {
			fallingBlocks.add(fallingBlock0);
			fallingBlock0.changeImage(Data.blockImages[16]);	
		}

	}

	/** Gets the furthest block down below the player **/
	private static Block getLastBlockInColumn(ArrayList<Block> column, int playerX, int playerY){


		Block furthestBlockDown = null;

		// list is empty or player is not on platform
		if (column == null || !Player.isOnPlatform() || column.size() == 0)
			return furthestBlockDown;

		outerLoop:
		for (Block fallingBlock : column) {
			
			// ignore the blocks above the player
			if (fallingBlock.y <= playerY)
				continue;	

			// Starting searching position is in the middle of the first block found
			int startY = fallingBlock.y + Data.TILE_SIZE/2;
			int startX = fallingBlock.x + Data.TILE_SIZE/2;


			furthestBlockDown = fallingBlock;
			
			Block nextBlock = fallingBlock;

			// Search for the last block in the column the player is standing on
			while (true){

				startY += Data.TILE_SIZE;

				nextBlock = getBlock(startX, startY);

				// next block was found
				if (nextBlock != null ){ 
					furthestBlockDown = nextBlock;
				
				// if there is no next block break out
				} else break outerLoop;
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



