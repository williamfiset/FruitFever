
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


	public Block(int x, int y, int width, int height, int type, GImage image){

		super(x, y, width, height, image);
		
		this.type = type;

	}
	
	public Block(int x, int y, int type, GImage image){
	
		this(x, y, Data.TILE_SIZE, Data.TILE_SIZE, type, image);
	
	}


	/** 
	 * Returns the block bounded in the region 
	 *(xPos, yPos) & (xPos + blockWidth, yPos + blockHeight)
	 * @param xPos - The X position of a region within the block
	 * @param yPos - the Y position of a region within the block
	 *
	 **/

	public static Block getBlock(int xPos, int yPos){

		for (Block block : FruitFever.blocks)
			if (block.contains(xPos, yPos)) // From java.awt.Rectangle.contains(x,y) 
				return block;
			
		// Block coordinates were not found, typically due to air space or out of bounds
		return null;
	}

	// Not sure if it is worth implementing, at least for now
	public static Block getAdjacentBlock(Block block){
		return null;
	}



}






















