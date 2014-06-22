/**
 *	ImageTransformer - Can flip a GImage horizontally or vertically, rotate it, or resize it.
 *
 * @author Micah Stairs
 * 
 **/

import acm.graphics.GImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


abstract class ImageTransformer {

	/** Rotates a GImage vertically **/
	public static GImage verticalFlip(GImage img) {
	
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[arr.length][arr[0].length];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[i].length; j++)
				arr2[i][j] = arr[arr.length - i - 1][j];
     
     	return new GImage(arr2);

    }
	
	/** Rotates a GImage horizontally **/
	public static GImage horizontalFlip(GImage img) {
	
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[arr.length][arr[0].length];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[i].length; j++)
				arr2[i][j] = arr[i][arr[0].length - j - 1];
     
     	return new GImage(arr2);

    }
	
	/** Rotates a GImage counter-clockwise **/
	public static GImage rotateCounterClockwise(GImage img) {
	
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[arr[0].length][arr.length];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[i].length; j++)
				arr2[j][i] = arr[i][arr[i].length - j - 1];
     
     	return new GImage(arr2);
		
    }
	
	/** Resizes (shrinks/stretches) a GImage **/
	public static GImage resize(GImage img, int newWidth, int newHeight) {
		
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[newHeight][newWidth];
		
		int oldWidth = arr[0].length;
		int oldHeight = arr.length;
		
		double widthFactor = (double) newWidth / (double) oldWidth;
		double heightFactor = (double) newHeight / (double) oldHeight;

		for (int i = 0; i < arr2.length; i++)
			for (int j = 0; j < arr2[i].length; j++)
				arr2[i][j] = arr[(int) ((double)i / heightFactor)][(int) ((double)j / widthFactor)];
     
     	return new GImage(arr2);
		
    }
	
	/** Rotates a GImage clockwise **/
	public static GImage rotateClockwise(GImage img) {
     	return rotateCounterClockwise(rotateCounterClockwise(rotateCounterClockwise(img)));
    }
	
	/** Rotates a GImage 180 degrees **/
	public static GImage rotate180(GImage img) {
     	return rotateCounterClockwise(rotateCounterClockwise(img));
    }
	
	/** Randomly rotates a GImage **/
	public static GImage rotateRandomly(GImage img) {
		int rotations = (int) (Math.random()*4);
		
		for(int i = 0; i < rotations; i++)
			img = rotateClockwise(img);
		
		return img;
	}
	
	/** Randomly mirrors a GImage horizontally **/
	public static GImage mirrorRandomly(GImage img) {
		if (Math.random() < 0.5)
			return horizontalFlip(img);
		else
			return img;
	}
	/** Crops a GImage **/
	public static GImage crop(GImage img, int width, int height) {
		try {
			int[][] arr = img.getPixelArray();
			int[][] arr2 = new int[Math.max(height, 0)][Math.max(width, 0)];

			for (int i = 0; i < height; i++)
				for (int j = 0; j < width; j++)
					arr2[i][j] = arr[i][j];
		 
			return new GImage(arr2);
		}
		/** Very uncommon exception, not completely sure where it came from
			Exception in thread "Thread-2" java.lang.NullPointerException
			at java.awt.image.FilteredImageSource.startProduction(Unknown Source)
			at java.awt.image.PixelGrabber.grabPixels(Unknown Source)
			at java.awt.image.PixelGrabber.grabPixels(Unknown Source)
			at acm.util.MediaTools.getPixelArray(MediaTools.java:337)
			at acm.graphics.GImage.getPixelArray(GImage.java:368)
			at ImageTransformer.crop(ImageTransformer.java:110)
			at ImageTransformer.crop(ImageTransformer.java:121)
			at ImageTransformer.crop(ImageTransformer.java:125)
			at ScreenHandler.adjustEnergyBar(ScreenHandler.java:230)
			at FruitFever.run(FruitFever.java:219)
			at acm.program.Program.runHook(Program.java:1568)
			at acm.program.Program.startRun(Program.java:1557)
			at acm.program.AppletStarter.run(Program.java:1895)
			at java.lang.Thread.run(Unknown Source)
		**/
		catch (NullPointerException e) {
			System.out.println("Weird null pointer exception in ImageTransformer.java");
			return img;
		}
	}
	
	public static GImage crop(GImage img, int width) {
		return crop(img, width, (int) img.getHeight());
	}
	
	public static GImage crop(GImage img, double width) {
		return crop(img, (int) width);
	}
	
	/** Extends a GImage vertically by stretching the center and retaining the top and bottom **/
	public static GImage extendVertically(GImage[] arr, int newHeight) {

		int centerHeight = (int) (newHeight - arr[0].getHeight() - arr[2].getHeight());

		arr[1] = resize(arr[1], (int) arr[1].getWidth(), centerHeight);
     
     	return joinVertically(arr);
		
    }
	
	/** Extends a GImage hoizontally by stretching the center and retaining the sides **/
	public static GImage extendHorizontally(GImage[] arr, int newWidth) {

		int centerWidth = (int) (newWidth - arr[0].getWidth() - arr[2].getWidth());

		arr[1] = resize(arr[1], centerWidth, (int) arr[1].getHeight());
     
     	return joinHorizontally(arr);
		
    }
	
	/** Joins three Gimages together on top of each other **/
	public static GImage joinVertically(GImage top, GImage center, GImage bottom) {
	
		int width = (int) top.getWidth(),
			newHeight = (int) (top.getHeight() + center.getHeight() + bottom.getHeight());
		
		int[][] arrTop = top.getPixelArray(),
				arrCenter = center.getPixelArray(),
				arrBottom = bottom.getPixelArray(),
				arrFull = new int[newHeight][width];
		
		for (int i = 0; i < (int) top.getHeight(); i++)
			for (int j = 0; j < width; j++)
				arrFull[i][j] = arrTop[i][j];
				
		int yOffset = (int) top.getHeight();
		
		for (int i = 0; i < (int) (center.getHeight()); i++)
			for (int j = 0; j < width; j++)
				arrFull[i + yOffset][j] = arrCenter[i][j];
		
		yOffset += (int) center.getHeight();
		
		for (int i = 0; i < (int) (bottom.getHeight()); i++)
			for (int j = 0; j < width; j++)
				arrFull[i + yOffset][j] = arrBottom[i][j];
     
     	return new GImage(arrFull);
		
    }
	
	/** Joins three Gimages together on top of each other **/
	public static GImage joinVertically(GImage[] arr) {
		return joinVertically(arr[0], arr[1], arr[2]);
	}
	
	/** Joins three Gimages together side by side **/
	public static GImage joinHorizontally(GImage left, GImage center, GImage right) {
	
		int height = (int) left.getHeight(),
			newWidth = (int) (left.getWidth() + center.getWidth() + right.getWidth());
		
		int[][] arrLeft = left.getPixelArray(),
				arrCenter = center.getPixelArray(),
				arrRight = right.getPixelArray(),
				arrFull = new int[height][newWidth];
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < (int) (left.getWidth()); j++)
				arrFull[i][j] = arrLeft[i][j];
				
		int xOffset = (int) left.getWidth();
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < (int) (center.getWidth()); j++)
				arrFull[i][j + xOffset] = arrCenter[i][j];
		
		xOffset += (int) center.getWidth();
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < (int) (right.getWidth()); j++)
				arrFull[i][j + xOffset] = arrRight[i][j];
     
     	return new GImage(arrFull);
		
    }
	
	/** Joins three Gimages together side by side **/
	public static GImage joinHorizontally(GImage[] arr) {
		return joinHorizontally(arr[0], arr[1], arr[2]);
	}
	
	/** Joins 9 Gimages together to make a GImage of a given size **/
	public static GImage joinSet(GImage[][] arr, int width, int height) {
		return extendVertically(new GImage[] {extendHorizontally(arr[0], width), extendHorizontally(arr[1], width), extendHorizontally(arr[2], width)}, height);
	}
	
	public static boolean isIdentical(GImage img1, GImage img2) {
		
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
			return false;
		
		int[][] arr1 = img1.getPixelArray();
		int[][] arr2 = img2.getPixelArray();
		
		for (int y = 0; y < img1.getHeight(); y++)
			for (int x = 0; x < img1.getWidth(); x++)
				if (arr1[y][x] != arr2[y][x])
					return false;
		
		return true;
	
	}
	
}