import java.io.*;
import java.awt.*;

public class SerializableHint implements Serializable {

	private static final long serialVersionUID = 1L;

	int x, y;
	String hint;
	
	public SerializableHint(double x, double y, String hint) {
		this.x = (int) x;
		this.y = (int) y;
		this.hint = hint;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getHint() {
		return hint;
	}

}