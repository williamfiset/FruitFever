import java.io.*;

public class SerializableThing implements Serializable {
	
	int x, y;
	int[][] pixelArray;
	
	public SerializableThing(int x, int y, int[][] pixelArray) {
		this.x = x;
		this.y = y;
		this.pixelArray = pixelArray;
	}

}