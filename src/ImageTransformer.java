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
	public static GImage verticalFlip(GImage img){
	
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[arr.length][arr[0].length];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[i].length; j++)
				arr2[i][j] = arr[arr.length - i - 1][j];
     
     	return new GImage(arr2);

    }
	
	/** Rotates a GImage horizontally **/
	public static GImage horizontalFlip(GImage img){
	
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[arr.length][arr[0].length];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[i].length; j++)
				arr2[i][j] = arr[i][arr[0].length - j - 1];
     
     	return new GImage(arr2);

    }
	
	/** Rotates a GImage counter-clockwise **/
	public static GImage rotateCounterClockwise(GImage img){
		
		int[][] arr = img.getPixelArray();
		int[][] arr2 = new int[arr[0].length][arr.length];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[i].length; j++)
				arr2[j][i] = arr[i][arr[i].length - j - 1];
     
     	return new GImage(arr2);
		
    }
	
	/** Resizes (shrinks/stretches) a GImage **/
	public static GImage resize(GImage img, int newWidth, int newHeight){
		
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
	public static GImage rotateClockwise(GImage img){
     	return rotateCounterClockwise(rotateCounterClockwise(rotateCounterClockwise(img)));
    }
	
	/** Rotates a GImage 180 degrees **/
	public static GImage rotate180(GImage img){
     	return rotateCounterClockwise(rotateCounterClockwise(img));
    }
	
	/** Randomly rotates a GImage **/
	public static GImage rotateRandomly(GImage img){
		int rotations = (int) (Math.random()*4);
		
		for(int i = 0; i < rotations; i++)
			img = rotateClockwise(img);
		
		return img;
	}
	
	/** Randomly mirrors a GImage horizontally **/
	public static GImage mirrorRandomly(GImage img){
		if(Math.random() < 0.5)
			return horizontalFlip(img);
		else
			return img;
	}
	
}