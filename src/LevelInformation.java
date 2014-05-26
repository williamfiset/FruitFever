/**
 *	LevelInformation - This class holds the information for one level including the high score and stars.
 *
 * @Author Micah Stairs
 *
 **/
 
 public class LevelInformation {
	
	public byte stars;
	public int number, highScore;
	public String name;
 
	public LevelInformation(int number, String name, int highScore, byte stars) {
		this.number = number;
		this.name = name;
		this.highScore = highScore;
		this.stars = stars;
	}
 
 }