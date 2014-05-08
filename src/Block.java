
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

	static HashMap <Integer, ArrayList <Block>> xBlockPositions = new HashMap <Integer, ArrayList <Block>> ();
	static HashMap <Integer, ArrayList <Block>> yBlockPositions = new HashMap <Integer, ArrayList <Block>> ();

	public Block(int x, int y, int width, int height, int type, GImage image){

		super(x, y, width, height, image);

		// Search if row exists within hashmap
		if (xBlockPositions.containsKey(x)) {
			
			// If it does, extract ArrayList, add element and push it back into hashmap
			ArrayList <Block> xPos = xBlockPositions.get(x);
			xPos.add(this);
			xBlockPositions.put(x, xPos);

		}else{
			ArrayList <Block> xPos = new ArrayList <Block> ();
			xPos.add(this);
			xBlockPositions.put(x, xPos);
		}
		

		// Search if column exists within hashmap
		if (yBlockPositions.containsKey(y)) {
			
			// If it does, extract ArrayList, add element and push it back into hashmap
			ArrayList <Block> yPos = yBlockPositions.get(y);
			yPos.add(this);
			yBlockPositions.put(y, yPos);

		}else{
			ArrayList <Block> yPos = new ArrayList <Block> ();
			yPos.add(this);
			yBlockPositions.put(y, yPos);
		}
		

		this.type = type;

	}
	
	public Block(int x, int y, int type, GImage image){
	
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE, type, image);
	
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
	 *(xPos, yPos) & (xPos + blockWidth, yPos + blockHeight)
	 * @param xPos - The X position of a region within the block
	 * @param yPos - the Y position of a region within the block
	 *
	 **/

	public static Block getBlock(int xPos, int yPos){

		// Gets both the center row and column containing the block were looking for
		int rowNumber = ( (xPos + FruitFever.viewX) / 25) * 25;
		int columnNumber = ( (yPos + FruitFever.viewY) / 25) * 25;

		// Adjust this 

		if (rowNumber < 0 || rowNumber> FruitFever.LEVEL_WIDTH)
			return null;
		
		if (columnNumber < 0 || columnNumber > FruitFever.LEVEL_HEIGHT)
			return null;	
		
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

		}catch(NullPointerException e){
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






















