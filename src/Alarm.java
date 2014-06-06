/**
 *	Alarm - This class is given a starting value to decrement from each time the main loop gets executed.
 *			It executes a method once it has finished counting to 0.
 *
 * @author Micah Stairs
 *
 */

 import java.lang.reflect.*;
 
class Alarm {
	
	public boolean active;
	private int counter;
	private Method method;
	private Object object;

	public Alarm(int counter, Method method, Object object) {
	
		active = true;
		this.counter = counter;
		this.method = method;
		this.object = object;
		
	}

	public void execute() {
		
		if (active && --counter <= 0) {
			try {
				method.invoke(object);
			}
			catch (IllegalAccessException e) { System.out.println("The alarm's method couldn't be called!"); }
			catch (InvocationTargetException e) { System.out.println("The alarm's method couldn't be called!"); }
			active = false;
			
			if (FruitFever.debugMode)
				System.out.println("Alarm (Powerup) has finished!");
		}
	}
	
}