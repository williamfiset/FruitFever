

/*
*
* @Author William Fiset
*
* Monday, June 16, 2014
*
*/


public class Slider {

	public Button button;
	private Thing bar;

	// Designated constructor
	Slider (Thing bar, Button button) {

		// Check if the button and the bar x positions look like those of a slider 
		if (!isASlider(bar, button)) 
			throw new IllegalArgumentException("\nThe X coordinates of the bar and the button do not match those of a slider\n");

		this.bar = bar;
		this.button = button;

	}


	// Add Slider to the screen
	public static void add() {

		// ScreenHandler.fruitFever.add(button);
		// ScreenHandler.fruitFever.add(bar);

		// ScreenHandler.addButtonsToScreen(button);
	}

	/* Places the position of the button to where the mouse is*/
	public void slideButton(int mouseXPos) {

		// new mouseXPos is on the slider
		if (mouseXPos > button.x && mouseXPos < button.x + button.width) {

			// Need to adjust for middle of button
			button.x = mouseXPos;
		}

	}

	/* @return: a percentage from 0 - 1 on where the progression of the slider is*/
	public double getPercentage() {

		try {
			return (button.x + button.width / 2 - bar.x) / bar.width;
		} catch(ArithmeticException zeroDivision){
			return 0.0;
		}
		
	}

	// checks if the button and the bars x positions look like those of a slider 
	private boolean isASlider(Thing bar, Button button){
		return ( button.x > bar.x && button.x + button.width < bar.x + bar.width  );
	}

}





