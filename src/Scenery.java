
/**
* @Author Micah Stairs
*
**/

import acm.graphics.*;
import java.awt.*;

public class Scenery {

	public int x, y, type;
	public GImage image;

	public Scenery(int x, int y, int type, GImage image){

		this.x = x;
		this.y = y;
		this.type = type;
		this.image = image;

	}

}