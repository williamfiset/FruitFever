import java.io.*;

public class SerializableImage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	int x, y;
	int[][] pixelArray;
	
	public SerializableImage(int x, int y, int[][] pixelArray) {
		this.x = x;
		this.y = y;
		this.pixelArray = pixelArray;
	}

}