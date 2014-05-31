/**
 *	LevelInformation - This simple class holds the information for one level including the high score and stars.
 *
 * @Author Micah Stairs
 *
 **/
 
 import java.io.*;
 
 public class LevelInformation implements Serializable {
	
	public String name;
	public int number, highScore;
	public byte stars;
	public boolean locked;
 
	public LevelInformation(String name, int number, int highScore, byte stars, boolean locked) {
		this.name = name;
		this.number = number;
		this.highScore = highScore;
		this.stars = stars;
		this.locked = locked;
	}
	
	public LevelInformation(String name, int number, boolean locked) {		
		this(name, number, 0, (byte) 0, locked);	
	}
 
 }