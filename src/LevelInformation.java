/**
 *	LevelInformation - This simple class holds the information for one level including the high score and stars.
 *
 * @Author Micah Stairs
 *
 **/
 
 import java.io.*;
 
 public class LevelInformation implements Serializable {

 	private static final long serialVersionUID = 1L;
	
	public String name;
	public int number;
	public byte stars;
	public boolean locked;
	public boolean completed;
 
	public LevelInformation(String name, int number, byte stars, boolean locked, boolean completed) {
		this.name = name;
		this.number = number;
		this.stars = stars;
		this.locked = locked;
		this.completed = completed;
	}
	
	public LevelInformation(String name, int number, boolean locked) {		
		this(name, number, (byte) 0, locked, false);	
	}
 
 }