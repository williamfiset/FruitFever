import java.io.*;

public class SerializableImage implements Serializable {
	
	int x, y;
	int[][] pixelArray;
	
	public SerializableImage(int x, int y, int[][] pixelArray) {
		this.x = x;
		this.y = y;
		this.pixelArray = pixelArray;
	}

}