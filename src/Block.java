
/**
*
* @Author William Fiset
*
* The Block Class provides functionality for blocks to have interact
* and be part of the FruitFever World
**/

import acm.graphics.*;
import java.awt.*;

public class Block extends GRectangle {
	
	public static int SIZE = 25;

	public int x, y, type;
	public GImage image;

	/**
	* @Param type - type defines what type of block it is, whether it is a 
	* brown block, snow block, or a black block we will be able to identify them with integer
	* values, for example a standard brown block is type 0, check the reference file to check
	* which type of block is which
	**/

	public Block(int x, int y, int type, GImage image){

		super(x, y, SIZE, SIZE);
		
		this.x = x;
		this.y = y;
		this.type = type;
		this.image = image;

	}
}