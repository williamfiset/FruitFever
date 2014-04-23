
/**
*
* @Author William Fiset
*
* The Block Class provides functionality for blocks to have interact
* and be part of the FruitFever World
**/

import acm.graphics.*;
import java.awt.*;

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
}