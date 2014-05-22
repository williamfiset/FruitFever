
/**
 *	Alarm - This class is given a starting value to decrement from each time the main loop gets executed.
 *			It can excute a peice of code at the beginning, and another at the end.
 *
 * @author Micah Stairs
 *
 */

class Alarm {
	
	public boolean active;
	private int counter, endCode;

	public Alarm (int counter, int startCode, int endCode) {
	
		active = true;
		this.counter = counter;
		this.endCode = endCode;
		
		switch (startCode) {
				
				case 0: break;
				case 1: break;
				case 2: break;
				// ...
				
			}
		
	}

	public void execute() {
		
		if (active && --counter <= 0) {
		
			switch (endCode) {
				
				case 0: break;
				case 1: break;
				case 2: break;
				// ...
				
			}
			
			active = false;
		
		}
	
	}
	
}