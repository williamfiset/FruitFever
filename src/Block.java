

/**
*
* @Author William Fiset
*
* The Block Class provides functionality for blocks to have interact
* and be part of the FruitFever World
**/

import acm.graphics.*;
import java.util.*;
import java.awt.*;

public class Block extends Thing {

	private static final long serialVersionUID = 1L;

	private static HashMap<Integer, ArrayList<Block>> xBlocks = new HashMap<>(), yBlocks = new HashMap<>();

	// The blocks that are in the current process of falling 
	static ArrayList<Block> fallingBlocks = new ArrayList<>();

	// The blocks that have a potential to fall
	static ArrayList<Block> naturalFallingBlockCondidates = new ArrayList<>();


	/** To fix issue #41 the first time you draw on the screen you must call naturalAnimate() **/
	private static boolean performedNaturalAnimate = false;

	private double dx, dy;

	/** Objects such as scenery or spikes that are located above and below the block (since they need to fall when the block falls) **/
	public ArrayList<Thing> connectedObjects = new ArrayList<>();

	public Block(int x, int y, int width, int height, GImage image, boolean canFall){

		super(x, y, width, height, image, canFall, false, Layer.BLOCKS);

		// Search if row exists within HashMap
		if (xBlocks.containsKey(x)) {
			
			// If it does, extract ArrayList, add element and push it back into hashmap
			ArrayList <Block> xPos = xBlocks.get(x);
			xPos.add(this);
			xBlocks.put(x, xPos);

		} else {
			ArrayList<Block> xPos = new ArrayList<Block> ();
			xPos.add(this);
			xBlocks.put(x, xPos);
		}
		

		// Search if column exists within hashmap
		if (yBlocks.containsKey(y)) {
			
			// If it does, extract ArrayList, add element and push it back into hashmap
			ArrayList <Block> yPos = yBlocks.get(y);
			yPos.add(this);
			yBlocks.put(y, yPos);

		} else {
			ArrayList<Block> yPos = new ArrayList<Block> ();
			yPos.add(this);
			yBlocks.put(y, yPos);
		}

	}
	
	public Block(int x, int y, GImage image, boolean canFall){
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE, image, canFall);
	}


	/** 
	 * When changing levels you must empty the block list or else
	 * you are left with the blocks from the previous level
	 */

	public static void resetBlockLists(){
	
		xBlocks.clear();
		yBlocks.clear();
		naturalFallingBlockCondidates.clear();
		fallingBlocks.clear();			
		
	}

	public static void resetPerformedNaturalAnimate(){
		performedNaturalAnimate = false;
	}

	public static void drawBlocks(){

		/** Temporary, make sure to optimize block drawing **/

		for (Block block : FruitFever.blocks)
			block.naturalAnimate();


		// Advanced drawing method needs work 

		/*

		performedNaturalAnimate = false;

		if (!performedNaturalAnimate) {
			
			for (Block block : FruitFever.blocks)
				block.naturalAnimate();
			performedNaturalAnimate = true;

		} else {

			outerLoop:
			for (int rowNumber = 0; rowNumber <= FruitFever.LEVEL_WIDTH; rowNumber += Data.TILE_SIZE) {
				ArrayList <Block> rowBlocks = xBlocks.get(rowNumber);


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

		*/
	}

	/** 
	 * Returns the block bounded in the region 
	 * (xPos, yPos) & (xPos + blockWidth, yPos + blockHeight)
	 * @param xPos - The X position of a region within the block
	 * @param yPos - the Y position of a region within the block
	 *
	 **/

	/** Returns the block contained within a given pair of coordinates **/
	public static Block getBlock(int xPos, int yPos){

		/*

		// Gets both the center row and column containing the block were looking for
		int rowNumber = ( (xPos + FruitFever.viewX) / Data.TILE_SIZE) * Data.TILE_SIZE;
		int rowNumber2 = ( (xPos + FruitFever.viewX) / Data.TILE_SIZE) * Data.TILE_SIZE - Data.TILE_SIZE;
		int rowNumber3 = ( (xPos + FruitFever.viewX) / Data.TILE_SIZE) * Data.TILE_SIZE + Data.TILE_SIZE;

		int columnNumber = ( (yPos + FruitFever.viewY) / Data.TILE_SIZE) * Data.TILE_SIZE;
		int columnNumber2 = ( (yPos + FruitFever.viewY) / Data.TILE_SIZE) * Data.TILE_SIZE - Data.TILE_SIZE;
		int columnNumber3 = ( (yPos + FruitFever.viewY) / Data.TILE_SIZE) * Data.TILE_SIZE + Data.TILE_SIZE;

		try {

			// Defines center row & Column
			ArrayList <Block> row = new ArrayList <Block> (); // xBlocks.get(rowNumber);
			ArrayList <Block> column = new ArrayList <Block> (); //yBlocks.get(columnNumber);

			if (isWithinRange(rowNumber , true) ) row.addAll(xBlocks.get(rowNumber)) ;
			if (isWithinRange(rowNumber2 , true) ) row.addAll(xBlocks.get(rowNumber2)) ;
			if (isWithinRange(rowNumber3 , true) ) row.addAll(xBlocks.get(rowNumber3)) ;
			if (isWithinRange(columnNumber , false) ) column.addAll(yBlocks.get(columnNumber)) ;
			if (isWithinRange(columnNumber2 , false) ) column.addAll(yBlocks.get(columnNumber2)) ;
			if (isWithinRange(columnNumber3 , false) ) column.addAll(yBlocks.get(columnNumber3)) ;

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
		
		*/


		// This is the old Block finder method
		
		for (Block block : FruitFever.blocks)
			if (block.contains(xPos, yPos)) // From java.awt.Rectangle.contains(x,y) 
				return block;
		


		// Block coordinates were not found, typically due to air space or out of bounds
		return null;
	}

	/** Populates the fallingBlockCondidates list to be more effective than looping through all the
	  * blocks very time you want to make a block fall **/
	public static void updateNaturalFallingBlockCandidates(){

		Block topBlock = null;
		Block firstBlockDown = null;
		Block secondBlockDown = null;

		/*
 		 * Loops through all the blocks and tests each to see if they meet the criteria to be a natural falling block
 		 * This function is made to be called once
 		 *
 		 * Criteria:
 		 * - block must not be alone (stationary platform)
 		 * - block must have two empty blocks directly below itself (so that when they fall they do not clog a tunnel)
		 */

		if (naturalFallingBlockCondidates.isEmpty()) {

			for (Block block : FruitFever.blocks) {
				if (block.withinScreen()) {
				
					topBlock = getBlock(block.x + Data.TILE_SIZE/2, block.y - Data.TILE_SIZE/2 );
					if (topBlock == null) continue;

					firstBlockDown = getBlock(block.x + Data.TILE_SIZE/2, block.y + Data.TILE_SIZE + Data.TILE_SIZE/2 );
					if (firstBlockDown != null) continue;

					secondBlockDown = getBlock(block.x + Data.TILE_SIZE/2, block.y + Data.TILE_SIZE*2 + Data.TILE_SIZE/2 );
					if (secondBlockDown != null) continue;

					if (!naturalFallingBlockCondidates.contains(block))
						naturalFallingBlockCondidates.add(block);
				}
			}
			

			// If there are no more blocks that are valid candidates to fall then pick any of the remaining blocks given 
			// there is not two blocks below them
			if (naturalFallingBlockCondidates.isEmpty()) {
				for (Block block : FruitFever.blocks ) {
					if (block.withinScreen()) {

						firstBlockDown = getBlock(block.x + Data.TILE_SIZE/2, block.y + Data.TILE_SIZE + Data.TILE_SIZE/2 );
						if (firstBlockDown != null) continue;

						secondBlockDown = getBlock(block.x + Data.TILE_SIZE/2, block.y + Data.TILE_SIZE*2 + Data.TILE_SIZE/2 );
						if (secondBlockDown != null) continue;

						if (!naturalFallingBlockCondidates.contains(block))
							naturalFallingBlockCondidates.add(block);

					}
				}
			}

		}

	}

	/** Selects all the block candidates that are worthy of falling **/
	public static void activateFallingBlocksByNaturalDisaster() {

		// Only loop through the blocks on the screen 
		// Selects a worthy falling block candidate and activates it
		// make sure that the block above doesn't start falling 

		int listLength = naturalFallingBlockCondidates.size();

		if (listLength == 0)
			return;
		
		// System.out.println(listLength);
		// very unlikely to select index 0? Micah's answer: FALSE, the numbers are truncated, so it has equal chance to return int 0 through listLength - 1
		int randomIndex = (int) (Math.random() * listLength);
		Block randomlySelectedBlock = naturalFallingBlockCondidates.get(randomIndex);

		// Makes sure falling block is on the screen when it falls 
		// Also activates falling blocks
		if (randomlySelectedBlock.inMotion()) {
			if (randomlySelectedBlock.withinLevel()) {

				// if (!fallingBlocks.contains(randomlySelectedBlock)) {
				randomlySelectedBlock.dy = 1;
				randomlySelectedBlock.changeImage(Data.blockImages[17][0]);
				fallingBlocks.add(randomlySelectedBlock);
				naturalFallingBlockCondidates.remove(randomlySelectedBlock);

			}
		}

		// Removing form list should be fine as long as this method is called at an interval
		// otherwise blocks will be falling very fast as the list gets sorter
		// naturalFallingBlockCondidates.remove(randomlySelectedBlock);
		
		// FruitFever.blocks.remove(randomlySelectedBlock);


	}


	public static void activateFallingBlocksWithPlayerPosition(int playerX, int playerY, boolean playerOnSurface) {


		// from the players position get the column of blocks below the position
		// get the last block and add it to the falling block list

		int column0 = ((playerX / Data.TILE_SIZE) * Data.TILE_SIZE);
		int column1 = ((playerX / Data.TILE_SIZE) * Data.TILE_SIZE) + Data.TILE_SIZE;
		int column2 = column1 + Data.TILE_SIZE;

		Block fallingBlock0 = getLastBlockInColumn(xBlocks.get(column0), playerX, playerY, playerOnSurface);
		Block fallingBlock1 = getLastBlockInColumn(xBlocks.get(column1), playerX, playerY, playerOnSurface);
		Block fallingBlock2 = getLastBlockInColumn(xBlocks.get(column2), playerX, playerY, playerOnSurface);


		/* To make the game more intense remove the else if clause */

		if (fallingBlock1 != null && !fallingBlocks.contains(fallingBlock1) ) {  
			
			fallingBlock1.dy = 1;
			if (fallingBlock1.canFall)
				fallingBlocks.add(fallingBlock1);

		} else if (fallingBlock2 != null && !fallingBlocks.contains(fallingBlock2) ) {  
			
			fallingBlock2.dy = 1;
			if (fallingBlock2.canFall)
				fallingBlocks.add(fallingBlock2);
			
		} else if (fallingBlock0 != null && !fallingBlocks.contains(fallingBlock0) ) {  
			
			fallingBlock0.dy = 1;
			if (fallingBlock0.canFall)
				fallingBlocks.add(fallingBlock0);
		}

	}

	/** Gets the furthest block down below the player **/
	private static Block getLastBlockInColumn(ArrayList<Block> column, int playerX, int playerY, boolean playerOnSurface){


		Block furthestBlockDown = null;

		// List is empty or player is not on platform
		if (!playerOnSurface || column == null)
			return null;

		outerLoop:
		for (Block fallingBlock : column) {
			
			// Ignore the blocks above the player or if it is already falling
			if (fallingBlock.y <= playerY || fallingBlock.inMotion())
				continue;	

			// Starting searching position is in the middle of the first block found
			int startY = fallingBlock.y + Data.TILE_SIZE/2;
			int startX = fallingBlock.x + Data.TILE_SIZE/2;


			furthestBlockDown = fallingBlock;
			
			Block nextBlock = fallingBlock;

			// Search for the last block in the column the player is standing on
			while (true) {

				startY += Data.TILE_SIZE;

				nextBlock = getBlock(startX, startY);

				// Next block was found
				if (nextBlock != null ){ 
					furthestBlockDown = nextBlock;
				
				// If there is no next block break out
				} else break outerLoop;
			}
		}
		return furthestBlockDown;
	}

	/** Moves the position of the falling blocks **/
	public static void motion() {

		for (int index = 0; index < fallingBlocks.size(); index++){

			Block fallingBlock = fallingBlocks.get(index);


			// Once you're sure the block is off the screen remove it
			if (fallingBlock.imageY > FruitFever.LEVEL_HEIGHT + Data.TILE_SIZE*3) {

				// Places block offscreen so that blocks dont pile on top of one another
				// fallingBlock.imageX = -1000;
				// fallingBlock.imageY = -1000;
				// fallingBlock.x = -1000;
				// fallingBlock.y = -1000;

				// Remove block completely (still missing those in xBlock and yBlock lists)
				FruitFever.removeThingFromLists(fallingBlock, FruitFever.blocks, fallingBlocks);
				// fallingBlocks.remove(index);
				
				index--;
			

			// Falling block is still on screen 
			} else {

				int bottomBlockX = fallingBlock.x + Data.TILE_SIZE / 2;
				int bottomBlockY = fallingBlock.y+ Data.TILE_SIZE;
				Block bottomBlock = getBlock( bottomBlockX ,  bottomBlockY);

				// Falling Block is free to move since there is no block below it.
				if (bottomBlock == null) {

					fallingBlock.imageY += fallingBlock.dy;

					// Needed for proper collision detection with scenery since fallingBlock.animate() hasn't been called;
					fallingBlock.y += fallingBlock.dy;
					
					for (Thing obj : FruitFever.things)
						if (obj.canBeCrushed)
							if (fallingBlock.contains(obj))
								obj.makeInvisible();

					// Move scenery with block
					for (Thing scenery : fallingBlock.connectedObjects) {

						scenery.imageY += fallingBlock.dy;
						scenery.animate();
					}

				}
			}
		}
	}

	/* Deprecate this asap, bad coding style (due to no closures or lambdas!)  */
	private static boolean isWithinRange(int row_column_number, boolean x) {
		return x ? (row_column_number >= 0 && row_column_number < FruitFever.LEVEL_WIDTH ) : (row_column_number >= 0 && row_column_number < FruitFever.LEVEL_HEIGHT);
	}

	private boolean inMotion() {
		return (dy > 0 || dx > 0);
	}

	/* Determines if the block is still within the level */
	public boolean withinLevel() {

		if (imageX > FruitFever.viewX && imageY > FruitFever.viewY)
			if (imageX < FruitFever.viewX + FruitFever.SCREEN_WIDTH + Data.TILE_SIZE)
					if (imageY < FruitFever.viewY + FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE) 
						return true;
		
		return false;		

	}

	/* Determines if the block is currently on the screen */
	public boolean withinScreen() {
		
		if (imageX > 0 && imageY > 0)
			if (imageX < FruitFever.SCREEN_WIDTH + Data.TILE_SIZE)
				if (imageY < FruitFever.SCREEN_HEIGHT + Data.TILE_SIZE)
					return true;

		return false;

	}


}

