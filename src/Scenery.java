
/**
* @Author Micah Stairs
*
**/

import acm.graphics.*;
import java.awt.*;

public class Scenery extends Thing{

	public int type;

	public Scenery(int x, int y, int type, GImage image){
		
		super(x, y, ImageTransformer.mirrorRandomly(image));
		
		this.type = type;
		
	}

}