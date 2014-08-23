
/*
 * @author Micah Stairs
*/

import acm.graphics.*;
import java.awt.*;

public class Hint extends Thing {

	private static final long serialVersionUID = 1L;

	/** Public instance variables **/
	public boolean visiting = false;
	public String hint = "";
	
	public Hint(int x, int y) {
		super(x, y, Data.hintSign[1]);
	}
	
	public void setVisited() {
		changeImage(Data.hintSign[2]);
		visiting = true;
	}
	
}