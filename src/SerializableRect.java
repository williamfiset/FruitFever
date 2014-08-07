import java.io.*;
import java.awt.*;

public class SerializableRect implements Serializable {
	
	int x, y, width, height;
	Color color;
	
	public SerializableRect(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

}