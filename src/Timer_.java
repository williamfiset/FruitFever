
/**
 *
 * @author William Fiset
 *
 * The Timer_ class (Called Timer_ & not Timer because there already exists a built in java class
 * called Timer) makes it easy to Time code between two lines.
 *
 * Usage Example: 
 *
 *    You wish to time code between line number 'a' and line number 'b'
 *
 *    Timer_ timer = new Timer_(); // At line # 'a'
 *    
 *    ...
 *    // Code you wish to test
 *    ...
 *
 *    timer.stop(true); // At line # b
 *
 */

class Timer_{


	private final double BILLION = 1e9;
	protected long startTime;
	protected long stopTime;

	public Timer_(){
		startTime =  System.nanoTime();
		stopTime = 0;
	}

	/**
     * @Param printDuration - A boolean value that determines if you want to 
     * print the duration of the timer to the console or simply have it returned.
     *
     * NOTE: The duration gets returned regardless the value of printDuration 
	 */

	public double stop(boolean printDuration){

		stopTime = System.nanoTime();
		double duration = ((stopTime - startTime) / BILLION);
		
		stopTime = 0;
		startTime = 0;

		if (printDuration)
			System.out.println(duration);

		return duration;
	}

	/** Overloaded method, does the same thing as ^ but only returns**/
	public double stop(){

		stopTime = System.nanoTime();
		double duration = ((stopTime - startTime) / BILLION);
		
		stopTime = 0;
		startTime = 0;

		return duration;
	}

	public double getTime(){

		stopTime = System.nanoTime();
		double duration = ((stopTime - startTime) / BILLION);
		return duration;
	}

}